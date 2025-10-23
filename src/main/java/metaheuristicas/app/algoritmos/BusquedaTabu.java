package metaheuristicas.app.algoritmos;

import metaheuristicas.app.utils.Swapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BusquedaTabu implements Algoritmo{

    private final List<String> log = new ArrayList<>();

    @Override
    public int[] resolver(int[][] flujos, int[][] distancias, String semilla, int k, int iteraciones,
                          int tenencia, float oscilacion, float estancamiento) {

        GreedyAleatorio randomGreedy = new GreedyAleatorio();
        int[] solucionActual = randomGreedy.resolver(flujos, distancias, semilla, k, iteraciones, tenencia, oscilacion, estancamiento);

        int nDepartamentos = solucionActual.length;
        int[] marcarNoMirarDLB = new int[nDepartamentos];
        int costeActual = calcularCoste(flujos, distancias, solucionActual);
        int[] mejorSolucion = solucionActual.clone();
        int costeMejorSolucion = costeActual;

        // Lista tabú como matriz de expiración para pares (i,j) con i<j
        int[][] expiracionTabu = new int[nDepartamentos][nDepartamentos];

        // Memoria a largo plazo: frecuencia (departamento u -> localización l)
        int[][] frecuenciaLargoPlazo = new int[nDepartamentos][nDepartamentos];
        for (int u = 0; u < nDepartamentos; u++) {
            int loc = solucionActual[u] - 1;
            frecuenciaLargoPlazo[u][loc]++;
        }

        Random rnd = new Random();
        try {
            if (semilla != null) {
                long seed = Long.parseLong(semilla.trim());
                rnd.setSeed(seed);
            }
        } catch (Exception ignored) { }

        // Oscilación estratégica
        int iteracionesSinMejorar = 0;
        final int umbralEstancamientoIter = Math.max(1, (int) Math.round(estancamiento * iteraciones));
        int iteracionesRestantesModoOscilacion = 0;
        boolean oscilandoIntensificar = true;

        for (int iteracion = 0; iteracion < iteraciones; iteracion++) {

            int mejorDptoA = -1, mejorDptoB = -1;
            int mejorDeltaCosto = Integer.MAX_VALUE;
            double mejorPuntaje = Double.POSITIVE_INFINITY;

            if (iteracionesRestantesModoOscilacion == 0 && iteracionesSinMejorar >= umbralEstancamientoIter) {
                oscilandoIntensificar = rnd.nextDouble() < oscilacion; // 50% por defecto
                iteracionesRestantesModoOscilacion = umbralEstancamientoIter;
            }

            boolean existeCandidato = false;
            for (int dptoA = 0; dptoA < nDepartamentos; dptoA++) {
                if (marcarNoMirarDLB[dptoA] == 1) continue;

                int mejorBparaDptoA = -1;
                int mejorDeltaParaDptoA = Integer.MAX_VALUE;
                double mejorPuntajeParaDptoA = Double.POSITIVE_INFINITY;

                for (int dptoB = dptoA + 1; dptoB < nDepartamentos; dptoB++) {

                    int delta = deltaSwap(flujos, distancias, solucionActual, dptoA, dptoB);
                    boolean esMovimientoTabu = expiracionTabu[dptoA][dptoB] > iteracion;
                    boolean superaCriterioAspiracion = esMovimientoTabu && (costeActual + delta < costeMejorSolucion);

                    if (!esMovimientoTabu || superaCriterioAspiracion) {
                        double puntaje = calcScore(delta, solucionActual, frecuenciaLargoPlazo,
                                dptoA, dptoB, iteracion,
                                iteracionesRestantesModoOscilacion, oscilandoIntensificar, oscilacion);

                        existeCandidato = true;

                        if (puntaje < mejorPuntajeParaDptoA || (puntaje == mejorPuntajeParaDptoA && delta < mejorDeltaParaDptoA)) {
                            mejorPuntajeParaDptoA = puntaje;
                            mejorDeltaParaDptoA = delta;
                            mejorBparaDptoA = dptoB;
                        }
                    }
                }

                if (mejorBparaDptoA != -1) {
                    if (mejorPuntajeParaDptoA < mejorPuntaje || (mejorPuntajeParaDptoA == mejorPuntaje && mejorDeltaParaDptoA < mejorDeltaCosto)) {
                        mejorPuntaje = mejorPuntajeParaDptoA;
                        mejorDeltaCosto = mejorDeltaParaDptoA;
                        mejorDptoA = dptoA;
                        mejorDptoB = mejorBparaDptoA;
                    }
                } else {
                    marcarNoMirarDLB[dptoA] = 1;
                }
            }

            if (!existeCandidato || todosDLBActivados(marcarNoMirarDLB)) {
                mejorDptoA = -1; mejorDptoB = -1; mejorDeltaCosto = Integer.MAX_VALUE; mejorPuntaje = Double.POSITIVE_INFINITY;

                for (int dptoA = 0; dptoA < nDepartamentos; dptoA++) {
                    for (int dptoB = dptoA + 1; dptoB < nDepartamentos; dptoB++) {
                        int delta = deltaSwap(flujos, distancias, solucionActual, dptoA, dptoB);

                        boolean esMovimientoTabu = expiracionTabu[dptoA][dptoB] > iteracion;
                        boolean superaCriterioAspiracion = esMovimientoTabu && (costeActual + delta < costeMejorSolucion);
                        if (esMovimientoTabu && !superaCriterioAspiracion) continue;

                        double puntaje = calcScore(delta, solucionActual, frecuenciaLargoPlazo,
                                dptoA, dptoB, iteracion,
                                iteracionesRestantesModoOscilacion, oscilandoIntensificar, oscilacion);

                        if (puntaje < mejorPuntaje || (puntaje == mejorPuntaje && delta < mejorDeltaCosto)) {
                            mejorPuntaje = puntaje;
                            mejorDeltaCosto = delta;
                            mejorDptoA = dptoA;
                            mejorDptoB = dptoB;
                        }
                    }
                }

                if (mejorDptoA == -1) break;
                // reset DLB
                Arrays.fill(marcarNoMirarDLB, 0);
            }

            // Aplicar movimiento
            Swapper.intercambia(solucionActual, mejorDptoA, mejorDptoB);
            costeActual += mejorDeltaCosto;

            // Actualizar memorias
            expiracionTabu[mejorDptoA][mejorDptoB] = iteracion + tenencia + 1;
            marcarNoMirarDLB[mejorDptoA] = 0; marcarNoMirarDLB[mejorDptoB] = 0;

            frecuenciaLargoPlazo[mejorDptoA][solucionActual[mejorDptoA]-1]++;
            frecuenciaLargoPlazo[mejorDptoB][solucionActual[mejorDptoB]-1]++;

            if (costeActual < costeMejorSolucion) {
                costeMejorSolucion = costeActual;
                mejorSolucion = solucionActual.clone();
                iteracionesSinMejorar = 0;
            } else {
                iteracionesSinMejorar++;
            }

            if (iteracionesRestantesModoOscilacion > 0) {
                iteracionesRestantesModoOscilacion--;
            }
        }

        return mejorSolucion;
    }

    private boolean todosDLBActivados(int[] dlb) {
        for (int v : dlb) if (v == 0) return false;
        return true;
    }

    private double calcScore(int delta, int[] s, int[][] largoPlazo,
                                    int i, int j, int it,
                                    int modoOscilacionRestante, boolean modoIntensificar, float oscilacion) {
        if (modoOscilacionRestante <= 0) return delta;
        int locI = s[j] - 1;   // posiciones tras el swap hipotético
        int locJ = s[i] - 1;
        int freq = largoPlazo[i][locI] + largoPlazo[j][locJ];
        double freqNorm = freq / (double) (1 + it);
        return delta + (modoIntensificar ? -oscilacion : +oscilacion) * freqNorm;
    }

    @Override
    public String nombreAlgoritmo() {
        return "BusquedaTabu";
    }

    @Override
    public String siglasAlgoritmo() {
        return "BT";
    }

    @Override
    public List<String> getLog() {
        return log;
    }

    @Override
    public boolean usaTenencia() {
        return true;
    }
}
