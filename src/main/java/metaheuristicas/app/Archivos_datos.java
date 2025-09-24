package metaheuristicas.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Archivos_datos {

    private final String nombre;
    private int[][] flujos;
    private int[][] distancias;

    public Archivos_datos(String ruta_fichero) throws IOException {

        this.nombre = ruta_fichero;
        leerDatosMatriz(ruta_fichero);

    }

    private void leerDatosMatriz(String rutaFichero) throws IOException {


        try (BufferedReader br = new BufferedReader(new FileReader(rutaFichero))) {

            int tam = Integer.parseInt(br.readLine().trim());
            br.readLine();

            flujos = new int[tam][tam];
            distancias = new int[tam][tam];

            for (int i = 0; i < tam; i++) {

                String[] datos = br.readLine().trim().split("\\s+");
                for (int j = 0; j < tam; j++) {

                    flujos[i][j] = Integer.parseInt(datos[j]);

                }

            }

            br.readLine();

            for (int i = 0; i < tam; i++) {

                String[] datos = br.readLine().trim().split("\\s+");
                for (int j = 0; j < tam; j++) {

                    distancias[i][j] = Integer.parseInt(datos[j]);

                }
            }
        }
    }

    //Getters
    public String getNombre() {
        return nombre;
    }

    public int[][] getFlujos() {
        return flujos;
    }

    public int[][] getDistancias() {
        return distancias;
    }

    public int getTam() {
        return flujos != null ? flujos.length : 0;
    }

}
