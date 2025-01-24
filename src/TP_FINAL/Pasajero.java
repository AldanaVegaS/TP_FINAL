package TP_FINAL;

public class Pasajero extends Thread{
    private final String nombre;
    private final Vuelo pasaje;
    private final Aeropuerto aeropuerto;
    private PuestoAtencion puestoAtencion;
    private Terminal terminal;
    private String puesto;
    private boolean atendidoEnPuestoAtencion = false;
    private boolean bajoEnTerminal = false;
    private final Hora hs;

    public Pasajero(String nom, Vuelo pas, Aeropuerto aero,Hora hora){
        this.nombre = nom;
        this.pasaje = pas;
        this.aeropuerto = aero;
        this.hs = hora;
    }

    public Vuelo getPasaje(){
        return pasaje;
    }
    public String getNombre(){
        return nombre;
    }

    public Vuelo getVuelo(){
        return pasaje;
    }

    public String getPuesto(){
        return puestoAtencion.getAerolinea();
    }

    public void setPuestoAtencion(PuestoAtencion puesto){
        puestoAtencion = puesto;
    }

    public void setTerminalYPuesto(Terminal term, String puesto){
        terminal=term;
        this.puesto=puesto;
    }

    public Terminal getTerminal(){
        return terminal;
    }

    public String getPuestoEmbarque(){
        return puesto;
    }

    
    public synchronized void marcarAtendido() {
        this.atendidoEnPuestoAtencion = true;
        notify(); // Notifica si otro hilo está esperando.
    }

    public synchronized void esperarAtencion() throws InterruptedException {
        while (!atendidoEnPuestoAtencion) {
            wait(); // Espera a ser notificado de que ha sido atendido.
        }
    }

    public synchronized void bajoEnTerminal(){
        this.bajoEnTerminal = true;
        notify();
    }

    public synchronized void esperarViaje() throws InterruptedException {
        while (!bajoEnTerminal) {
            wait(); 
        }
    }
   
    public void ingresoATerminal(Terminal terminal) throws InterruptedException{
        int horaActual = hs.getHora();
        int tiempoRestante = pasaje.getHoraSalida() - horaActual;
        if (tiempoRestante > 2 && hs.getHora()>6 && hs.getHora()<22) { 
            System.out.println("\t\t\t\t"+Colores.RED+Colores.NEGRITA+"TERMINAL "+terminal.getNombre()+Colores.RESET+" ---> "+nombre + " decide ir al free-shop.");
            terminal.getFreeShop().ingresar(this);
        } else {
            System.out.println("\t\t\t\t"+Colores.RED+Colores.NEGRITA+"TERMINAL "+terminal.getNombre()+Colores.RESET+" ---> "+nombre + " va directamente a la sala de embarque.");
        }
        terminal.getSala().esperarVuelo(this);
    }

    public void finalizar(){
        Thread.currentThread().interrupt();
    }


    @Override
    public void run(){    
        System.out.println(Colores.PURPLE+Colores.NEGRITA+"AEROPUERTO "+Colores.RESET+"---> Ingresa pasajero: "+nombre+", pasaje: "+pasaje.toString());
        try {
            //Es atendido por el puesto de informe y derivado al puesto de atencion correspondiente
            aeropuerto.atencionPuestoInfo(this);

            //Es atendido en el puesto de atencion
            puestoAtencion.ingresarPasajero(this);

            //Sube al people mover
            this.esperarAtencion();
            aeropuerto.subirAlPeopleMover(this);

            //pasajero en la terminal
            this.esperarViaje();
            this.ingresoATerminal(terminal);

        } catch (InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        
        
    }

    
}
