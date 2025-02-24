package TP_FINAL;

public class Pasajero extends Thread{
    private final String nombre;
    private final Vuelo pasaje;
    private final Aeropuerto aeropuerto;
    private PuestoAtencion puestoAtencion;
    private Terminal terminal;
    private String puesto;
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

    public PuestoAtencion getPuesto(){
        return puestoAtencion;
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
   
    public void ingresoATerminal(Terminal terminal) throws InterruptedException{
        int horaActual = hs.getHora();
        int tiempoRestante = pasaje.getHoraSalida() - horaActual;
        boolean ingreso = Math.random() < 0.5;
        if (ingreso && tiempoRestante > 2) { 
            System.out.println("\t\t\t\t"+Colores.RED+Colores.NEGRITA+"TERMINAL "+terminal.getNombre()+Colores.RESET+" ---> "+nombre + " decide ir al free-shop.");
            terminal.getFreeShop().ingresar(this);
        } else {
            System.out.println("\t\t\t\t"+Colores.RED+Colores.NEGRITA+"TERMINAL "+terminal.getNombre()+Colores.RESET+" ---> "+nombre + " va directamente a la sala de embarque.");
        }
        terminal.getSala().esperarVuelo(this);
        Thread.currentThread().interrupt();
    }

    @Override
    public void run(){    
        System.out.println(Colores.PURPLE+Colores.NEGRITA+"AEROPUERTO "+Colores.RESET+"---> Ingresa pasajero: "+nombre+", pasaje: "+pasaje.toString());
        try {
           //Es atendido por el puesto de informe y derivado al puesto de atencion correspondiente
           aeropuerto.atencionPuestoInfo(this);

           //Es atendido por el puesto de atencion y realiza el check-in
           aeropuerto.esperarTurno(this);
           puestoAtencion.hacerCheckIn(this);
           aeropuerto.salirDelPuesto();

           // Sube al people mover;
           aeropuerto.getPeopleMover().usarTren(this);

           // Ingresa a la terminal
           this.ingresoATerminal(terminal);

        } catch (InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        
        
    }

    
}
