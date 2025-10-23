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


        //for (int i = 0; i < solucion.length; i++) System.out.println("Departamento " + (i + 1) + " -> Localización " + solucion[i]);
    }

    /**
     * En la Terminal, escribir: java -cp target/classes metaheuristicas.app.Main parametros.txt
     */
    public static void main(String[] args) {


        int k;
        int iteraciones;
        int coste;
        int[] solucion;
        int[][] flujos;
        int[][] distancias;

        String ruta_base="src/main/resources/";
        String nombreParametros;
        ArchivoResultados archivoResultados;
        ArchivoParametros archivoParametros;

        String[] algoritmos;
        String[] datasets;
        String[] semillas;

        Map<String, String[]> parametros;

        if (args.length < 1) {
            System.err.println("Debes pasar el nombre del fichero de parámetros como argumento.");
            return;
        }

        nombreParametros = args[0];
        archivoParametros = new ArchivoParametros(ruta_base, nombreParametros);

        try {
            archivoParametros.lectura();
            parametros = archivoParametros.getParametros();

            algoritmos = cargar(parametros, "Algoritmos");
            datasets = cargar(parametros, "Dataset");
            semillas = parametros.getOrDefault("Semillas", new String[0]);
            k = Parser.toInt(cargar(parametros, "K")[0]);
            iteraciones = Parser.toInt(cargar(parametros, "Iteraciones")[0]);

            for (String dataset : datasets) {

                ArchivoDatos datos = new ArchivoDatos(ruta_base, dataset);
                datos.lectura();

                flujos = datos.getFlujos();
                distancias = datos.getDistancias();

                for (String algoritmo : algoritmos) {

                    Algoritmo algoritmo_actual = AlgoritmoFactory.nuevoAlgoritmo(algoritmo);
                    if(algoritmo_actual.requiereSemilla()) {
                        for (String semilla : semillas) {
                            Validator.validateSeed(semilla);

                            solucion = algoritmo_actual.resolver(flujos, distancias, semilla, k, iteraciones);
                            coste = algoritmo_actual.calcularCoste(flujos, distancias, solucion);

                            archivoResultados = new ArchivoResultados(ruta_base, algoritmo_actual.nombreAlgoritmo(), algoritmo_actual.siglasAlgoritmo(), dataset, semilla);

                            imprimirSolucion(solucion, algoritmo_actual, dataset, flujos, distancias, semilla);

                            if (algoritmo_actual.tieneLogs())
                                archivoResultados.escribirLogs(coste, algoritmo_actual.getLog());
                            else
                                archivoResultados.escribir(coste);

                        }

                    } else {
                        solucion = algoritmo_actual.resolver(flujos,distancias,null, k, iteraciones);
                        coste = algoritmo_actual.calcularCoste(flujos, distancias, solucion);

                        archivoResultados = new ArchivoResultados(ruta_base, algoritmo_actual.nombreAlgoritmo(), algoritmo_actual.siglasAlgoritmo(), dataset);

                        imprimirSolucion(solucion, algoritmo_actual, dataset, flujos, distancias,null);
                        archivoResultados.escribir(coste);
                    }

                }

            }

        } catch (IOException ex) {
            System.err.println("Ha ocurrido un error: " + ex.getMessage());
        }
    }
}