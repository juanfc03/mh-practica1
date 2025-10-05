package metaheuristicas.app;

import metaheuristicas.app.algoritmos.Algoritmo;
import metaheuristicas.app.algoritmos.AlgoritmoFactory;
//import metaheuristicas.app.utils.LeerMatriz;
import metaheuristicas.app.utils.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
//import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static Map<String, String[]> leerParametrosArgs(String ruta) throws IOException {

        Map<String, String[]> parametros = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        String linea;
        while ((linea = br.readLine()) != null) {
            linea = linea.trim();
            if (linea.isEmpty() || linea.startsWith("#")) continue; // Ignora líneas vacías o comentarios
            String[] partes = linea.split("=", 2);
            if (partes.length == 2) {

                String[] valores = partes[1].trim().split("\\s+");
                parametros.put(partes[0].trim(), valores);
            }
        }
        br.close();
        return parametros;
    }

    private static String[] cargar(Map<String,String[]> parametros, String nombreParametro) {
        return parametros.get(nombreParametro);
    }

    /*
        private static void mostrarDatosCargados(String[] algoritmos, String[] datasets, String[] semillas, int k) {
            System.out.println("DATOS CARGADOS");
            System.out.println("Algoritmos: " + Arrays.toString(algoritmos));
            System.out.println("Datasets: " + Arrays.toString(datasets));
            System.out.println("Semillas: " + Arrays.toString(semillas));
            System.out.println("K: " + k);
        }
    */

    /*
    private static void mostrarDataset(ArchivoDatos datos, String dataset) {
        System.out.println("\n############################");
        System.out.println("## DATASET: " + dataset);
        System.out.println("############################");

        System.out.println("Matriz de flujo (F):");
        LeerMatriz.leer(datos.getFlujos());
        System.out.println("Matriz de distancias (D):");
        LeerMatriz.leer(datos.getDistancias());
    }
    */

    private static void imprimirSolucion(int[] solucion, Algoritmo algoritmo, String dataset,
                                         int[][] flujos, int[][] distancias, String semilla){
        System.out.println("\n=== Algoritmo: "
                            + algoritmo.nombreAlgoritmo()
                            + "dataset=" + dataset
                            + "Semilla=" + semilla == null ? "No usa semilla" : semilla
                            + "===");

        System.out.println("Solución:");

        for (int i = 0; i < solucion.length; i++)
            System.out.println("Departamento " + (i + 1) + " -> Localización " + solucion[i]);

        //System.out.println("Coste: " + algoritmo_actual.calcularCoste(datos.getFlujos(), datos.getDistancias(), solucion));
        System.out.println("Coste: " + algoritmo.calcularCoste(flujos, distancias, solucion));

    }

    public static void main(String[] args) {

        // Poner en cmd.exe java -cp target/classes metaheuristicas.app.Main src/main/resources/parametros.txt

        int k;
        long seed;
        int[] solucion;
        int[][] flujos;
        int[][] distancias;

        ArchivoDatos datos;
        Algoritmo algoritmo_actual;

        String ruta_base="src/main/resources/";
        String[] algoritmos;
        String[] datasets;
        String[] semillas;

        if (args.length < 1) {
            System.err.println("Debes pasar la ruta del fichero de parámetros como argumento.");
            return;
        }

        try {

            Map<String, String[]> parametros = leerParametrosArgs(args[0]);

            algoritmos = cargar(parametros, "Algoritmos");
            datasets = cargar(parametros, "Dataset");
            semillas = parametros.getOrDefault("Semillas", new String[0]);
            k = Parser.toInt(cargar(parametros, "K")[0]);

            //mostrarDatosCargados(algoritmos, datasets, semillas, k);

            for (String dataset : datasets) {

                datos = new ArchivoDatos(ruta_base + dataset);

                //mostrarDataset(datos, dataset);

                flujos = datos.getFlujos();
                distancias = datos.getDistancias();

                for(String algoritmo : algoritmos){

                    algoritmo_actual = AlgoritmoFactory.nuevoAlgoritmo(algoritmo);

                    if (!algoritmo_actual.usaSemilla() || !(semillas.length > 0)) {
                        solucion = algoritmo_actual.resolver(flujos, distancias, null, algoritmo_actual.usaK() ? k : 0);

                        imprimirSolucion(solucion, algoritmo_actual, dataset, flujos, distancias, null);

                        return;
                    }

                    for(String semilla : semillas){ //Si hay semillas, ejecutamos una por semilla

                        if (semilla == null || semilla.isBlank()) {
                            throw new IllegalArgumentException("Valor de semilla vacío en parámetros");
                        }

                        seed = Parser.toLong(semilla);

                        solucion = algoritmo_actual.resolver(flujos, distancias, seed, algoritmo_actual.usaK() ? k : 0);

                        imprimirSolucion(solucion, algoritmo_actual, dataset, flujos, distancias, semilla);
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("Ha ocurrido un error: " + ex.getMessage());
        }
    }
}