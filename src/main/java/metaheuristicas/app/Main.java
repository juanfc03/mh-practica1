package metaheuristicas.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.Arrays;

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

    public static void main(String[] args) {

        try{

            int[][][] matrices = leerDatosMatriz("src/main/resources/_ejemplo.txt");
            int[][] F = matrices[0];
            int[][] D = matrices[1];

            System.out.println("Matriz de flujo (F):");
            leerMatriz(F);
            System.out.println("Matriz de distancias (D):");
            leerMatriz(D);

            //TODO Implementar greedy y después greedy aleatorio
            //TODO Crearse cada uno una rama a partir de develop

        }catch(Exception ex){

            System.err.println("Ha ocurrido un error: " +ex.getMessage());

        }

    }
}