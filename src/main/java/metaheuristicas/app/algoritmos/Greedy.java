package metaheuristicas.app.algoritmos;

import java.util.*;

public class Greedy implements Algoritmo {

    @Override
    public String nombreAlgoritmo() { return "Greedy"; }


    /**
     * Calcula la importancia de cada departamento y la centralidad de cada localización
     * @param matriz1
     * @param matriz2
     * @param tam
     * @param importancia
     * @param centralidad
     */
    private void calculosImportanciaCentralidad(int[][] matriz1, int[][] matriz2, int tam, int[] importancia, int[] centralidad) {

        for(int i = 0; i < tam; i++)
            for(int j = 0; j < tam; j++){

                importancia[i] += matriz1[i][j]; // Este sumatorio va de i a j solo porque es simétrica
                centralidad[i] += matriz2[i][j];

            }
    }

    /**
     * Ordena los departamentos por importancia de mayor a menor
     * @param importancia array de importancia para ordenar los departamentos
     * @return devuelve el vector de departamentos ordenados
     */
    private Integer[] ordenarDepartamentos(int[] importancia) {
        Integer[] ordenados = new Integer[importancia.length];

        for (int i = 0; i < ordenados.length; i++)
            ordenados[i] = i;

        Arrays.sort(ordenados, (a, b) -> Integer.compare(importancia[b], importancia[a]));

        return ordenados;
    }

    /**
     * Ordena las localizaciones de menor a mayor centralidad
     * @param centralidad array de centralidad para ordenar las localizaciones
     * @return devuelve el vector de localizaciones ordenadas
     */
    private Integer[] ordenarLocalizaciones(int[] centralidad) {
        Integer[] ordenados = new Integer[centralidad.length];

        for (int i = 0; i < ordenados.length; i++)
            ordenados[i] = i;

        Arrays.sort(ordenados, Comparator.comparingInt(a -> centralidad[a]));

        return ordenados;
    }

    /**
     * Da la solución del algoritmo al problema, asignando el más importante a la localización más central
     * @param tam
     * @param departamentos
     * @param localizaciones
     * @return
     */
    private int[] asignarSolucion(int tam, Integer[] departamentos, Integer[] localizaciones) {
        int[] solucion = new int[tam];

        for (int i = 0; i < tam; i++)
            solucion[departamentos[i]] = localizaciones[i] + 1;

        return solucion;
    }

    private int[] resolver_interno(int[][] matriz1, int[][] matriz2, long semilla, int k) {

        // 1,2) Calcular importancia y centralidad
        int tam = matriz1.length;

        int[] importancia  = new int[tam];
        int[] centralidad = new int[tam];

        calculosImportanciaCentralidad(matriz1, matriz2, tam, importancia, centralidad);

        // 3) Ordenar departamentos y localizaciones
        Integer[] departamentos = ordenarDepartamentos(importancia);
        Integer[] localizaciones = ordenarLocalizaciones(centralidad);

        // TODO Seguir por aquí para el random, hacerlo con int[] y luego se cambia a arraylist

            Random rand;

            rand = new Random(semilla);
            int pos = rand.nextInt(k - 1);

//        for (int i = localizaciones.length - 1; i > 0; i--) {
//            int j = rand.nextInt(i + 1); // Número aleatorio entre 0 e i
//            // Intercambiamos ordenLocalizaciones[i] y ordenLocalizaciones[j]
//            int tmp = localizaciones[i];
//            localizaciones[i] = localizaciones[j];
//            localizaciones[j] = tmp;
//        }

            /* También se puede hacer así:
                java.util.List<Integer> listaLocs = java.util.Arrays.asList(ordenLocalizaciones);
                java.util.Collections.shuffle(listaLocs, rnd);
             */

        // 4) Solución
        return asignarSolucion(tam, departamentos, localizaciones);
    }

    /**
     * Heurística Greedy para el Quadratic Assignment Problem (QAP).
     * Esta función genera una solución asignando
     * departamentos a localizaciones de manera intuitiva:
     * 1) Calcula la "importancia" de cada departamento como la suma de su flujo hacia los demás.
     * 2) Calcula la "centralidad" de cada localización como la suma de sus distancias hacia todas las demás.
     * 3) Ordena los departamentos de mayor a menor importancia y las localizaciones de menor a mayor centralidad.
     * 4) Asigna el departamento más importante a la localización más central, el siguiente al segundo, etc.
     *
     * @param matriz1 Matriz de flujo entre departamentos (f_ij).
     * @param matriz2 Matriz de distancias entre localizaciones (d_kl).
     * @return Array con la permutación de asignaciones inicial.
     */
    @Override
    public int[] resolver(int[][] matriz1, int[][] matriz2) {

        return resolver_interno(matriz1, matriz2, 0, 0);

    }

    /**
     * Heurística Greedy aleatorizado para el Quadratic Assignment Problem (QAP).
     * Esta función genera una solución asignando
     * departamentos a localizaciones de manera intuitiva:
     * 1) Calcula la "importancia" de cada departamento como la suma de su flujo hacia los demás.
     * 2) Calcula la "centralidad" de cada localización como la suma de sus distancias hacia todas las demás.
     * 3) Ordena los departamentos de mayor a menor importancia y las localizaciones de menor a mayor centralidad.
     * 4) Asigna el departamento más importante a la localización más central, el siguiente al segundo, etc.
     *
     * @param matriz1 Matriz de flujo entre departamentos (f_ij).
     * @param matriz2 Matriz de distancias entre localizaciones (d_kl).
     * @param semilla Semilla para la aleatoriedad.
     * @return Array con la permutación de asignaciones inicial.
     */
    @Override
    public int[] resolver(int[][] matriz1, int[][] matriz2, Long semilla, int k){

        return resolver_interno(matriz1,matriz2, semilla, k);

    }

    /**
     * Calcula el coste total de una asignación de departamentos a localizaciones.
     * El coste se calcula como la suma de F[i][j] * D[S[i]-1][S[j]-1] para todos los pares (i,j).
     *
     * @param matriz1 Matriz de flujo entre departamentos.
     * @param matriz2 Matriz de distancias entre localizaciones.
     * @param S Asignación de departamentos a localizaciones (1-based).
     * @return Coste total de la asignación.
     */
    @Override
    public int calcularCoste(int[][] matriz1, int[][] matriz2, int[] S) {

        int coste=0;
        int tam=matriz1.length;
        for(int i = 0; i < tam; i++)
            for(int j = 0; j < tam; j++)
                coste+=matriz1[i][j] * matriz2[S[i]-1][S[j]-1];

        return coste;

    }
}
