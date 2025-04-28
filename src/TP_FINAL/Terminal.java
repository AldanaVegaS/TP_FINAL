package TP_FINAL;
import java.util.Arrays;

public class Terminal {
    private final String nombre;
    private final FreeShop freeShop;
    private final String puestos[];
    private final int cantPuestos;

    public Terminal(String  n, FreeShop free, int cantPuestos){
        nombre=n;
        freeShop=free;
        puestos=crearPuestos(cantPuestos);
        this.cantPuestos = cantPuestos;
    }

    public String  getNombre(){
        return nombre;
    }

    public FreeShop getFreeShop(){
        return freeShop;
    } 

    public String[] getPuestos(){
        return puestos;
    }

    public int getCantPuestos(){
        return cantPuestos;
    }

    
    @Override
    public String toString(){
        return "Nombre:"+nombre+"  Freeshop:"+freeShop.toString()+"  Puesto de Embarque:"+Arrays.toString(puestos);
    }

    private String[] crearPuestos(int cantPuestos){
        String localPuestos [] = new String[cantPuestos];
        for(int i=0;i<cantPuestos;i++){
            localPuestos[i] = "Puesto embarque "+(i+1);
        }
        return localPuestos;

    }
}