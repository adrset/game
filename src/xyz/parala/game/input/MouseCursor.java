package xyz.parala.game.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

/**
 * MouseCursor class
 *
 * @author Adrian Setniewski
 *
 */

public class MouseCursor extends GLFWCursorPosCallback {
	private static double posX = 0;
	private static double posY = 0;
	private static double dx = 0;
	private static double dy = 0;
	private static boolean firstMouse = true;

	@Override
	public void invoke(long window, double xpos, double ypos) {
		 if (firstMouse)
		    {
		        posX = xpos;
		        posY = ypos;
		        firstMouse = false;
		    }
		dx = (float) (xpos - posX);
		dy = (float) (posY - ypos);

		posX = xpos;
		posY = ypos;
	}

	public static double getPosX() {
		return posX;
	}

	public static double getPosY() {
		return posY;
	}

	public double getDX() {
		double tmp = dx;
		dx = 0;
		return tmp;
	}

	public double getDY() {
		double tmp = dy;
		dy = 0;
		return tmp;
	}

}
