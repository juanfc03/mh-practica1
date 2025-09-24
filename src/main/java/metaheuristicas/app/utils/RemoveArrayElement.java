package metaheuristicas.app.utils;

public class RemoveArrayElement {
    public static Object[] removeElement(Object[] array, int i) {
        System.arraycopy(array, i + 1, array,i, array.length - 1 - i);
        return array;
    }
}
