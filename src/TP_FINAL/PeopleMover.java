package TP_FINAL;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PeopleMover{
    private final int capacidadMax; // Capacidad máxima del tren
    private int cantPasajeros = 0; // Cantidad de pasajeros actuales
    private int[] cantPasajerosPorTerminal = {0, 0, 0}; // Cantidad de pasajeros actuales por terminal
    private final List<String> terminales = Arrays.asList("A", "B", "C"); // Terminales en orden
    private boolean enMovimiento = false; // Indica si el tren está en movimiento
    private final Lock lock = new ReentrantLock();
    private final Condition puedeSubir = lock.newCondition();
    private final Condition puedeSalir = lock.newCondition();
    private final Condition puedeBajar = lock.newCondition();
    private String terminalActual = " "; // Terminal actual del tren

    public PeopleMover(int capacidadMax) {
        this.capacidadMax = capacidadMax;
    }

    public void usarTren(Pasajero pasajero) throws InterruptedException {
        lock.lock();
        try {
            while (enMovimiento || cantPasajeros >= capacidadMax) {// Espera si el tren está lleno o en movimiento
                puedeSubir.await(); 
            }

            //Ingresa al tren
            cantPasajeros++;
            int index = terminales.indexOf(pasajero.getTerminal().getNombre());
            cantPasajerosPorTerminal[index]++;
            System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> "+pasajero.getNombre() + " subió al tren hacia la Terminal " + pasajero.getTerminal().getNombre()+", hay "+cantPasajeros+" pasajeros en el tren.");

            //Avisa si el tren está lleno
            if (cantPasajeros >= capacidadMax) {
                puedeSalir.signal(); // Despierta al tren
            }

            // Espera a que el tren llegue a la terminal correspondiente
            while (!terminalActual.equals(pasajero.getTerminal().getNombre())) {
                puedeBajar.await();
            }

            //Baja del tren
            cantPasajeros++;
            cantPasajerosPorTerminal[index]--;
            
            if (cantPasajerosPorTerminal[index] == 0) {
                puedeSalir.signal(); // Despierta al tren si bajaron todos los pasajeros de la terminal
                
            }
           
        } finally {
            lock.unlock();
        }
    }

    public void salirDeLaEstacion() throws InterruptedException {
        lock.lock();
        try {
            while (cantPasajeros<capacidadMax) {
                puedeSalir.await(); // Espera a completar la capacidad máxima
            }

            //Sale hacia las terminales
            System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> "+"Tren saliendo de la estación.");
            enMovimiento = true;

            
            bajarPasajeros("A");
            bajarPasajeros("B");
            bajarPasajeros("C");
                   
            //Vuelve al lugar de origen
            enMovimiento = false;
            cantPasajeros = 0;
            cantPasajerosPorTerminal = new int[]{0, 0, 0};
            terminalActual = " ";
            System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> "+"El tren ha vuelto a la estación.");
            puedeSubir.signalAll(); // Despierta a los pasajeros que esperan subir
        } finally {
            lock.unlock();
        }
    }

    private void bajarPasajeros(String terminal) throws InterruptedException{
        terminalActual = terminal;
        int index = terminales.indexOf(terminal);
        System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> "+"El tren llega a la Terminal " + terminal + ". Bajan "
                 +  cantPasajerosPorTerminal[index]+ " pasajeros.");
                
        puedeBajar.signalAll(); // Despierta a los pasajeros que deben bajar
        while(cantPasajerosPorTerminal[index] > 0){
            puedeSalir.await();

        }
        Thread.sleep(500);// Simular tiempo de viaje
    }

    @Override
    public String toString(){
        return "People Mover con capacidad máxima de "+capacidadMax;
    }
}
