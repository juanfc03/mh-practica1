package metaheuristicas.app.algoritmos;

import metaheuristicas.app.utils.Parser;
import metaheuristicas.app.utils.Sorter;

import java.util.*;

public class AlgGAC4G2 implements Algoritmo {
    /**
     * Asigna el departamento más importante a la localización más central, el siguiente al segundo, etc; de forma aleatoria.
     * @return Array con la permutación de asignaciones inicial.
     */
    @Override
    public int[] resolver(int[][] flujos, int[][] distancias, String semilla, int k, int iteraciones, int tenencia, float oscilacion, float escalamiento) {
        int tam = flujos.length;
        int[] importancia = new int[tam];
        int[] centralidad = new int[tam];
        int[] solucion = new int[tam];
        Random rand = new Random(Parser.toLong(semilla));
        ArrayList<Integer> departamentos;
        ArrayList<Integer> localizaciones;

        calculosImportanciaCentralidad(flujos, distancias, tam, importancia, centralidad);

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
    public String nombreAlgoritmo() { return "GreedyAleatorio"; }

    @Override
    public String siglasAlgoritmo() {return "GA";}

    @Override
    public boolean tieneLogs() {return false;}

    @Override
    public List<String> getLog() { return null;}
}
