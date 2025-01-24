package TP_FINAL;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class PuestoAtencion {
    private final String aerolinea;
    private final int max;
    private final Semaphore semaforoPuesto; // Controla la cantidad maxima de pasajeros en el puesto de atención
    private final BlockingQueue<Pasajero> hallCentral;
    private Terminal[] terminales;

    public PuestoAtencion(String aero, int m){
        aerolinea=aero;
        max=m;
        this.semaforoPuesto = new Semaphore(m);
        this.hallCentral = new LinkedBlockingQueue<>(30);
    }

    public void setTerminales(Terminal[] terminales) {
        this.terminales = terminales;   
    }

    public String getAerolinea(){
        return aerolinea;
    }

    public void ingresarPasajero(Pasajero pasajero) throws InterruptedException {
        if (!semaforoPuesto.tryAcquire()) {
            System.out.println("\t\t"+Colores.YELLOW+Colores.NEGRITA+"PUESTO DE ATENCION "+Colores.RESET+"---> "+pasajero.getNombre() + " espera en el hall central de " + aerolinea);
            hallCentral.put(pasajero);
        } else {
            atender(pasajero);
        }
    }    

    private void atender(Pasajero pasajero) throws InterruptedException{
        asignarTerminal(pasajero);
        System.out.println("\t\t"+Colores.YELLOW+Colores.NEGRITA+"PUESTO DE ATENCION "+Colores.RESET+"---> "+pasajero.getNombre() + " se le asigna una terminal y puesto de embarque: " + pasajero.getTerminal().getNombre());
        Thread.sleep(300);
        semaforoPuesto.release();
        pasajero.marcarAtendido();
        ingresarDesdeHall();//Cumple la funcion del guardia permitiendo el ingreso desde el hall
    }

    private void ingresarDesdeHall() throws InterruptedException {
        Pasajero siguiente = hallCentral.poll();
        if (siguiente != null) {
            semaforoPuesto.acquire();
            atender(siguiente);
        }
    }
    
    private void asignarTerminal(Pasajero pasajero) {
        for (Terminal terminal : terminales) {
            PuestoEmbarque[] puestos = terminal.getPuestos();
            for (PuestoEmbarque puesto : puestos) {
                for (int i = 0; i < puesto.getVuelos().size(); i++) {
                    if (puesto.getVuelos().get(i).equals(pasajero.getPasaje().getIdVuelo())) {
                        pasajero.setTerminalYPuesto(terminal, puesto.getNombre());
                        return;
                    }
                }
            }
        }
    }
    
    @Override
    public String toString(){
        return "Aerolinea: "+aerolinea+"  Cantidad maxima:"+max;
    }
}
