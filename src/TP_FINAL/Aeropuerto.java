package TP_FINAL;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class Aeropuerto {
   private final Semaphore puestoInformacion = new Semaphore(1);
   private final PuestoAtencion puestosAtencion [];
   private final PeopleMover peopleMover;
   private final Terminal terminales[]; 
   private final Vuelo vuelos[];
   private final Hora hora;
   private final AsignadorDeVuelos asignadorDeVuelos;

   public Aeropuerto(PuestoAtencion puestosAten[], PeopleMover mover, Terminal term[], Vuelo v[], Hora hs){
        puestosAtencion=puestosAten;
        peopleMover=mover;
        vuelos=v;
        hora = hs;
        asignadorDeVuelos = new AsignadorDeVuelos(term,vuelos);
        terminales = asignadorDeVuelos.getTerminales();
        for (int i = 0; i < puestosAten.length; i++) {
         puestosAtencion[i].setTerminales(terminales);
      }
       iniciarPeopleMover();
   }

   private void iniciarPeopleMover() {
      peopleMover.start();
   }

   public void atencionPuestoInfo(Pasajero pasajero) throws InterruptedException{
      synchronized (hora) {
          while(hora.getHora()<6 || hora.getHora()>22){
            hora.wait();
         }
      }
      this.puestoInformacion.acquire();
      System.out.println("\t"+Colores.CYAN+Colores.NEGRITA+"PUESTO DE INFORMES "+Colores.RESET+"---> "+pasajero.getNombre()+" está siendo atendido");
      derivarPasajero(pasajero);
      Thread.sleep(100);
      this.puestoInformacion.release();
   }

   private void derivarPasajero(Pasajero pasajero){
      int i = 0;
      boolean derivado = false;
      while(i < puestosAtencion.length && !derivado){
         String aerolinea = pasajero.getPasaje().getAerolinea();
         if(puestosAtencion[i].getAerolinea().equals(aerolinea)){
            pasajero.setPuestoAtencion(puestosAtencion[i]);
            derivado = true;
         }
         i++;
      }
      System.out.println("\t"+Colores.CYAN+Colores.NEGRITA+"PUESTO DE INFORMES "+Colores.RESET+"---> "+pasajero.getNombre()+" fue derivado al puesto de atencion "+pasajero.getPuesto());
   }

   
   public void subirAlPeopleMover(Pasajero pasajero) throws InterruptedException{
      peopleMover.subirPasajero(pasajero);
   }


   @Override
   public String toString(){
      return "PUESTO DE INFORMACION:\n"+puestoInformacion.toString()+"\n\n"+
               "PUESTOS DE ATENCION:\n"+Arrays.toString(puestosAtencion)+"\n\n"+"PEOPLE MOVER:\n"+
               peopleMover.toString()+"\n\n"+"TERMINALES:\n"+Arrays.toString(terminales)+"\n\n"+"VUELOS:\n"+Arrays.toString(vuelos)+"\n\n";
   }
}
