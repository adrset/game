package xyz.parala.game.renderer;

import java.util.List;

import xyz.parala.game.model.Renderable;
import xyz.parala.game.shader.WorldShader;

public class Renderer {

	WorldShader shader;

	public Renderer(String shaderName) {
		shader = new WorldShader(shaderName + ".vs", shaderName + ".fs");
	}
	
	public void render(List<Renderable> toRender) {
		for(Renderable r : toRender) {
			r.draw();
		}
	}

}
