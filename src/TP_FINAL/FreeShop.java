package TP_FINAL;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class FreeShop {
    private final String nombre;
    private final int max;
    private final CajaFreeShop cajas[] = new CajaFreeShop[2];
    private final Semaphore lugaresDisponibles;


    public FreeShop(String n, int m, Hora hs){
        nombre=n;
        max=m;
        lugaresDisponibles = new Semaphore(max);
        cajas[0]= new CajaFreeShop("CAJA 1 - "+nombre);
        cajas[1]= new CajaFreeShop("CAJA 2 - "+nombre);
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

            salirFreeShop(pasajero);
        }else{
            System.out.println("\t\t\t\t\t"+Colores.BLUE+Colores.NEGRITA+nombre+Colores.RESET+" ---> "+pasajero.getNombre()+" no pudo ingresar al freeshop");  
        }
    }

    private void realizarCompra(Pasajero pasajero) throws InterruptedException{
        CajaFreeShop cajaSeleccionada = (cajas[0].getCola().size() <= cajas[1].getCola().size()) ? cajas[0] : cajas[1];
        cajaSeleccionada.ingresar(pasajero);
    }

    private void salirFreeShop(Pasajero pasajero){
        System.out.println("\t\t\t\t\t"+Colores.BLUE+Colores.NEGRITA+nombre+Colores.RESET+" ---> "+pasajero.getNombre()+" saliendo del freeshop");
        lugaresDisponibles.release();
    }


    @Override
    public String toString(){
        return "Nombre:"+nombre+"  Cantidad maxima:"+max+ "  Cajas:"+Arrays.toString(cajas);
    }
}
