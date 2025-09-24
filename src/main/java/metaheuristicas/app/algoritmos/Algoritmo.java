package metaheuristicas.app.algoritmos;

public interface Algoritmo {

    String nombreAlgoritmo();

    int[] resolver(int[][] matriz1, int[][] matriz2, Long semilla, int k);

    int calcularCoste(int[][] matriz1,  int[][] matriz2, int[] S);

    // Por defecto: determinista y no usa k
    default boolean usaSemilla() { return false; }
    default boolean usaK() { return false; }

}
