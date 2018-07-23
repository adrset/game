package xyz.parala.game.shader;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

/**
 * ShaderProgram class. Abstract class that loads and compiles GLSL shader.
 *
 * @author Adrian Setniewski
 *
 */

public class ShaderProgram {

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = ShaderLoader.getInstance().loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = ShaderLoader.getInstance().loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
	}

	protected int getUniformLocation(String uniformName) {
		int val =  GL20.glGetUniformLocation(programID, uniformName);
		if(val == -1) {
			// Helper check for developer :)
			System.err.println("SHADER::Could not bind to uniform variable in shader! (name: {" + uniformName + "})");
		}
		return val;
	}

	public void start() {
		GL20.glUseProgram(programID);
	}

	public void stop() {
		GL20.glUseProgram(0);
	}

	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}

	public void setUniform(String name, Vector3f vector) {
		GL20.glUniform3f(getUniformLocation(name), vector.x, vector.y, vector.z);
	}

	public void setUniform(String name, Vector2f vector) {
		GL20.glUniform2f(getUniformLocation(name), vector.x, vector.y);
	}
	
	public void setUniform(String name, float value) {
		GL20.glUniform1f(getUniformLocation(name), value);
	}
	
	public void setUniform(String name, boolean value) {
		float toLoad = value ? 1 : 0;
		GL20.glUniform1f(getUniformLocation(name), toLoad);
	}
	

	public void setUniform(String name, Matrix4f matrix) {
		GL20.glUniformMatrix4fv(getUniformLocation(name), false, matrix.get(matrixBuffer));
	}

}