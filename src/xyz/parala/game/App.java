package xyz.parala.game;

import java.util.LinkedList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import xyz.parala.game.model.Renderable;
import xyz.parala.game.renderer.Renderer;
import xyz.parala.game.window.Window;

public class App implements Runnable {
	
	private Window window;
	private Thread thread;
	private Renderer renderer;
	private int height, width;
	String title;
	
	public App( String title, int width, int height) {
		this.title = title;
		this. height = height;
		this.width = width;
		thread = new Thread(this, "Game");
		thread.start();
	}

	public static void main(String[] args) {
		new App("MyGame", 800, 600);
	}

	@Override
	public void run() {
		window = new Window(title, width, height);
		renderer = new Renderer("/xyz/parala/game/shader/basic");
		loop();
	}
	
	private void loop() {
		GL11.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		while(!window.shouldClose()) {
			window.clear();
			
			renderer.render(new LinkedList<Renderable>());
			
			window.swapBuffers();
			GLFW.glfwPollEvents();
		}
		
		window.close();
	}

}
