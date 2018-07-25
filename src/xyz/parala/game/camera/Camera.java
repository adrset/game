package xyz.parala.game.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import xyz.parala.game.input.Keyboard;
import xyz.parala.game.model.Updatable;

public class Camera implements Updatable {
	Vector3f position;
	Matrix4f view;
	Vector3f target; // just a temporary variable
	Vector3f up;
	Vector3f front;
	boolean needsUpdate = true;
	float cameraSpeed;

	public Camera() {
		view = new Matrix4f();
		position = new Vector3f(0, 0, 10);
		target = new Vector3f();
		front = new Vector3f(0.0f, 0.0f, -1.0f);
		up = new Vector3f(0.0f, 1.0f, 0.0f);
		cameraSpeed = 0.1f;
	}

	public Matrix4f getView() {
		return view;
	}

	public void update() {
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
			target = target.set(front).mul(cameraSpeed);
			position = position.add(target);
			needsUpdate = true;
		}
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
			target = target.set(front).mul(cameraSpeed);
			position = position.sub(target);
			needsUpdate = true;
		}
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_A)) {
			position.x -= 0.1f;
			target = target.set(front);
			target = target.cross(up);
			target = target.normalize().mul(cameraSpeed);
			position = position.sub(target);
			System.out.println(target);
			needsUpdate = true;
		}
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_D)) {
			position.x += 0.1f;
			target = target.set(front);
			target = target.cross(up);
			target = target.normalize().mul(cameraSpeed);
			position = position.add(target);
			needsUpdate = true;
		}

		if (needsUpdate) {
			view.identity();
			target.set(position).add(front);
			view.lookAt(position, target, up);
		}
		needsUpdate = false;
	}

}
