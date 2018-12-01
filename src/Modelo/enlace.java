/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.Timer;
import proyectotelematicai.Servidor;
import proyectotelematicai.Task;
import proyectotelematicai.Task1;

/**
 *
 * @author willi
 */
public class enlace {
    private int timestamp;
    private Servidor s;
    private Host host1;
    private Host host2;
    private Timer timer;
    private int validar;
    public enlace(int timestamp, Host host1, Host host2) {
        this.timer = new Timer();
        this.timestamp = timestamp;
        this.host1 = host1;
        this.host2 = host2;
        this.validar=0;
        this.s=null;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public Host getHost1() {
        return host1;
    }

    public void setHost1(Host host1) {
        this.host1 = host1;
    }

    public Host getHost2() {
        return host2;
    }

    public void setHost2(Host host2) {
        this.host2 = host2;
    }
    
     public void addtask(Task1 task, int time) {
        this.timer.schedule(task, time);
    }

    public void purgeTimer() {
        this.timer = new Timer();
    }

    public void cancelTimer() {
        this.timer.cancel();
    }

    public void Start(Servidor r){
        this.s=r;
        this.addtask(new Task1(r, this), 10000);
    }
    
    public void validar(Host h){
        if(h.getDir_mac().equals(host1.getDir_mac())||h.getDir_mac().equals(host2.getDir_mac())){
            validar++;
        }
        if(validar>=2){
            validar=0;
            cancelTimer();
            purgeTimer();
            Start(s);
        }
    }
    
}
