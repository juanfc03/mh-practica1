package metaheuristicas.app.algoritmos;

import metaheuristicas.app.utils.Swapper;

public class BusquedaLocal implements Algoritmo{

    @Override
    public int[] resolver(int[][] flujos, int[][] distancias, String semilla, int k, int iteraciones){

        GreedyAleatorio randomGreedy = new GreedyAleatorio();
        int[] solucion = randomGreedy.resolver(flujos, distancias, semilla, k, iteraciones);

        int n = solucion.length;
        int[] dlb = new int[n];
        int it = 0;
        boolean hubo_mejora = true;

        while (it < iteraciones && hubo_mejora) {

            hubo_mejora = false;

            for (int i = 0; i < n; i++) {
                if (dlb[i] == 1) continue;

                boolean mejoraI = false;

                for (int j = i + 1; j < n; j++) {

                    int delta = deltaSwap(flujos, distancias, solucion, i, j);
                    if (delta <0 ) {
                        Swapper.intercambia(solucion, i, j);
                        dlb[i] = 0;
                        dlb[j] = 0;
                        hubo_mejora = true;
                        mejoraI = true;
                        it++;
                        break;
                    }
                }

                if (!mejoraI) dlb[i] = 1;
                else break;
            }
        }

        return solucion;
    }

    /**
     * Calcula el cambio en el coste (delta) al intercambiar i y j en la solución S.
     */
    private int deltaSwap(int[][] flujos, int[][] distancias, int[] solucion, int i, int j) {
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

    @Override
    public String nombreAlgoritmo() {return "BusquedaLocal";}

}
