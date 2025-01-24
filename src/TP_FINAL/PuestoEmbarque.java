package TP_FINAL;

import java.util.ArrayList;
import java.util.List;

public class PuestoEmbarque {
    private final String nombre;
    private final List<String> vuelos;

    public PuestoEmbarque(String n){
        nombre=n;
        this.vuelos = new ArrayList<>();
    }

    public String getNombre(){
        return nombre;
    }

    public void asignarVuelo(String v){
        vuelos.add(v);
    }

    public List<String> getVuelos() {
        return vuelos;
    }

    @Override
    public String toString(){
        return "Nombre:"+nombre+"  Vuelos:"+vuelos.toString();
    }
}
