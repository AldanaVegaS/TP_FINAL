package TP_FINAL;
public class Hora{
    private int hs;

    public Hora (){
        hs = 0;
    }

    public synchronized int getHora(){
        return hs;
    }

    public synchronized  void incrementarHora(){
        this.hs = this.hs % 24 + 1;
        System.out.println("HORA: "+this.hs);        
        this.notifyAll();
    }

    public synchronized void esperar(int hora) throws InterruptedException{
        while(hs != hora){
            this.wait();
        }
    }
}