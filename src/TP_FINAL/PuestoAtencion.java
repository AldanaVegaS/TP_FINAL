package TP_FINAL;

import java.util.concurrent.Semaphore;

public class PuestoAtencion {
    private final String aerolinea;
    private final int max;
    private final Semaphore semaforoPuesto; // Controla la cantidad maxima de pasajeros en el puesto de atención
    private Terminal[] terminales;
    private final Semaphore semaforoAtencion = new Semaphore(1);//Controla el ingreso al puesto de atención


    public PuestoAtencion(String aero, int m){
        aerolinea=aero;
        max=m;
        this.semaforoPuesto = new Semaphore(m);
    }

    public void setTerminales(Terminal[] terminales) {
        this.terminales = terminales;   
    }

    public String getAerolinea(){
        return aerolinea;
    }

    public void hacerCheckIn(Pasajero pasajero) throws InterruptedException{
        //Ingresar al puesto de atencion
        semaforoPuesto.acquire();
        System.out.println("\t\t"+Colores.YELLOW+Colores.NEGRITA+"PUESTO DE ATENCION "+aerolinea.toUpperCase()+Colores.RESET+"---> "+pasajero.getNombre()+" entró al puesto de atención");
        
        //Es atendido de a un pasajero a la vez
        semaforoAtencion.acquire();
        System.out.println("\t\t"+Colores.YELLOW+Colores.NEGRITA+"PUESTO DE ATENCION "+aerolinea.toUpperCase()+Colores.RESET+"---> "+pasajero.getNombre()+" está siendo atendido");
        asignarTerminal(pasajero); 
        Thread.sleep(300);
        System.out.println("\t\t"+Colores.YELLOW+Colores.NEGRITA+"PUESTO DE ATENCION "+aerolinea.toUpperCase()+Colores.RESET+"---> "+pasajero.getNombre() + " se le asigna una terminal y puesto de embarque: " + pasajero.getTerminal().getNombre()+"-"+pasajero.getPuestoEmbarque());
        
        //Libera el puesto de atencion
        semaforoAtencion.release();
        semaforoPuesto.release();
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

    public synchronized boolean estaDisponible(){
        return (semaforoPuesto.availablePermits() > 0);
    }
    
    @Override
    public String toString(){
        return "Aerolinea: "+aerolinea+"  Cantidad maxima:"+max;
    }
}
