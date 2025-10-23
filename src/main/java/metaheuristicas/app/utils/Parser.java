package metaheuristicas.app.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {

    public static int toInt(String cadena) {
        return Integer.parseInt(cadena);
    }

    public static Long toLong(String cadena) {
        return Long.parseLong(cadena);
    }

    public static float toFloat(String cadena) {
        return Float.parseFloat(cadena);
    }

    public static String toString(int number) {
        return String.valueOf(number);
    }

    public static ArrayList<Integer> toArray(int[] array) {
        Integer[] result = new Integer[array.length];

        for (int i = 0; i < array.length; i++)
            result[i] = i;

        return new ArrayList<>(Arrays.asList(result));
    }

    public static String solucionToString(int[] solucion) {
        return Arrays.toString(solucion);
    }

}
