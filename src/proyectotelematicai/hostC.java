/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectotelematicai;

import Modelo.Host;

/**
 *
 * @author willi
 */
public class hostC {
    public static void main(String[] args) {
        Host h = new Host();
        h.setId("C");
        h.setDir_mac("F0:E1:D2:C3:B4:A7");
        Host h2= new Host();
        h2.setDir_mac("F0:E1:D2:C3:B4:A5");
        h.addVecinos(h2);
        Host h3= new Host();
        h3.setDir_mac("F0:E1:D2:C3:B4:A6");
        h.addVecinos(h3);
        
        Cliente c = new Cliente(h);
    }
    
}
