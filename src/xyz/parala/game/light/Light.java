package xyz.parala.game.light;

import org.joml.Vector3f;

import xyz.parala.game.model.Entity;
import xyz.parala.game.model.Mesh;
import xyz.parala.game.shader.ShaderProgram;

public class Light extends Entity {
	
	Vector3f ambient;
	Vector3f specular;
	Vector3f diffuse;
	float constant;
	float linear;
	float quadratic;
	
	public Light(Vector3f position, Vector3f ambient, Vector3f specular, Vector3f diffuse, float constant, float linear, float quadratic) {
		super(null, position, new Vector3f());
		this.diffuse = diffuse;
		this.ambient = ambient;
		this.specular = specular;
		this.linear = linear;
		this.quadratic = quadratic;
		this.constant = constant;
	}
	
	public Light(Mesh[] meshes, Vector3f position, Vector3f ambient, Vector3f specular, Vector3f diffuse, float constant, float linear, float quadratic) {
		super(meshes, position, new Vector3f());
		this.diffuse = diffuse;
		this.ambient = ambient;
		this.specular = specular;
		this.linear = linear;
		this.quadratic = quadratic;
		this.constant = constant;
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
	
	
	
		
	
	

}
