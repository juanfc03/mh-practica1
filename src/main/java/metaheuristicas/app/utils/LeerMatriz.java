package metaheuristicas.app.utils;

import java.util.Arrays;

public class LeerMatriz {

    public static void leer(int[][] matriz){

        for(int[] fila :  matriz)
            System.out.println(Arrays.toString(fila));
        System.out.println();

    }

}
