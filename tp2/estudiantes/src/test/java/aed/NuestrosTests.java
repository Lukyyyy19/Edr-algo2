package aed;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.Arrays;

public class NuestrosTests {
    Edr edr;
    int d_aula;
    int cant_alumnos;
    int[] solucion;

    @BeforeEach
    void setUp() {
        d_aula = 5;
        cant_alumnos = 4;
        solucion = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        edr = new Edr(d_aula, cant_alumnos, solucion);
    }

    @Test
    void estudiante_cambia_respuesta_incorrecta_por_correcta() {
        double[] notas;
        double[] notas_esperadas;

        // Estudiante 0 responde mal
        edr.resolver(0, 0, 5);
        notas = edr.notas();
        notas_esperadas = new double[] { 0.0, 0.0, 0.0, 0.0 };
        assertTrue(Arrays.equals(notas_esperadas, notas));

        // Estudiante 0 cambia a respuesta correcta
        edr.resolver(0, 0, 0);
        notas = edr.notas();
        notas_esperadas = new double[] { 10.0, 0.0, 0.0, 0.0 };
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    @Test
    void estudiante_cambia_respuesta_correcta_por_incorrecta() {
        double[] notas;
        double[] notas_esperadas;

        // Estudiante 0 responde bien
        edr.resolver(0, 0, 0);
        edr.resolver(0, 1, 1);
        notas = edr.notas();
        notas_esperadas = new double[] { 20.0, 0.0, 0.0, 0.0 };
        assertTrue(Arrays.equals(notas_esperadas, notas));

        // Estudiante 0 cambia respuesta correcta por incorrecta
        edr.resolver(0, 0, 5);
        notas = edr.notas();
        notas_esperadas = new double[] { 10.0, 0.0, 0.0, 0.0 };
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    @Test
    void estudiante_cambia_entre_respuestas_incorrectas() {
        double[] notas;
        double[] notas_esperadas;

        // Estudiante 0 responde mal
        edr.resolver(0, 0, 5);
        notas = edr.notas();
        notas_esperadas = new double[] { 0.0, 0.0, 0.0, 0.0 };
        assertTrue(Arrays.equals(notas_esperadas, notas));

        // Estudiante 0 cambia a otra respuesta incorrecta
        edr.resolver(0, 0, 7);
        notas = edr.notas();
        notas_esperadas = new double[] { 0.0, 0.0, 0.0, 0.0 };
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    @Test
    void entregar_sin_responder_nada() {
        double[] notas;
        double[] notas_esperadas;

        // Todos entregan sin responder
        for (int alumno = 0; alumno < 4; alumno++) {
            edr.entregar(alumno);
        }

        notas = edr.notas();
        notas_esperadas = new double[] { 0.0, 0.0, 0.0, 0.0 };
        assertTrue(Arrays.equals(notas_esperadas, notas));

        int[] copiones = edr.chequearCopias();
        int[] copiones_esperados = new int[] {};
        assertTrue(Arrays.equals(copiones_esperados, copiones));

        NotaFinal[] notas_finales = edr.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[] {
                new NotaFinal(0.0, 3),
                new NotaFinal(0.0, 2),
                new NotaFinal(0.0, 1),
                new NotaFinal(0.0, 0)
        };

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));
    }

    @Test
    void copiarse_elige_vecino_con_mas_respuestas() {
        double[] notas;
        double[] notas_esperadas;

        // Estudiante 0 tiene 1 respuesta
        edr.resolver(0, 0, 0);

        // Estudiante 2 tiene 2 respuestas
        edr.resolver(2, 1, 1);
        edr.resolver(2, 2, 2);

        // Estudiante 1 se copia (vecinos: 0 y 2)
        // Debe copiar de estudiante 2 que tiene más respuestas
        edr.copiarse(1);

        notas = edr.notas();
        notas_esperadas = new double[] { 10.0, 10.0, 20.0, 0.0 };
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    @Test
    void corregir_con_diferentes_notas_mismo_orden() {
        double[] notas;

        // Crear notas diferentes para cada estudiante
        edr.resolver(0, 0, 0);
        edr.resolver(0, 1, 1);
        edr.resolver(0, 2, 2);

        edr.resolver(1, 0, 0);
        edr.resolver(1, 1, 1);

        edr.resolver(2, 0, 0);

        for (int alumno = 0; alumno < 4; alumno++) {
            edr.entregar(alumno);
        }

        notas = edr.notas();
        double[] notas_esperadas = new double[] { 30.0, 20.0, 10.0, 0.0 };
        assertTrue(Arrays.equals(notas_esperadas, notas));

        NotaFinal[] notas_finales = edr.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[] {
                new NotaFinal(30.0, 0),
                new NotaFinal(20.0, 1),
                new NotaFinal(10.0, 2),
                new NotaFinal(0.0, 3)
        };

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));
    }

    @Test
    void solo_un_estudiante() {
        d_aula = 5;
        cant_alumnos = 1;
        solucion = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        Edr solo = new Edr(d_aula, cant_alumnos, solucion);
        double[] notas;
        double[] notas_esperadas;

        solo.copiarse(cant_alumnos - 1);

        notas = solo.notas();
        notas_esperadas = new double[] { 0.0 };
        assertTrue(Arrays.equals(notas_esperadas, notas));

        solo.consultarDarkWeb(cant_alumnos, solucion);

        // // Solo el estudiante 0 responde todas las preguntas correctamente
        // for (int pregunta = 0; pregunta < 10; pregunta++) {
        // edr.resolver(0, pregunta, pregunta);
        // }
        // edr.entregar(0);
        int[] copiones = solo.chequearCopias();
        int[] copiones_esperados = new int[] {};
        assertTrue(Arrays.equals(copiones_esperados, copiones));

        NotaFinal[] notas_finales = solo.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[] {
                new NotaFinal(100.0, 0)
        };

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));

    }

    @Test
    void estudiante_esquina() {
        d_aula = 5;
        cant_alumnos = 3;
        solucion = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        Edr esq = new Edr(d_aula, cant_alumnos, solucion);
        assertTrue(Arrays.equals(esq.notas(), new double[] { 0.0, 0.0, 0.0 }));
        esq.resolver(1, 3, 3);

        esq.copiarse(0);
        assertTrue(Arrays.equals(esq.notas(), new double[] { 10.0, 10.0, 0.0 }));
    }

    @Test
    void dark_misma_nota() {
        d_aula = 5;
        cant_alumnos = 3;
        solucion = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        Edr edr3 = new Edr(d_aula, cant_alumnos, solucion);
        assertTrue(Arrays.equals(edr3.notas(), new double[] { 0.0, 0.0, 0.0 }));
        edr3.consultarDarkWeb(2, solucion);
        assertTrue(Arrays.equals(edr3.notas(), new double[] { 100.0, 100.0, 0.0 }));
    }

    @Test
    void vacio_no_cambia() {
        d_aula = 5;
        cant_alumnos = 3;
        solucion = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        Edr edr4 = new Edr(d_aula, cant_alumnos, solucion);
        assertTrue(Arrays.equals(edr4.notas(), new double[] { 0.0, 0.0, 0.0 }));
        edr4.consultarDarkWeb(1, new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 });
        assertTrue(Arrays.equals(edr4.notas(), new double[] { 0.0, 0.0, 0.0 }));
        NotaFinal[] notas_finales = edr4.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[] {
                new NotaFinal(0.0, 2),
                new NotaFinal(0.0, 1),
                new NotaFinal(0.0, 0),
        };
        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));
    }

    @Test
    void menor_id_mayor_nota() {
        d_aula = 5;
        cant_alumnos = 3;
        solucion = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        Edr edr4 = new Edr(d_aula, cant_alumnos, solucion);
        edr4.resolver(0, 0, 0);
        edr4.resolver(0, 1, 1);
        edr4.resolver(1, 0, 0);
        NotaFinal[] notas_finales = edr4.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[] {
                new NotaFinal(20.0, 0),
                new NotaFinal(10.0, 1),
                new NotaFinal(0.0, 2),
        };
        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));
    }
}
