package xyz.parala.game.chunks;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.joml.Vector3f;

public class Chunk {

	public static final int SIZE = 8;
	public static final int HEIGHT = 128;
	private List<Box> boxes;
	
	// position in relation to world
	// each box position will be relative to it
	private Vector3f position;
	static Random rand = new Random();
	public final static float SCALE = 1.0f;
	
	public Chunk(Vector3f position) {
		this.boxes = generateBoxes();
		this.position = position;
	}
	
	private static List<Box> generateBoxes(){
		float scale = SCALE;
		List<Box> boxes = new LinkedList<>();
		for(int ii=0;ii<Chunk.SIZE;ii++) {
			for(int jj=0;jj<Chunk.HEIGHT;jj++) {
				for(int kk=0;kk<Chunk.SIZE;kk++) {
					Vector3f vec = new Vector3f(scale * (float)ii *2.0f, scale *  (float)jj*2.0f, scale * 2.0f  *  (float)kk);
					boxes.add(new Box(vec, new Vector3f(), scale, new Vector3f(rand.nextFloat(),rand.nextFloat(),rand.nextFloat())));
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
