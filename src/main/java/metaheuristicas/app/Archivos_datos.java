package metaheuristicas.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Archivos_datos {

    private final String nombre;
    private int[][] matriz1;
    private int[][] matriz2;

    public Archivos_datos(String ruta_fichero) throws IOException {

        this.nombre = ruta_fichero;
        leerDatosMatriz(ruta_fichero);

    }

    private void leerDatosMatriz(String rutaFichero) throws IOException {


        try (BufferedReader br = new BufferedReader(new FileReader(rutaFichero))) {

            int tam = Integer.parseInt(br.readLine().trim());
            br.readLine();

            matriz1 = new int[tam][tam];
            matriz2 = new int[tam][tam];
            for (int i = 0; i < tam; i++) {

                String[] datos = br.readLine().trim().split("\\s+");
                for (int j = 0; j < tam; j++) {

                    matriz1[i][j] = Integer.parseInt(datos[j]);

                }

            }

            br.readLine();

            for (int i = 0; i < tam; i++) {

                String[] datos = br.readLine().trim().split("\\s+");
                for (int j = 0; j < tam; j++) {

                    matriz2[i][j] = Integer.parseInt(datos[j]);

                }

            }

        }

    }

    // Utilidades
    public static void leerMatriz(int[][] matriz){

        for(int[] fila :  matriz) System.out.println(Arrays.toString(fila));
        System.out.println();

    }

    //Getters
    public String getNombre() {
        return nombre;
    }
    public int[][] getMatriz1() {
        return matriz1;
    }
    public int[][] getMatriz2() {
        return matriz2;
    }
    public int getTam() {
        return matriz1 != null ? matriz1.length : 0;
    }

}
