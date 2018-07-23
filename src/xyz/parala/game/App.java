package xyz.parala.game;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import xyz.parala.game.camera.Camera;
import xyz.parala.game.model.Entity;
import xyz.parala.game.model.Quad;
import xyz.parala.game.model.Renderable;
import xyz.parala.game.renderer.Renderer;
import xyz.parala.game.window.Window;

public class App implements Runnable {

	private Window window;
	private Thread thread;
	private Renderer renderer;
	private Camera camera;
	private int height, width;
	String title;
	private List<Entity> renderables;

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
		renderer = new Renderer("/xyz/parala/game/shader/basic", width, height);
		renderables = new ArrayList<Entity>();
		loop();
	}

	private void loop() {

		float vertices[] = { -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f, 0.0f, 0.5f, 0.0f };
		renderables.add(new Quad(vertices));
		while (!window.shouldClose()) {
			GL11.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
			window.clear();

			renderer.render(renderables, camera);

			window.swapBuffers();
			GLFW.glfwPollEvents();
		}

		window.close();
	}

}
