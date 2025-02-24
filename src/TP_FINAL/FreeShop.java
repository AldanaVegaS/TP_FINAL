package TP_FINAL;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class FreeShop {
    private final String nombre;
    private final int max;
    private final Semaphore[] cajasDisponibles = {new Semaphore(1,true), new Semaphore(1,true)};
    private final Semaphore lugaresDisponibles;


    public FreeShop(String n, int m, Hora hs){
        nombre=n;
        max=m;
        lugaresDisponibles = new Semaphore(max);
    }
 
    public String getNombre(){
        return nombre;
    }

    public void ingresar(Pasajero pasajero) throws InterruptedException{
        if (lugaresDisponibles.tryAcquire()){
            System.out.println("\t\t\t\t\t"+Colores.BLUE+Colores.NEGRITA+nombre+Colores.RESET+" ---> "+pasajero.getNombre()+" ingresando al freeshop");
            boolean compra = Math.random() < 0.5;
        
            if (compra) {
                System.out.println("\t\t\t\t\t"+Colores.BLUE+Colores.NEGRITA+nombre+Colores.RESET+" ---> "+ pasajero.getNombre() + " está mirando productos y decide comprar.");
                realizarCompra(pasajero);
            } else {
                System.out.println("\t\t\t\t\t"+Colores.BLUE+Colores.NEGRITA+nombre+Colores.RESET+" ---> "+ pasajero.getNombre() + " solo está mirando productos.");
                Thread.sleep(500); // Simular tiempo de mirar productos
            }

            lugaresDisponibles.release();
        }else{
            System.out.println("\t\t\t\t\t"+Colores.BLUE+Colores.NEGRITA+nombre+Colores.RESET+" ---> "+pasajero.getNombre()+" no pudo ingresar al freeshop");  
        }
    }

    private void realizarCompra(Pasajero pasajero) throws InterruptedException{
        Semaphore seleccionado = (cajasDisponibles[0].getQueueLength() <= cajasDisponibles[1].getQueueLength()) ? cajasDisponibles[0] : cajasDisponibles[1];
        String nombreCaja = (seleccionado == cajasDisponibles[0]) ? "CAJA 1" : "CAJA 2";
        System.out.println("\t\t\t\t\t"+Colores.BLUE+Colores.NEGRITA + nombre +Colores.RESET+ " ---> " + pasajero.getNombre() + " se une a la cola de la "  +nombreCaja+ ".");

        seleccionado.acquire();
        System.out.println("\t\t\t\t\t\t"+Colores.BLUE_FONDO+Colores.NEGRITA + nombreCaja +Colores.RESET+ " ---> Atendiendo a " + pasajero.getNombre() + ".");
        Thread.sleep(100); 
        System.out.println("\t\t\t\t\t\t"+Colores.BLUE_FONDO+Colores.NEGRITA + nombreCaja +Colores.RESET+ " ---> Finalizada la compra de " + pasajero.getNombre() + ".");
        seleccionado.release();
    }


    @Override
    public String toString(){
        return "Nombre:"+nombre+"  Cantidad maxima:"+max+ "  Cajas:"+Arrays.toString(cajasDisponibles);
    }
}
