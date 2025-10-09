package metaheuristicas.app.utils;

import java.util.ArrayList;

public class Sorter {

    public static ArrayList<Integer> sortAsc(int[] array) {
        ArrayList<Integer> idx = new ArrayList<>();
        for (int i = 0; i < array.length; i++) idx.add(i);
        idx.sort(java.util.Comparator.comparingInt(i -> array[i]));
        return idx;
    }

    public static ArrayList<Integer> sortDesc(int[] array) {
        ArrayList<Integer> idx = new ArrayList<>();
        for (int i = 0; i < array.length; i++) idx.add(i);
        idx.sort((a, b) -> Integer.compare(array[b], array[a]));
        return idx;
    }

}