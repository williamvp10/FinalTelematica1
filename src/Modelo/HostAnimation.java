/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author willi
 */
public class HostAnimation {

    private Host host;
    private ArrayList<Mensaje> mensajes;
    private int x, y, xt, yt;
    private Image r;

    public HostAnimation(Host h, int x, int y) {
      
        this.mensajes = new ArrayList<Mensaje>();
        this.r = new ImageIcon("host.png").getImage();
        this.host = h;
        this.x = x;
        this.y = y;

        if (x < 150) {
            this.xt = x - 80;
            this.yt = y + 20;
        } else {
            this.xt = x + 90;
            this.yt = y + 20;
        }

    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Image getR() {
        return r;
    }

    public void setR(Image r) {
        this.r = r;
    }

    public int getXt() {
        return xt;
    }

    public void setXt(int xt) {
        this.xt = xt;
    }

    public int getYt() {
        return yt;
    }

    public void setYt(int yt) {
        this.yt = yt;
    }
    
    public ArrayList<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(ArrayList<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public void addMensaje(String msg, int x2, int y2) {
        if (x2 != 0 && y2 != 0) {
            Mensaje e = new Mensaje(msg, this.x, x2, this.y, y2);
            this.mensajes.add(e);
        }
    }

}
