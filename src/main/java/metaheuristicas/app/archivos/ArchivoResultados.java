package metaheuristicas.app.archivos;

import metaheuristicas.app.utils.Parser;
import metaheuristicas.app.utils.Trimmer;

import java.io.*;
import java.util.List;

public class ArchivoResultados implements Archivo {
    private final String nombreAlgoritmo;
    private final String siglasAlgoritmo;
    private final String nombreDataset;
    private final String numeroSemilla;
    private final String extensionArchivo = ".txt";
    private final File archivo;

    public ArchivoResultados(String ruta_base, String nombre_algoritmo, String siglas_algoritmo, String dataset) {
        String carpeta_resultados = "results/";
        String carpeta_especifica = nombre_algoritmo;
        File carpetaEsp = new File(ruta_base, carpeta_resultados + "/" + carpeta_especifica);

        if (!carpetaEsp.exists()) {
            carpetaEsp.mkdir();
        }

        numeroSemilla = "";
        nombreAlgoritmo = nombre_algoritmo;
        siglasAlgoritmo = siglas_algoritmo;
        nombreDataset = Trimmer.eliminarExtension(dataset);

        String nombre_archivo = siglas_algoritmo + "-" + nombreDataset + extensionArchivo;
        this.archivo = new File(carpetaEsp, nombre_archivo);

        eliminar();
    }

    public ArchivoResultados(String ruta_base, String nombre_algoritmo, String siglas_algoritmo, String dataset, String semilla) {
        String carpeta_resultados = "results/";
        String carpeta_especifica = nombre_algoritmo;
        File carpetaEsp = new File(ruta_base, carpeta_resultados + "/" + carpeta_especifica);

        if (!carpetaEsp.exists()) {
            carpetaEsp.mkdir();
        }

        nombreAlgoritmo = nombre_algoritmo;
        siglasAlgoritmo = siglas_algoritmo;
        nombreDataset = Trimmer.eliminarExtension(dataset);
        numeroSemilla = semilla == null ? "Sin semilla" : semilla;

        String nombre_archivo = siglas_algoritmo + "-" + nombreDataset + "-" + numeroSemilla + extensionArchivo;
        this.archivo = new File(carpetaEsp, nombre_archivo);

        eliminar();
    }

    @Override
    public void lectura() {}

    public void escribir(int coste) throws IOException {

        FileWriter writer = new FileWriter(archivo, true);

        abrir_escritura(writer, "Nombre del Algoritmo: ", nombreAlgoritmo);
        abrir_escritura(writer, "\nDataset:", nombreDataset);
        abrir_escritura(writer, "\nSemilla:", numeroSemilla);
        abrir_escritura(writer, "\nCoste: ", Parser.intToString(coste));
        abrir_escritura(writer, "\n\n", "");

        cerrar_escritura(writer);
    }

    public void escribirLogs(int coste, List<String> logs) throws IOException {
        try {
            FileWriter fileWriter = new FileWriter(archivo, true);

            abrir_escritura(fileWriter, "Nombre del Algoritmo: ", nombreAlgoritmo);
            abrir_escritura(fileWriter, "\nDataset:", nombreDataset);
            abrir_escritura(fileWriter, "\nSemilla:", numeroSemilla);
            abrir_escritura(fileWriter, "\n\n", "");

            for (String log : logs)
                fileWriter.write(log + "\n");

            abrir_escritura(fileWriter, "Coste final: ", Parser.intToString(coste));
            fileWriter.write("\n --------------------------- \n");

            cerrar_escritura(fileWriter);

        } catch (IOException e) {
            e.printStackTrace();
        }

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
