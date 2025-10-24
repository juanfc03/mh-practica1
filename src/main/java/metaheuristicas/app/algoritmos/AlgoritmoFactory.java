package metaheuristicas.app.algoritmos;

public interface AlgoritmoFactory {
    static Algoritmo nuevoAlgoritmo(String nombre) {

        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre del algoritmo no puede estar vacío");

        return switch (nombre.trim().toUpperCase()) {
            case "GREEDY" -> new AlgGEC4G2();
            case "GREEDYALEATORIO"  -> new AlgGAC4G2();
            case "BUSQUEDALOCAL" -> new AlgBLC4G2();
            case "BUSQUEDATABU" -> new AlgBTC4G2();
            default -> throw new IllegalArgumentException("Algoritmo no existe" + nombre);
        };

    }
}
