package aed;

import java.util.ArrayList;

public class Edr {

    private ColaPrioridadHeap<Estudiante>.HandlerHeap[] _estudiantesPorId;
    private ColaPrioridadHeap<Estudiante> _estudiantesPorPromedio;
    private int _ladoAula;
    private int[][] _conteoRespuestas;
    private int estudiantes_copiones;

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        _ladoAula = LadoAula;
        _estudiantesPorId = new ColaPrioridadHeap.HandlerHeap[Cant_estudiantes];
        _estudiantesPorPromedio = new ColaPrioridadHeap<Estudiante>(Cant_estudiantes);
        _conteoRespuestas = new int[ExamenCanonico.length][10];
        for (int i = 0; i < Cant_estudiantes; i++) { // O(E)
            Estudiante estudiante = new Estudiante(i, new Examen(ExamenCanonico.length, ExamenCanonico)); // O(R)
            _estudiantesPorId[i] = _estudiantesPorPromedio.insertar(estudiante);
        } // Ordenar no suma complejidad por que estamos sumando un conjunto completo ya
          // ordenado (Heapify)
    }// Costo Total: O(E) * O(R) = O(E*R)

    // -------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas() {
        double[] notas = new double[_estudiantesPorId.length];
        for (int i = 0; i < _estudiantesPorId.length; i++) { // O(E)
            ColaPrioridadHeap<Estudiante>.HandlerHeap handler = _estudiantesPorId[i];
            if (handler != null) {
                notas[i] = handler.getValor().getExamen().getPromedio(); // O(1)
            }
        }
        return notas;
    }// O(E)

    // ------------------------------------------------COPIARSE------------------------------------------------------------------------

    public void copiarse(int estudiante) {
        Estudiante est = _estudiantesPorId[estudiante].getValor();
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
                ColaPrioridadHeap<Estudiante>.HandlerHeap handlerVecino = _estudiantesPorId[vecino];
                if (handlerVecino != null) {
                    Estudiante estVecino = handlerVecino.getValor();
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

            for (int i = 0; i < est.getExamen().cantidadPreguntas(); i++) { // O(R)
                if (mejorVecino.getExamen().getRespuesta(i) != -1 && est.getExamen().getRespuesta(i) == -1) {
                    resolver(est.getId(), i, mejorVecino.getExamen().getRespuesta(i));
                    break;
                }
            }
        } // O(R)

    }// Costo Total: O(R) + O(R) + O(log(E)) = O(R + log(E))

    // -----------------------------------------------RESOLVER----------------------------------------------------------------

    public void resolver(int estudiante, int NroEjercicio, int res) {
        ColaPrioridadHeap<Estudiante>.HandlerHeap handler = _estudiantesPorId[estudiante];
        if (res != -1) {
            _conteoRespuestas[NroEjercicio][res]++;
        }

        if (handler.getValor().getExamen().getRespuesta(NroEjercicio) != -1) {
            _conteoRespuestas[NroEjercicio][handler.getValor().getExamen().getRespuesta(NroEjercicio)]--;
        }

        handler.getValor().completarEjercicio(NroEjercicio, res);
        _estudiantesPorPromedio.actualizar(handler);// O(log(E))

    }// Costo Total: O(log(E)) = O(log(E))

    // ------------------------------------------------CONSULTAR DARK
    // WEB-------------------------------------------------------

    public void consultarDarkWeb(int n, int[] examenDW) {
        ColaPrioridadHeap<Estudiante>.HandlerHeap[] ests = new ColaPrioridadHeap.HandlerHeap[n];
        for (int i = 0; i < n; i++) {
            ColaPrioridadHeap<Estudiante>.HandlerHeap estudiante = _estudiantesPorPromedio.desencolar();
            ests[i] = estudiante;
        } // O(n log(E))
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < examenDW.length; j++) { // O(n*R)
                resolver(ests[i].getValor().getId(), j, examenDW[j]); // Como no esta dentro del heap, no debe
                                                                      // reordenar, por lo que es O(1)
            }
            _estudiantesPorId[ests[i].getValor().getId()] = _estudiantesPorPromedio
                    .insertar(ests[i].getValor()); // O(log(E))
        }
    }// Costo Total: n*log(E) + n*O(R) + n*log(E) + = O(n(R + log(E)))

    // -------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        ColaPrioridadHeap<Estudiante>.HandlerHeap estudianteHandler = _estudiantesPorId[estudiante];
        estudianteHandler.getValor().entregar();
        _estudiantesPorPromedio.actualizar(estudianteHandler);
    }// Costo Total: O(log(E))

    // -----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        NotaFinal[] nfinalLista = new NotaFinal[_estudiantesPorId.length - estudiantes_copiones];
        ColaPrioridadHeap<Estudiante> cola = new ColaPrioridadHeap(
                _estudiantesPorPromedio.getLongitud() - estudiantes_copiones);

        for (int i = 0; i < _estudiantesPorId.length; i++) {// O(E)
            ColaPrioridadHeap<Estudiante>.HandlerHeap handler = _estudiantesPorId[i];
            if (!handler.getValor().getSeCopio()) {
                if (handler != null) {
                    cola.insertarInverso(handler.getValor());// O(log(E))
                }
            }

        }
        for (int i = 0; i < cola.getLongitud(); i++) {// O(E)
            ColaPrioridadHeap<Estudiante>.HandlerHeap handler = cola.desencolarInverso();// O(log(E))
            if (handler != null) {

                NotaFinal nfinal = new NotaFinal(handler.getValor().getExamen().getPromedio(),
                        handler.getValor().getId());
                nfinalLista[i] = nfinal;
            }
        }
        return nfinalLista;
    }// Costo Total: O(E) * O(log E) + O(E) * O(log E) + O(E) = O(E*log(E))

    // -------------------------------------------------------CHEQUEAR
    // COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        ArrayList<Integer> copias = new ArrayList<>();
        for (int i = 0; i < _estudiantesPorId.length; i++) {// O(E)
            Estudiante estudiante = _estudiantesPorId[i].getValor();
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
            if (respuestasCopiadas > 0 && respuestasCopiadas == estudiante.getExamen().cantidadRespuestas()) { // O (1)
                estudiante.setSeCopio(true);
                copias.add(i);
                estudiantes_copiones++;
            }
        }
        int[] result = new int[copias.size()];
        for (int i = 0; i < copias.size(); i++) { // O(E)
            result[i] = copias.get(i);
        }
        return result;
    }// Costo Total: O(E) * O(R) + O(E) = O(E * R)
}
