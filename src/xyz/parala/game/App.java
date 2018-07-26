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
	private List<Entity> entites;

	public App(String title, int width, int height) {
		this.title = title;
		this.height = height;
		this.width = width;
		thread = new Thread(this, "Game");
		thread.start();
	}

	public static void main(String[] args) {
		new App("MyGame", 800, 600);
	}

	@Override
	public void run() {
		camera = new Camera();
		window = new Window(title, width, height);
		String path = "/xyz/parala/game/shader/basic";
		renderer = new Renderer( new ShaderProgram(path + ".vs", path + ".fs"), width, height);
		entites = new ArrayList<Entity>();
		//glm::vec3(0.05f, 0.03f,0.02f), glm::vec3(1.0f),glm::vec3(0.8f),glm::vec3(1.0f, 0.0f, 0.0f)
		light = new Light(new Vector3f(10, 200, 10), new Vector3f(0.05f, 0.03f,0.02f), new Vector3f(1.0f), new Vector3f(0.8f), 1.0f, 0, 0);
		loop();
	}

	private void loop() {

		float vertices[] = { -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f, 0.0f, 0.5f, 0.0f };
		entites.add(new Quad(vertices));
		try {
			entites.add(new Entity(MeshLoader.load("eyeball.dae"), new Vector3f(0,0,-10), new Vector3f()));
			entites.add(new Entity(MeshLoader.load("nanosuit.dae"), new Vector3f(0,-10,-10), new Vector3f()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		while (!window.shouldClose()) {
			GL11.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
			window.clear();
			
			doLogic();
			light.setPosition(camera.getPosition());
			renderer.render(entites, camera, light);
			

			window.swapBuffers();
			GLFW.glfwPollEvents();
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
