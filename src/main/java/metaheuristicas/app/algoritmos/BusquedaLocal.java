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

    @Override
    public String nombreAlgoritmo() {return "BusquedaLocal";}

}
