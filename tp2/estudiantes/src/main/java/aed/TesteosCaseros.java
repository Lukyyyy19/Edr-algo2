package aed;

public class TesteosCaseros {
    public static void main(String[] args) {
        // Crear un examen canónico
        int[] examenCanonico = { 1, 2, 3, 4, 5 };
        Edr edr = new Edr(2, 4, examenCanonico);

        edr.resolver(2, 0, 1);
        // Realizar algunas operaciones de prueba
        // ...
    }
}
