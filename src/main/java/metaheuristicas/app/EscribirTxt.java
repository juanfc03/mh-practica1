package metaheuristicas.app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EscribirTxt {
    private final int coste;
    private final String nombreAlgoritmo;
    private final String dataset;
    private final String semilla;
    private final File archivo;

    private void escribir() {
        try {
            FileWriter escribir = new FileWriter(archivo, true);

            escribir.write("Nombre del Algoritmo: " + nombreAlgoritmo);
            escribir.write("\nDataset: " + dataset);
            escribir.write("\nSemilla: " + semilla);
            escribir.write("\nCoste: " + coste);
            escribir.write("\n\n");

            escribir.close();

        } catch (IOException e) {
            System.out.println("Error al escribir el archivo" + e.getMessage());
        }
    }

    public EscribirTxt(String nombreAlgoritmo, String dataset, String semilla, String ruta_base, int coste) {
        String ruta;
        this.nombreAlgoritmo = nombreAlgoritmo;
        this.dataset = dataset;
        this.semilla = semilla == null ? "Sin semilla" : semilla;
        this.coste = coste;
        ruta = ruta_base + "resultados.txt";
        this.archivo = new File(ruta);

        escribir();
    }
}
