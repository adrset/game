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
	static final int SIZE = 400;
	static final int MAX_PIXEL_COLOR = 256*256*256;
	static final float MAX_HEIGHT = 10;

	int vaoId;
	int vertexCount;
	Set<Integer> vbos = new HashSet<>();
	
	public Terrain(String file) throws Exception{
		this(Utils.ioResourceToByteBuffer(file), file);
	}
	
	public Terrain(ByteBuffer imageData, String filePath) throws Exception {
		super(null, new Vector3f(0.0f, -10.0f, 0.0f), new Vector3f());
		// in future pass a mesh after computing indices etc.
		  
	            BufferedImage image = null;
	            image = ImageIO.read(new File(Class.class.getResource(filePath).getPath()));
	            int VERTEX_COUNT = image.getHeight();
	           // if(image.getWidth() != image.getHeight()) {
	           // 	throw new Exception("Terrain: image must be a square!");
	            //}
	            int count = VERTEX_COUNT * VERTEX_COUNT;
	    		float[] vertices = new float[count * 3];
	    		float[] normals = new float[count * 3];
	    		float[] textureCoords = new float[count*2];
	    		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
	    		int vertexPointer = 0;
	    		for(int i=0;i<VERTEX_COUNT;i++){
	    			for(int j=0;j<VERTEX_COUNT;j++){
	    				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
	    				vertices[vertexPointer*3+1] = getHeight(j, i, image);
	    				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
	    				normals[vertexPointer*3] = 0;
	    				normals[vertexPointer*3+1] = 1;
	    				normals[vertexPointer*3+2] = 0;
	    				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
	    				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
	    				vertexPointer++;
	    			}
	    		}
	    		int pointer = 0;
	    		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
	    			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
	    				int topLeft = (gz*VERTEX_COUNT)+gx;
	    				int topRight = topLeft + 1;
	    				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
	    				int bottomRight = bottomLeft + 1;
	    				indices[pointer++] = topLeft;
	    				indices[pointer++] = bottomLeft;
	    				indices[pointer++] = topRight;
	    				indices[pointer++] = topRight;
	    				indices[pointer++] = bottomLeft;
	    				indices[pointer++] = bottomRight;
	    			}
	    		}
	    		TextureCache textCache = TextureCache.getInstance();
	    		Texture texture = textCache.getTexture(filePath);
	    		Material material = new Material(new Vector4f(), new Vector4f(), new Vector4f(), 1.0f);
	    		material.setTexture(texture);
	    		
	    		vertexCount = indices.length;

	    		Mesh[] m = new Mesh[1];
	    		m[0] = new Mesh(vertices, textureCoords, normals, indices);
	    		m[0].setMaterial(material);
	    		super.setMeshes(m);
	    		
	            
		 
	}
	
	public static float getHeight(int x, int z, BufferedImage image) {
		if(x<0 || x>= image.getHeight() || z <0 || z>image.getHeight()) {
			return 0;
		}
		
		float height =  image.getRGB(x, z);
		height+= MAX_PIXEL_COLOR/2f;
		height/= MAX_PIXEL_COLOR/2f;
		return height *=MAX_HEIGHT;
		
	}
	


	@Override
	public void update(ShaderProgram shader) {
		// TODO Auto-generated method stub

	}

}
