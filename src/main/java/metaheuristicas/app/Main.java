package metaheuristicas.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Comparator;
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

    /**
     * @brief Heurística Greedy para el Quadratic Assignment Problem (QAP).
     *
     * Esta función genera una solución asignando
     * departamentos a localizaciones de manera intuitiva:
     * 1) Calcula la "importancia" de cada departamento como la suma de su flujo hacia los demás.
     * 2) Calcula la "centralidad" de cada localización como la suma de sus distancias hacia todas las demás.
     * 3) Ordena los departamentos de mayor a menor importancia y las localizaciones de menor a mayor centralidad.
     * 4) Asigna el departamento más importante a la localización más central, el siguiente al segundo, etc.
     *
     * @param F Matriz de flujo entre departamentos (f_ij).
     * @param D Matriz de distancias entre localizaciones (d_kl).
     * @return Array con la permutación de asignaciones inicial.
     */
    public static int[] greedy(int[][] F, int[][] D) {

        int tam = F.length;

        // 1) y 2) Calculamos la importancia de cada departamento y centralidad de cada localización
        int[] importancia = new int[tam];
        int[] centralidad = new int[tam];
        for(int i = 0; i < tam; i++)
            for(int j = 0; j < tam; j++){

                importancia[i] += F[i][j]; // Este sumatorio va de i a j solo porque es simétrica
                centralidad[i] += D[i][j];

            }


        // 3) Ordenar los departamentos de mayor a menor importancia
        // y las localizaciones de menor a mayor centralidad
        Integer[] ordenDepartamentos = new Integer[tam];
        for (int i = 0; i < tam; i++) ordenDepartamentos[i] = i;
        Arrays.sort(ordenDepartamentos, (a,b) -> Integer.compare(importancia[b], importancia[a]));
        //System.out.println(Arrays.toString(ordenDepartamentos));

        Integer[] ordenLocalizaciones = new Integer[tam];
        for (int i = 0; i < tam; i++) ordenLocalizaciones[i] = i;
        Arrays.sort(ordenLocalizaciones, Comparator.comparingInt(a -> centralidad[a]));
        //System.out.println(Arrays.toString(ordenLocalizaciones));

        // 4) Damos la solución, asignar el más importante a la localización más central
        int[] S = new int[tam];
        for (int i = 0; i < tam; i++) S[ordenDepartamentos[i]] = ordenLocalizaciones[i] + 1;

        return S;
    }

    /**
     * @brief Calcula el coste total de una asignación de departamentos a localizaciones.
     *
     * El coste se calcula como la suma de F[i][j] * D[S[i]-1][S[j]-1] para todos los pares (i,j).
     *
     * @param F Matriz de flujo entre departamentos.
     * @param D Matriz de distancias entre localizaciones.
     * @param S Asignación de departamentos a localizaciones (1-based).
     * @return Coste total de la asignación.
     */
    public static int calcularCoste(int[][] F,  int[][] D, int[] S) {

        int coste=0;
        int tam=F.length;
        for(int i = 0; i < tam; i++)
            for(int j = 0; j < tam; j++)
                coste+=F[i][j] * D[S[i]-1][S[j]-1];

        return coste;

    }

    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Debes pasar la ruta del fichero de parámetros como argumento.");
            return;
        }

        try {
            // Poner en cmd.exe java -cp target/classes metaheuristicas.app.Main src/main/resources/parametros.txt
            Map<String, String[]> params = leerParametrosArgs(args[0]);
            //String[] algoritmo = params.get("Algoritmo");
            String[] dataset = params.get("Dataset");

            //HACK solución temporal, pillamos el primer fichero del dataset, luego habrá que pillar varios
            int[][][] matrices = leerDatosMatriz("src/main/resources/" + dataset[0]);
            int[][] F = matrices[0];
            int[][] D = matrices[1];

            System.out.println("Matriz de flujo (F):");
            leerMatriz(F);
            System.out.println("Matriz de distancias (D):");
            leerMatriz(D);

            //System.out.println("Algoritmos: " + Arrays.toString(algoritmo));
            //System.out.println("Dataset: " + Arrays.toString(dataset));

            int[] S = greedy(F, D);
            for (int i = 0; i < S.length; i++)
                System.out.println("Departamento " + (i + 1) + " -> Localización " + S[i]);

            System.out.println("Coste: " + calcularCoste(F, D, S));

            // TODO Implementar Greedy aleatorio


        } catch (Exception ex) {
            System.err.println("Ha ocurrido un error: " + ex.getMessage());
        }
    }
}