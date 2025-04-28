package TP_FINAL;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PeopleMover {
    private final int capacidadMax; // Capacidad máxima del tren
    private final List<String> terminales = Arrays.asList("A", "B", "C"); // Terminales en orden
    private final Lock lock = new ReentrantLock(true);
    private final Condition esperarSubir = lock.newCondition();
    private final Condition esperarBajar = lock.newCondition();
    private final Condition esperarSalir = lock.newCondition();
    private final Condition esperarContinuar = lock.newCondition();

    private final int numTerminales; // Número total de estaciones (origen + intermedias)
    private int pasajerosABordo; // Pasajeros actuales en el tren
    private String estacionActual = " "; // Estación actual del tren (0 es origen)
    private boolean enMovimiento; // Indica si el tren está en la estación origen
    private boolean trenLleno; // Indica si el tren alcanzó su capacidad

    private int[] pasajerosPorEstacion; // Pasajeros por estación destino

    public PeopleMover(int capacidadMax) {
        this.capacidadMax = capacidadMax;
        this.numTerminales = 3;
        this.pasajerosABordo = 0;
        this.enMovimiento = false;
        this.trenLleno = false;
        this.pasajerosPorEstacion = new int[numTerminales];
    }




    // Método para que un pasajero suba al tren
    public void subirPasajero(Pasajero pasajero) throws InterruptedException {
        lock.lock();
        try {
            System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> " + pasajero.getNombre() + " intenta subir al tren.");
            while (enMovimiento || pasajerosABordo >= capacidadMax) {
                esperarSubir.await(); // Espera si el tren no está en origen o está lleno
            }

            pasajerosABordo++;
            String estacionDestino = pasajero.getTerminal();
            int indice = terminales.indexOf(estacionDestino);
            pasajerosPorEstacion[indice]++;
            System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> "+pasajero.getNombre() + " subió al tren hacia la Terminal " + estacionDestino+", hay "+pasajerosABordo+" pasajeros en el tren.");
            
            // Notifica al conductor si el tren está lleno
            if (pasajerosABordo >= capacidadMax) {
                trenLleno = true;
                esperarSalir.signal(); // Avisa al conductor si el tren está lleno
            }
        } finally {
            lock.unlock();
        }
    }

    // Método para que un pasajero baje del tren
    public void bajarPasajero(Pasajero pasajero) throws InterruptedException {
        lock.lock();
        try {
            String estacionDestino = pasajero.getTerminal();
            while (!estacionActual.equals(estacionDestino)) {
                esperarBajar.await(); // Espera hasta llegar a la estación destino
            }
            pasajerosABordo--;
            int indice = terminales.indexOf(estacionDestino);
            pasajerosPorEstacion[indice]--;
            
            if(pasajerosPorEstacion[indice] == 0){
                System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> Todos los pasajeros bajaron en estación " + estacionDestino);
                esperarContinuar.signal(); // Avisa al conductor si no hay más pasajeros en la estación
            }
        } finally {
            lock.unlock();
        }        
    }
   
    // Método del conductor para controlar el tren
    public void conducir() throws InterruptedException {
        lock.lock();
        try {
            // Espera en la estación origen hasta que el tren esté lleno o sea la hora de partir
            System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> Tren en terminal origen, esperando pasajeros.");
            while (!trenLleno) {
                System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> Esperando a que el tren esté lleno.");
                boolean señal = esperarSalir.await(3000,TimeUnit.MILLISECONDS);//Si paso una hora y no se llenó el tren, se va igual
                if (!señal) {
                    // Timeout ocurrió
                    System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"--->Se acabó el tiempo de espera. El tren partirá igual aunque no esté lleno. "+pasajerosABordo+" pasajeros a bordo.");
                    break;
                }
            }

            //Transcurrio el tiempo de espera o el tren está lleno
            if (pasajerosABordo == 0) {//Paso el tiempo y no hay pasajeros
                System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> No hay pasajeros, el tren no puede partir.");
            }else{
                // Sale de la estación origen
                enMovimiento = true;
                System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> Tren sale de la estación origen con " + pasajerosABordo + " pasajeros." 
                + pasajerosPorEstacion[0] + " pasajeros en A, " + pasajerosPorEstacion[1] + " pasajeros en B, " + pasajerosPorEstacion[2] + " pasajeros en C.");

                // Recorre las estaciones 1 a numEstaciones-1
                for (String terminal: terminales) {
                    int i = terminales.indexOf(terminal);
                    estacionActual = terminal;
                    System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> Tren llega a estación " + terminal);
                    if (pasajerosPorEstacion[i] > 0) {
                        esperarBajar.signalAll();// Avisa a los pasajeros para que bajen
                        esperarContinuar.await(); // Espera a que los pasajeros bajen
                    }
                    
                    // Simula tiempo en la estación
                    Thread.sleep(150);
                }

                // Regresa vacío a la estación origen
                trenLleno = false;
                enMovimiento = false;
                estacionActual = " ";
                pasajerosABordo = 0;
                Arrays.fill(pasajerosPorEstacion, 0);
                System.out.println("\t\t\t"+Colores.GREEN+Colores.NEGRITA+"PEOPLE MOVER "+Colores.RESET+"---> Tren regresa vacío a la estación origen.");
                // Simula tiempo de regreso
                Thread.sleep(300);
            }
            
        } finally {
            lock.unlock();
        }
            
        
    }


    @Override
    public String toString(){
        return "People Mover con capacidad máxima de "+capacidadMax;
    }
}
