package TP_FINAL;

import TP_FINAL.AsignadorDeVuelos.Tupla;

public class Pasajero extends Thread{
    private final String nombre;
    private final Vuelo pasaje;
    private final Aeropuerto aeropuerto;
    private Tupla terminalPuesto;
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

    public boolean esSuTerminal(String terminal){
        return terminalPuesto.getTerminal().getNombre().equals(terminal);
    }

    public String getTerminal(){
        return terminalPuesto.getTerminal().getNombre();
    }

    public void ingresoATerminal(Terminal terminal) throws InterruptedException{
        int horaActual = hs.getHora();
        int tiempoRestante = pasaje.getHoraSalida() - horaActual;
        boolean ingreso = Math.random() < 0.5;//Determina de manera aleatoria si el pasajero se dirige al free-shop o no
        Thread.sleep(50); // Simular tiempo de decidir
        if (ingreso && tiempoRestante > 2) { 
            System.out.println("\t\t\t\t"+Colores.RED+Colores.NEGRITA+"TERMINAL "+terminal.getNombre()+Colores.RESET+" ---> "+nombre + " decide ir al free-shop.");
            terminal.getFreeShop().ingresar(this);
        } else {
            System.out.println("\t\t\t\t"+Colores.RED+Colores.NEGRITA+"TERMINAL "+terminal.getNombre()+Colores.RESET+" ---> "+nombre + " va directamente a la sala de embarque.");
        }

        //Esperar el tiempo restante hasta la salida del vuelo
        System.out.println("\t\t\t\t\t"+Colores.WHITE+Colores.NEGRITA+"SALA DE EMBARQUE"+Colores.RESET+" ---> " + getNombre() + " espera en la sala de embarque para el vuelo " + getVuelo().getIdVuelo() + ".");
        hs.esperarHora(pasaje.getHoraSalida());
        System.out.println("\t\t\t\t\t"+Colores.WHITE+Colores.NEGRITA+"SALA DE EMBARQUE"+Colores.RESET+" ---> " + getNombre() + " embarca en el vuelo " + getVuelo().getIdVuelo() + ".");
    }

    @Override
    public void run(){    
        System.out.println(Colores.PURPLE+Colores.NEGRITA+"AEROPUERTO "+Colores.RESET+"---> Ingresa pasajero: "+nombre+", pasaje: "+pasaje.toString());
        try {
            //Es atendido por el puesto de informe y derivado al puesto de atencion correspondiente
            PuestoAtencion puestoAtencion = aeropuerto.atencionPuestoInfo(this);

            //Es atendido en el puesto de atención
            aeropuerto.ingresarPuestoAtencion(this, puestoAtencion);
            terminalPuesto = puestoAtencion.hacerCheckIn(this);
            aeropuerto.salirPuestoAtencion(this,puestoAtencion);

            //Espera al people mover para ser trasladado a su terminal
            aeropuerto.getPeopleMover().subirPasajero(this);
            aeropuerto.getPeopleMover().bajarPasajero(this);

            //Se dirige al freeshop o a espera su vuelo
            ingresoATerminal(terminalPuesto.getTerminal());

        } catch (InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    
}
