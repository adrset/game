package xyz.parala.game.shader;

public class WorldShader extends ShaderProgram {

	public WorldShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);

	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");// bind variable from shader
		super.bindAttribute(1, "texture_coord");
		super.bindAttribute(2, "normal");
	}

}
