package aed;

public class Estudiante implements Comparable<Estudiante> {
    private int _id;
    private Examen _examen;
    private boolean _entregado;
    private boolean _seCopio;

    public Estudiante(int id, Examen examen) {
        _id = id;
        _entregado = false;
        _examen = examen;
        _entregado = false;
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

    public boolean getSeCopio() {
        return _seCopio;
    }

    public void setSeCopio(boolean b) {
        _seCopio = b;
    }

    public boolean getEntregado() {
        return _entregado;
    }

    @Override
    public int compareTo(Estudiante otra) {
        if (otra.getExamen().getPromedio() == this.getExamen().getPromedio()) {
            return this._id - otra.getId();
        }
        return Double.compare(this.getExamen().getPromedio(), otra.getExamen().getPromedio());
    }
}
