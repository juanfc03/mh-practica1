package metaheuristicas.app.algoritmos;

import metaheuristicas.app.utils.Swapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BusquedaTabu implements Algoritmo{

    private final List<String> log = new ArrayList<>();

    //FIXME: Pasarlo al fichero de parametros
    private static final int TENENCIA_TABU = 3;           // Tenencia tabú
    private static final double OSCILACION = 0.50;      // Prob. intensificar vs diversificar
    private static final double ESTANCAMIENTO = 0.05;   // 5% de iteraciones sin mejorar

    @Override
    public int[] resolver(int[][] flujos, int[][] distancias, String semilla, int k, int iteraciones) {

        GreedyAleatorio randomGreedy = new GreedyAleatorio();
        int[] solucionInicial = randomGreedy.resolver(flujos, distancias, semilla, k, iteraciones);
        int[] solucionActual = randomGreedy.resolver(flujos, distancias, semilla, k, iteraciones);

        int nUnidades = solucionActual.length;
        int[] marcarNoMirarDLB = new int[nUnidades];
        int costeActual = calcularCoste(flujos, distancias, solucionActual);
        int[] mejorSolucion = solucionActual.clone();
        int costeMejorSolucion = costeActual;

        // Lista tabú como matriz de expiración para pares (i,j) con i<j
        int[][] expiracionTabu = new int[nUnidades][nUnidades];

        // Memoria a largo plazo: frecuencia (unidad u -> localización l)
        int[][] frecuenciaLargoPlazo = new int[nUnidades][nUnidades];
        for (int u = 0; u < nUnidades; u++) {
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
        final int umbralEstancamientoIter = Math.max(1, (int) Math.round(ESTANCAMIENTO * iteraciones));
        int iteracionesRestantesModoOscilacion = 0;
        boolean oscilandoIntensificar = true;

        for (int iteracion = 0; iteracion < iteraciones; iteracion++) {

            int mejorUnidadA = -1, mejorUnidadB = -1;
            int mejorDeltaCosto = Integer.MAX_VALUE;
            double mejorPuntaje = Double.POSITIVE_INFINITY;

            if (iteracionesRestantesModoOscilacion == 0 && iteracionesSinMejorar >= umbralEstancamientoIter) {
                oscilandoIntensificar = rnd.nextDouble() < OSCILACION; // 50% por defecto
                iteracionesRestantesModoOscilacion = umbralEstancamientoIter;
            }

            boolean existeCandidato = false;
            for (int unidadA = 0; unidadA < nUnidades; unidadA++) {
                if (marcarNoMirarDLB[unidadA] == 1) continue;

                int mejorBparaUnidadA = -1;
                int mejorDeltaParaUnidadA = Integer.MAX_VALUE;
                double mejorPuntajeParaUnidadA = Double.POSITIVE_INFINITY;

                for (int unidadB = unidadA + 1; unidadB < nUnidades; unidadB++) {

                    int delta = deltaSwap(flujos, distancias, solucionActual, unidadA, unidadB);
                    boolean esMovimientoTabu = expiracionTabu[unidadA][unidadB] > iteracion;
                    boolean superaCriterioAspiracion = esMovimientoTabu && (costeActual + delta < costeMejorSolucion);

                    if (!esMovimientoTabu || superaCriterioAspiracion) {
                        double puntaje = calcScore(delta, solucionActual, frecuenciaLargoPlazo,
                                unidadA, unidadB, iteracion,
                                iteracionesRestantesModoOscilacion, oscilandoIntensificar);

                        existeCandidato = true;

                        if (puntaje < mejorPuntajeParaUnidadA || (puntaje == mejorPuntajeParaUnidadA && delta < mejorDeltaParaUnidadA)) {
                            mejorPuntajeParaUnidadA = puntaje;
                            mejorDeltaParaUnidadA = delta;
                            mejorBparaUnidadA = unidadB;
                        }
                    }
                }

                if (mejorBparaUnidadA != -1) {
                    if (mejorPuntajeParaUnidadA < mejorPuntaje || (mejorPuntajeParaUnidadA == mejorPuntaje && mejorDeltaParaUnidadA < mejorDeltaCosto)) {
                        mejorPuntaje = mejorPuntajeParaUnidadA;
                        mejorDeltaCosto = mejorDeltaParaUnidadA;
                        mejorUnidadA = unidadA;
                        mejorUnidadB = mejorBparaUnidadA;
                    }
                } else {
                    marcarNoMirarDLB[unidadA] = 1;
                }
            }

            if (!existeCandidato || todosDLBActivados(marcarNoMirarDLB)) {
                mejorUnidadA = -1; mejorUnidadB = -1; mejorDeltaCosto = Integer.MAX_VALUE; mejorPuntaje = Double.POSITIVE_INFINITY;

                for (int unidadA = 0; unidadA < nUnidades; unidadA++) {
                    for (int unidadB = unidadA + 1; unidadB < nUnidades; unidadB++) {
                        int delta = deltaSwap(flujos, distancias, solucionActual, unidadA, unidadB);

                        boolean esMovimientoTabu = expiracionTabu[unidadA][unidadB] > iteracion;
                        boolean superaCriterioAspiracion = esMovimientoTabu && (costeActual + delta < costeMejorSolucion);
                        if (esMovimientoTabu && !superaCriterioAspiracion) continue;

                        double puntaje = calcScore(delta, solucionActual, frecuenciaLargoPlazo,
                                unidadA, unidadB, iteracion,
                                iteracionesRestantesModoOscilacion, oscilandoIntensificar);

                        if (puntaje < mejorPuntaje || (puntaje == mejorPuntaje && delta < mejorDeltaCosto)) {
                            mejorPuntaje = puntaje;
                            mejorDeltaCosto = delta;
                            mejorUnidadA = unidadA;
                            mejorUnidadB = unidadB;
                        }
                    }
                }

                if (mejorUnidadA == -1) break;
                // reset DLB
                Arrays.fill(marcarNoMirarDLB, 0);
            }

            // Aplicar movimiento
            Swapper.intercambia(solucionActual, mejorUnidadA, mejorUnidadB);
            costeActual += mejorDeltaCosto;

            // Actualizar memorias
            expiracionTabu[mejorUnidadA][mejorUnidadB] = iteracion + TENENCIA_TABU + 1;
            marcarNoMirarDLB[mejorUnidadA] = 0; marcarNoMirarDLB[mejorUnidadB] = 0;

            frecuenciaLargoPlazo[mejorUnidadA][solucionActual[mejorUnidadA]-1]++;
            frecuenciaLargoPlazo[mejorUnidadB][solucionActual[mejorUnidadB]-1]++;

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
                                    int modoOscilacionRestante, boolean modoIntensificar) {
        if (modoOscilacionRestante <= 0) return delta;
        int locI = s[j] - 1;   // posiciones tras el swap hipotético
        int locJ = s[i] - 1;
        int freq = largoPlazo[i][locI] + largoPlazo[j][locJ];
        double freqNorm = freq / (double) (1 + it);
        return delta + (modoIntensificar ? -OSCILACION : +OSCILACION) * freqNorm;
    }

    @Override
    public String nombreAlgoritmo() { return "BusquedaTabu"; }

    @Override
    public String siglasAlgoritmo() {return "BT";}

    @Override
    public List<String> getLog() { return log; }

}
