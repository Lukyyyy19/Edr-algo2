package aed;

public class Examen {
    private int[] _respuestas;
    private int _cantidadRespuestas;
    private double _cantidadRespuestasCorrectas;
    private int[] _respuestasCorrectas;

    public Examen(int cantidadPreguntas, int[] respuestasCorrectas) {
        _respuestas = new int[cantidadPreguntas];
        for (int i = 0; i < cantidadPreguntas; i++) {
            _respuestas[i] = -1;
        }
        _respuestasCorrectas = respuestasCorrectas;
    }

    public Examen(int[] respuestas) {
        _respuestas = respuestas;
    }

    public int[] getRespuestas() {
        return _respuestas;
    }

    public int getRespuesta(int pregunta) {
        return _respuestas[pregunta];
    }

    public void setRespuesta(int pregunta, int respuesta) {
        if (_respuestas[pregunta] != -1) {
            if (_respuestas[pregunta] == _respuestasCorrectas[pregunta]) {
                _cantidadRespuestasCorrectas--;
            }
            _cantidadRespuestas--;
        }
        _respuestas[pregunta] = respuesta;
        if (respuesta == _respuestasCorrectas[pregunta]) {
            _cantidadRespuestasCorrectas++;
        }
        _cantidadRespuestas++;
    }

    public double getPromedio() {
        if (_cantidadRespuestas == 0) {
            return 0;
        }
        return (_cantidadRespuestasCorrectas / (double) _respuestas.length) * 100;
    }

    public int cantidadPreguntas() {
        return _respuestas.length;
    }

    public int cantidadRespuestas() {
        return _cantidadRespuestas;
    }
}
