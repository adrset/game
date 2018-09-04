package xyz.parala.game.model;

import java.util.LinkedList;
import java.util.List;

import org.joml.Vector3f;

public class Chunk {

	public static final int SIZE = 4;
	private List<Box> boxes;
	
	// position in relation to world
	// each box position will be relative to it
	private Vector3f position;
	public final static float SCALE = 1.0f;
	
	public Chunk(Vector3f position) {
		this.boxes = generateBoxes();
		this.position = position;
	}
	
	private static List<Box> generateBoxes(){
		float scale = SCALE;
		List<Box> boxes = new LinkedList<>();
		for(int ii=0;ii<Chunk.SIZE;ii++) {
			for(int jj=0;jj<Chunk.SIZE;jj++) {
				for(int kk=0;kk<Chunk.SIZE;kk++) {
					Vector3f vec = new Vector3f(scale * (float)ii *2.0f, scale *  (float)jj*2.0f, scale * 2.0f  *  (float)kk);
					boxes.add(new Box(vec, new Vector3f(), scale));
				}
			}
		}
		
		return boxes;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public Vector3f getPosition() {
		return this.position;
	}

	public List<Box> getBoxes() {
		return this.boxes;
	}

}
