package metaheuristicas.app.algoritmos;
import metaheuristicas.app.utils.Sorter;

import java.util.*;

public class AlgGEC4G2 implements Algoritmo{
    /**
     * Asigna el departamento más importante a la localización más central, el siguiente al segundo, etc.
     * @return Array con la permutación de asignaciones inicial.
     */
    @Override
    public int[] resolver(int[][] flujos, int[][] distancias, String semilla, int k, int iteraciones, int tenencia, float oscilacion, float escalamiento) {
        int tam = flujos.length;
        int[] importancia = new int[tam];
        int[] centralidad = new int[tam];
        int[] solucion = new int[tam];
        ArrayList<Integer> departamentos;
        ArrayList<Integer> localizaciones;

        calculosImportanciaCentralidad(flujos, distancias, tam, importancia, centralidad);

        departamentos = Sorter.sortDesc(importancia);
        localizaciones = Sorter.sortAsc(centralidad);

        for (int i = 0; i < tam; i++)
            solucion[departamentos.get(i)] = localizaciones.get(i) + 1;

        return solucion;
    }

    @Override
    public String nombreAlgoritmo() { return "Greedy"; }

    @Override
    public boolean requiereSemilla() {
        return false;
    }

    @Override
    public String siglasAlgoritmo() {return "GE";}

    @Override
    public boolean tieneLogs() {return false;}

    @Override
    public List<String> getLog() { return null;}
}