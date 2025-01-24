package TP_FINAL;

public class Hora extends Thread{
    private int hs;

    public Hora (){
        hs = 0;
    }

    public synchronized int getHora(){
        return hs;
    }

    @Override
    public void run(){
        while(true){
            try {
                synchronized (this) {
                    this.hs = this.hs % 24 + 1;
                    System.out.println("HORA: "+this.hs);
                    this.notifyAll();
                }
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }


}
