package metaheuristicas.app.algoritmos;

import metaheuristicas.app.utils.RemoveArrayElement;

import java.util.*;

public class GreedyAleatorio implements Algoritmo {

    @Override
    public String nombreAlgoritmo() { return "GreedyAleatorio"; }

    /**
     * @brief Calcula la importancia de cada departamento y la centralidad de cada localización
     */
    private void calculosImportanciaCentralidad(int[][] matriz1, int[][] matriz2, int tam, int[] importancia, int[] centralidad) {

        for(int i = 0; i < tam; i++)
            for(int j = 0; j < tam; j++){

                importancia[i] += matriz1[i][j]; // Este sumatorio va de i a j solo porque es simétrica
                centralidad[i] += matriz2[i][j];

            }
    }

    /**
     * @brief Ordena los departamentos por importancia de mayor a menor
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
     * @brief Ordena las localizaciones de menor a mayor centralidad
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
     * @brief Heurística Greedy aleatorizado para el Quadratic Assignment Problem (QAP).
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
     * @param k Valor K variable.
     * @return Array con la permutación de asignaciones inicial.
     */
    @Override
    public int[] resolver(int[][] matriz1, int[][] matriz2, Long semilla, int k) {
        int tam = matriz1.length;
        int[] importancia = new int[tam];
        int[] centralidad = new int[tam];
        int[] solucion = new int[tam];
        Random rand = new Random(semilla);

        // 1) y 2) Importancia / centralidad
        calculosImportanciaCentralidad(matriz1, matriz2, tam, importancia, centralidad);

        // 3) Ordenaciones
        Integer[] departamentos = ordenarDepartamentos(importancia);
        Integer[] localizaciones = ordenarLocalizaciones(centralidad);

        // 4) Construcción aleatoria con RCL por tamaño K
        //TODO Hay que revisar este algoritmo, funciona pero creo hace algo mal, luego te lo comento cuando me digas TJ
        for (int step = 0; step < tam; step++) {
            int boundDep = Math.min(k, departamentos.length);
            int boundLoc = Math.min(k, localizaciones.length);

            if (boundDep <= 0 || boundLoc == 0) {
                throw new IllegalStateException("No hay candidatos disponibles en el paso " + step);
            }

            int posDep = rand.nextInt(boundDep); // índice aleatorio entre los K mejores
            int posLoc = rand.nextInt(boundLoc);

            int dep = departamentos[posDep];
            int loc = localizaciones[posLoc];

            solucion[dep] = loc + 1; // 1-based

            // “Eliminar” los usados (crea un array nuevo con -1 tamaño)
            departamentos = RemoveArrayElement.removeElement(departamentos, posDep);
            localizaciones = RemoveArrayElement.removeElement(localizaciones, posLoc);
        }

        return solucion;
    }

    @Override
    public boolean usaSemilla() { return true; }
    @Override
    public boolean usaK() { return true; }
}
