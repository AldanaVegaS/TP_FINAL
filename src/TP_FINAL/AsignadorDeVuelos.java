package TP_FINAL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AsignadorDeVuelos {
    public class Tupla {
        private Terminal terminal;
        private String puesto;

        public Terminal getTerminal() {
            return terminal;
        }
        public String getPuesto() {
            return puesto;
        }
    }


    private final List<Tupla> tuplas = new ArrayList<>(); // Lista de tuplas que contienen la terminal y el puesto de embarque
    private final HashMap<Vuelo, Tupla> vuelosATerminal = new HashMap<>(); // Mapeo de terminal y puesto de embarque a vuelos

    public AsignadorDeVuelos(Terminal[] terminales,Vuelo[] vuelos) {
        crearTuplas(terminales);
        asignar(vuelos);
    }


    public Tupla getTerminalYPuesto(String codigoVuelo) {
        for (Vuelo vuelo : vuelosATerminal.keySet()) {
            if (vuelo.getIdVuelo().equals(codigoVuelo)) {
                return vuelosATerminal.get(vuelo);
            }
        }
        return null;
    }


    private void crearTuplas(Terminal[] terminales) {
        for (Terminal terminal : terminales) {
            int cantidadPuestos = terminal.getCantPuestos(); // Obtener la cantidad de puestos de embarque
            for (int i = 0; i < cantidadPuestos; i++) {
                Tupla t = new Tupla();
                t.terminal = terminal;
                t.puesto = "Puesto embarque " + (i + 1); // Asignar el nombre del puesto de embarque    
                tuplas.add(t);
            }
        }
    }


    private void asignar(Vuelo[] vuelos) {
       for(Vuelo vuelo : vuelos){
            boolean conflicto; // Bandera para detectar conflictos
            Tupla t;

            do { 
                conflicto = false; // Reiniciar la bandera de conflicto
                t = tuplas.get((int) (Math.random() * tuplas.size()));

                for (Map.Entry<Vuelo, Tupla> entry : vuelosATerminal.entrySet()) {
                    Vuelo vueloExistente = entry.getKey(); // Método que busca el Vuelo por su id
                    Tupla tuplaExistente = entry.getValue();
                        
                    if (vuelo.getHoraSalida()==vueloExistente.getHoraSalida() &&
                        t.getPuesto().equals(tuplaExistente.getPuesto())) {
                        conflicto = true;
                        break;
                    }
                }

            } while (conflicto); // Repetir hasta que no haya conflicto
            
            vuelosATerminal.putIfAbsent(vuelo, t);    

        }
    }


}

