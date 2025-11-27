package aed;

import java.lang.reflect.Array;

//FALTA HACERLO MIN HEAP Y PONER LA COMPARACION DE ENTREGADO
public class ColaPrioridadHeap<T extends Comparable<T>> {
    private HandlerHeap[] _nodos;
    private int _ultimo;

    public int getLongitud() {
        return _nodos.length;
    }

    public HandlerHeap getProm(int index) {
        return _nodos[index];
    }

    public ColaPrioridadHeap(int capacidad) {
        _nodos = crearArray(capacidad);
        _ultimo = 0;
    }

    HandlerHeap[] crearArray(int size) {
        return (HandlerHeap[]) Array.newInstance(HandlerHeap.class, size);
    }

    public HandlerHeap insertar(T valor) {
        HandlerHeap nuevo = new HandlerHeap(valor, _ultimo);
        _nodos[_ultimo] = nuevo;
        SiftUp(_ultimo, false);
        _ultimo++;
        return nuevo;
    }

    public HandlerHeap insertarInverso(T estudiante) {
        HandlerHeap nuevo = new HandlerHeap(estudiante, _ultimo);
        if (_nodos[_ultimo] == null) {
            _nodos[_ultimo] = nuevo;
        }
        _nodos[_ultimo] = nuevo;
        SiftUp(_ultimo, true);
        _ultimo++;
        return nuevo;
    }

    private void SiftUp(int index, boolean inverso) {

        int padre = (index - 1) / 2;
        if (inverso) {

            if (index > 0
                    && comparar(_nodos[index].getValor(), _nodos[padre].getValor(), true) > 0) {
                HandlerHeap temp = _nodos[index];
                _nodos[index] = _nodos[padre];
                _nodos[padre] = temp;
                _nodos[index]._heapIndex = index;
                _nodos[padre]._heapIndex = padre;
                SiftUp(padre, inverso);
            }
            return;
        }
        if (index > 0
                && comparar(_nodos[index].getValor(), _nodos[padre].getValor(), false) < 0) {
            HandlerHeap temp = _nodos[index];
            _nodos[index] = _nodos[padre];
            _nodos[padre] = temp;
            _nodos[index]._heapIndex = index;
            _nodos[padre]._heapIndex = padre;
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
                    && comparar(_nodos[hijoIzquierdo].getValor(),
                            _nodos[hijoDerecho].getValor(), true) < 0) {
                mejorHijo = hijoDerecho;
            }

            if (comparar(_nodos[index].getValor(), _nodos[mejorHijo].getValor(), true) < 0) {
                HandlerHeap temp = _nodos[index];
                _nodos[index] = _nodos[mejorHijo];
                _nodos[mejorHijo] = temp;
                _nodos[index]._heapIndex = index;
                _nodos[mejorHijo]._heapIndex = mejorHijo;
                SiftDown(mejorHijo, inverso);
            }
            return;
        }

        if (hijoDerecho < _ultimo
                && comparar(_nodos[hijoIzquierdo].getValor(),
                        _nodos[hijoDerecho].getValor(), false) > 0) {
            mejorHijo = hijoDerecho;
        }

        if (comparar(_nodos[index].getValor(), _nodos[mejorHijo].getValor(), false) > 0) {
            HandlerHeap temp = _nodos[index];
            _nodos[index] = _nodos[mejorHijo];
            _nodos[mejorHijo] = temp;
            _nodos[index]._heapIndex = index;
            _nodos[mejorHijo]._heapIndex = mejorHijo;
            SiftDown(mejorHijo, inverso);
        }
    }

    private int comparar(T a, T b, boolean inverso) {
        return a.compareTo(b);
    }

    private void reOrdenar(int heapIndex) {
        if (heapIndex >= _ultimo)
            return;
        HandlerHeap handler = _nodos[heapIndex];

        if (comparar(handler.getValor(), _nodos[(heapIndex - 1) / 2].getValor(), false) < 0) {
            SiftUp(heapIndex, false);
        } else {
            SiftDown(heapIndex, false);
        }

    }

    private void reOrdenarInvertido(int heapIndex) {
        if (heapIndex >= _ultimo)
            return;
        HandlerHeap handler = _nodos[heapIndex];

        if (comparar((T) handler.getValor(), _nodos[(heapIndex - 1) / 2].getValor(), true) < 0) {
            SiftUp(heapIndex, true);
        } else {
            SiftDown(heapIndex, true);
        }

    }

    public HandlerHeap desencolar() {
        HandlerHeap handler = _nodos[0];
        if (_ultimo != 0) {
            HandlerHeap temp = _nodos[_ultimo - 1];
            _nodos[0] = temp;
            _nodos[_ultimo - 1] = null;
        } else {
            HandlerHeap temp = _nodos[_ultimo];
            _nodos[0] = temp;
            _nodos[_ultimo] = null;
        }
        if (_ultimo > 0) {
            _ultimo--;
        }
        handler._heapIndex = -1;
        reOrdenar(0);
        return handler;
    }

    public HandlerHeap desencolarInverso() {
        HandlerHeap handler = _nodos[0];
        if (_ultimo != 0) {
            HandlerHeap temp = _nodos[_ultimo - 1];
            _nodos[0] = temp;
            _nodos[_ultimo - 1] = null;
        } else {
            HandlerHeap temp = _nodos[_ultimo];
            _nodos[0] = temp;
            _nodos[_ultimo] = null;
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
        private T _valor;
        private int _heapIndex;

        public HandlerHeap(T estudiante, int heapIndex) {
            _valor = estudiante;
            _heapIndex = heapIndex;
        }

        public T getValor() {
            return _valor;
        }
    }
}
