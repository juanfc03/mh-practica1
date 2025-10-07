package metaheuristicas.app.algoritmos;

import metaheuristicas.app.utils.Swapper;

public class BusquedaLocal implements Algoritmo{

    @Override
    public int[] resolver(int[][] flujos, int[][] distancias, String semilla, int k){

        GreedyAleatorio randomGreedy = new GreedyAleatorio();
        int[] solucion = randomGreedy.resolver(flujos, distancias, semilla, k);

        int n = solucion.length;
        int[] dlb = new int[n];      // 0 por defecto
        int iteraciones = 0;         // nº de vecinos evaluados (presupuesto 5000)
        boolean hubo_mejora = true;

        //int costeActual = calcularCoste(matriz1, matriz2, solucion);

        // 4) Búsqueda local: primer-mejor + DLB + límite 5000 evaluaciones
        while (iteraciones < 5000 && hubo_mejora) {

            hubo_mejora = false;

            for (int i = 0; i < n; i++) {
                if (dlb[i] == 1) continue; // DLB: saltar i si está desactivado

                boolean mejoraI = false;

                for (int j = i + 1; j < n; j++) {
                    // Evaluar vecino (i, j): swap temporal
                    //intercambia(solucion, i, j);
                    int delta = deltaSwap(flujos, distancias, solucion, i, j);

                    // Recalcular coste completo tras el swap
                    //int nuevoCoste = calcularCoste(matriz1, matriz2, solucion);
                    if (delta <0 ) {
                        // Aceptar PRIMERA mejora
                        Swapper.intercambia(solucion, i, j);
                        //costeActual +=delta;
                        dlb[i] = 0;
                        dlb[j] = 0;
                        hubo_mejora = true;
                        mejoraI = true;

                        iteraciones++; // hemos consumido una evaluación

                        // Primer-mejor: mantener el swap y reiniciar desde i=0
                        break;
                    } //else {
                        // No mejora: revertir swap
                      //  intercambia(solucion, i, j);
                    //}

                    // Presupuesto agotado: sal de j; for i saldrá abajo
                    if (iteraciones >= 5000) break;
                }

                // Si para i no hubo ninguna mejora, desactivar con DLB
                if (!mejoraI) dlb[i] = 1;

                // Si mejora o agoto presupuesto, reinicia pasada o termina
                if (mejoraI || iteraciones == 5000) break;
            }
        }

        return solucion;
    }

    /**
     * Calcula el cambio en el coste (delta) al intercambiar i y j en la solución S.
     */
    // FIXME: revisar lógica
    private int deltaSwap(int[][] flujos, int[][] distancias, int[] solucion, int i, int j) {
        if (i == j) return 0;

        int tam = solucion.length;
        int posI = solucion[i] -1; // localización actual de i
        int posJ = solucion[j] -1; // localización actual de j

        int delta = 0;

        // Contribuciones con todos los demás k distintos de i y j
        for (int k = 0; k < tam; k++) {
            if (k == i || k == j) continue;
            int posK = solucion[k] -1;

            // Cambios en flujos salientes de i y j
            delta += flujos[i][k] * (distancias[posJ][posK] - distancias[posI][posK]);
            delta += flujos[j][k] * (distancias[posI][posK] - distancias[posJ][posK]);

            // Cambios en flujos entrantes hacia i y j
            delta += flujos[k][i] * (distancias[posK][posJ] - distancias[posK][posI]);
            delta += flujos[k][j] * (distancias[posK][posI] - distancias[posK][posJ]);
        }

        // Interacción directa entre i y j (ambas direcciones de flujo)
        delta += flujos[i][j] * (distancias[posJ][posI] - distancias[posI][posJ]);
        delta += flujos[j][i] * (distancias[posI][posJ] - distancias[posJ][posI]);

        return delta;
    }

    @Override
    public String nombreAlgoritmo() {return "BusquedaLocal";}

}
