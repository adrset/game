package xyz.parala.game.light;

import org.joml.Vector3f;

import xyz.parala.game.model.Renderable;
import xyz.parala.game.shader.ShaderProgram;

public class Light implements Renderable {
	
	Vector3f ambient;
	Vector3f specular;
	Vector3f diffuse;
	float constant;
	float linear;
	float quadratic;
	Vector3f position;
	
	public Light(Vector3f position, Vector3f ambient, Vector3f specular, Vector3f diffuse, float constant, float linear, float quadratic) {
		this.position = position;
		this.diffuse = diffuse;
		this.ambient = ambient;
		this.specular = specular;
		this.linear = linear;
		this.quadratic = quadratic;
		this.constant = constant;
	}

	@Override
	public void draw(ShaderProgram shader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ShaderProgram shader) {
		shader.setUniform("pointLight.position", position);
		shader.setUniform("pointLight.ambient", ambient);
		shader.setUniform("pointLight.specular", specular);
		shader.setUniform("pointLight.diffuse", diffuse);
		shader.setUniform("pointLight.constant", constant);
		shader.setUniform("pointLight.linear", linear);
		shader.setUniform("pointLight.quadratic", quadratic);
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	
	
		
	
	

}
