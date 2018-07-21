package xyz.parala.game.model;

import java.util.HashMap;
import java.util.Map;

public class MeshCache {

	private static MeshCache INSTANCE;

	private Map<String, Mesh[]> meshMap;

	private MeshCache() {
		meshMap = new HashMap<>();
	}

	public static synchronized MeshCache getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MeshCache();
		}
		return INSTANCE;
	}

	public Mesh[] getMesh(String path) throws Exception {
		Mesh[] mesh = meshMap.get(path);
		if (mesh == null) {
			mesh = MeshLoader.load(path);
			meshMap.put(path, mesh);
		}
		return mesh;
	}
}
