package aed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import aed.ColaPrioridadHeap.HandlerHeap;

public class Edr {

    private HandlerHeap[] _estudiantesPorId;
    public ColaPrioridadHeap _estudiantesPorPromedio;
    private int d_aula; // provisorio hasta ver que hacemos, si tenemos una clase aula o que
    private int[][] _conteoRespuestas;

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        d_aula = LadoAula;
        _estudiantesPorId = new HandlerHeap[Cant_estudiantes];
        _estudiantesPorPromedio = new ColaPrioridadHeap(Cant_estudiantes);
        _conteoRespuestas = new int[ExamenCanonico.length][10];
        for (int i = 0; i < Cant_estudiantes; i++) { // O(E)
            Estudiante estudiante = new Estudiante(i, new Examen(ExamenCanonico.length, ExamenCanonico)); // O(R)
            _estudiantesPorId[i] = _estudiantesPorPromedio.insertar(estudiante);
        } // Ordenar no suma complejidad por que estamos sumando un conjunto completo ya
          // ordenado (Heapify)
    }// O(E*R)

    // -------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas() {
        double[] notas = new double[_estudiantesPorId.length];
        for (int i = 0; i < _estudiantesPorId.length; i++) { // O(E)
            HandlerHeap handler = _estudiantesPorId[i];
            if (handler != null) {
                notas[i] = handler.getEstudiante().getExamen().getPromedio(); // O(1) //esto no se si esta bien o rompe
                                                                              // encapsulamiento
            }
        }
        return notas;
    }// O(E)

    // ------------------------------------------------COPIARSE------------------------------------------------------------------------

    public void copiarse(int estudiante) {
        Estudiante est = _estudiantesPorId[estudiante].getEstudiante();
        int[] vecinos = { estudiante - 1, estudiante + 1, (int) (estudiante - Math.ceil(d_aula / 2)) }; // toma todos
                                                                                                        // los Id de los
                                                                                                        // vecinos
        Estudiante mejorVecino = null;
        int mejorVecinoRtas = 0;
        for (int i = 0; i < vecinos.length; i++) {// O(1)
            int rtasVecino = 0;
            int vecino = vecinos[i];
            if (vecino >= 0 && vecino < _estudiantesPorId.length) {
                HandlerHeap handlerVecino = _estudiantesPorId[vecino];
                if (handlerVecino != null) {
                    Estudiante estVecino = handlerVecino.getEstudiante();
                    for (int j = 0; j < estVecino.getExamen().cantidadPreguntas(); j++) { // O(R)
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
                resolver(est.getId(), i, mejorVecino.getExamen().getRespuesta(i));
                break;
            }
        } // O(R)

    }// O(R)

    // -----------------------------------------------RESOLVER----------------------------------------------------------------

    public void resolver(int estudiante, int NroEjercicio, int res) {
        HandlerHeap handler = _estudiantesPorId[estudiante];
        _conteoRespuestas[NroEjercicio][res]++;
        handler.getEstudiante().completarEjercicio(NroEjercicio, res);
        _estudiantesPorPromedio.reOrdenar(handler.getHeapIndex());// O(log(E))

    }// O(log(E))

    // ------------------------------------------------CONSULTAR DARK
    // WEB-------------------------------------------------------

    public void consultarDarkWeb(int n, int[] examenDW) {
        HandlerHeap[] ests = new HandlerHeap[n];
        for (int i = 0; i < n; i++) {
            HandlerHeap estudiante = _estudiantesPorPromedio.desencolar();
            ests[i] = estudiante;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < examenDW.length; j++) {
                if (ests[i].getEstudiante().getExamen().getRespuesta(j) != -1) {
                    _conteoRespuestas[j][ests[i].getEstudiante().getExamen().getRespuesta(j)]--;
                }
                resolver(ests[i].getEstudiante().getId(), j, examenDW[j]);
            }
            _estudiantesPorPromedio.insertar(ests[i].getEstudiante());
        }
    }

    // -------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        HandlerHeap estudianteHandler = _estudiantesPorId[estudiante];
        estudianteHandler.getEstudiante().entregar();
        _estudiantesPorPromedio.reOrdenar(estudianteHandler.getHeapIndex());
    }// O(1)

    // -----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        ArrayList<NotaFinal> nfinalLista = new ArrayList<NotaFinal>();
        ColaPrioridadHeap cola = new ColaPrioridadHeap(_estudiantesPorPromedio.getLongitud());
        // for (int i = 0; i < _estudiantesPorPromedio.getLongitud(); i++) {
        // _estudiantesPorPromedio.reOrdenarInvertido(i);
        // }
        for (int i = 0; i < _estudiantesPorPromedio.getLongitud(); i++) {
            HandlerHeap handler = _estudiantesPorPromedio.getProm(i);
            if (!handler.getEstudiante().getSeCopio()) {
                // NotaFinal nfinal = new
                // NotaFinal(handler.getEstudiante().getExamen().getPromedio(),
                // handler.getEstudiante().getId());
                // nfinalLista.add(nfinal);
                HandlerHeap h = _estudiantesPorPromedio.getProm(i);
                if (h != null) {
                    cola.insertarInverso(h.getEstudiante());
                }
            }

        }
        for (int i = 0; i < cola.getLongitud(); i++) {
            HandlerHeap handler = cola.getProm(i);
            if (handler != null) {

                NotaFinal nfinal = new NotaFinal(handler.getEstudiante().getExamen().getPromedio(),
                        handler.getEstudiante().getId());
                nfinalLista.add(nfinal);
            }
        }
        NotaFinal[] ad = new NotaFinal[nfinalLista.size()];
        // ad = nfinalLista.toArray(ad);

        // Invertir el arreglo con un for loop
        // for (int i = 0; i < ad.length / 2; i++) {
        // NotaFinal temp = ad[i];
        // ad[i] = ad[ad.length - 1 - i];
        // ad[ad.length - 1 - i] = temp;
        // }

        // OREDENAR nfinalLista
        // Comparator que ordena por nota descendente y luego id descendente
        // Comparator<NotaFinal> comp = (a, b) -> {
        // int cmp = Double.compare(b._nota, a._nota); // nota descendente
        // if (cmp != 0)
        // return cmp;
        // return Integer.compare(b._id, a._id); // id descendente
        // };

        // Collections.sort(nfinalLista, comp);
        ad = nfinalLista.toArray(ad);
        return ad;
    }

    // -------------------------------------------------------CHEQUEAR
    // COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        ArrayList<Integer> copias = new ArrayList<>();
        for (int i = 0; i < _estudiantesPorId.length; i++) {// O(E)
            Estudiante estudiante = _estudiantesPorId[i].getEstudiante();
            int respuestasCopiadas = 0;
            for (int j = 0; j < estudiante.getExamen().cantidadPreguntas(); j++) {// O(R)
                // double suma = 0;
                if (estudiante.getExamen().getRespuesta(j) != -1) {
                    double cantidadRespuestas = _conteoRespuestas[j][estudiante.getExamen().getRespuesta(j)];
                    // for (int k = 0; k < _estudiantesPorId.length; k++) {// O(E)
                    // if (k != i) {
                    // Estudiante otroEstudiante = _estudiantesPorId[k].getEstudiante();
                    // if (otroEstudiante.getExamen().getRespuesta(j) ==
                    // estudiante.getExamen().getRespuesta(j)) {
                    // // Se encontró un compañero que copió la respuesta
                    // suma += 1;
                    // }
                    // }
                    // }
                    // }
                    if ((cantidadRespuestas - 1)
                            / (_estudiantesPorId.length - 1) >= 0.25) {
                        // Se encontró un compañero que copió la respuesta
                        respuestasCopiadas++;
                    }
                }
            }
            if (respuestasCopiadas > 0 && respuestasCopiadas == estudiante.getExamen().cantidadRespuestas()) {
                estudiante.setSeCopio(true);
                copias.add(i);
            }
        }
        int[] result = new int[copias.size()];
        for (int i = 0; i < copias.size(); i++) {
            result[i] = copias.get(i);
        } // Esto fue la forma rapida que se me ocurrio, seguro hay algo mejor
        return result;
    }// O(E^2 * R) NO RESPETA EL ENUNCIADO
}
