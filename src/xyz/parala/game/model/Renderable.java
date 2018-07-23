package xyz.parala.game.model;

import xyz.parala.game.shader.ShaderProgram;

public interface Renderable {
	void draw(ShaderProgram shader);
}
