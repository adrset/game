package xyz.parala.game.model;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Loads and manages all available terrains.
 *
 * @param  url  an absolute URL giving the base location of the image
 * @param  name the location of the image, relative to the url argument
 * @return      the image at the specified URL
 * @see         Image
 */

public class TerrainManager {

	static final int SIZE = 1000;
	static final int MAX_PIXEL_COLOR = 256*256*256;
	static final float MAX_HEIGHT = 10;
	Mesh[] meshes = null;
	
	Map<String, Terrain> cachedTerrains;
	List<Terrain> terrainsToReturn = new ArrayList<Terrain>();
	
	int VERTEX_COUNT = 200;
	public TerrainManager() throws Exception {
		cachedTerrains = new HashMap<String, Terrain>();
		int count = VERTEX_COUNT * VERTEX_COUNT;
 		float[] vertices = new float[count * 3];
 		float[] normals = new float[count * 3];
 		float[] textureCoords = new float[count*2];
 		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
 		int vertexPointer = 0;
 		for(int i=0;i<VERTEX_COUNT;i++){
 			for(int j=0;j<VERTEX_COUNT;j++){
 				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
 				vertices[vertexPointer*3+1] = 0;
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
 		meshes = new Mesh[1];
 		meshes[0] = new Mesh(vertices, textureCoords, normals, indices);
 		TextureCache textCache = TextureCache.getInstance();
 		Texture texture = textCache.getTexture("/xyz/parala/game/texture/indeks.jpg");
 		Material material = new Material(new Vector4f(), new Vector4f(), new Vector4f(), 1.0f);
 		material.setTexture(texture);


 		
 		meshes[0].setMaterial(material);
	
	}
	
	
	private Terrain getTerrainFromMap(int i, int j) throws Exception {
		String key = i + "," + j;
		Terrain t = cachedTerrains.get(key);
		if (t == null) {
			t = new Terrain(meshes, new Vector3f(i*SIZE, 0, j*SIZE));
			cachedTerrains.put(key, t);
		}
		return t;
	}
	

	private Terrain generateTerrain(Vector3f pos) throws Exception {
       
        
 		
 		return new Terrain(meshes, pos);
	}
	
	public Terrain loadTerrain() {
		return null;
	}
	
	private void saveTerrain(Terrain t) {
		
	}
	
	/**
	 * Loads and manages all available terrains.
	 *
	 * @param  position  posiiton of a player
	 * @param  radius set the boundaries the amount of terrains to be returned. ( passing zero means no terrains will be returned)
	 * @return      the image at the specified URL
	 * @throws Exception 
	 * @see         Image
	 */
	
	public List<Terrain> getTerrains(Vector3f position, int radius) throws Exception {
		if(radius == 0)
			return null;
		
		// decrementing to simplify the calculations
		radius--;
		
		Vector3f tmp = new Vector3f(position);
		
		tmp.y = 0;
		int i=0, j=0;
		
		if(tmp.x>=0) {
			i = (int) (tmp.x/SIZE);
		}else {
			i = (int) (tmp.x/SIZE) - 1;
		}
		
		if(tmp.z>=0) {
			j = (int) (tmp.z/SIZE);
		}else {
			j = (int) (tmp.z/SIZE) - 1;
		}
		
		int k = i- radius;
		int l = j- radius;
		terrainsToReturn.clear();
		
		for(int ii = k; ii< k + 2* radius; ii++) {
			for(int jj = l; jj< l + 2* radius; jj++) {
				
				//System.out.println(ii + "," + jj);
				terrainsToReturn.add(getTerrainFromMap(ii, jj));
			}
		}
		

		System.out.println("Rendering " + terrainsToReturn.size() + " terrains! Cached: " + cachedTerrains.size());
		
		return terrainsToReturn;
	}

}
