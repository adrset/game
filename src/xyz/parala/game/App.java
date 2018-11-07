package xyz.parala.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import xyz.parala.game.camera.Camera;
import xyz.parala.game.input.Keyboard;
import xyz.parala.game.light.Light;
import xyz.parala.game.model.Entity;
import xyz.parala.game.model.MeshLoader;
import xyz.parala.game.model.Renderable;
import xyz.parala.game.renderer.InstanceRenderer;
import xyz.parala.game.renderer.Renderer;
import xyz.parala.game.shader.ShaderProgram;
import xyz.parala.game.util.Timer;
import xyz.parala.game.window.Window;

public class App implements Runnable {

	private Window window;
	private Thread thread;
	private Renderer renderer;
	private String file = null;
	private Camera camera;
	private int height, width;
	private Light light;
	String title;
	int iteration = 0;
	boolean fullScreen = false;
	private List<Entity> entites;
	private List<Vector3f> positions;
	private Set<Entity> spheres;
	final int N = 125;
	final int FPS_OUT = 50;
	int loop = 0;
	InstanceRenderer iRenderer;
	Timer timer;

	public App(String title, int width, int height, boolean fullScreen, String file) {
		this.fullScreen = fullScreen;
		this.title = title;
		this.file = file;
		this.height = height;
		this.width = width;
		spheres = new HashSet<>();
		positions = new ArrayList<>();
		thread = new Thread(this, "Game");
		thread.start();
		timer = new Timer();
	}

	public static void main(String... args) {
		if (args.length >= 1) {
			String file = (args[0]);

			new App("MyGame", 800, 600, false, file);
		}else {
			System.out.println("Please specify xyz-data file!");
		}
	}

	@Override
	public void run() {
		camera = new Camera();
		camera.setPosition(new Vector3f(0, 0, 3));
		window = new Window(title, width, height, fullScreen);

		try {
			String path = "/xyz/parala/game/shader/";
			renderer = new Renderer(new ShaderProgram(path + "basic.vs", path + "basic.fs"), width, height);

			iRenderer = new InstanceRenderer(new ShaderProgram(path + "instance.vs", path + "instance.fs"), width,
					height, MeshLoader.load("untitled.dae"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		String fileName = "/xyz/parala/game/data/xyz.dat";

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				String vs[] = line.split(" ");

				if (!line.isEmpty())
					positions.add(
							new Vector3f(Float.parseFloat(vs[0]), Float.parseFloat(vs[1]), Float.parseFloat(vs[2])));
			}

			for (int ii = 0; ii < N; ii++) {
				spheres.add(new Entity(null, new Vector3f(ii, 0, 0), new Vector3f(), 0.19f));
			}
			iteration++;

		} catch (IOException e) {
			e.printStackTrace();
		}

		entites = new ArrayList<Entity>();
		light = new Light(null, new Vector3f(10, 200, 10), new Vector3f(0.05f, 0.03f, 0.02f), new Vector3f(1.0f),
				new Vector3f(0.8f), 1.0f, 0, 0);

		try {
			entites.add(new Entity(MeshLoader.load("untitled.dae"), new Vector3f(0, 0, 0), new Vector3f(), 2.3f));
			entites.get(entites.size() - 1).getMeshes()[0].setLines(true);
			loop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loop() throws Exception {

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		while (!window.shouldClose()) {
			timer.start();

			GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
			window.clear();

			doLogic();

			iRenderer.renderSpheres(spheres, camera, light);
			light.setPosition(camera.getPosition());
			renderer.render(entites, camera, light);
			window.swapBuffers();
			GLFW.glfwPollEvents();
			if (loop++ % FPS_OUT == 0) {
				System.out.println(1.0f / timer.stop());
			}

		}

		window.close();
	}

	private void doLogic() {

		int ii = 0;
		Iterator<Entity> iter = spheres.iterator();
		if (loop % 5 == 0) {
			while (iter.hasNext()) {
				Entity e = iter.next();
				e.setPosition(positions.get((ii + N * iteration) % positions.size()));

				ii++;
			}
		}

		iteration++;

		for (Renderable e : entites) {
			e.update(renderer.getShader());
		}
		camera.update(renderer.getShader());

		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			window.requestClose();
		}
	}

}
