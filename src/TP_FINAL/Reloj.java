package TP_FINAL;

public class Reloj extends Thread{
    private Hora hs;

    public Reloj(Hora hs){
        this.hs=hs;
    }

    @Override
    public void run(){
        while(true){
            try {
                Thread.sleep(3000);
                hs.incrementarHora();
            } catch (InterruptedException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
    
}
