package xyz.parala.game.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.joml.Vector3f;

/**
 * Loads and manages all available terrains.
 *
 * @param url  an absolute URL giving the base location of the image
 * @param name the location of the image, relative to the url argument
 * @return the list of Terrains in range
 * @see Terrain
 */

public class ChunkManager {
	
	static final int MAX_HEIGHT = 10;

	// in future chunks will be stored in region class

	int cacheForget = 10;

	Map<String, Chunk> cachedChunks = new TreeMap<>();;
	Set<Chunk> chunksToReturn = new HashSet<>();

	public ChunkManager() {

	}

	private Chunk loadChunk(int x, int y, int z, float offset) throws Exception {
		if(y> MAX_HEIGHT) return null;
		String key = x+","+y+","+z;
		Chunk ch = cachedChunks.get(key);
		if(ch!= null) {
			return ch;
		}
		
		ch = new Chunk(new Vector3f(x * 8f, y* 8f, z* 8f ));
		cachedChunks.put(key, ch);
		return ch;
	}

	private void saveChunk(Chunk ch) throws Exception {

	}

	/**
	 * Loads and manages all chunks to render.
	 *
	 * @param position position of a player
	 * @param radius
	 * @throws Exception
	 */

	public Set<Chunk> getChunks(Vector3f position, int radius) throws Exception {
		//chunksToReturn.clear();
		final float chunkOffset = Chunk.SIZE * 2.0f;

		Vector3f tmp = new Vector3f(position);
		tmp.y =0; 
		int i = 0, j = 0, k = 0;

		if (tmp.x >= 0) {
			i = (int) (tmp.x / chunkOffset);
		} else {
			i = (int) (tmp.x / chunkOffset) - 1;
		}
		
		if (tmp.y >= 0) {
			j = (int) (tmp.y / chunkOffset);
		} else {
			j = (int) (tmp.y / chunkOffset) - 1;
		}

		if (tmp.z >= 0) {
			k = (int) (tmp.z / chunkOffset);
		} else {
			k = (int) (tmp.z / chunkOffset) - 1;
		}
		
		int m = i- radius;
		int n = j- radius;
		int o = k- radius;
		System.out.println(cachedChunks.size());
		
		for(int ii = m; ii< m + 2* radius; ii++) {
			for(int jj = n; jj< n + 2* radius; jj++) {
				for(int kk = o; kk< o + 2* radius; kk++) {
					Chunk ch = loadChunk(ii,jj,kk, chunkOffset);
					if(ch!= null)
						chunksToReturn.add(ch);
				}
			}
		}
		
		Iterator<Chunk> tt = chunksToReturn.iterator();
		while(tt.hasNext()) {
			Chunk t = tt.next();
			int x=0,z=0;
			x = (int) t.getPosition().x / 8;
			z = (int) t.getPosition().z / 8;
			
			if(Math.pow(i-x,2) + Math.pow(k-z,2) > cacheForget*cacheForget ) {
				tt.remove();
				
			}
			
		}
		

		return chunksToReturn;
	}

}
