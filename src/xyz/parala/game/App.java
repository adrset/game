package xyz.parala.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import xyz.parala.game.camera.Camera;
import xyz.parala.game.input.Keyboard;
import xyz.parala.game.light.Light;
import xyz.parala.game.model.Chunk;
import xyz.parala.game.model.ChunkManager;
import xyz.parala.game.model.Entity;
import xyz.parala.game.model.MeshLoader;
import xyz.parala.game.model.Renderable;
import xyz.parala.game.model.Terrain;
import xyz.parala.game.model.TerrainManager;
import xyz.parala.game.renderer.InstanceRenderer;
import xyz.parala.game.renderer.Renderer;
import xyz.parala.game.shader.ShaderProgram;
import xyz.parala.game.window.Window;

public class App implements Runnable {

	private Window window;
	private Thread thread;
	private Renderer renderer;
	private Camera camera;
	private int height, width;
	private Light light;
	String title;
	float time = 0.0f;
	boolean fullScreen = false;
	private List<Entity> entites;
	InstanceRenderer iRenderer;
	ChunkManager chunkManager;
	Set<Chunk> chunks;
	boolean enabled =false;
	int iteration = 1;
	float sum = 0;

	public App(String title, int width, int height, boolean fullScreen) {
		this.fullScreen = fullScreen;
		this.title = title;
		this.height = height;
		this.width = width;
		thread = new Thread(this, "Game");
		thread.start();
	}

	public static void main(String... args) {
		new App("MyGame", 400, 300, false);
	}

	@Override
	public void run() {
		chunkManager = new ChunkManager();
		camera = new Camera();
		camera.setPosition(new Vector3f(0, 20, 0));
		window = new Window(title, width, height, fullScreen);
		String path = "/xyz/parala/game/shader/";
		try {
		renderer = new Renderer( new ShaderProgram(path + "basic.vs", path + "basic.fs"), width, height);
		iRenderer = new InstanceRenderer( new ShaderProgram(path + "instance.vs", path + "instance.fs"), width, height, MeshLoader.load("CrateModel.dae"));
		entites = new ArrayList<Entity>();
		//for(int i=0;i<1000;i++) { // 10 chunks = 10*4^3 boxes
		//	chunks.add(new Chunk(new Vector3f(i*Chunk.SIZE*scale*2.00f, 0,0)));
		//}
			
			light = new Light(null, new Vector3f(10, 200, 10), new Vector3f(0.05f, 0.03f,0.02f), new Vector3f(1.0f), new Vector3f(0.8f), 1.0f, 0, 0);
		} catch (Exception e) {

			e.printStackTrace();
		}
		loop();
	}

	private void loop() {
		TerrainManager t = null;
		try {
			t = new TerrainManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		int kl = 2;
		
		while (!window.shouldClose()) {
			double time = System.nanoTime();
			try {
				
				if (Keyboard.isKeyPressedOnce(GLFW.GLFW_KEY_U)) {
					kl++;
				}else if(Keyboard.isKeyPressedOnce(GLFW.GLFW_KEY_I)) {
					kl--;
				}
				chunks = chunkManager.getChunks(camera.getPosition(), kl);
				System.out.println(chunks.size()*64);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
			window.clear();

			doLogic();
			light.setPosition(new Vector3f((float) (100.0f * Math.cos(this.time)), 10.0f, (float) (100.0f * Math.sin(this.time += 0.03))));
			List<Entity> ents = new ArrayList<Entity>(entites);
		
			iRenderer.renderChunks(chunks, camera, light);
			renderer.render(ents, camera, light);
			window.swapBuffers();
			GLFW.glfwPollEvents();
			
			double time2 = System.nanoTime() - time;
			time2*= Math.pow(10, -9);
			
			System.out.println(1.0/time2);
			
		}

		window.close();
	}
	
	private void doLogic() {
		
		for(Renderable e: entites) {
			e.update(renderer.getShader());
		}
		camera.update(renderer.getShader());
		
		if(Keyboard.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			window.requestClose();
		}
	}

}
