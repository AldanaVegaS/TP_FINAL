package TP_FINAL;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Aeropuerto {
   private final Semaphore puestoInformacion = new Semaphore(1,true);
   private final PuestoAtencion puestosAtencion [];
   private final PeopleMover peopleMover;
   private final Terminal terminales[]; 
   private final Vuelo vuelos[];
   private final AsignadorDeVuelos asignadorDeVuelos;
   private final Lock guardia = new ReentrantLock();
   private final Condition ingreso = guardia.newCondition();
   private final HashMap<String, PuestoAtencion> mapeoAPuestos;//mapeo de aerolineas a puestos de atencion


   public Aeropuerto(PuestoAtencion puestosAten[],PeopleMover mover, Terminal term[], Vuelo v[], Hora hs, HashMap<String, PuestoAtencion> aerolineasAPuestos){
        puestosAtencion=puestosAten;
        peopleMover=mover;
        vuelos=v;
        asignadorDeVuelos = new AsignadorDeVuelos(term,vuelos);
        terminales=term;
        mapeoAPuestos = aerolineasAPuestos;
        for (PuestoAtencion puesto : puestosAtencion) {
            puesto.setAsignadorDeVuelos(asignadorDeVuelos);
        }
   }
   

   public PeopleMover getPeopleMover(){
      return peopleMover;
   }

   public PuestoAtencion[] getPuestosAtencion(){
      return puestosAtencion;
   }


   public PuestoAtencion atencionPuestoInfo(Pasajero pasajero) throws InterruptedException{
      //Esperar que sea la hora de atencion
      hora.horarioLaboral();
     
      //Ingresar al puesto de informacion
      this.puestoInformacion.acquire();
      System.out.println("\t"+Colores.CYAN+Colores.NEGRITA+"PUESTO DE INFORMES "+Colores.RESET+"---> "+pasajero.getNombre()+" está siendo atendido");
      
      //Asignar el puesto de atencion correspondiente al pasajero
      PuestoAtencion puesto = mapeoAPuestos.get(pasajero.getPasaje().getAerolinea());
      Thread.sleep(20);
      System.out.println("\t"+Colores.CYAN+Colores.NEGRITA+"PUESTO DE INFORMES "+Colores.RESET+"---> "+pasajero.getNombre()+" fue derivado al puesto de atencion "+puesto.getAerolinea());

      //Liberar el puesto de informacion
      this.puestoInformacion.release();

      return puesto;
   }

   public void ingresarPuestoAtencion(Pasajero pasajero, PuestoAtencion puestoAtencion) throws InterruptedException{
      //Esperar a que haya lugar en el puesto de atencion
      guardia.lock();
      try {
          while (puestoAtencion.estaLleno()) {
              System.out.println("\t\t"+Colores.YELLOW+Colores.NEGRITA+"PUESTO DE ATENCION "+Colores.RESET+"---> "+pasajero.getNombre()+" espera ser atendido en el puesto de atencion "+puestoAtencion.getAerolinea());
              ingreso.await();
          }
          puestoAtencion.ingresar(pasajero);
          ingreso.signalAll();
      } finally {
         guardia.unlock();
      }
      
   }

   public void salirPuestoAtencion(Pasajero pasajero, PuestoAtencion puestoAtencion){
      guardia.lock();
       try {
           puestoAtencion.salir();
           ingreso.signalAll();
       } finally {
           guardia.unlock();
       }
   }


   @Override
   public String toString(){
      return "PUESTO DE INFORMACION:\n"+puestoInformacion.toString()+"\n\n"+
               "PUESTOS DE ATENCION:\n"+Arrays.toString(puestosAtencion)+"\n\n"+"PEOPLE MOVER:\n"+peopleMover.toString()+"\n\n"
               +"TERMINALES:\n"+Arrays.toString(terminales)+"\n\n"
               +"\n\n"+"VUELOS:\n"+Arrays.toString(vuelos)+"\n\n";
   }
}
