package aed;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Handler;

//FALTA HACERLO MIN HEAP Y PONER LA COMPARACION DE ENTREGADO
public class ColaPrioridadHeap<T extends Comparable<T>> {
    private HandlerHeap[] _estudiantes;
    private int _ultimo;

    public int getLongitud() {
        return _estudiantes.length;
    }

    public HandlerHeap getProm(int index) {
        return _estudiantes[index];
    }

    public ColaPrioridadHeap(int capacidad) {
        _estudiantes = crearArray(capacidad);
        _ultimo = 0;
    }

    HandlerHeap[] crearArray(int size) {
        return (HandlerHeap[]) Array.newInstance(HandlerHeap.class, size);
    }

    public HandlerHeap insertar(T estudiante) {
        HandlerHeap nuevo = new HandlerHeap(estudiante, _ultimo);
        _estudiantes[_ultimo] = nuevo;
        SiftUp(_ultimo, false);
        _ultimo++;
        return nuevo;
    }

    public HandlerHeap insertarInverso(T estudiante) {
        // int hijoIzquierdo = _ultimo * 2 + 1;
        // int hijoDerecho = _ultimo * 2 + 2;
        HandlerHeap nuevo = new HandlerHeap(estudiante, _ultimo);
        if (_estudiantes[_ultimo] == null) {
            _estudiantes[_ultimo] = nuevo;
            // _ultimo++;
            // return nuevo;
        }
        _estudiantes[_ultimo] = nuevo;
        SiftUp(_ultimo, true);
        _ultimo++;
        return nuevo;
    }

    private void SiftUp(int index, boolean inverso) {

        int padre = (index - 1) / 2;
        if (inverso) {

            if (index > 0
                    && comparar(_estudiantes[index].getEstudiante(), _estudiantes[padre].getEstudiante(), true) > 0) {
                HandlerHeap temp = _estudiantes[index];
                _estudiantes[index] = _estudiantes[padre];
                _estudiantes[padre] = temp;
                _estudiantes[index]._heapIndex = index;
                _estudiantes[padre]._heapIndex = padre;
                SiftUp(padre, inverso);
            }
            return;
        }
        if (index > 0
                && comparar(_estudiantes[index].getEstudiante(), _estudiantes[padre].getEstudiante(), false) < 0) {
            HandlerHeap temp = _estudiantes[index];
            _estudiantes[index] = _estudiantes[padre];
            _estudiantes[padre] = temp;
            _estudiantes[index]._heapIndex = index;
            _estudiantes[padre]._heapIndex = padre;
            SiftUp(padre, inverso);
        }
    }

    private void SiftDown(int index, boolean inverso) {
        int hijoIzquierdo = index * 2 + 1;
        int hijoDerecho = index * 2 + 2;
        int mejorHijo = hijoIzquierdo;
        if (hijoIzquierdo >= _ultimo)
            return;

        if (inverso) {

            if (hijoDerecho < _ultimo
                    && comparar(_estudiantes[hijoIzquierdo].getEstudiante(),
                            _estudiantes[hijoDerecho].getEstudiante(), true) < 0) {
                mejorHijo = hijoDerecho;
            }

            if (comparar(_estudiantes[index].getEstudiante(), _estudiantes[mejorHijo].getEstudiante(), true) < 0) {
                HandlerHeap temp = _estudiantes[index];
                _estudiantes[index] = _estudiantes[mejorHijo];
                _estudiantes[mejorHijo] = temp;
                _estudiantes[index]._heapIndex = index;
                _estudiantes[mejorHijo]._heapIndex = mejorHijo;
                SiftDown(mejorHijo, inverso);
            }
            return;
        }

        if (hijoDerecho < _ultimo
                && comparar(_estudiantes[hijoIzquierdo].getEstudiante(),
                        _estudiantes[hijoDerecho].getEstudiante(), false) > 0) {
            mejorHijo = hijoDerecho;
        }

        if (comparar(_estudiantes[index].getEstudiante(), _estudiantes[mejorHijo].getEstudiante(), false) > 0) {
            HandlerHeap temp = _estudiantes[index];
            _estudiantes[index] = _estudiantes[mejorHijo];
            _estudiantes[mejorHijo] = temp;
            _estudiantes[index]._heapIndex = index;
            _estudiantes[mejorHijo]._heapIndex = mejorHijo;
            SiftDown(mejorHijo, inverso);
        }
    }

    private int comparar(T a, T b, boolean inverso) {
        return a.compareTo(b);
    }

    private void reOrdenar(int heapIndex) {
        if (heapIndex >= _ultimo)
            return;
        HandlerHeap handler = _estudiantes[heapIndex];

        if (comparar(handler.getEstudiante(), _estudiantes[(heapIndex - 1) / 2].getEstudiante(), false) < 0) {
            SiftUp(heapIndex, false);
        } else {
            SiftDown(heapIndex, false);
        }

    }

    private void reOrdenarInvertido(int heapIndex) {
        if (heapIndex >= _ultimo)
            return;
        HandlerHeap handler = _estudiantes[heapIndex];

        if (comparar((T) handler.getEstudiante(), _estudiantes[(heapIndex - 1) / 2].getEstudiante(), true) < 0) {
            SiftUp(heapIndex, true);
        } else {
            SiftDown(heapIndex, true);
        }

    }

    public HandlerHeap desencolar() {
        HandlerHeap handler = _estudiantes[0];
        if (_ultimo != 0) {
            HandlerHeap temp = _estudiantes[_ultimo - 1];
            _estudiantes[0] = temp;
            _estudiantes[_ultimo - 1] = null;
        } else {
            HandlerHeap temp = _estudiantes[_ultimo];
            _estudiantes[0] = temp;
            _estudiantes[_ultimo] = null;
        }
        if (_ultimo > 0) {
            _ultimo--;
        }
        handler._heapIndex = -1;
        reOrdenar(0);
        return handler;
    }

    public HandlerHeap desencolarInverso() {
        HandlerHeap handler = _estudiantes[0];
        if (_ultimo != 0) {
            HandlerHeap temp = _estudiantes[_ultimo - 1];
            _estudiantes[0] = temp;
            _estudiantes[_ultimo - 1] = null;
        } else {
            HandlerHeap temp = _estudiantes[_ultimo];
            _estudiantes[0] = temp;
            _estudiantes[_ultimo] = null;
        }
        _ultimo--;
        reOrdenarInvertido(0);
        return handler;
    }

    public void actualizar(HandlerHeap handler) {
        if (handler._heapIndex >= 0)
            reOrdenar(handler._heapIndex);
    }

    public class HandlerHeap {
        private T _estudiante;
        private int _heapIndex;

        public HandlerHeap(T estudiante, int heapIndex) {
            _estudiante = estudiante;
            _heapIndex = heapIndex;
        }

        public T getEstudiante() {
            return _estudiante;
        }
    }
}
