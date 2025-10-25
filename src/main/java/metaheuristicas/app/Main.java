package metaheuristicas.app;

import metaheuristicas.app.algoritmos.Algoritmo;
import metaheuristicas.app.algoritmos.AlgoritmoFactory;
import metaheuristicas.app.archivos.ArchivoDatos;
import metaheuristicas.app.archivos.ArchivoParametros;
import metaheuristicas.app.archivos.ArchivoResultados;
import metaheuristicas.app.utils.Parser;
import metaheuristicas.app.utils.Validator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class Main {

    private static String[] cargar(Map<String,String[]> parametros, String nombreParametro) {
        return parametros.get(nombreParametro);
    }

    private static void imprimirSolucion(int[] solucion, Algoritmo algoritmo, String dataset,
                                         int[][] flujos, int[][] distancias, String semilla, long endtime){
        semilla = semilla == null ? "No usa semilla" : semilla;

        System.out.println("\n<--------------------------------------------------------------------------------->"
                            + "\n Algoritmo: " + algoritmo.nombreAlgoritmo()
                            + "\n Dataset: " + dataset
                            + "\n Semilla: " + semilla
                            + "\n Coste: " + algoritmo.calcularCoste(flujos, distancias, solucion)
                            + "\n Tiempo de ejecución: " + endtime
                            + "\n<--------------------------------------------------------------------------------->"
                            + "\n Solucion: ");


        System.out.println(Arrays.toString(solucion));

    }

    /**
     * En la Terminal, escribir: java -cp target/classes metaheuristicas.app.Main parametros.txt
     */
    public static void main(String[] args) {

        int k;
        int iteraciones;
        int coste;
        int tenencia;
        float oscilacion;
        float estancamiento;

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
            tenencia = Parser.toInt(cargar(parametros, "Tenencia")[0]);
            oscilacion = Parser.toFloat(cargar(parametros, "Oscilacion")[0]);
            estancamiento = Parser.toFloat(cargar(parametros, "Estancamiento")[0]);

            for (String dataset : datasets) {

                ArchivoDatos datos = new ArchivoDatos(ruta_base, dataset);
                datos.lectura();

                flujos = datos.getFlujos();
                distancias = datos.getDistancias();

                for (String algoritmo : algoritmos) {
                    long startTime = System.currentTimeMillis();
                    long endTime;
                    Algoritmo algoritmo_actual = AlgoritmoFactory.nuevoAlgoritmo(algoritmo);
                    if(algoritmo_actual.requiereSemilla()) {
                        for (String semilla : semillas) {
                            Validator.validateSeed(semilla);
                            if(algoritmo_actual.getLog()!=null)
                                algoritmo_actual.getLog().clear();

                            if (algoritmo_actual.usaTenencia())
                                solucion = algoritmo_actual.resolver(flujos, distancias, semilla, k, iteraciones, tenencia, oscilacion, estancamiento);
                            else
                                solucion = algoritmo_actual.resolver(flujos, distancias, semilla, k, iteraciones, 0, 0, 0);
                            endTime = System.currentTimeMillis() - startTime;
                            coste = algoritmo_actual.calcularCoste(flujos, distancias, solucion);

                            archivoResultados = new ArchivoResultados(ruta_base, algoritmo_actual.nombreAlgoritmo(), algoritmo_actual.siglasAlgoritmo(), dataset, semilla);

                            imprimirSolucion(solucion, algoritmo_actual, dataset, flujos, distancias, semilla, endTime);
                            if (algoritmo_actual.tieneLogs())
                                archivoResultados.escribirLogs(coste, algoritmo_actual.getLog());
                            else
                                archivoResultados.escribir(coste);

                        }

                    } else {
                        solucion = algoritmo_actual.resolver(flujos,distancias,null, k, iteraciones, 0, 0,0);
                        endTime = System.currentTimeMillis() - startTime;
                        coste = algoritmo_actual.calcularCoste(flujos, distancias, solucion);

                        archivoResultados = new ArchivoResultados(ruta_base, algoritmo_actual.nombreAlgoritmo(), algoritmo_actual.siglasAlgoritmo(), dataset);

                        imprimirSolucion(solucion, algoritmo_actual, dataset, flujos, distancias,null, endTime);
                        archivoResultados.escribir(coste);
                    }
                }

            }

        } catch (IOException ex) {
            System.err.println("Ha ocurrido un error: " + ex.getMessage());
        }
    }
}