/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectotelematicai;

import java.io.IOException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author willi
 */
public class Task extends TimerTask {
    private Cliente cliente;
    
    public Task(Cliente r){
        this.cliente=r;
    }
    
    @Override
    public void run() {
        System.out.println("enlacevivio");
        this.cliente.enviarEnlaceVivo();
        this.cliente.addtask(new Task(this.cliente), 7000);
    }

}
