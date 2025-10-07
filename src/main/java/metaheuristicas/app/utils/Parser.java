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

    public static ArrayList<Integer> toArray(int[] array) {
        Integer[] result = new Integer[array.length];

        for (int i = 0; i < array.length; i++)
            result[i] = i;

        return new ArrayList<>(Arrays.asList(result));
    }
}
