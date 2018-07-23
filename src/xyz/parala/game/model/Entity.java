package xyz.parala.game.model;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import xyz.parala.game.shader.ShaderProgram;

public abstract class Entity implements Renderable {
	Mesh[] meshes;
	Vector3f position; // common for all meshes
	Vector3f rotation; // common for all meshes
	Matrix4f model;

	public Entity(Mesh[] meshes, Vector3f position, Vector3f rotation) {
		this.meshes = meshes;
		this.position = position;
		this.rotation = rotation;
		model = new Matrix4f();
	}
	
	protected void setModelUniform(ShaderProgram shader) {
		model.identity();
		model.translate(position);
		shader.setUniform("model", 	model);
	}

	@Override
	public void draw(ShaderProgram shader) {
		setModelUniform(shader);
		for (Mesh mesh : meshes) {
			mesh.draw(shader);
		}

	}

	public abstract void update();

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

}
