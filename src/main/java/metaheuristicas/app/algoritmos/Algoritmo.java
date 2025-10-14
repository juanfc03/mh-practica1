package metaheuristicas.app.algoritmos;

public interface Algoritmo {

    String nombreAlgoritmo();

    int[] resolver(int[][] flujos, int[][] distancias, String semilla, int k, int iteraciones);

    /**
     * Calcula el coste de una asignación entre flujos y distancias
     * @return Coste total de la asignación.
     */
    default int calcularCoste(int[][] flujos, int[][] distancias, int[] solucion){
        int coste = 0;
        int tam = flujos.length;

        for(int i = 0; i < tam; i++)
            for(int j = 0; j < tam; j++)
                coste += flujos[i][j] * distancias[solucion[i]-1][solucion[j]-1];

        return coste;
    }

    /**
     * Calcula el cambio en el coste (delta) al intercambiar i y j en la solución S.
     */
    default int deltaSwap(int[][] flujos, int[][] distancias, int[] solucion, int i, int j) {
        if (i == j) return 0;

        int tam = solucion.length;
        int posI = solucion[i] -1; // localización actual de i
        int posJ = solucion[j] -1; // localización actual de j

        int delta = 0;

        for (int k = 0; k < tam; k++) {
            if (k == i || k == j) continue;
            int posK = solucion[k] -1;

            delta += flujos[i][k] * (distancias[posJ][posK] - distancias[posI][posK]);
            delta += flujos[j][k] * (distancias[posI][posK] - distancias[posJ][posK]);

            delta += flujos[k][i] * (distancias[posK][posJ] - distancias[posK][posI]);
            delta += flujos[k][j] * (distancias[posK][posI] - distancias[posK][posJ]);
        }

        delta += flujos[i][j] * (distancias[posJ][posI] - distancias[posI][posJ]);
        delta += flujos[j][i] * (distancias[posI][posJ] - distancias[posJ][posI]);

        return delta;
    }

    /**
     * Calcula la importancia de cada departamento como la suma de su flujo hacia los demás
     * y la centralidad de cada localización como la suma de sus distancias hacia todas las demás
     */
    default void calculosImportanciaCentralidad(int[][] flujos, int[][] distancias, int tam, int[] importancia, int[] centralidad) {
        for(int i = 0; i < tam; i++)
            for(int j = 0; j < tam; j++){

                importancia[i] += flujos[i][j]*2;
                centralidad[i] += distancias[i][j]*2;

            }
    }

    default boolean requiereSemilla(){
        return true;
    }
}
