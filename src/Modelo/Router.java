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
public class Router {

    private String name;
    private String direccion;
    private String mascara;

    private ArrayList<String> conecciones;
    private ArrayList<Integer> costos;
    private ArrayList<ArrayList<String>> tablaEnrrutamiento;

    public Router() {
        this.name = "";
        this.direccion = "";
        this.mascara = "";
        this.conecciones = new ArrayList<>();
        this.costos = new ArrayList<>();
        this.tablaEnrrutamiento = new ArrayList<>();
    }

    public Router(String name, String direccion, String mascara, ArrayList<String> conecciones, ArrayList<Integer> costos) {
        this.tablaEnrrutamiento = new ArrayList<>();
        this.name = name;
        this.direccion = direccion;
        this.mascara = mascara;
        this.conecciones = conecciones;
        this.costos = costos;
        this.tablaEnrrutamiento = new ArrayList<>();
        iniciarTabla();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMascara() {
        return mascara;
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

    public void setCenecciones(ArrayList<String> cenecciones) {
        this.conecciones = cenecciones;
    }

    public ArrayList<Integer> getCostos() {
        return costos;
    }

    public void setCostos(ArrayList<Integer> costos) {
        this.costos = costos;
    }

    public ArrayList<String> getConecciones() {
        return conecciones;
    }

    public void setConecciones(ArrayList<String> conecciones) {
        this.conecciones = conecciones;
    }

    public ArrayList<ArrayList<String>> getTablaEnrrutamiento() {
        return tablaEnrrutamiento;
    }

    public void setTablaEnrrutamiento(ArrayList<ArrayList<String>> tablaEnrrutamiento) {
        this.tablaEnrrutamiento = tablaEnrrutamiento;
    }

    public void iniciarTabla() {
        this.tablaEnrrutamiento.add(this.conecciones);
        ArrayList<String> c = new ArrayList<>();
        for (int i = 0; i < costos.size(); i++) {
            c.add("" + costos.get(i));
        }
        this.tablaEnrrutamiento.add(c);
    }

    public void addTabla(String name, String value) {
        this.tablaEnrrutamiento.get(0).add(name);
        this.tablaEnrrutamiento.get(1).add(value);
    }

    public void updateinfoTabla(int i, String name, String value) {
        this.tablaEnrrutamiento.get(0).set(i, name);
        this.tablaEnrrutamiento.get(1).set(i, value);
    }

    //calcular mejor ruta 
    public boolean updateTabla(Router r) {
        
        boolean modificacion=false;
        ArrayList<ArrayList<String>> tabla2 = r.getTablaEnrrutamiento();
        for (int i = 0; i < tablaEnrrutamiento.get(0).size(); i++) {
            for (int j = 0; j < tabla2.get(0).size(); j++) {
                if(!findRout(tabla2.get(0).get(j)) && !tabla2.get(0).get(j).equals(this.name) ){
                    this.tablaEnrrutamiento.get(0).add(tabla2.get(0).get(j));
                    int value = valConection(r.getName());
                    int value2 = Integer.parseInt(tabla2.get(1).get(j));
                    this.tablaEnrrutamiento.get(1).add(""+(value+value2));
                    System.out.println(tablaEnrrutamiento.get(0).toString());
                    System.out.println(tablaEnrrutamiento.get(1).toString());
                    modificacion=true;
                }else if (tablaEnrrutamiento.get(0).get(i).equals(tabla2.get(0).get(j))&& !tabla2.get(0).get(j).equals(this.name)) {
                    int value = Integer.parseInt(tablaEnrrutamiento.get(1).get(i));
                    int value2 = Integer.parseInt(tabla2.get(1).get(j));
                    int value3 = value2+valConection(r.getName());
                    if(value3<value){
                        tablaEnrrutamiento.get(1).set(i, ""+value3);
                        System.out.println(tablaEnrrutamiento.get(0).toString());
                        System.out.println(tablaEnrrutamiento.get(1).toString());
                        modificacion=true;
                    }
                }
            }
        }
        return modificacion;
    }
    
    public int valConection(String name){
        int val=0;
        for (int i = 0; i < this.costos.size(); i++) {
            if(this.conecciones.get(i).equals(name)){
               val=this.costos.get(i);
               break;
            }
        }
        return val;
    }

    public boolean findRout(String n){
        boolean val=false;
        for (int i = 0; i < this.tablaEnrrutamiento.get(0).size(); i++) {
            if(n.equals(this.tablaEnrrutamiento.get(0).get(i))){
                val=true;
            }
        }
        return val;
    }
}
