package xyz.parala.game.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
	Vector3f position;
	Matrix4f view;
	
	public Camera() {
		view = new Matrix4f();
		position = new Vector3f();
	}
	
	public Matrix4f getView() {
		return view;
	}
	
	public void update() {
		view.translate(-position.x, -position.y, -position.z);
	}
	

}
