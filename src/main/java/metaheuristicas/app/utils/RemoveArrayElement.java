package metaheuristicas.app.utils;

import java.util.Arrays;

public class RemoveArrayElement {

    public static Integer[] removeElement(Integer[] array, int i) {
        Integer[] res = Arrays.copyOf(array, array.length - 1);
        // copia [0, i)
        System.arraycopy(array, 0, res, 0, i);
        // copia (i, end]
        if (i < array.length - 1) {
            System.arraycopy(array, i + 1, res, i, array.length - i - 1);
        }
        return res;
    }
}
