package metaheuristicas.app.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Sorter {
    private Sorter() {}

    public static ArrayList<Integer> sortAsc(int[] array) {
        Integer[] ordered = new Integer[array.length];

        for (int i = 0; i < array.length; i++)
            ordered[i] = i;

        Arrays.sort(ordered, Comparator.comparingInt(a -> array[a]));

        return new ArrayList<>(Arrays.asList(ordered));
    }

    public static ArrayList<Integer> sortDesc(int[] array) {
        Integer[] ordered = new Integer[array.length];

        for (int i = 0; i < ordered.length; i++)
            ordered [i] = i;

        Arrays.sort(ordered, (a, b) -> Integer.compare(array[b], array[a]));

        return new ArrayList<>(Arrays.asList(ordered));
    }

}
