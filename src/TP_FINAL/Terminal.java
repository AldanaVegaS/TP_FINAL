package TP_FINAL;

import java.util.Arrays;

public class Terminal {
    private final String nombre;
    private final FreeShop freeShop;
    private final SalaEmbarque sala;
    private final PuestoEmbarque puestos[];

    public Terminal(String n, FreeShop free, SalaEmbarque s, int cantPuestos){
        nombre=n;
        freeShop=free;
        sala=s;
        puestos=crearPuestos(cantPuestos);
    }

    public String getNombre(){
        return nombre;
    }

    public FreeShop getFreeShop(){
        return freeShop;
    } 

    public PuestoEmbarque[] getPuestos(){
        return puestos;
    }

    public SalaEmbarque getSala(){
        return sala;
    }

    
    @Override
    public String toString(){
        return "Nombre:"+nombre+"  Freeshop:"+freeShop.toString()+"  Puesto de Embarque:"+Arrays.toString(puestos);
    }

    private PuestoEmbarque[] crearPuestos(int cantPuestos){
        PuestoEmbarque localPuestos [] = new PuestoEmbarque[cantPuestos];
        for(int i=0;i<cantPuestos;i++){
            localPuestos[i] = new PuestoEmbarque(("Puesto embarque "+(i+1)));
        }
        return localPuestos;

    }
}
