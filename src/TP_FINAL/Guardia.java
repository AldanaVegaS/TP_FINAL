package TP_FINAL;

public class Guardia extends Thread {
    private final Hora hora;
    private final Aeropuerto aeropuerto;
    

    public Guardia(PuestoAtencion[] puestos, Hora hs,Aeropuerto aeropuerto) {
        this.aeropuerto = aeropuerto;
        this.hora = hs;
    }


    @Override
    public void run() {
        while (true) {
            try {
                while (hora.getHora() >= 6 && hora.getHora() <= 22) {
                    aeropuerto.permitirIngreso();
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }







}
