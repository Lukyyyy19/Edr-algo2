package aed;

//FALTA HACERLO MIN HEAP Y PONER LA COMPARACION DE ENTREGADO
public class ColaPrioridadHeap {
    private HandlerHeap[] _estudiantes;
    private int _ultimo;

    public int getLongitud() {
        return _estudiantes.length;
    }

    public HandlerHeap getProm(int index) {
        return _estudiantes[index];
    }

    public ColaPrioridadHeap(int capacidad) {
        _estudiantes = new HandlerHeap[capacidad];
        _ultimo = 0;
    }

    public HandlerHeap insertar(Estudiante estudiante) {
        // int hijoIzquierdo = _ultimo * 2 + 1;
        // int hijoDerecho = _ultimo * 2 + 2;
        HandlerHeap nuevo = new HandlerHeap(estudiante, _ultimo);
        if (_estudiantes[_ultimo] == null) {
            _estudiantes[_ultimo] = nuevo;
            _ultimo++;
            return nuevo;
        }
        _estudiantes[_ultimo] = nuevo;
        SiftUp(_ultimo);
        return nuevo;
    }

    private void SiftUp(int index) {

        int padre = (index - 1) / 2;
        if (index > 0 && compare(_estudiantes[index].getEstudiante(), _estudiantes[padre].getEstudiante()) < 0) {
            HandlerHeap temp = _estudiantes[index];
            _estudiantes[index] = _estudiantes[padre];
            _estudiantes[padre] = temp;
            _estudiantes[index]._heapIndex = index;
            _estudiantes[padre]._heapIndex = padre;
            SiftUp(padre);
        }
    }

    private void SiftDown(int index) {
        int hijoIzquierdo = index * 2 + 1;
        int hijoDerecho = index * 2 + 2;
        int mejorHijo = hijoIzquierdo;
        if (hijoIzquierdo >= _ultimo)
            return;

        if (hijoDerecho < _ultimo
                && compare(_estudiantes[hijoIzquierdo].getEstudiante(),
                        _estudiantes[hijoDerecho].getEstudiante()) > 0) {
            mejorHijo = hijoDerecho;
        }

        if (compare(_estudiantes[index].getEstudiante(), _estudiantes[mejorHijo].getEstudiante()) > 0) {
            HandlerHeap temp = _estudiantes[index];
            _estudiantes[index] = _estudiantes[mejorHijo];
            _estudiantes[mejorHijo] = temp;
            _estudiantes[index]._heapIndex = index;
            _estudiantes[mejorHijo]._heapIndex = mejorHijo;
            SiftDown(mejorHijo);
        }
    }

    private boolean EsMenor(Estudiante menor, Estudiante mayor) {
        if (menor.getEntregado() != mayor.getEntregado()) {
            if (menor.getEntregado()) {
                return false;
            } else {
                return true;
            }
        } else {

            if (menor.getExamen().getPromedio() < mayor.getExamen().getPromedio()) {
                return true;
            } else if (menor.getExamen().getPromedio() == mayor.getExamen().getPromedio()) {
                return menor.getId() > mayor.getId();
            }
        }

        return false;
    }

    private boolean EsMayor(Estudiante mayor, Estudiante menor) {
        if (mayor.getEntregado() != menor.getEntregado()) {
            if (mayor.getEntregado()) {
                return false;
            } else {
                return true;
            }
        }
        if (mayor.getExamen().getPromedio() > menor.getExamen().getPromedio()) {
            return true;
        } else if (mayor.getExamen().getPromedio() == menor.getExamen().getPromedio()) {
            return mayor.getId() < menor.getId();
        }
        return false;
    }

    private int compare(Estudiante a, Estudiante b) {
        if (a.getEntregado() != b.getEntregado()) {
            if (a.getEntregado()) {
                return 1;
            } else {
                return -1;
            }
        }
        int comparador = Double.compare(a.getExamen().getPromedio(), b.getExamen().getPromedio());
        if (comparador != 0) {
            return comparador;
        } else {
            return Integer.compare(b.getId(), a.getId());
        }
    }

    public void reOrdenar(int heapIndex) {
        if (heapIndex >= _ultimo)
            return;
        HandlerHeap handler = _estudiantes[heapIndex];

        if (compare(handler.getEstudiante(), _estudiantes[(heapIndex - 1) / 2].getEstudiante()) < 0) {
            SiftUp(heapIndex);
        } else {
            SiftDown(heapIndex);
        }

    }

    public HandlerHeap desencolar() {
        HandlerHeap handler = _estudiantes[0];
        HandlerHeap temp = _estudiantes[_estudiantes.length - 1];
        _estudiantes[0] = temp;
        _estudiantes[_estudiantes.length - 1] = null;
        reOrdenar(0);
        return handler;
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
