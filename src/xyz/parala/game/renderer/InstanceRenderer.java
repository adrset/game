package xyz.parala.game.renderer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;

import xyz.parala.game.camera.Camera;
import xyz.parala.game.filter.FrustumCullingFilter;
import xyz.parala.game.light.Light;
import xyz.parala.game.model.Box;
import xyz.parala.game.model.Chunk;
import xyz.parala.game.model.Entity;
import xyz.parala.game.model.Material;
import xyz.parala.game.model.Mesh;
import xyz.parala.game.model.Texture;
import xyz.parala.game.shader.ShaderProgram;

public class InstanceRenderer {

	private static final int BUFFER_INSTANCES = 100000;
	private static final int FLOATS_PER_INSTANCE = 16; // storing only transformation matrix
	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(BUFFER_INSTANCES * FLOATS_PER_INSTANCE);
	private Mesh[] m;
	private int vbo;
	FrustumCullingFilter filter;
	private int index = 0;
	ShaderProgram shader;
	boolean update = true;

	Matrix4f projection;

	public InstanceRenderer(ShaderProgram shader, int width, int height, Mesh[] m) {
		this.m = m;
		filter = new FrustumCullingFilter();
		vbo = createInstanceVBO();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		
		for (int j = 0; j < m.length; j++) {
			for (int i = 5; i < 9; i++)
				glEnableVertexAttribArray(i);

			GL30.glBindVertexArray(m[j].getVaoId());
			addInstanceAttribute(5, 4, FLOATS_PER_INSTANCE, 0);
			addInstanceAttribute(6, 4, FLOATS_PER_INSTANCE, 4);
			addInstanceAttribute(7, 4, FLOATS_PER_INSTANCE, 8);
			addInstanceAttribute(8, 4, FLOATS_PER_INSTANCE, 12);

			GL30.glBindVertexArray(0);

		}
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		this.shader = shader;

		projection = new Matrix4f();
		projection.setPerspective((float) Math.toRadians(70), (float) width / height, 0.01f, 100000000.0f, false);
	}

	public void addInstanceAttribute(int attribute, int dataSize, int dataLength, int offset) {

		GL20.glVertexAttribPointer(attribute, dataSize, GL11.GL_FLOAT, false, dataLength * 4, offset * 4);
		GL33.glVertexAttribDivisor(attribute, 1);

	}

	public void renderChunks(List<Chunk> chunks, Camera camera, Light light) {

		shader.start();
		shader.setUniform("projection", projection);
		shader.setUniform("view", camera.getView());
		filter.updateFrustum(projection, camera.getView());
		light.update(shader);
		index = 0;
		final int size = chunks.size() * Chunk.SIZE * Chunk.SIZE * Chunk.SIZE;
		float[] data = new float[FLOATS_PER_INSTANCE * size];
		for (Chunk chunk : chunks) {

			for (Entity e : chunk.getBoxes()) {

				updateTransformationMatrixWithRelativePosition(data, e, chunk.getPosition());

			}

		}

		updateVBO(data);

		for (int i = 0; i < m.length; i++) {
			prepareTexture(m[i]);
			shader.setUniform("material.shininess", 0.1f);

			GL30.glBindVertexArray(m[i].getVaoId());

			for (int j = 0; j < 9; j++)
				glEnableVertexAttribArray(j);

			GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, m[i].getVertexCount(), GL11.GL_UNSIGNED_INT, 0, size);
			glBindTexture(GL_TEXTURE_2D, 0);

		}

		for (int i = 0; i < 9; i++)
			GL20.glDisableVertexAttribArray(i);

		GL30.glBindVertexArray(0);

		shader.stop();
	}

	private void render(List<Box> boxes, Camera camera, Light light) {
		shader.start();
		shader.setUniform("projection", projection);
		shader.setUniform("view", camera.getView());
		filter.updateFrustum(projection, camera.getView());
		light.update(shader);
		index = 0;

		float[] data = new float[boxes.size() * FLOATS_PER_INSTANCE];
		for (Entity e : boxes) {

			updateTransformationMatrix(data, e);

		}

		updateVBO(data);

		for (int i = 0; i < m.length; i++) {
			prepareTexture(m[i]);
			shader.setUniform("material.shininess", 0.1f);

			GL30.glBindVertexArray(m[i].getVaoId());
			for (int j = 0; j < 9; j++)
				glEnableVertexAttribArray(j);

			GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, m[i].getVertexCount(), GL11.GL_UNSIGNED_INT, 0,
					boxes.size());
			glBindTexture(GL_TEXTURE_2D, 0);

		}

		for (int i = 0; i < 9; i++)
			GL20.glDisableVertexAttribArray(i);

		GL30.glBindVertexArray(0);

		shader.stop();

	}

	private void prepareTexture(Mesh mesh) {
		Material material = mesh.getMaterial();
		Texture texture = material != null ? material.getTexture() : null;
		if (texture != null) {
			// Activate first texture bank
			glActiveTexture(GL_TEXTURE0);
			// Bind the texture
			glBindTexture(GL_TEXTURE_2D, texture.getId());
			shader.setUniform("material.diffuse", 0);
		}
		texture = material != null ? material.getNormalMap() : null;
		if (texture != null) {
			// Activate first texture bank
			glActiveTexture(GL_TEXTURE0 + 1);
			// Bind the texture
			glBindTexture(GL_TEXTURE_2D, texture.getId());
			shader.setUniform("material.specular", 1);
		}
	}

	private void updateTransformationMatrixWithRelativePosition(float[] data, Entity entity, Vector3f rel) {
		Matrix4f transformationMatrix = entity.createModelMatrix(rel);
		storeMatrixData(transformationMatrix, data);
	}

	private void updateTransformationMatrix(float[] data, Entity entity) {
		Matrix4f transformationMatrix = entity.createModelMatrix();
		storeMatrixData(transformationMatrix, data);
	}

	private void storeMatrixData(Matrix4f matrix, float[] data) {
		data = matrix.get(data, index);
		index += 16;
	}

	public void updateVBO(float[] data) {
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity() * 4, GL15.GL_STREAM_DRAW);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	public int createInstanceVBO() {
		int size = BUFFER_INSTANCES * FLOATS_PER_INSTANCE;
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, size * 4, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vbo;
	}

	public void setProjection(int width, int height) {
		projection = new Matrix4f();
		projection.setPerspective((float) Math.toRadians(70), (float) width / height, 0.01f, 100000000.0f, false);
	}

}
