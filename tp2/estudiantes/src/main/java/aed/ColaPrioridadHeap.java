package aed;

public class ColaPrioridadHeap {
    private HandlerHeap[] _estudiantes;
    private int _ultimo;

    public ColaPrioridadHeap(int capacidad) {
        _estudiantes = new HandlerHeap[capacidad];
        _ultimo = 0;
    }

    public HandlerHeap insertar(Estudiante estudiante) {
        int hijoIzquierdo = _ultimo * 2 + 1;
        int hijoDerecho = _ultimo * 2 + 2;
        HandlerHeap nuevo = new HandlerHeap(estudiante, _ultimo);
        if (_estudiantes[_ultimo] == null) {
            _estudiantes[_ultimo] = nuevo;
            _ultimo++;
            return nuevo;
        } else if (_estudiantes[hijoIzquierdo] == null) {
            _estudiantes[hijoIzquierdo] = nuevo;
            if (estudiante.getExamen().getPromedio() > _estudiantes[_ultimo].getEstudiante().getExamen()
                    .getPromedio()) {
                SiftUp(hijoIzquierdo);
            } else {
                SiftDown(hijoIzquierdo);
            }
            return nuevo;
        } else if (_estudiantes[hijoDerecho] == null) {
            _estudiantes[hijoDerecho] = nuevo;
            if (estudiante.getExamen().getPromedio() > _estudiantes[_ultimo].getEstudiante().getExamen()
                    .getPromedio()) {
                SiftUp(hijoDerecho);
            } else {
                SiftDown(hijoDerecho);
            }
            _ultimo++;
            return nuevo;

        }
        return null;
    }

    private void SiftUp(int index) {
        // while (index > 0) {
        // int parentIndex = (index - 1) / 2;
        // if (_estudiantes[index].getEstudiante().getExamen().getPromedio() >
        // _estudiantes[parentIndex]
        // .getEstudiante().getExamen().getPromedio()) {
        // HandlerHeap temp = _estudiantes[index];
        // _estudiantes[index] = _estudiantes[parentIndex];
        // _estudiantes[parentIndex] = temp;
        // _estudiantes[index]._heapIndex = index;
        // _estudiantes[parentIndex]._heapIndex = parentIndex;
        // index = parentIndex;
        // } else if (_estudiantes[index].getEstudiante().getExamen().getPromedio() ==
        // _estudiantes[parentIndex]
        // .getEstudiante().getExamen().getPromedio()) {
        // if (_estudiantes[index].getEstudiante().getId() >
        // _estudiantes[parentIndex].getEstudiante().getId()) {
        // HandlerHeap temp = _estudiantes[index];
        // _estudiantes[index] = _estudiantes[parentIndex];
        // _estudiantes[parentIndex] = temp;
        // _estudiantes[index]._heapIndex = index;
        // _estudiantes[parentIndex]._heapIndex = parentIndex;
        // index = parentIndex;
        // }
        // }
        // }
        int padre = (index - 1) / 2;
        if (index > 0 && EsMayor(_estudiantes[index].getEstudiante(), _estudiantes[padre].getEstudiante())) {
            HandlerHeap temp = _estudiantes[index];
            _estudiantes[index] = _estudiantes[padre];
            _estudiantes[padre] = temp;
            _estudiantes[index]._heapIndex = index;
            _estudiantes[padre]._heapIndex = padre;
            SiftUp(padre);
        }
    }

    private void SiftDown(int index) {
        // while (_estudiantes[index * 2 + 1] != null) {
        // int hijoIzquierdo = index * 2 + 1;
        // int hijoDerecho = index * 2 + 2;
        // int mayor = index;

        // if (hijoIzquierdo < _ultimo
        // && _estudiantes[hijoIzquierdo].getEstudiante().getExamen().getPromedio() >
        // _estudiantes[mayor]
        // .getEstudiante().getExamen().getPromedio()) {
        // mayor = hijoIzquierdo;
        // } else if
        // (_estudiantes[hijoIzquierdo].getEstudiante().getExamen().getPromedio() ==
        // _estudiantes[mayor]
        // .getEstudiante().getExamen().getPromedio()) {
        // if (_estudiantes[hijoIzquierdo].getEstudiante().getId() >
        // _estudiantes[mayor].getEstudiante().getId()) {
        // mayor = hijoIzquierdo;
        // }
        // }

        // if (hijoDerecho < _ultimo
        // && _estudiantes[hijoDerecho].getEstudiante().getExamen().getPromedio() >
        // _estudiantes[mayor]
        // .getEstudiante().getExamen().getPromedio()) {
        // mayor = hijoDerecho;
        // } else if
        // (_estudiantes[hijoDerecho].getEstudiante().getExamen().getPromedio() ==
        // _estudiantes[mayor]
        // .getEstudiante().getExamen().getPromedio()) {
        // if (_estudiantes[hijoDerecho].getEstudiante().getId() >
        // _estudiantes[mayor].getEstudiante().getId()) {
        // mayor = hijoDerecho;
        // }
        // }
        // if (mayor == index) {
        // break;
        // }
        // HandlerHeap temp = _estudiantes[index];
        // _estudiantes[index] = _estudiantes[mayor];
        // _estudiantes[mayor] = temp;
        // _estudiantes[index]._heapIndex = index;
        // _estudiantes[mayor]._heapIndex = mayor;
        // index = mayor;
        // }
        int hijoIzquierdo = index * 2 + 1;
        int hijoDerecho = index * 2 + 2;
        int mejorHijo = hijoIzquierdo;
        if (hijoIzquierdo >= _ultimo)
            return;

        if (hijoDerecho < _ultimo
                && EsMenor(_estudiantes[hijoIzquierdo].getEstudiante(), _estudiantes[hijoDerecho].getEstudiante())) {
            mejorHijo = hijoDerecho;
        }

        if (EsMenor(_estudiantes[index].getEstudiante(), _estudiantes[mejorHijo].getEstudiante())) {
            HandlerHeap temp = _estudiantes[index];
            _estudiantes[index] = _estudiantes[mejorHijo];
            _estudiantes[mejorHijo] = temp;
            _estudiantes[index]._heapIndex = index;
            _estudiantes[mejorHijo]._heapIndex = mejorHijo;
            SiftDown(mejorHijo);
        }
    }

    private boolean EsMenor(Estudiante menor, Estudiante mayor) {
        if (menor.getExamen().getPromedio() < mayor.getExamen().getPromedio()) {
            return true;
        } else if (menor.getExamen().getPromedio() == mayor.getExamen().getPromedio()) {
            return menor.getId() < mayor.getId();
        }
        return false;
    }

    private boolean EsMayor(Estudiante mayor, Estudiante menor) {
        if (mayor.getExamen().getPromedio() > menor.getExamen().getPromedio()) {
            return true;
        } else if (mayor.getExamen().getPromedio() == menor.getExamen().getPromedio()) {
            return mayor.getId() > menor.getId();
        }
        return false;
    }

    public void reOrdenar(int heapIndex) {
        HandlerHeap handler = _estudiantes[heapIndex];
        if (handler != null) {
            if (handler.getEstudiante().getExamen().getPromedio() > _estudiantes[(heapIndex - 1) / 2].getEstudiante()
                    .getExamen()
                    .getPromedio()) {
                SiftUp(heapIndex);
            } else {
                SiftDown(heapIndex);
            }
        }
    }

    public class HandlerHeap {
        private Estudiante _estudiante;
        private int _heapIndex;

        public HandlerHeap(Estudiante estudiante, int heapIndex) {
            _estudiante = estudiante;
            _heapIndex = heapIndex;
        }

        public Estudiante getEstudiante() {
            return _estudiante;
        }

        public int getHeapIndex() {
            return _heapIndex;
        }
    }
}
