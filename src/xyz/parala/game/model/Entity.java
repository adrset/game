package xyz.parala.game.model;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import xyz.parala.game.shader.ShaderProgram;

public class Entity implements Renderable {
	Mesh[] meshes;
	protected Vector3f position; // common for all meshes
	public Vector3f rotation; // common for all meshes
	public Matrix4f model;
	public Vector3f up;
	public Vector3f right;
	public Vector3f forward;
	protected float scale = 1.0f;
	
	public void setScale(float f) {
		scale = f;
	}
	
	protected void setMeshes(Mesh[] m) {
		this.meshes = m;
	}
	
	public Mesh[] getMeshes() {
		return this.meshes;
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
	
	public Entity(Mesh[] meshes, Vector3f position, Vector3f rotation, float scale) {
		this.scale = scale;
		this.meshes = meshes;
		this.position = position;
		this.rotation = rotation;
		up = new Vector3f(0,1,0);
		right = new Vector3f(1,0,0);
		forward = new Vector3f(0,0,1);
		model = new Matrix4f();
	}
	
	public Matrix4f createModelMatrix() {
		model.identity();
		
		model.translate(position);
		model.rotate(rotation.x, right);
		model.rotate(rotation.y, up);
		model.rotate(rotation.z, forward);
		model.scale(scale);
		return model;
	}
	
	public Matrix4f createModelMatrix(Vector3f rel) {
		model.identity();
		Vector3f tmp = new Vector3f(position);
		tmp.add(rel);
		model.translate(tmp);
		model.rotate(rotation.x, right);
		model.rotate(rotation.y, up);
		model.rotate(rotation.z, forward);
		model.scale(scale);
		return model;
	}
	
	protected void setModelUniform(ShaderProgram shader) {
		
		shader.setUniform("model", 	createModelMatrix()); // on purpose
	}

	@Override
	public void draw(ShaderProgram shader) {
		setModelUniform(shader);
		if(meshes != null) {
			for (Mesh mesh : meshes) {
				mesh.draw(shader);
			}
		}

	}
	
	public void increaseRotation(Vector3f toAdd) {
		rotation.add(toAdd);
	}


	public Vector3f getPosition() {
		return position;
	}
	
	public void increasePosition(Vector3f dr) {
		this.position.add(dr);
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
