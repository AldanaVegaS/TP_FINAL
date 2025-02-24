package TP_FINAL;

public class SalaEmbarque {
    private final String nombre;
    private final Hora hora;
 
    public SalaEmbarque(Hora hs, String n) {
        this.hora = hs;
        nombre = n;
    }


    public void esperarVuelo(Pasajero pasajero) throws InterruptedException {
        System.out.println("\t\t\t\t\t"+Colores.WHITE+Colores.NEGRITA+nombre+Colores.RESET+" ---> " + pasajero.getNombre() + " espera en la sala de embarque para el vuelo " + pasajero.getVuelo().getIdVuelo() + ".");
        hora.esperar(pasajero.getVuelo().getHoraSalida());
        System.out.println("\t\t\t\t\t"+Colores.WHITE+Colores.NEGRITA+nombre+Colores.RESET+" ---> " + pasajero.getNombre() + " embarca en el vuelo " + pasajero.getVuelo().getIdVuelo() + ".");
        
    }



}
