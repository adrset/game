package xyz.parala.game.model;

import org.joml.Vector3f;

public class Box extends Entity {
	
	// Box doesn't have it's own mesh, beacuase it is meant to be rendered in instanced manner.
	// Mesh could be static here, but the maker decided to move it to InstanceRenderer class.

	public Box(Vector3f position, Vector3f rotation, float scale) {
		super(null, position, rotation, scale);
	}
	

}
