package metaheuristicas.app.algoritmos;

public class BusquedaLocal implements Algoritmo{

    @Override
    public String nombreAlgoritmo() {return "BusquedaLocal";}

    @Override
    public int[] resolver(int[][] matriz1, int[][] matriz2, Long semilla, int k){

        // 1) Solución inicial con Greedy Aleatorio
        GreedyAleatorio ga = new GreedyAleatorio();
        int[] solucion = ga.resolver(matriz1, matriz2, semilla, k);

        // 2) Coste inicial (ajusta el orden si tu firma difiere)
        int costeActual = calcularCoste(matriz1, matriz2, solucion);

        // 3) Estructuras BL
        int n = solucion.length;
        int[] dlb = new int[n];      // 0 por defecto
        int iteraciones = 0;         // nº de vecinos evaluados (presupuesto 5000)
        boolean hubo_mejora = true;

        // 4) Búsqueda local: primer-mejor + DLB + límite 5000 evaluaciones
        while (iteraciones < 5000 && hubo_mejora) {

            hubo_mejora = false;

            for (int i = 0; i < n; i++) {
                if (dlb[i] == 1) continue; // DLB: saltar i si está desactivado

                boolean mejoraI = false;

                for (int j = i + 1; j < n; j++) {
                    // Evaluar vecino (i, j): swap temporal
                    intercambia(solucion, i, j);

                    // Recalcular coste completo tras el swap
                    int nuevoCoste = calcularCoste(matriz1, matriz2, solucion);
                    iteraciones++; // hemos consumido una evaluación

                    if (nuevoCoste < costeActual) {
                        // Aceptar PRIMERA mejora
                        costeActual = nuevoCoste;
                        dlb[i] = 0;
                        dlb[j] = 0;
                        hubo_mejora = true;
                        mejoraI = true;

                        // Primer-mejor: mantener el swap y reiniciar desde i=0
                        break;
                    } else {
                        // No mejora: revertir swap
                        intercambia(solucion, i, j);
                    }

                    // Presupuesto agotado: sal de j; for i saldrá abajo
                    if (iteraciones >= 5000) break;
                }

                // Si para i no hubo ninguna mejora, desactivar con DLB
                if (!mejoraI) dlb[i] = 1;

                // Si mejora o agoto presupuesto, reinicia pasada o termina
                if (mejoraI || iteraciones == 5000) break;
            }
        }

        //TODO Mejorar la eficiencia del algoritmo usando deltaSwap tal y como dice el guión de prácticas
        return solucion;
    }

    //TODO Esto puede ir en Utils, separado del código normal
    /*
     * Intercambia las posiciones i y j en el array S (swap in-place).
     */
    private void intercambia(int[] S, int i, int j){
        int tmp = S[i];
        S[i] = S[j];
        S[j] = tmp;
    }

    /**
     * Calcula el cambio en el coste (delta) al intercambiar i y j en la solución S.
     * Complejidad: O(n).
     */
    // FIXME: revisar lógica, falta meterla en el resolver(), lo hacemos juntos cuando podamos
    private int deltaSwap(int[][] F, int[][] D, int[] S, int i, int j) {
        if (i == j) return 0;

        int n = S.length;
        int pi = S[i]; // localización actual de i
        int pj = S[j]; // localización actual de j

        int delta = 0;

        // Contribuciones con todos los demás k distintos de i y j
        for (int k = 0; k < n; k++) {
            if (k == i || k == j) continue;
            int pk = S[k];

            // Cambios en flujos salientes de i y j
            delta += F[i][k] * (D[pj][pk] - D[pi][pk]);
            delta += F[j][k] * (D[pi][pk] - D[pj][pk]);

            // Cambios en flujos entrantes hacia i y j
            delta += F[k][i] * (D[pk][pj] - D[pk][pi]);
            delta += F[k][j] * (D[pk][pi] - D[pk][pj]);
        }

        // Interacción directa entre i y j (ambas direcciones de flujo)
        delta += F[i][j] * (D[pj][pi] - D[pi][pj]);
        delta += F[j][i] * (D[pi][pj] - D[pj][pi]);

        return delta;
    }

    @Override
    public boolean usaSemilla() { return true; }
    @Override
    public boolean usaK() { return true; }

}
