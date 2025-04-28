package TP_FINAL;

public class Hora {
    private int hs;

    public Hora (){
        hs = 5;
    }

    public synchronized void horarioLaboral(){
        while(hs < 6 || hs > 21){
            try {
                this.wait();
            } catch (InterruptedException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    public synchronized void esperarHora(int hora) throws InterruptedException{
        while (hs != hora) {
            this.wait();
        }
    }

    public synchronized int getHora(){
        return hs;
    }

    public synchronized void incrementarHora(){
        this.hs = this.hs % 24 + 1;
        System.out.println("HORA: "+this.hs);
        this.notifyAll();
    }

}
