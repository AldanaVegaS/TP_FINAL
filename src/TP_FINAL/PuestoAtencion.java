package TP_FINAL;

import TP_FINAL.AsignadorDeVuelos.Tupla;
import java.util.concurrent.Semaphore;

public class PuestoAtencion {
    private final String aerolinea;
    private final int max;
    private final Semaphore semaforoPuesto=new Semaphore(1, true); // Controla la cantidad maxima de pasajeros en el puesto de atención
    private int cantPasajeros=0;
    private AsignadorDeVuelos asignador;

    public PuestoAtencion(String aero, int m){
        aerolinea=aero;
        max=m;
    }

    public void setAsignadorDeVuelos(AsignadorDeVuelos asignadorDeVuelos){
        asignador=asignadorDeVuelos;
    }

    public String getAerolinea(){
        return aerolinea;
    }

    
    public void ingresar(Pasajero pasajero){
        cantPasajeros++;
        System.out.println("\t\t"+Colores.YELLOW+Colores.NEGRITA+"PUESTO DE ATENCION "+Colores.RESET+"---> "+pasajero.getNombre()+" ingresa en el puesto de atencion "+getAerolinea());
      
    }

    public Tupla hacerCheckIn(Pasajero pasajero) throws InterruptedException{
        semaforoPuesto.acquire();
        System.out.println("\t\t"+Colores.YELLOW+Colores.NEGRITA+"PUESTO DE ATENCION "+aerolinea.toUpperCase()+Colores.RESET+"---> "+pasajero.getNombre()+" está siendo atendido");
        Tupla t = asignador.getTerminalYPuesto(pasajero.getPasaje().getIdVuelo());
        Thread.sleep(20);
        System.out.println("\t\t"+Colores.YELLOW+Colores.NEGRITA+"PUESTO DE ATENCION "+aerolinea.toUpperCase()+Colores.RESET+"---> "+pasajero.getNombre() + " se le asigna una terminal y puesto de embarque: " + t.getTerminal().getNombre()+"-"+t.getPuesto());
        semaforoPuesto.release(); 

        return t;
    }   

    public void salir(){
        cantPasajeros--;
    }

    public boolean estaLleno(){
        return cantPasajeros>=max;
    }

  
    
    @Override
    public String toString(){
        return "Aerolinea: "+aerolinea+"  Cantidad maxima:"+max;
    }
}
