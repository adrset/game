package xyz.parala.game;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import xyz.parala.game.camera.Camera;
import xyz.parala.game.input.Keyboard;
import xyz.parala.game.light.Light;
import xyz.parala.game.model.Entity;
import xyz.parala.game.model.MeshLoader;
import xyz.parala.game.model.Quad;
import xyz.parala.game.model.Renderable;
import xyz.parala.game.model.Terrain;
import xyz.parala.game.model.TerrainManager;
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
	List<Terrain> terrains;

	public App(String title, int width, int height, boolean fullScreen) {
		this.fullScreen = fullScreen;
		this.title = title;
		this.height = height;
		this.width = width;
		thread = new Thread(this, "Game");
		thread.start();
	}

	public static void main(String[] args) {
		new App("MyGame", 200, 120, false);
	}

	@Override
	public void run() {
		camera = new Camera();
		camera.setPosition(new Vector3f(0, 20, 0));
		window = new Window(title, width, height, fullScreen);
		String path = "/xyz/parala/game/shader/basic";
		renderer = new Renderer( new ShaderProgram(path + ".vs", path + ".fs"), width, height);
		entites = new ArrayList<Entity>();
		//glm::vec3(0.05f, 0.03f,0.02f), glm::vec3(1.0f),glm::vec3(0.8f),glm::vec3(1.0f, 0.0f, 0.0f)
		try {
			light = new Light(MeshLoader.load("nanosuit.dae"), new Vector3f(10, 200, 10), new Vector3f(0.05f, 0.03f,0.02f), new Vector3f(1.0f), new Vector3f(0.8f), 1.0f, 0, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loop();
	}

	private void loop() {
		TerrainManager t = null;
		try {
			t = new TerrainManager();
		 

		float vertices[] = { -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f, 0.0f, 0.5f, 0.0f };
		entites.add(new Quad(vertices));
		
			entites.add(new Entity(MeshLoader.load("nanosuit.dae"), new Vector3f(0,-10,-10), new Vector3f()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		while (!window.shouldClose()) {
			double time = System.nanoTime();
			try {
				terrains = t.getTerrains(camera.getPosition(), 3);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			window.clear();
			//GLFW.glfwSetCursorPos(window.getID(), width / 2, height / 2);
			//GLFW.glfwd
			doLogic();
			light.setPosition(new Vector3f((float) (100.0f * Math.cos(time)), 10.0f, (float) (100.0f * Math.sin(time += 0.03))));
			List<Entity> ents = new ArrayList<Entity>(entites);
			ents.addAll(terrains);
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
