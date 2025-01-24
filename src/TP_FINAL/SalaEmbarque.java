package TP_FINAL;

import java.util.ArrayList;
import java.util.List;

public class SalaEmbarque {
    private final String nombre;
    private final Hora hora;
    private final List<Pasajero> pasajeros = new ArrayList<>();

    public SalaEmbarque(Hora hs, String n) {
        this.hora = hs;
        nombre = n;
    }

    public List<Pasajero> getPasajeros() {
        return new ArrayList<>(pasajeros);
    }

    public void esperarVuelo(Pasajero pasajero) throws InterruptedException {
        synchronized (hora) {
            pasajeros.add(pasajero);
            System.out.println("\t\t\t\t\t"+Colores.WHITE+Colores.NEGRITA+nombre+Colores.RESET+" ---> " + pasajero.getNombre() + " espera en la sala de embarque para el vuelo " + pasajero.getVuelo().getIdVuelo() + ".");

            // Esperar hasta que sea la hora de salida del vuelo
            while (hora.getHora() < pasajero.getVuelo().getHoraSalida()) {
                hora.wait(); 
            }

            System.out.println("\t\t\t\t\t"+Colores.WHITE+Colores.NEGRITA+nombre+Colores.RESET+" ---> " + pasajero.getNombre() + " embarca en el vuelo " + pasajero.getVuelo().getIdVuelo() + ".");
            pasajeros.remove(pasajero); 
            pasajero.finalizar();
        }
    }



}
