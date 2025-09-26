package metaheuristicas.app.algoritmos;

public interface Algoritmo {

    String nombreAlgoritmo();

    int[] resolver(int[][] matriz1, int[][] matriz2, Long semilla, int k);

    /**
     * Calcula el coste total de una asignación de departamentos a localizaciones.
     * El coste se calcula como la suma de F[i][j] * D[S[i]-1][S[j]-1] para todos los pares (i,j).
     *
     * @param matriz1 Matriz de flujo entre departamentos.
     * @param matriz2 Matriz de distancias entre localizaciones.
     * @param S Asignación de departamentos a localizaciones (1-based).
     * @return Coste total de la asignación.
     */
    default int calcularCoste(int[][] matriz1,  int[][] matriz2, int[] S){
        int coste=0;
        int tam=matriz1.length;
        for(int i = 0; i < tam; i++)
            for(int j = 0; j < tam; j++)
                coste+=matriz1[i][j] * matriz2[S[i]-1][S[j]-1];

        return coste;
    }

    // Por defecto: determinista y no usa k
    default boolean usaSemilla() { return false; }
    default boolean usaK() { return false; }

}
