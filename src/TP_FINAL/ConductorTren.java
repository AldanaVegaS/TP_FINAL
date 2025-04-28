package TP_FINAL;

public class ConductorTren extends Thread {
    private final PeopleMover tren;
    private Hora hs; // Instancia de Hora para obtener la hora actual  

    public ConductorTren(PeopleMover tren, Hora hora) {
        this.tren = tren;
        this.hs = hora; 
    }

    @Override
    public void run() {
        while(true){
            hs.horarioLaboral();
            try {
                tren.conducir(); // Llama al método conducir del tren
            } catch (InterruptedException e) {
                e.printStackTrace(); // Maneja la excepción
                Thread.currentThread().interrupt(); // Restablece el estado de interrupción
            }
        }
    }
    
}
