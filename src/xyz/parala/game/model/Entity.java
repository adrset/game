package xyz.parala.game.model;

import org.joml.Vector3f;

import xyz.parala.game.shader.ShaderProgram;

public class Entity implements Renderable {
	Mesh[] meshes;
	Vector3f position; // common for all meshes
	Vector3f rotation; // common for all meshes

	public Entity(Mesh[] meshes, Vector3f position, Vector3f rotation) {
		this.meshes = meshes;
		this.position = position;
		this.rotation = rotation;
	}

	@Override
	public void draw(ShaderProgram shader) {
		if (meshes != null) {
			for (Mesh mesh : meshes) {
				mesh.draw(shader);
			}
		}

	}

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
