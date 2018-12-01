/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectotelematicai;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author willi
 */
public class Ventana {

    JFrame frame;

    public Ventana() {
        
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setBackground(Color.white);
    }

    public void start() {
        frame.setVisible(true);
    }
}
