package xyz.parala.game.util;

import java.util.List;

public class Lists {
	 public static int[] listIntToArray(List<Integer> list) {
	        int[] result = list.stream().mapToInt((Integer v) -> v).toArray();
	        return result;
	    }

	    public static float[] listToArray(List<Float> list) {
	        int size = list != null ? list.size() : 0;
	        float[] floatArr = new float[size];
	        for (int i = 0; i < size; i++) {
	            floatArr[i] = list.get(i);
	        }
	        return floatArr;
	}
}
