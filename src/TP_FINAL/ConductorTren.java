package TP_FINAL;

public class ConductorTren extends Thread {
    private final PeopleMover tren;

    public ConductorTren(PeopleMover tren) {
        this.tren = tren;
    }

    @Override
    public void run() {
        while (true) { 
            try {
                tren.salirDeLaEstacion();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt(); // Restore interrupted status
            }
        }
    }
    
}
