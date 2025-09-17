package metaheuristicas.app;

public interface Algoritmo {

    String nombreAlgoritmo();
    int[] resolver(int[][] matriz1, int[][] matriz2);

    int[] resolver(int[][] matriz1, int[][] matriz2, Long semilla);

    int calcularCoste(int[][] matriz1,  int[][] matriz2, int[] S);

}
