package metaheuristicas.app.algoritmos;

import metaheuristicas.app.utils.Parser;
import metaheuristicas.app.utils.Swapper;

import java.util.ArrayList;
import java.util.List;

public class BusquedaLocal implements Algoritmo{

    private final List<String> log = new ArrayList<>();

    @Override
    public int[] resolver(int[][] flujos, int[][] distancias, String semilla, int k, int iteraciones){

        GreedyAleatorio randomGreedy = new GreedyAleatorio();
        int[] solucion_inicial = randomGreedy.resolver(flujos, distancias, semilla, k, iteraciones);
        int costeInicial = calcularCoste(flujos,distancias,solucion_inicial);
        log.add(String.format(
                "---------------------------------- \n" +
                "Solucion inicial=%s \nCoste inicial=%d \n"
                + "---------------------------------- \n",
                Parser.solucionToString(solucion_inicial),
                costeInicial
        ));
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

                        int costeActual = calcularCoste(flujos, distancias, solucion);
                        log.add(String.format(
                                "Iteración %d: Intercambio(%d,%d), Mejora=%d, Coste=%d, Sol.act=%s",
                                it,
                                i,
                                j,
                                delta,
                                costeActual,
                                Parser.solucionToString(solucion)
                        ));
                        break;
                    }
                }

                if (!mejoraI) {
                    dlb[i] = 1;
                }

                if (!hubo_mejora) {
                    log.add(String.format(
                            "Iteración %d: No hubo mejora",
                            it
                    ));
                }

                else break;
            }
        }

        int costeFinal = calcularCoste(flujos, distancias, solucion);

        log.add(String.format(
                "--------------------------- \n" +
                "Solucion final=%s",
                Parser.solucionToString(solucion_inicial),
                costeFinal
        ));

        return solucion;
    }

    @Override
    public String nombreAlgoritmo() {return "BusquedaLocal";}

    @Override
    public String siglasAlgoritmo() {return "BL";}

    @Override
    public List<String> getLog() { return log; }

}
