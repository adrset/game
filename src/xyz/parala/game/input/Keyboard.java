package xyz.parala.game.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * Keyboard Class.
 *
 * @author Adrian Setniewski
 *
 */

public class Keyboard extends GLFWKeyCallback{

	public static boolean[] keys = new boolean[512];
	public static boolean[] keysPressed = new boolean[512];
	
	@Override
	public void invoke(long windows, int key, int scancode, int action, int mode) {
		if(key != -1) // some keyboard buttons give key code = -1, that's not a problem, because they are used rather by system than game.
			keys[key] = (action != GLFW.GLFW_RELEASE);
		
	}
	
	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
	
	public static boolean isKeyPressedOnce(int button) {
		if (!keys[button]) {
			keysPressed[button] = false;
			return keys[button];

		} else {
			if (!keysPressed[button]) {
				keysPressed[button] = true;
				return keys[button];
			} else {
				return false;
			}
		}

	}

}
