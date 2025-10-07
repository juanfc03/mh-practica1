package metaheuristicas.app.utils;

public class Validator {

    public static void validateSeed(String seed) {
        if (seed == null || seed.isBlank())
            throw new IllegalArgumentException("Valor de semilla vacío en parámetros");
    }
}
