package TP_FINAL;

import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Aeropuerto {
   private final Semaphore puestoInformacion = new Semaphore(0);
   private final PuestoAtencion puestosAtencion [];
   private final PeopleMover peopleMover;
   private final Terminal terminales[]; 
   private final Vuelo vuelos[];
   private final AsignadorDeVuelos asignadorDeVuelos;
   private final Lock lock = new ReentrantLock();
   private final Condition puedePasar = lock.newCondition();
   private final Condition ingreso = lock.newCondition();

   public Aeropuerto(PuestoAtencion puestosAten[], PeopleMover mover, Terminal term[], Vuelo v[]){
        puestosAtencion=puestosAten;
        peopleMover=mover;
        vuelos=v;
        asignadorDeVuelos = new AsignadorDeVuelos(term,vuelos);
        terminales = asignadorDeVuelos.getTerminales();
        for (int i = 0; i < puestosAten.length; i++) {
         puestosAtencion[i].setTerminales(terminales);
      }
   }

   public PeopleMover getPeopleMover(){
      return peopleMover;
   }

   public PuestoAtencion[] getPuestosAtencion(){
      return puestosAtencion;
   }

   public void iniciarDiaLaboral(){
      this.puestoInformacion.release();
   }

   public void terminarDiaLaboral(){
      try {
         this.puestoInformacion.acquire();
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         e.printStackTrace();
      }
   }

   public void atencionPuestoInfo(Pasajero pasajero) throws InterruptedException{
      //Ingresar al puesto de informacion
      this.puestoInformacion.acquire();
      System.out.println("\t"+Colores.CYAN+Colores.NEGRITA+"PUESTO DE INFORMES "+Colores.RESET+"---> "+pasajero.getNombre()+" está siendo atendido");
      
      //Derivar pasajero a un puesto de atencion
      int i = 0;
      while(i < puestosAtencion.length ){
         String aerolinea = pasajero.getPasaje().getAerolinea();
         if(puestosAtencion[i].getAerolinea().equals(aerolinea)){
            pasajero.setPuestoAtencion(puestosAtencion[i]);
         }
         i++;
      }
      Thread.sleep(100);
      System.out.println("\t"+Colores.CYAN+Colores.NEGRITA+"PUESTO DE INFORMES "+Colores.RESET+"---> "+pasajero.getNombre()+" fue derivado al puesto de atencion "+pasajero.getPuesto().getAerolinea());

      //Liberar el puesto de informacion
      this.puestoInformacion.release();
   }

   //El pasajero espera su turno para ingresar al puesto
   public void esperarTurno(Pasajero pasajero) throws InterruptedException{
      PuestoAtencion puesto = pasajero.getPuesto();
      lock.lock();
      try {
         System.out.println("\t\t"+Colores.YELLOW+Colores.NEGRITA+"PUESTO DE ATENCION "+puesto.getAerolinea().toUpperCase()+Colores.RESET+"---> "+pasajero.getNombre()+" espera su turno para ingresar al puesto de atención");
         while(!puesto.estaDisponible()){
            puedePasar.await();
         }
      } finally {
         lock.unlock();
      }
   }

   //El pasajero sale del puesto de atención
   public void salirDelPuesto(){
      lock.lock();
      try {
         ingreso.signal();
      } finally {
         lock.unlock();
      }
   }

   //El guardia permite el ingreso al puesto de atención cuando hay lugar
   public void permitirIngreso() throws InterruptedException{
      lock.lock();
      try {
         while (!puestosDisponibles()) { 
             ingreso.await();
         }
         puedePasar.signalAll();
      } finally {
          lock.unlock();
      }
  }

  public boolean puestosDisponibles(){
   lock.lock();
   try {
      for(PuestoAtencion puesto: puestosAtencion){
         if(puesto.estaDisponible()){
            return true;
         }
      }
      return false;
   } finally {
       lock.unlock();
      }
   }



   @Override
   public String toString(){
      return "PUESTO DE INFORMACION:\n"+puestoInformacion.toString()+"\n\n"+
               "PUESTOS DE ATENCION:\n"+Arrays.toString(puestosAtencion)+"\n\n"+"PEOPLE MOVER:\n"+
               peopleMover.toString()+"\n\n"+"TERMINALES:\n"+Arrays.toString(terminales)+"\n\n"+"VUELOS:\n"+Arrays.toString(vuelos)+"\n\n";
   }
}
