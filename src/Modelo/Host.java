/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.ArrayList;

/**
 *
 * @author willi
 */
public class Host {

    private String id;
    private String dir_mac;
    private ArrayList<Host> vecinos;
    private ArrayList<Conecciones> conecciones;

    public Host() {
        id = "";
        dir_mac = "";
        this.vecinos = new ArrayList<>();
        this.conecciones = new ArrayList<>();
    }

    public Host(String id, String dir_mac) {
        this.id = id;
        this.dir_mac = dir_mac;
        this.vecinos = new ArrayList<>();
        this.conecciones = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDir_mac() {
        return dir_mac;
    }

    public void setDir_mac(String dir_mac) {
        this.dir_mac = dir_mac;
    }

    public ArrayList<Host> getVecinos() {
        return vecinos;
    }

    public void setVecinos(ArrayList<Host> vecinos) {
        this.vecinos = vecinos;
    }

    public boolean addVecinos(Host h) {
        boolean var = false;
        if (buscarVecino(h.getDir_mac()) == -1) {
            this.vecinos.add(h);
            var = true;
        }
        return var;
    }

    public boolean deleteVecinos(String dir) {
        boolean var = false;
        if (buscarVecino(dir) == -1) {
            this.vecinos.remove(buscarVecino(dir));
            var = true;
        }
        return var;
    }

    public int buscarVecino(String dir) {
        int var = -1;
        for (int i = 0; i < this.vecinos.size(); i++) {
            if (this.vecinos.get(i).getDir_mac().equals(dir)) {
                var = i;
                break;
            }
        }
        return var;
    }

    public ArrayList<Conecciones> getConecciones() {
        return conecciones;
    }

    public void addConecciones(Conecciones c) {
        boolean var=false;
        for (int i = 0; i < conecciones.size(); i++) {
            if(c.getHost().getDir_mac().equals(this.conecciones.get(i).getHost().getDir_mac())){
                var=true;
                break;
            }
        }
        if(!var){
        this.conecciones.add(c);
        }
    }

    public void setConecciones(ArrayList<Conecciones> c) {
        this.conecciones = c;
    }

    public boolean updateTabla(Host b) {
        boolean var = false;
        for (int i = 0; i < conecciones.size(); i++) {

        }
        return var;
    }

    public Conecciones buscarConeccion(Host a) {
        for (int i = 0; i < this.conecciones.size(); i++) {
            if (a.getDir_mac().equals(this.conecciones.get(i).getHost().getDir_mac())) {
                return this.conecciones.get(i);
            }
        }
        return null;
    }

    public boolean updateConeccion(Host a, Host next, int time) {
        for (int i = 0; i < this.conecciones.size(); i++) {
            if (a.getDir_mac().equals(this.conecciones.get(i).getHost().getDir_mac())) {
                this.conecciones.get(i).setNext(next);
                this.conecciones.get(i).setTimestamp(time);
                return true;
            }
        }
        return false;
    }
    public boolean deleteConeccion(Host a) {
        for (int i = 0; i < this.conecciones.size(); i++) {
            if (a.getDir_mac().equals(this.conecciones.get(i).getHost().getDir_mac())) {
                this.conecciones.remove(i);
                return true;
            }
        }
        return false;
    }
    

    public ArrayList<String> valoresConecciones() {
        ArrayList<String> val = new ArrayList<>();
        for (int i = 0; i < this.conecciones.size(); i++) {
            String a = "" + this.conecciones.get(i).getHost().getDir_mac();
            a += " |  " + this.conecciones.get(i).getTimestamp();
            a += "  | " + this.conecciones.get(i).getNext().getDir_mac();
            val.add(a);
        }
        
        return val;
    }

}
