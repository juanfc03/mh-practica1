package metaheuristicas.app.archivos;

import metaheuristicas.app.utils.Parser;
import metaheuristicas.app.utils.Trimmer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ArchivoDatos implements Archivo {

    private final FileReader lector;
    private int[][] flujos;
    private int[][] distancias;

    public ArchivoDatos(String ruta_base, String ruta_dataset) throws IOException {
        lector = new FileReader(ruta_base + ruta_dataset);
    }

    @Override
    public void lectura() throws IOException {
        leerDatos();
    }

    private void copiarDatos(int tam, int[][] datos, BufferedReader br) throws IOException {
        for (int i = 0; i < tam; i++) {
            String[] aCopiar = Trimmer.eliminarTodosEspacios(br.readLine());

            for (int j = 0; j < tam; j++)
                datos[i][j] = Parser.toInt(aCopiar[j]);
        }

    }

    private void leerDatos() throws IOException {
        BufferedReader br = new BufferedReader(lector);

        int tam = Parser.toInt(br.readLine().trim());
        br.readLine();

        flujos = new int[tam][tam];
        distancias = new int[tam][tam];

        copiarDatos(tam, flujos, br);
        br.readLine();
        copiarDatos(tam, distancias, br);
    }

    //Getters
    public int[][] getFlujos() {
        return flujos;
    }

    public int[][] getDistancias() {
        return distancias;
    }

}
