package xyz.parala.game.chunks;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import xyz.parala.game.model.Entity;

public class Box extends Entity {
	
	// Box doesn't have it's own mesh, beacuase it is meant to be rendered in instanced manner.
	// Mesh could be static here, but the maker decided to move it to InstanceRenderer class.
	boolean update = true;
	Vector3f color;
	public Box(Vector3f position, Vector3f rotation, float scale, Vector3f color) {
		super(null, position, rotation, scale);
		this.color = color;
	}
	public Vector3f getColor() {
		return this.color;
	}
	@Override
	public Matrix4f createModelMatrix(Vector3f rel) {
		if(update) {
			super.createModelMatrix(rel);
			update = false;
		}
		return model;
	}
}
