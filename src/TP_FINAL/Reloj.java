package TP_FINAL;

public class Reloj extends Thread {
    private Hora hora;
    private Aeropuerto aeropuerto;

    public Reloj(Hora hora, Aeropuerto aeropuerto) {
        this.hora = hora;
        this.aeropuerto = aeropuerto;
    }

    @Override
    public void run() {
        while (true) { 
            try {
                hora.incrementarHora();
                if(hora.getHora() == 6){
                    //System.out.println("El aeropuerto abre sus puertas");
                    aeropuerto.iniciarDiaLaboral();
                }else if(hora.getHora() == 21){
                    //System.out.println("El aeropuerto cierra sus puertas");
                    aeropuerto.terminarDiaLaboral();
                }
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                System.err.println("Error en el reloj: " + ex.getMessage());
            }
        }
    }
    
}
