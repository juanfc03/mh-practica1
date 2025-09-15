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

    public static void leerMatriz(int [][] matriz){

        for(int[] fila :  matriz){

            System.out.println(Arrays.toString(fila));

        }
        System.out.println();

    }

    public static int[][][] leerDatosMatriz(String rutaFichero) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(rutaFichero));
        int tam = Integer.parseInt(br.readLine());
        br.readLine();

        int [][] F = new int[tam][tam];
        int [][] D = new int[tam][tam];
        for (int i = 0; i < tam; i++) {

            String[] datos = br.readLine().trim().split("\\s+");
            for (int j = 0; j < tam; j++) {

                F[i][j] = Integer.parseInt(datos[j]);

            }

        }

        br.readLine();

        for (int i = 0; i < tam; i++) {

            String[] datos = br.readLine().trim().split("\\s+");
            for (int j = 0; j < tam; j++) {

                D[i][j] = Integer.parseInt(datos[j]);

            }

        }

        br.close();
        return new int[][][]{F,D};

    }

    public static Map<String, String[]> leerParametrosArgs(String ruta) throws IOException {

        Map<String, String[]> parametros = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        String linea;
        while ((linea = br.readLine()) != null) {
            linea = linea.trim();
            if (linea.isEmpty() || linea.startsWith("#")) continue; // Ignora líneas vacías o comentarios
            String[] partes = linea.split("=", 2);
            if (partes.length == 2) {

                String[] valores = Arrays.stream(partes[1].split(","))
                        .map(String::trim)
                        .toArray(String[]::new);
                parametros.put(partes[0].trim(), valores);
            }
        }
        br.close();
        return parametros;
    }


    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Debes pasar la ruta del fichero de parámetros como argumento.");
            return;
        }

        try {
            // Poner en cmd.exe java -cp target/classes metaheuristicas.app.Main src/main/resources/parametros.txt
            Map<String, String[]> params = leerParametrosArgs(args[0]);
            String[] algoritmo = params.get("Algoritmo");
            String[] dataset = params.get("Dataset");

            //HACK solución temporal, pillamos el primer fichero del dataset, luego habrá que pillar varios
            int[][][] matrices = leerDatosMatriz("src/main/resources/" + dataset[0]);
            int[][] F = matrices[0];
            int[][] D = matrices[1];

            System.out.println("Matriz de flujo (F):");
            leerMatriz(F);
            System.out.println("Matriz de distancias (D):");
            leerMatriz(D);

            System.out.println("Algoritmos: " + Arrays.toString(algoritmo));
            System.out.println("Dataset: " + Arrays.toString(dataset));

            //TODO Implementar greedy y después greedy aleatorio
            //TODO Crearse cada uno una rama a partir de develop

        } catch (Exception ex) {
            System.err.println("Ha ocurrido un error: " + ex.getMessage());
        }
    }
}