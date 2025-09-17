package metaheuristicas.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static Map<String, String[]> leerParametrosArgs(String ruta) throws IOException {

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

    private static Algoritmo crearAlgoritmo(String nombreAlgoritmo) {

        switch (nombreAlgoritmo.toUpperCase()) {

            case "GREEDY": return new GreedyAlg();
            //TODO Añadir los siguientes aquí...
            default: throw new IllegalArgumentException("Algoritmo no registrado: " + nombreAlgoritmo);

        }

    }

    private static void imprimirSolucion(int[] S){

        System.out.println("Solución:");
        for (int i = 0; i < S.length; i++)
            System.out.println("Departamento " + (i + 1) + " -> Localización " + S[i]);

    }

    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Debes pasar la ruta del fichero de parámetros como argumento.");
            return;
        }

        try {
            // Poner en cmd.exe java -cp target/classes metaheuristicas.app.Main src/main/resources/parametros.txt
            Map<String, String[]> params = leerParametrosArgs(args[0]);
            String[] algoritmos = params.get("Algoritmos");
            String[] datasets = params.get("Dataset");
            String[] semillas = params.getOrDefault("Semillas", new String[0]);
            String ruta_base="src/main/resources/";

            System.out.println("Algoritmos: " + Arrays.toString(algoritmos));
            System.out.println("Datasets: " + Arrays.toString(datasets));
            if (semillas.length > 0) System.out.println("Semillas: " + Arrays.toString(semillas));
            System.out.println();

            for (String ds : datasets) {

                System.out.println("\n############################");
                System.out.println("## DATASET: " + ds);
                System.out.println("############################");

                Archivos_datos datos = new Archivos_datos(ruta_base + ds);
                int[][] F = datos.getMatriz1();
                int[][] D = datos.getMatriz2();

                System.out.println("Matriz de flujo (F):");
                Archivos_datos.leerMatriz(F);
                System.out.println("Matriz de distancias (D):");
                Archivos_datos.leerMatriz(D);

                for(String nombre_alg : algoritmos){

                    Algoritmo alg = crearAlgoritmo(nombre_alg);

                    if(semillas.length > 0){ //Si hay semillas, ejecutamos una por semilla

                        for(String sem : semillas){

                            if (sem == null || sem.isBlank()) {
                                throw new IllegalArgumentException("Valor de semilla vacío en parámetros");
                            }
                            long seed = Long.parseLong(sem);
                            int[] S = alg.resolver(F, D, seed);
                            System.out.println("\n=== " + alg.nombreAlgoritmo()
                                    + " | dataset=" + ds + " | seed=" + seed + " ===");

                            imprimirSolucion(S);
                            System.out.println("Coste: " + alg.calcularCoste(datos.getMatriz1(), datos.getMatriz2(), S));

                        }

                    }else{

                        int[] S = alg.resolver(F, D);
                        System.out.println("\n=== " + alg.nombreAlgoritmo()
                                + " | dataset=" + ds + " ===");

                        imprimirSolucion(S);
                        System.out.println("Coste: " + alg.calcularCoste(datos.getMatriz1(), datos.getMatriz2(), S));

                    }

                }

            }


        } catch (Exception ex) {
            System.err.println("Ha ocurrido un error: " + ex.getMessage());
        }
    }
}