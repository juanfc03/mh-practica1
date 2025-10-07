package metaheuristicas.app.archivos;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ArchivoResultados implements Archivo {
    private final File archivo;

    public ArchivoResultados(String ruta_base, String ruta_resultados) {
        String ruta;
        String carpeta_resultados = "results/";
        ruta = ruta_base + carpeta_resultados + ruta_resultados;
        this.archivo = new File(ruta);
        eliminar();
    }

    @Override
    public void lectura() {}

    public void escribir(String nombre, String dataset, String semilla, int coste) throws IOException {

        semilla = semilla == null ? "Sin semilla" : semilla;

        FileWriter writer = new FileWriter(archivo, true);

        abrir_escritura(writer, "Nombre del Algoritmo: ", nombre);
        abrir_escritura(writer, "\nDataset", dataset);
        abrir_escritura(writer, "\nSemilla", semilla);
        abrir_escritura(writer, "\nCoste: ", String.valueOf(coste));
        abrir_escritura(writer, "\n\n", "");

        cerrar_escritura(writer);
    }

    private void abrir_escritura(FileWriter fileWriter, String texto, String atributo) throws IOException {
        if (atributo.isBlank()) atributo = "";
        fileWriter.write(texto + atributo);
    }

    private void cerrar_escritura(FileWriter fileWriter) throws IOException {
        fileWriter.close();
    }

    private void eliminar() {
        if (archivo.exists()) {
            archivo.delete();
        }
    }
}
