package TP_FINAL;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    public static PuestoAtencion puestosAtencion[]= new PuestoAtencion[3];
    public static PeopleMover mover;
    public static Terminal terminales[] = new Terminal[3];
    public static Aeropuerto aeropuerto;
    public static int cantPuestosA = 0;
    public static int cantTerminales = 0;
    public static int cantVuelos = 0;
    public static Vuelo vuelos[] = new Vuelo[15];
    public static int cantPasajeros = 15; //Cantidad maxima de hilos pasajeros que se pueden crear juntos 
    private static final Random random = new Random();
    private static int contadorPasajeros = 1;
    private static final Hora hs = new Hora();
    private static Reloj reloj;
    private static ConductorTren conductorTren;
    private static ScheduledThreadPoolExecutor scheduler = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);

    public static void main (String []args) throws InterruptedException{
        cartelAeropuerto();

        cargarDatos();

        aeropuerto = new Aeropuerto(puestosAtencion,mover,terminales,vuelos);

        reloj = new Reloj(hs,aeropuerto);        
        reloj.start();
        conductorTren = new ConductorTren(mover);
        conductorTren.start();
        Guardia guardia = new Guardia(puestosAtencion, hs, aeropuerto);
        guardia.start();
        //System.out.println(aeropuerto.toString());

        Runnable task = () -> {
            int pasajeros = (int) (Math.random() * (cantPasajeros - 0 + 1) + 1); 
                for (int i = 1; i <= pasajeros; i++) {
                    // Filtrar vuelos que no hayan salido aún
                    List<Vuelo> vuelosDisponibles;
                    if (hs.getHora() < 21) {
                        vuelosDisponibles = Arrays.stream(vuelos)
                            .filter(v -> v.getHoraSalida() > hs.getHora()+3) // Solo vuelos que salen en las próximas 3 horas
                            .toList();   
                    }else{
                        vuelosDisponibles = Arrays.stream(vuelos).toList();
                    }
                    

                    if (!vuelosDisponibles.isEmpty()) { 
                        int index = (int) (Math.random() * vuelosDisponibles.size()); // Elegir un vuelo aleatorio entre los disponibles
                        Vuelo vueloAsignado = vuelosDisponibles.get(index);
                        
                        Pasajero pasajero = new Pasajero(("Pasajero " + contadorPasajeros), vueloAsignado, aeropuerto, hs);
                        pasajero.start();
                        contadorPasajeros++;
                    } else {
                        System.out.println("No hay vuelos disponibles para asignar a los nuevos pasajeros.");
                    }
                }
            
        };
        int initialDelay = 0;
        int period = random.nextInt(3000) + 1000;
        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    public static void cargarDatos(){
        //Carga los datos del archivo de texto en las estructuras
        try (Scanner input = new Scanner(new File("src\\TP_FINAL\\Datos.txt"))) {
            while (input.hasNextLine()) {
                String line = input.nextLine();
                cargarDatosAux(line);
            }
        } catch (IOException ex) {
            System.err.println("Error al leer el archivo: " + ex.getMessage());
        }
    }

    private static void cargarDatosAux(String line){
        switch(line.charAt(0)){
            case '1': cargaPuestoAtencion(line);
                      break;
            case '2': cargaPeopleMover(line);
                      break;
            case '3': cargaTerminal(line);
                      break;
            case '4': cargaVuelo(line);
                      break;
        }
    }

    private static void cargaPuestoAtencion(String line){
        PuestoAtencion puesto;
        String aeropuertoName;
        int max;

        line=line.substring(2);
        StringTokenizer dato = new StringTokenizer(line,",");

        aeropuertoName = dato.nextToken();
        max = Integer.parseInt(dato.nextToken());

        puesto = new PuestoAtencion(aeropuertoName, max);
        puestosAtencion[cantPuestosA]=puesto;
        cantPuestosA++;
    }

    private static void cargaPeopleMover(String line){
        int max;

        line=line.substring(2);
        StringTokenizer dato = new StringTokenizer(line,",");
        
        max = Integer.parseInt(dato.nextToken());
        mover = new PeopleMover(max);
    }

    private static void cargaTerminal(String line){
        Terminal terminal;
        String nombre, nombreFreeshop, nombreSala;
        int maxFreeshop, cantPuestosEmb;

        line=line.substring(2);
        StringTokenizer dato = new StringTokenizer(line,",");

        nombre=dato.nextToken();
        nombreFreeshop=dato.nextToken();
        maxFreeshop=Integer.parseInt(dato.nextToken());
        nombreSala=dato.nextToken();
        cantPuestosEmb=Integer.parseInt(dato.nextToken());

        FreeShop fshop = new FreeShop(nombreFreeshop, maxFreeshop);
        SalaEmbarque sala = new SalaEmbarque(hs, nombreSala);
        terminal = new Terminal(nombre, fshop, sala, cantPuestosEmb);
        terminales[cantTerminales]=terminal;
        cantTerminales++;
    }  

    private static void cargaVuelo(String line){
        String id, nombreAerolinea;
        int hora;
        line = line.substring(2);
        StringTokenizer dato = new StringTokenizer(line,",");
        id = dato.nextToken();
        nombreAerolinea = dato.nextToken();
        hora = Integer.parseInt(dato.nextToken());
        Vuelo v = new Vuelo(id,nombreAerolinea,hora);
        vuelos[cantVuelos]=v;
        cantVuelos++;
    }

    private static void cartelAeropuerto(){
        String borde = "x".repeat(70);
        String espacioLateral = "x" + " ".repeat(68) + "x";
        String textoCentrado = centrarTexto("AEROPUERTO", 68);

        System.out.println(Colores.PURPLE+borde); // Parte superior del cartel
        System.out.println(espacioLateral); // Línea vacía
        System.out.println(espacioLateral); // Línea vacía
        System.out.println("x" + textoCentrado + "x"); // Texto centrado
        System.out.println(espacioLateral); // Línea vacía
        System.out.println(espacioLateral); // Línea vacía
        System.out.println(borde+Colores.RESET); // Parte inferior del cartel
        System.out.println("\n");
    }
    
    private static String centrarTexto(String texto, int ancho) {
        int espaciosIzquierda = (ancho - texto.length()) / 2;
        int espaciosDerecha = ancho - texto.length() - espaciosIzquierda;
        return " ".repeat(espaciosIzquierda) + texto + " ".repeat(espaciosDerecha);
    }
}


