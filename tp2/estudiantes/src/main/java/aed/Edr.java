package aed;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Handler;

import aed.ColaPrioridadHeap.HandlerHeap;
import javafx.util.Pair;

public class Edr {

    private HandlerHeap[] _estudiantesPorId;
    private ColaPrioridadHeap _estudiantesPorPromedio;
    private int d_aula;

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        d_aula = LadoAula;
        Pair<Integer, Integer> posicion = new Pair<>(0, 0);
        _estudiantesPorId = new HandlerHeap[Cant_estudiantes];
        _estudiantesPorPromedio = new ColaPrioridadHeap(Cant_estudiantes);
        for (int i = 0; i < Cant_estudiantes; i++) {
            posicion = new Pair<>(i % LadoAula, i / LadoAula);
            Estudiante estudiante = new Estudiante(i, new Examen(ExamenCanonico.length, ExamenCanonico), posicion);
            _estudiantesPorId[i] = _estudiantesPorPromedio.insertar(estudiante);
        }
    }

    // -------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas() {
        double[] notas = new double[_estudiantesPorId.length];
        for (int i = 0; i < _estudiantesPorId.length; i++) {
            HandlerHeap handler = _estudiantesPorId[i];
            if (handler != null) {
                notas[i] = handler.getEstudiante().getExamen().getPromedio();
            }
        }
        return notas;
    }

    // ------------------------------------------------COPIARSE------------------------------------------------------------------------

    public void copiarse(int estudiante) {
        Estudiante est = _estudiantesPorId[estudiante].getEstudiante();
        int[] vecinos = { estudiante - 1, estudiante + 1, estudiante - d_aula };
        Estudiante mejorVecino = null;
        int mejorVecinoRtas = 0;
        for (int i = 0; i < vecinos.length; i++) {
            int rtasVecino = 0;
            int vecino = vecinos[i];
            if (vecino >= 0 && vecino < _estudiantesPorId.length) {
                HandlerHeap handlerVecino = _estudiantesPorId[vecino];
                if (handlerVecino != null) {
                    Estudiante estVecino = handlerVecino.getEstudiante();
                    for (int j = 0; j < estVecino.getExamen().cantidadPreguntas(); j++) {
                        if (estVecino.getExamen().getRespuesta(j) != -1 && est.getExamen().getRespuesta(j) == -1) {
                            rtasVecino++;
                        }
                    }
                    if (mejorVecino == null) {
                        mejorVecino = estVecino;
                        mejorVecinoRtas = rtasVecino;
                    } else if (rtasVecino >= mejorVecinoRtas) {
                        mejorVecino = estVecino;
                        mejorVecinoRtas = rtasVecino;
                    }

                }
            }
        }

        for (int i = 0; i < est.getExamen().cantidadPreguntas(); i++) {
            if (mejorVecino.getExamen().getRespuesta(i) != -1 && est.getExamen().getRespuesta(i) == -1) {
                est.getExamen().setRespuesta(i, mejorVecino.getExamen().getRespuesta(i));
                break;
            }
        }

    }

    // -----------------------------------------------RESOLVER----------------------------------------------------------------

    public void resolver(int estudiante, int NroEjercicio, int res) {
        HandlerHeap handler = _estudiantesPorId[estudiante];
        handler.getEstudiante().completarEjercicio(NroEjercicio, res);
        _estudiantesPorPromedio.reOrdenar(handler.getHeapIndex());

    }

    // ------------------------------------------------CONSULTAR DARK
    // WEB-------------------------------------------------------

    public void consultarDarkWeb(int n, int[] examenDW) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    // -------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        _estudiantesPorId[estudiante].getEstudiante().entregar();
    }

    // -----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        throw new UnsupportedOperationException("Sin implementar");
    }

    // -------------------------------------------------------CHEQUEAR
    // COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        ArrayList<Integer> copias = new ArrayList<>();
        for (int i = 0; i < _estudiantesPorId.length; i++) {
            Estudiante estudiante = _estudiantesPorId[i].getEstudiante();
            int respuestasCopiadas = 0;
            for (int j = 0; j < estudiante.getExamen().cantidadPreguntas(); j++) {
                double suma = 0;
                if (estudiante.getExamen().getRespuesta(j) != -1) {
                    for (int k = 0; k < _estudiantesPorId.length; k++) {
                        if (k != i) {
                            Estudiante otroEstudiante = _estudiantesPorId[k].getEstudiante();
                            if (otroEstudiante.getExamen().getRespuesta(j) == estudiante.getExamen().getRespuesta(j)) {
                                // Se encontró un compañero que copió la respuesta
                                suma += 1;
                            }
                        }
                    }
                }
                if (suma / (_estudiantesPorId.length - 1) >= 0.25) {
                    // Se encontró un compañero que copió la respuesta
                    respuestasCopiadas++;
                }
            }
            if (respuestasCopiadas > 0 && respuestasCopiadas == estudiante.getExamen().cantidadRespuestas()) {
                copias.add(i);
            }
        }
        int[] result = new int[copias.size()];
        for (int i = 0; i < copias.size(); i++) {
            result[i] = copias.get(i);
        }
        return result;
    }
}
