package aed;

import javafx.util.Pair;

public class Estudiante {
    private int _id;
    private Examen _examen;
    private boolean _entregado;
    private Pair<Integer, Integer> _posicion;

    public Estudiante(int id, Examen examen, Pair<Integer, Integer> posicion) {
        _id = id;
        _entregado = false;
        _examen = examen;
        _posicion = posicion;
    }

    public int getId() {
        return _id;
    }

    public Examen getExamen() {
        return _examen;
    }

    public boolean isEntregado() {
        return _entregado;
    }

    public void completarEjercicio(int nroEjercicio, int respuesta) {
        _examen.setRespuesta(nroEjercicio, respuesta);
    }

    public void entregar() {
        _entregado = true;
    }
}
