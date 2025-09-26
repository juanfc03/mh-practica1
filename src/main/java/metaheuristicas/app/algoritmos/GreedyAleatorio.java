package metaheuristicas.app.algoritmos;

import metaheuristicas.app.utils.Sorter;

import java.util.*;

public class GreedyAleatorio implements Algoritmo {

    @Override
    public String nombreAlgoritmo() { return "GreedyAleatorio"; }

    /**
     * Calcula la importancia de cada departamento y la centralidad de cada localización
     */
    private void calculosImportanciaCentralidad(int[][] matriz1, int[][] matriz2, int tam, int[] importancia, int[] centralidad) {

        for(int i = 0; i < tam; i++)
            for(int j = 0; j < tam; j++){

                importancia[i] += matriz1[i][j]; // Este sumatorio va de i a j solo porque es simétrica
                centralidad[i] += matriz2[i][j];

            }
    }

    /**
     * Heurística Greedy aleatorizado para el Quadratic Assignment Problem (QAP).
     * Esta función genera una solución asignando
     * departamentos a localizaciones de manera intuitiva:
     * 1) Calcula la "importancia" de cada departamento como la suma de su flujo hacia los demás.
     * 2) Calcula la "centralidad" de cada localización como la suma de sus distancias hacia todas las demás.
     * 3) Ordena los departamentos de mayor a menor importancia y las localizaciones de menor a mayor centralidad.
     * 4) Asigna el departamento más importante a la localización más central, el siguiente al segundo, etc; de forma aleatoria.
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
        ArrayList<Integer> departamentos;
        ArrayList<Integer> localizaciones;

        calculosImportanciaCentralidad(matriz1, matriz2, tam, importancia, centralidad);

        departamentos = Sorter.sortDesc(importancia);
        localizaciones = Sorter.sortAsc(centralidad);

        for (int i = 0; i < tam; i++) {
            int limiteDep = Math.min(k, departamentos.size());
            int limiteLoc = Math.min(k, localizaciones.size());

            if (limiteLoc == 0) break;  //sale del for para evitar coger posiciones que no existen de los arrays

            int posDep = rand.nextInt(limiteDep);
            int posLoc = rand.nextInt(limiteLoc);

            int departamento = departamentos.get(posDep);
            int localizacion = localizaciones.get(posLoc);

            solucion[departamento] = localizacion + 1;

            departamentos.remove(posDep);
            localizaciones.remove(posLoc);
        }

        return solucion;
    }

    @Override
    public boolean usaSemilla() { return true; }
    @Override
    public boolean usaK() { return true; }
}
