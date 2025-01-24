package TP_FINAL;


class AsignadorDeVuelos {
    private final Terminal[] terminales;

    public AsignadorDeVuelos(Terminal[] terminales,Vuelo[] vuelos) {
        this.terminales = terminales;
        asignarPuestosEmbarque(vuelos);
    }

    public Terminal[] getTerminales() {
        return terminales;
    }

    private void asignarPuestosEmbarque(Vuelo[] vuelos) {
        int v = 0;//contador de vuelos
        for (Terminal terminal : terminales) {
            PuestoEmbarque[] puestos = terminal.getPuestos();
            for (PuestoEmbarque puesto : puestos) {
                // Asignar vuelos aleatorios a los puestos
                int cantVuelos = (int) (Math.random() * 2) + 1;//cantidad de vuelos que se pueden asignar a un puesto(1 o 2 vuelos)
                for (int k = 0; k<cantVuelos; k++) {
                    if (v < vuelos.length) {
                        puesto.asignarVuelo(vuelos[v].getIdVuelo());
                        v++;
                    }
                }
            }
        }
    }
}