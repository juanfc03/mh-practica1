package metaheuristicas.app;

import java.util.Arrays;
import java.util.Comparator;

public class GreedyAlg implements Algoritmo {

    @Override
    public String nombreAlgoritmo() { return "Greedy"; }

    /**
     * @brief Heurística Greedy para el Quadratic Assignment Problem (QAP).
     *
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

        int tam = matriz1.length;

        // 1) y 2) Calculamos la importancia de cada departamento y centralidad de cada localización
        int[] importancia = new int[tam];
        int[] centralidad = new int[tam];
        for(int i = 0; i < tam; i++)
            for(int j = 0; j < tam; j++){

                importancia[i] += matriz1[i][j]; // Este sumatorio va de i a j solo porque es simétrica
                centralidad[i] += matriz2[i][j];

            }


        // 3) Ordenar los departamentos de mayor a menor importancia
        // y las localizaciones de menor a mayor centralidad
        Integer[] ordenDepartamentos = new Integer[tam];
        for (int i = 0; i < tam; i++) ordenDepartamentos[i] = i;
        Arrays.sort(ordenDepartamentos, (a, b) -> Integer.compare(importancia[b], importancia[a]));
        //System.out.println(Arrays.toString(ordenDepartamentos));

        Integer[] ordenLocalizaciones = new Integer[tam];
        for (int i = 0; i < tam; i++) ordenLocalizaciones[i] = i;
        Arrays.sort(ordenLocalizaciones, Comparator.comparingInt(a -> centralidad[a]));
        //System.out.println(Arrays.toString(ordenLocalizaciones));

        // 4) Damos la solución, asignar el más importante a la localización más central
        int[] S = new int[tam];
        for (int i = 0; i < tam; i++) S[ordenDepartamentos[i]] = ordenLocalizaciones[i] + 1;

        return S;
    }

    @Override
    public int[] resolver(int[][] matriz1, int[][] matriz2, Long semilla){ return null; }

    /**
     * @brief Calcula el coste total de una asignación de departamentos a localizaciones.
     *
     * El coste se calcula como la suma de F[i][j] * D[S[i]-1][S[j]-1] para todos los pares (i,j).
     *
     * @param matriz1 Matriz de flujo entre departamentos.
     * @param matriz2 Matriz de distancias entre localizaciones.
     * @param S Asignación de departamentos a localizaciones (1-based).
     * @return Coste total de la asignación.
     */
    @Override
    public int calcularCoste(int[][] matriz1,  int[][] matriz2, int[] S) {

        int coste=0;
        int tam=matriz1.length;
        for(int i = 0; i < tam; i++)
            for(int j = 0; j < tam; j++)
                coste+=matriz1[i][j] * matriz2[S[i]-1][S[j]-1];

        return coste;

    }
}
