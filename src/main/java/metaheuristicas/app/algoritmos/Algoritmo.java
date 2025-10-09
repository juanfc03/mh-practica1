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
