package xyz.parala.game.window;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_HIDDEN;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import xyz.parala.game.input.Keyboard;
import xyz.parala.game.input.Mouse;

public class Window {
	long windowID;
	String title;
	boolean mode;
	int width;
	int height;
	boolean forceClose;
	

	public Window(String name, int desiredWidth, int desiredHeight) {
		this.title = name;
		this.width = desiredWidth;
		this.height = desiredHeight;
		forceClose = false;
		init();
	}
	
	public long getID() {
		return windowID;
	}
	
	public void requestClose() {
		forceClose = true;
	}
	
	public void centerCursor() {
		glfwSetCursorPos(windowID, width / 2, height / 2);
	}
	
	public void init() {

		if (!glfwInit()) {
			throw new RuntimeException("glfw_init_failed");
		}

		// Set error printing to System.err
		GLFWErrorCallback.createPrint(System.err).set();

		System.out.println("glfw_version " + Version.getVersion() + "!");

		// Set window to not resizable
		glfwWindowHint(GLFW_RESIZABLE, GL11.GL_FALSE); // TODO: after resizing the window, apply changes to renderer in
														// order to render into entire window

		// Create window
		
		windowID = glfwCreateWindow(width, height, title, NULL, NULL);
		

		glfwShowWindow(windowID);

		// center the window
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(windowID, (int) ((vidmode.width() - width) * 0.5), (int) ((vidmode.height() - height) * 0.5));

		// openGL calls now available only for this thread
		glfwMakeContextCurrent(windowID);
		
		// very important
		GL.createCapabilities();

		// Set input callbacks
		glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
		glfwSetKeyCallback(windowID, new Keyboard());
		glfwSetCursorPosCallback(windowID, Mouse.mouseCursor);
		glfwSetScrollCallback(windowID, Mouse.mouseScroll);
		glfwSetMouseButtonCallback(windowID, Mouse.mouseButtons);
	}
	
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
	}
	
	public boolean shouldClose() {
		return (glfwWindowShouldClose(windowID) || forceClose);
	}
	
	public void swapBuffers() {
		glfwSwapBuffers(windowID);
	}
	
	public void close() {
		glfwFreeCallbacks(windowID);
		glfwDestroyWindow(windowID);
		// Terminate GLFW
		glfwTerminate();
	}

}
