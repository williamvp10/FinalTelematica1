/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author willi
 */
public class Conecciones {
    private Host host;
    private int timestamp;
    private Host next;

    public Conecciones(Host host, int timestamp, Host next) {
        this.host = host;
        this.timestamp = timestamp;
        this.next = next;
    }
    
    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public Host getNext() {
        return next;
    }

    public void setNext(Host next) {
        this.next = next;
    }

    
}
