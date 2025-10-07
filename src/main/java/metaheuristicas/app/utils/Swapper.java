package metaheuristicas.app.utils;

public class Swapper {

    public static void intercambia(int[] solucion, int i, int j){
        int tmp = solucion[i];
        solucion[i] = solucion[j];
        solucion[j] = tmp;
    }

}
