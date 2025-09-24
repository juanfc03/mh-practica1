package metaheuristicas.app.algoritmos;

public interface AlgoritmoFactory {
    public static Algoritmo nuevoAlgoritmo(String nombre) {

        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre del algoritmo no puede estar vacío");

        return switch (nombre.toUpperCase()) {
            case "GREEDY" -> new Greedy();
            default -> throw new IllegalArgumentException("Algoritmo no existe" + nombre);
        };

    }
}
