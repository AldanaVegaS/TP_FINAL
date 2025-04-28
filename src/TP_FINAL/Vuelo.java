package TP_FINAL;

public class Vuelo {
    private final String idVuelo;
    private final String aerolinea;
    private final int horaSalida;
    private boolean asignado = false;

    public Vuelo(String vuelo, String aero, int hora){
        this.idVuelo=vuelo;
        this.aerolinea=aero;
        this.horaSalida=hora;
    }

    public void setAsignado(){
        this.asignado = true;
    }

    public boolean getAsignado(){
        return asignado;
    }

    public String getIdVuelo(){
        return idVuelo;
    }

    public String getAerolinea(){
        return aerolinea;
    }

    public int getHoraSalida(){
        return horaSalida;
    }

    @Override
    public String toString(){
        return ("IDVuelo:"+idVuelo+", aerolinea:"+aerolinea+", hora salida:"+horaSalida);
    }
}