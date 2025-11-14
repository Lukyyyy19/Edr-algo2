package aed;

import java.util.ArrayList;
import aed.ColaPrioridadHeap.HandlerHeap;

public class Edr {

    private HandlerHeap[] _estudiantesPorId;
    private ColaPrioridadHeap _estudiantesPorPromedio;
    private int _ladoAula;
    private int[][] _conteoRespuestas;
    private int estudiantes_copiones;

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        _ladoAula = LadoAula;
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
        int[] vecinos = { estudiante - 1, estudiante + 1, (int) (estudiante - Math.ceil(_ladoAula / 2)) }; // toma todos
                                                                                                           // los Id de
                                                                                                           // los
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
                    } else if (rtasVecino > mejorVecinoRtas) {
                        mejorVecino = estVecino;
                        mejorVecinoRtas = rtasVecino;
                    } else if (rtasVecino == mejorVecinoRtas) {
                        if (mejorVecino.getId() < estVecino.getId()) {
                            mejorVecino = estVecino;
                            mejorVecinoRtas = rtasVecino;
                        }
                    }

                }
            }
        }
        if (mejorVecino != null) {
          
            for (int i = 0; i < est.getExamen().cantidadPreguntas(); i++) {
                if (mejorVecino.getExamen().getRespuesta(i) != -1 && est.getExamen().getRespuesta(i) == -1) {
                    resolver(est.getId(), i, mejorVecino.getExamen().getRespuesta(i));
                    break;
                }
            }
        } // O(R)

    }// O(R)

    // -----------------------------------------------RESOLVER----------------------------------------------------------------

    public void resolver(int estudiante, int NroEjercicio, int res) {
        HandlerHeap handler = _estudiantesPorId[estudiante];
        if(res!=-1){
            
            _conteoRespuestas[NroEjercicio][res]++;
        }
        handler.getEstudiante().completarEjercicio(NroEjercicio, res);
        if (handler.getHeapIndex() >= 0) {
            _estudiantesPorPromedio.reOrdenar(handler.getHeapIndex());// O(log(E))
        }

    }// O(log(E))

    // ------------------------------------------------CONSULTAR DARK
    // WEB-------------------------------------------------------

    public void consultarDarkWeb(int n, int[] examenDW) {
        HandlerHeap[] ests = new HandlerHeap[n];
        for (int i = 0; i < n; i++) {
            HandlerHeap estudiante = _estudiantesPorPromedio.desencolar();
            ests[i] = estudiante;
        } // O(n log(E))
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < examenDW.length; j++) {
                if (ests[i].getEstudiante().getExamen().getRespuesta(j) != -1) {
                    _conteoRespuestas[j][ests[i].getEstudiante().getExamen().getRespuesta(j)]--;
                }
                resolver(ests[i].getEstudiante().getId(), j, examenDW[j]);
            }
            _estudiantesPorId[ests[i].getEstudiante().getId()] = _estudiantesPorPromedio
                    .insertar(ests[i].getEstudiante());
        }
    }// O(n(R + log(E)))

    // -------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        HandlerHeap estudianteHandler = _estudiantesPorId[estudiante];
        estudianteHandler.getEstudiante().entregar();
        _estudiantesPorPromedio.reOrdenar(estudianteHandler.getHeapIndex());
    }// O(log(E))

    // -----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        ArrayList<NotaFinal> nfinalLista = new ArrayList<NotaFinal>();
        ColaPrioridadHeap cola = new ColaPrioridadHeap(_estudiantesPorPromedio.getLongitud() - estudiantes_copiones);

        for (int i = 0; i < _estudiantesPorId.length; i++) {// O(E)
            HandlerHeap handler = _estudiantesPorId[i];
            if (!handler.getEstudiante().getSeCopio()) {
                if (handler != null) {
                    cola.insertarInverso(handler.getEstudiante());// O(log(E))
                }
            }

        }
        for (int i = 0; i < cola.getLongitud(); i++) {// O(E)
            HandlerHeap handler = cola.desencolarInverso();// O(log(E))
            if (handler != null) {

                NotaFinal nfinal = new NotaFinal(handler.getEstudiante().getExamen().getPromedio(),
                        handler.getEstudiante().getId());
                nfinalLista.add(nfinal);
            }
        }
        NotaFinal[] ad = new NotaFinal[nfinalLista.size()];
        for (int i = 0; i < nfinalLista.size(); i++) { // O(E)
            ad[i] = nfinalLista.get(i);
        }
        return ad;
    }// O(E*log(E))

    // -------------------------------------------------------CHEQUEAR
    // COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        ArrayList<Integer> copias = new ArrayList<>();
        for (int i = 0; i < _estudiantesPorId.length; i++) {// O(E)
            Estudiante estudiante = _estudiantesPorId[i].getEstudiante();
            int respuestasCopiadas = 0;
            for (int j = 0; j < estudiante.getExamen().cantidadPreguntas(); j++) {// O(R)
                if (estudiante.getExamen().getRespuesta(j) != -1) {
                    double cantidadRespuestas = _conteoRespuestas[j][estudiante.getExamen().getRespuesta(j)];
                    if ((cantidadRespuestas - 1)
                            / (_estudiantesPorId.length - 1) >= 0.25) {
                        respuestasCopiadas++;
                    }
                }
            }
            if (respuestasCopiadas > 0 && respuestasCopiadas == estudiante.getExamen().cantidadRespuestas()) {
                estudiante.setSeCopio(true);
                copias.add(i);
                estudiantes_copiones++;
            }
        }
        int[] result = new int[copias.size()];
        for (int i = 0; i < copias.size(); i++) {
            result[i] = copias.get(i);
        }
        return result;
    }// O(E^2 * R) NO RESPETA EL ENUNCIADO
}
