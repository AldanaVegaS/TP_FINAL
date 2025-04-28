package TP_FINAL;

public class PuestoEmbarque {
    private final String nombre;

    public PuestoEmbarque(String n){
        nombre=n;
    }

    public String getNombre(){
        return nombre;
    }

    @Override
    public String toString(){
        return "Nombre:"+nombre;
    }
}
