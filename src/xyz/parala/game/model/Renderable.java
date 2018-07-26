package xyz.parala.game.model;

import xyz.parala.game.shader.ShaderProgram;

public interface Renderable {
	public void draw(ShaderProgram shader);
	public void update(ShaderProgram shader);
}
