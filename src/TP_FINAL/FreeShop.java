package TP_FINAL;


import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class FreeShop {
    private final String nombre;
    private final int max;
    private final Semaphore[] cajasDisponibles = {new Semaphore(1,true), new Semaphore(1,true)};
    private BlockingQueue<Pasajero> cola;
    private final AtomicInteger[] cajasPasajeros;

    public FreeShop(String n, int m){
        nombre=n;
        max=m;
        cola = new LinkedBlockingQueue<>(max);
        cajasPasajeros = new AtomicInteger[2]; // Initialize the array
        // Inicializamos los AtomicInteger a 0 (sin pasajeros)
        for (int i = 0; i < 2; i++) {
            cajasPasajeros[i] = new AtomicInteger(0);
        }
    }
 
    public String getNombre(){
        return nombre;
    }

    public void ingresar(Pasajero pasajero) throws InterruptedException{
        if (cola.offer(pasajero)){
            System.out.println("\t\t\t\t\t"+Colores.BLUE+Colores.NEGRITA+nombre+Colores.RESET+" ---> "+pasajero.getNombre()+" ingresando al freeshop");   
            boolean compra = Math.random() < 0.5;
        
            if (compra) {
                System.out.println("\t\t\t\t\t"+Colores.BLUE+Colores.NEGRITA+nombre+Colores.RESET+" ---> "+ pasajero.getNombre() + " está mirando productos y decide comprar.");
                Thread.sleep(300); // Simular tiempo de mirar productos
                realizarCompra(pasajero);
            } else {
                System.out.println("\t\t\t\t\t"+Colores.BLUE+Colores.NEGRITA+nombre+Colores.RESET+" ---> "+ pasajero.getNombre() + " solo está mirando productos.");
                Thread.sleep(200); // Simular tiempo de mirar productos
            }

            System.out.println("\t\t\t\t\t"+Colores.BLUE+Colores.NEGRITA+nombre+Colores.RESET+" ---> "+pasajero.getNombre()+" saliendo del freeshop");
            cola.remove(pasajero);
        }else{
            System.out.println("\t\t\t\t\t"+Colores.BLUE+Colores.NEGRITA+nombre+Colores.RESET+" ---> "+pasajero.getNombre()+" no pudo ingresar al freeshop");  
        }
    }

    private void realizarCompra(Pasajero pasajero) throws InterruptedException{
        // Elegir la caja con menos personas esperando
        int cajaSeleccionada = (cajasPasajeros[0].get() <= cajasPasajeros[1].get()) ? 0 : 1;
        String nombreCaja = (cajaSeleccionada == 0) ? "CAJA 1" : "CAJA 2";
        
        // Incrementar el contador de pasajeros en espera para la caja seleccionada
        cajasPasajeros[cajaSeleccionada].incrementAndGet();  // Aumentamos la cantidad de pasajeros esperando
        System.out.println("\t\t\t\t\t"+Colores.BLUE+Colores.NEGRITA + nombre +Colores.RESET+ " ---> " + pasajero.getNombre() + " se une a la cola de la "  +nombreCaja+ ".");


        // Intentar adquirir un permiso para la caja seleccionada
        cajasDisponibles[cajaSeleccionada].acquire();//La caja atiende un pasajero a la vez
        System.out.println("\t\t\t\t\t\t"+Colores.BLUE_FONDO+Colores.NEGRITA + nombreCaja+"-" +nombre+Colores.RESET+ " ---> Atendiendo a " + pasajero.getNombre() + ".");
        
        // Simulamos el proceso de compra
        Thread.sleep(30);  // Simula el tiempo de atención en la caja

        // Finalizada la compra
        System.out.println("\t\t\t\t\t\t"+Colores.BLUE_FONDO+Colores.NEGRITA + nombreCaja+"-"+nombre+Colores.RESET+ " ---> Finalizada la compra de " + pasajero.getNombre() + ".");     
        
        // Liberamos el permiso para la caja
        cajasDisponibles[cajaSeleccionada].release();
        
        // Decrementamos el contador de pasajeros esperando
        cajasPasajeros[cajaSeleccionada].decrementAndGet();  // Reducimos la cantidad de pasajeros esperando
    }


    @Override
    public String toString(){
        return "Nombre:"+nombre+"  Cantidad maxima:"+max+ "  Cajas:"+Arrays.toString(cajasDisponibles);
        return "Nombre:"+nombre+"  Cantidad maxima:"+max+ "  Cajas:"+Arrays.toString(cajasDisponibles);
    }
}
