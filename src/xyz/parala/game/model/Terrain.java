package xyz.parala.game.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.joml.Vector3f;
import org.joml.Vector4f;

import xyz.parala.game.shader.ShaderProgram;
import xyz.parala.game.util.Utils;

public class Terrain extends Entity {


	
	public Terrain(Mesh[] meshes, Vector3f position) throws Exception {
		super(meshes, position, new Vector3f());
		// in future pass a mesh after computing indices etc.
		  
	}
	


	@Override
	public void update(ShaderProgram shader) {
		// TODO Auto-generated method stub

	}

}
