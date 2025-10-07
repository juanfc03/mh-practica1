package metaheuristicas.app.utils;

import java.util.ArrayList;

public class Sorter {

    public static ArrayList<Integer> sortAsc(int[] array) {
        var ordered = Parser.toArray(array);

        ordered.sort(null);

        return ordered;
    }

    public static ArrayList<Integer> sortDesc(int[] array) {
        var ordered = Parser.toArray(array);

        ordered.sort((a, b) -> -1 * a.compareTo(b));

        return ordered;
    }

}