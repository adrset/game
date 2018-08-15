package xyz.parala.game.model;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import xyz.parala.game.shader.ShaderProgram;

public class Entity implements Renderable {
	Mesh[] meshes;
	protected Vector3f position; // common for all meshes
	Vector3f rotation; // common for all meshes
	Matrix4f model;
	Vector3f up;
	Vector3f right;
	Vector3f forward;
	
	protected void setMeshes(Mesh[] m) {
		this.meshes = m;
	}

	public Entity(Mesh[] meshes, Vector3f position, Vector3f rotation) {
		this.meshes = meshes;
		this.position = position;
		this.rotation = rotation;
		up = new Vector3f(0,1,0);
		right = new Vector3f(1,0,0);
		forward = new Vector3f(0,0,1);
		model = new Matrix4f();
	}
	
	protected void setModelUniform(ShaderProgram shader) {
		model.identity();
		
		model.translate(position);
		model.rotate(rotation.x, right);
		model.rotate(rotation.y, up);
		model.rotate(rotation.z, forward);
		shader.setUniform("model", 	model);
	}

	@Override
	public void draw(ShaderProgram shader) {
		setModelUniform(shader);
		for (Mesh mesh : meshes) {
			mesh.draw(shader);
		}

	}
	
	public void increaseRotation(Vector3f toAdd) {
		rotation.add(toAdd);
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

	@Override
	public void update(ShaderProgram shader) {
		// TODO Auto-generated method stub
		
	}

}
