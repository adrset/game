package xyz.parala.game.renderer;

import java.util.List;

import org.joml.Matrix4f;

import xyz.parala.game.camera.Camera;
import xyz.parala.game.model.Renderable;
import xyz.parala.game.shader.ShaderProgram;
import xyz.parala.game.shader.WorldShader;

public class Renderer {

	ShaderProgram shader;
	
	Matrix4f projection;

	public Renderer(String shaderName, int width, int height) {
		shader = new ShaderProgram(shaderName + ".vs", shaderName + ".fs");
		setProjection(width, height);
	}
	
	public void setProjection(int width, int height) {
		projection = new Matrix4f();
		//projection.setPerspective((float) Math.toRadians(70), (float) width / height, 0.01f, 100.0f, false);
	}
	
	public void render(List<Renderable> toRender, Camera camera) {
		
		shader.start();
		shader.setUniform("projection", projection);
		shader.setUniform("view", camera.getView());
		for(Renderable r : toRender) {
			// Let each renderable access to setting shader values
			r.draw(shader);
		}
		shader.stop();
	}
	
	public ShaderProgram getShader() {
		return shader;
	}

}
