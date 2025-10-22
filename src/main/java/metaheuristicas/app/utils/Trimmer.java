package metaheuristicas.app.utils;

public class Trimmer {
    public static String eliminarEspacio(String str) {
        return str.trim();
    }
    public static String[] eliminarTodosEspacios(String str) {
        return str.trim().split("\\s+");
    }
    public static String eliminarExtension(String str) {
        return str.substring(0,str.lastIndexOf("."));
    }
}
