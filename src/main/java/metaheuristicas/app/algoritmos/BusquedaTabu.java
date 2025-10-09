package metaheuristicas.app.algoritmos;

public class BusquedaTabu implements Algoritmo{

    @Override
    public int[] resolver(int[][] flujos, int[][] distancias, String semilla, int k, int iteraciones){

        return  new int[] {0,0};

    }

    @Override
    public String nombreAlgoritmo() {return "BusquedaTabu";}
}
