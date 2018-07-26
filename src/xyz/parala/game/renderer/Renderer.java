package xyz.parala.game.renderer;

import java.util.List;

import org.joml.Matrix4f;

import xyz.parala.game.camera.Camera;
import xyz.parala.game.light.Light;
import xyz.parala.game.model.Entity;
import xyz.parala.game.model.Renderable;
import xyz.parala.game.shader.ShaderProgram;

public class Renderer {

	ShaderProgram shader;
	
	Matrix4f projection;

	public Renderer(ShaderProgram shader, int width, int height) {
		this.shader = shader;
		setProjection(width, height);
	}
	
	public void setProjection(int width, int height) {
		projection = new Matrix4f();
		projection.setPerspective((float) Math.toRadians(70), (float) width / height, 0.01f, 100.0f, false);
	}
	
	public void render(List<Entity> toRender, Camera camera, Light light) {
		
		shader.start();
		
		shader.setUniform("projection", projection);
		shader.setUniform("view", camera.getView());
		light.update(shader);
		for(Entity e : toRender) {

			// Let each renderable access to setting shader values
			e.draw(shader);
		}
		shader.stop();
	}
	
	public ShaderProgram getShader() {
		return shader;
	}

}
