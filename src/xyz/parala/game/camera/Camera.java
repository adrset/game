package xyz.parala.game.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import xyz.parala.game.input.Keyboard;
import xyz.parala.game.input.Mouse;
import xyz.parala.game.model.Renderable;
import xyz.parala.game.shader.ShaderProgram;

public class Camera implements Renderable {
	Vector3f position;
	Matrix4f view;
	Vector3f target; // just a temporary variable
	Vector3f up;
	Vector3f front;
	float pitch = 0;
	float yaw = -90;
	boolean needsUpdate = true;
	float cameraSpeed;
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f pos) {
		this.position = pos;
	}

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


	@Override
	public void draw(ShaderProgram shader) {
		
	}

	@Override
	public void update(ShaderProgram shader) {
		
		shader.setUniform("viewPos", position);
		yaw   += (float) (Mouse.mouseCursor.getDX() * 0.4);
		pitch +=  (float) (Mouse.mouseCursor.getDY() * 0.4);  
		
		if(pitch < -89.0f) {
			pitch = -89.0f;
		}else if(pitch > 89.0f) {
			pitch = 89.0f;
		}
		
		target.zero();
		target.x = (float) (Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw)));
		target.y = (float) Math.sin(Math.toRadians(pitch));
		target.z = (float) (Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw)));
		target.normalize();
		
		front.set(target);
		float speedUp = 1;
		if(Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			speedUp = 10;
		}
		
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_W)) {
			target = target.set(front).mul(cameraSpeed*speedUp);
			position = position.add(target);
			needsUpdate = true;
		}
		
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_S)) {
			target = target.set(front).mul(cameraSpeed*speedUp);
			position = position.sub(target);
			needsUpdate = true;
		}
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_A)) {
			//position.x -= 0.1f;
			target = target.set(front);
			target = target.cross(up);
			target = target.normalize().mul(cameraSpeed*speedUp);
			position = position.sub(target);
			needsUpdate = true;
		}
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_D)) {
			//position.x += 0.1f;
			target = target.set(front);
			target = target.cross(up);
			target = target.normalize().mul(cameraSpeed*speedUp);
			position = position.add(target);
			needsUpdate = true;
		}

		if (true) {
			view.identity();
			target.set(position).add(front);
			view.lookAt(position, target, up);
		}
		needsUpdate = false;
		
	}

}
