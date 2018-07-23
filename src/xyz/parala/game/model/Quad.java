package xyz.parala.game.model;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import xyz.parala.game.shader.ShaderProgram;

public class Quad extends Entity {
	int vaoID;
	int vboID;

	public Quad(float[] vertices) {
		super(null, new Vector3f(0.0f, 0.0f, -10.0f), new Vector3f());
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		// Position VBO

		vboID = glGenBuffers();
		FloatBuffer posBuffer = MemoryUtil.memAllocFloat(vertices.length);
		posBuffer.put(vertices).flip();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	// Overriding because Entity class uses Meshes
	@Override
	public void draw(ShaderProgram shader) {
		setModelUniform(shader);
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glDrawArrays(GL_TRIANGLES, 0, 3);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
