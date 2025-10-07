package metaheuristicas.app;

import metaheuristicas.app.algoritmos.Algoritmo;
import metaheuristicas.app.algoritmos.AlgoritmoFactory;
import metaheuristicas.app.archivos.ArchivoDatos;
import metaheuristicas.app.archivos.ArchivoParametros;
import metaheuristicas.app.archivos.ArchivoResultados;
import metaheuristicas.app.utils.Parser;
import metaheuristicas.app.utils.Validator;

import java.io.IOException;
import java.util.Map;

public class Main {

    private static String[] cargar(Map<String,String[]> parametros, String nombreParametro) {
        return parametros.get(nombreParametro);
    }

    private static void imprimirSolucion(int[] solucion, Algoritmo algoritmo, String dataset,
                                         int[][] flujos, int[][] distancias, String semilla){
        semilla = semilla == null ? "No usa semilla" : semilla;

        System.out.println("\n<--------------------------------------------------------------------------------->"
                            + "\n Algoritmo: " + algoritmo.nombreAlgoritmo()
                            + "\n Dataset: " + dataset
                            + "\n Semilla: " + semilla
                            + "\n Coste: " + algoritmo.calcularCoste(flujos, distancias, solucion)
                            + "\n<--------------------------------------------------------------------------------->"
                            + "\n Solucion: ");


         for (int i = 0; i < solucion.length; i++) System.out.println("Departamento " + (i + 1) + " -> Localización " + solucion[i]);
    }

    /**
     * En la Terminal, escribir: java -cp target/classes metaheuristicas.app.Main parametros.txt resultados.txt
     */
    public static void main(String[] args) {


        int k;
        int coste;
        int[] solucion;
        int[][] flujos;
        int[][] distancias;

        String ruta_base="src/main/resources/";
        String nombreParametros;
        String nombreResultados;
        ArchivoResultados archivoResultados;
        ArchivoParametros archivoParametros;

        String[] algoritmos;
        String[] datasets;
        String[] semillas;

        Map<String, String[]> parametros;

        if (args.length < 2) {
            System.err.println("Debes pasar el nombre del fichero de parámetros y de resultados como argumento.");
            return;
        }

        nombreParametros = args[0];
        nombreResultados = args[1];
        archivoParametros = new ArchivoParametros(ruta_base, nombreParametros);
        archivoResultados = new ArchivoResultados(ruta_base, nombreResultados);

        try {
            archivoParametros.lectura();
            parametros = archivoParametros.getParametros();

            algoritmos = cargar(parametros, "Algoritmos");
            datasets = cargar(parametros, "Dataset");
            semillas = parametros.getOrDefault("Semillas", new String[0]);
            k = Parser.toInt(cargar(parametros, "K")[0]);

            for (String algoritmo : algoritmos) {
                Algoritmo algoritmo_actual = AlgoritmoFactory.nuevoAlgoritmo(algoritmo);

                for(String dataset : datasets){
                    ArchivoDatos datos = new ArchivoDatos(ruta_base, dataset);
                    datos.lectura();

                    flujos = datos.getFlujos();
                    distancias = datos.getDistancias();

                    for (String semilla : semillas) {
                        Validator.validateSeed(semilla);

                        solucion = algoritmo_actual.resolver(flujos, distancias, semilla, k);
                        coste = algoritmo_actual.calcularCoste(flujos, distancias, solucion);

                        imprimirSolucion(solucion, algoritmo_actual, dataset, flujos, distancias, semilla);
                        archivoResultados.escribir(algoritmo_actual.nombreAlgoritmo(), dataset, semilla, coste);
                    }
                }
            }

        } catch (IOException ex) {
            System.err.println("Ha ocurrido un error: " + ex.getMessage());
        }
    }
}