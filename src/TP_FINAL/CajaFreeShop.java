package TP_FINAL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CajaFreeShop {
    private final String nombre;
    private final BlockingQueue<Pasajero> cola;

    public CajaFreeShop(String n){
        nombre=n;
        cola = new LinkedBlockingQueue<>(20);
    }

    public String getNombre(){
        return nombre;
    }

    public BlockingQueue<Pasajero> getCola(){
        return cola;
    }

    public void ingresar(Pasajero pasajero) throws InterruptedException{
        System.out.println("\t\t\t\t\t\t"+Colores.BLUE_FONDO+Colores.NEGRITA + nombre +Colores.RESET+ " ---> " + pasajero.getNombre() + " se une a la cola de la " + nombre + ".");
        cola.put(pasajero);

        // pasajero espera su turno en la cola
        while (!cola.peek().equals(pasajero)) {
            Thread.sleep(100);
        }

        atender();
        
    }

    public synchronized void atender() throws InterruptedException{
        Pasajero p = cola.poll(); // Sacar al pasajero de la cola
        System.out.println("\t\t\t\t\t\t"+Colores.BLUE_FONDO+Colores.NEGRITA + nombre +Colores.RESET+ " ---> Atendiendo a " + p.getNombre() + ".");
        Thread.sleep(100); 
        System.out.println("\t\t\t\t\t\t"+Colores.BLUE_FONDO+Colores.NEGRITA + nombre +Colores.RESET+ " ---> Finalizada la compra de " + p.getNombre() + ".");
         
    }


    @Override
    public String toString(){
        return "Nombre:"+nombre;
    }
}
