package aed;

public class TesteosCaseros {
    public static void main(String[] args) {
        // Crear un examen canónico
        int[] examenCanonico = { 1, 2, 3, 4, 5 };
        Edr edr = new Edr(2, 4, examenCanonico);

        edr.resolver(2, 2, 1);
        edr.resolver(1, 1, 2);
        edr.resolver(3, 0, 0);
        edr.resolver(3, 1, 2);
        edr.resolver(0, 0, 1);
        edr.resolver(0, 1, 2);

        for (int n = 0; n < 4; n++) {
            System.out.println(edr._estudiantesPorPromedio.desencolar().getEstudiante().getId());
        }
        // Realizar algunas operaciones de prueba
        // ...
    }
}
