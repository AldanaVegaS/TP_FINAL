package TP_FINAL;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PeopleMover extends Thread {
    private final int capacidadMax; // Capacidad máxima del tren
    private final BlockingQueue<Pasajero> pasajeros; // Pasajeros actuales en el tren
    private final String[] terminales = {"A", "B", "C"}; // Terminales en orden
    private final Lock lockSubir = new ReentrantLock();
    private final Condition puedeSubir = lockSubir.newCondition();
    private final Lock lockSalir = new ReentrantLock();
    private final Condition puedeSalir = lockSalir.newCondition();
    private boolean enMovimiento = false; // Indica si está en recorrido
    private final Hora reloj;

    public PeopleMover(int capacidadMax, Hora hs) {
        this.capacidadMax = capacidadMax;
        this.pasajeros = new LinkedBlockingDeque<>(capacidadMax);
        this.reloj = hs;
    }

    // Método para subir un pasajero al tren
    public void subirPasajero(Pasajero pasajero) throws InterruptedException {
        lockSubir.lock();
        try {
            while (enMovimiento || pasajeros.size() >= capacidadMax) {
                puedeSubir.await(); // Esperar si el tren está en movimiento o lleno.
            }

            synchronized (pasajeros) {
                if (pasajeros.size() < capacidadMax) {// Si hay espacio en el tren
                    pasajeros.put(pasajero);
                    System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> "+pasajero.getNombre() + " subió al tren hacia la Terminal " + pasajero.getTerminal().getNombre()+", hay "+pasajeros.size()+" pasajeros en el tren.");
                    lockSalir.lock();
                    try {
                        puedeSalir.signalAll();
                    } finally {
                        lockSalir.unlock();
                    }
                } else {
                    System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> "+pasajero.getNombre() + " espera porque el tren está lleno.");
                }
            }
        } finally {
            lockSubir.unlock();
        }
        
    }

    // Método que simula el recorrido del tren
    public void iniciarRecorrido() throws InterruptedException {
        while (true) {
            synchronized (reloj) {
                while(reloj.getHora()<6 || reloj.getHora()>22){
                  reloj.wait();
               }
            }
            // Esperar hasta que el tren esté lleno o este por salir un vuelo
            lockSalir.lock();
            try {
                while (!trenListo()) { 
                    puedeSalir.await();
                }
                enMovimiento = true;
            } finally {
                lockSalir.unlock();
            }
            System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> "+ "El tren inicia su recorrido con " + pasajeros.size() + " pasajeros.");

            // Recorrer las terminales en orden
            for (String terminal : terminales) {
                System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> "+"El tren llega a la Terminal " + terminal + ".");
                Thread.sleep(500);// Simular tiempo de viaje
                bajarPasajeros(terminal);
                
            }

            // Vaciar el tren al final del recorrido
            synchronized (pasajeros) {
                pasajeros.clear();
                
            }
            System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> "+"El tren regresa vacío al inicio.");
            Thread.sleep(1000);//Simula el tiempo que le toma regresar al comienzo del recorrido
            lockSubir.lock();
            try {
                enMovimiento = false;
                puedeSubir.signalAll();
            } finally {
                lockSubir.unlock();
            }
            
        }
    }

    // Método para que los pasajeros bajen en su terminal
    private void bajarPasajeros(String terminalActual) {
        synchronized (pasajeros) {
            System.err.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> "+ "Bajando pasajeros en la Terminal " + terminalActual + ".");
            pasajeros.removeIf(pasajero -> {
                if (pasajero.getTerminal().getNombre().equals(terminalActual)) {
                    System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> "+pasajero.getNombre() + " baja en la Terminal " + terminalActual + ".");
                    pasajero.bajoEnTerminal();
                    return true;
                }
                return false;
            });
        }
    }

    // Método para verificar si el tren está listo para salir
    private boolean trenListo() {
        // Si el tren está lleno, puede salir
        if (pasajeros.size() >= capacidadMax) {
            return true;
        }

        // Verificar si algún pasajero tiene un vuelo próximo
        int horaActual = reloj.getHora();
        for (Pasajero pasajero : pasajeros) {
            if (Math.abs(pasajero.getVuelo().getHoraSalida() - horaActual) <= 2) { // Vuelo próximo dentro de 2 horas
                System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> Vuelo próximo detectado: " + pasajero.getVuelo().getIdVuelo());
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        try {
            iniciarRecorrido();
        } catch (InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public String toString(){
        return "People Mover con capacidad máxima de "+capacidadMax;
    }
}
