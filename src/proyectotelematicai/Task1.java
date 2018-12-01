/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectotelematicai;

import Modelo.enlace;
import java.io.IOException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author willi
 */
public class Task1 extends TimerTask {
    private Servidor s;
    private enlace e;
    
    public Task1(Servidor r, enlace e){
        this.s=r;
        this.e=e;
    }
    
    @Override
    public void run() {
        System.out.println("desconectar");
        this.s.borrarEnlace(e);
    }

}
