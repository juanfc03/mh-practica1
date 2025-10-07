package metaheuristicas.app.archivos;

import metaheuristicas.app.utils.Trimmer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ArchivoParametros implements Archivo {
    public final String ruta;
    private final Map<String, String[]> parametros;

    public ArchivoParametros(String ruta_base, String ruta_parametros) {
        parametros = new HashMap<>();
        ruta = ruta_base + ruta_parametros;
    }

    @Override
    public void lectura() throws IOException {
        leerParametros();
    }

    private void leerParametros() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        String linea;
        while ((linea = br.readLine()) != null) {
            linea = Trimmer.eliminarEspacio(linea);
            if (linea.isEmpty() || linea.startsWith("#")) continue; // Ignora líneas vacías o comentarios
            String[] partes = linea.split("=", 2);
            if (partes.length == 2) {
                String[] valores = Trimmer.eliminarTodosEspacios(partes[1]);
                parametros.put(partes[0].trim(), valores);
            }
        }
        br.close();
    }

    public Map<String, String[]> getParametros() {
        return parametros;
    }
}
