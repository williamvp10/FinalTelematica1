/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectotelematicai;

import Modelo.Conecciones;
import Modelo.Host;
import Modelo.HostAnimation;
import Modelo.Mensaje;
import Modelo.enlace;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author willi
 */
public class animation extends JPanel implements ActionListener {

    private Timer timer;
    private final int DELAY = 15;
    ArrayList<HostAnimation> hosts;
    ArrayList<Image> rout;
    Servidor s;

    public animation(Servidor s) {
        this.s = s;
        rout = new ArrayList<Image>();
        hosts = new ArrayList<HostAnimation>();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D h = (Graphics2D) g;

        ArrayList<enlace> enlaces = this.s.getEnlaces();
        for (int i = 0; i < enlaces.size(); i++) {
            HostAnimation h1 = this.hosts.get(find_host(enlaces.get(i).getHost1()));
            HostAnimation h2 = this.hosts.get(find_host(enlaces.get(i).getHost2()));
            g.setColor(Color.BLACK);
            g.drawLine(h1.getX() + 25, h1.getY() + 25, h2.getX() + 25, h2.getY() + 25);
            g.setColor(Color.RED);
            g.drawString(" " + enlaces.get(i).getTimestamp(), (h1.getX() + 25 + h2.getX() + 25) / 2, (h1.getY() + 25 + h2.getY() + 25) / 2);
        }

        g.setColor(Color.BLACK);
        //paint images
        for (int i = 0; i < hosts.size(); i++) {
            g.drawString(hosts.get(i).getHost().getId() + " :" + hosts.get(i).getHost().getDir_mac(), hosts.get(i).getX() + 25, hosts.get(i).getY() - 20);
            g.drawImage(hosts.get(i).getR(), hosts.get(i).getX(), hosts.get(i).getY(), this);
        }

//        for (int i = 0; i < hosts.size(); i++) {
//            Host a = hosts.get(i).getHost();
//            for (int j = 0; j < a.getConecciones().size(); j++) {
//                int x2 = 0, y2 = 0;
//                for (int k = 0; k < routers.size(); k++) {
//                    if (a.getConecciones().get(j).equals(routers.get(k).getRouter().getName())) {
//                        x2 = routers.get(k).getX();
//                        y2 = routers.get(k).getY();
//                        g.drawLine(routers.get(i).getX() + 20, routers.get(i).getY() + 20, x2 + 20, y2 + 20);
//                    }
//                }
//            }
//        }
        //paint TABLES
        g.setColor(Color.RED);
        for (int i = 0; i < hosts.size(); i++) {

            g.drawString("        DESTINO       |VAL|          NEXT", hosts.get(i).getXt() + 10, hosts.get(i).getYt());
            ArrayList<String> valores = hosts.get(i).getHost().valoresConecciones();
            if (valores != null && valores.size() != 0) {
                int valy = hosts.get(i).getYt();
                for (int j = 0; j < valores.size(); j++) {
                    valy += 10;
                    g.drawString(valores.get(j), hosts.get(i).getXt(), valy);

                }
            } else {
                g.drawString("sin datos", hosts.get(i).getXt(), hosts.get(i).getYt() + 10);
            }
        }

        //paint Mensaje
        g.setColor(Color.BLUE);
        for (int i = 0; i < hosts.size(); i++) {
            ArrayList<Mensaje> msg = hosts.get(i).getMensajes();
            for (int j = 0; j < msg.size(); j++) {
                if (msg.get(j).isActivo()) {
                    g.drawString(msg.get(j).getMsg(), msg.get(j).getX(), msg.get(j).getY());
                    if (msg.get(j).getX() < msg.get(j).getX2()) {
                        msg.get(j).setX(msg.get(j).getX() + 1);
                    } else if (msg.get(j).getX() > msg.get(j).getX2()) {
                        msg.get(j).setX(msg.get(j).getX() - 1);
                    }
                    if (msg.get(j).getY() < msg.get(j).getY2()) {
                        msg.get(j).setY(msg.get(j).getY() + 1);
                    } else if (msg.get(j).getY() > msg.get(j).getY2()) {
                        msg.get(j).setY(msg.get(j).getY() - 1);
                    }
                    if (msg.get(j).getX() == msg.get(j).getX2() && msg.get(j).getY() == msg.get(j).getY2()) {
                        msg.get(j).setActivo(false);
                        if (msg.get(j).getMsg().split(":")[1].equals("enlace vivo")) {
                            for (int k = 0; k < hosts.size(); k++) {
                                if (hosts.get(k).getX() == msg.get(j).getX() && hosts.get(k).getY() == msg.get(j).getY()) {
                                    this.s.enviarReps(hosts.get(k), hosts.get(i));
                                    break;
                                }
                            }
                        } else if (msg.get(j).getMsg().split(":")[1].equals("resp enlace vivo")) {
                            for (int k = 0; k < hosts.size(); k++) {
                                if (hosts.get(k).getX() == msg.get(j).getX() && hosts.get(k).getY() == msg.get(j).getY()) {
                                    this.s.validarTablaInfoEnlace(hosts.get(k), hosts.get(i));
                                    this.s.validarTablaInfoVecino(hosts.get(k), hosts.get(i));
                                    
                                    break;
                                }
                            }
                        } else if (msg.get(j).getMsg().split(":")[1].equals("info vecinos")) {
                            for (int k = 0; k < hosts.size(); k++) {
                                if (hosts.get(k).getX() == msg.get(j).getX() && hosts.get(k).getY() == msg.get(j).getY()) {
                                    this.s.validarTablaInfoEnlace(hosts.get(k), hosts.get(i));
                                    this.s.validarTablaInfoVecino(hosts.get(k), hosts.get(i));
                                    break;
                                }
                            }
                        }
                        else if ( msg.get(j).getMsg().split(":")[1].equals("update")) {
                            for (int k = 0; k < hosts.size(); k++) {
                                if (hosts.get(k).getX() == msg.get(j).getX() && hosts.get(k).getY() == msg.get(j).getY()) {
                                    this.s.validarTablaInfoEnlace(hosts.get(k), hosts.get(i));
                                    this.s.validarTablaInfoVecino(hosts.get(k), hosts.get(i));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        //paint mensajes
        g.setColor(Color.BLUE);
        g.drawString("------------------------------------------------------------------------------------------------------------", 0, 550);
        g.drawString("Mensajes", 30, 570);

        g.drawString("H:enlace vivo:  origen-destino-timestamp", 30, 585);
        g.setColor(Color.red);
        g.drawString("H:respuesta enlace vivo:  origen-destino-timestamp", 30, 600);
        g.setColor(Color.BLACK);
        g.drawString("H:actualizacion:  origen-destino-info routing", 30, 615);
        Toolkit.getDefaultToolkit().sync();
    }

    public void addHostAnimation(HostAnimation r) {
        this.hosts.add(r);
    }

    public void addMensaje(Host origen, String msg, Host destino) {
        int orig = find_host(origen);
        int des = find_host(destino);
        if (orig != -1 && des != - 1) {
            this.hosts.get(orig).addMensaje(msg, this.hosts.get(des).getX(), this.hosts.get(des).getY());

        }

    }

    public void addConeccion(Host a, Conecciones c) {
        System.out.println(" entroo");
        for (int i = 0; i < this.hosts.size(); i++) {
            if (this.hosts.get(i).getHost().getDir_mac().equals(a.getDir_mac())) {
                this.hosts.get(i).getHost().addConecciones(c);
                break;
            }
        }
    }

    public void updateConeccion(Host origen, Host a, Host next, int time) {
        for (int i = 0; i < this.hosts.size(); i++) {
            if (this.hosts.get(i).getHost().getDir_mac().equals(origen.getDir_mac())) {
                this.hosts.get(i).getHost().updateConeccion(a, next, time);
                break;
            }
        }
    }

//    public int getPosxRouter(String id) {
//        int posx = 0;
//        for (int i = 0; i < this.routers.size(); i++) {
//            if (this.routers.get(i).getRouter().getName().equals(name)) {
//                posx = this.routers.get(i).getX();
//            }
//        }
//        return posx;
//    }
//    
//    public int getPosyRouter(String name) {
//        int posy = 0;
//        for (int i = 0; i < this.routers.size(); i++) {
//            if (this.routers.get(i).getRouter().getName().equals(name)) {
//                posy = this.routers.get(i).getY();
//            }
//        }
//        return posy;
//    }
//    
//    public void updateRouter(Router r) {
//        for (int i = 0; i < this.routers.size(); i++) {
//            if (this.routers.get(i).getRouter().getName().equals(r.getName())) {
//                this.routers.get(i).setRouter(r);
//                break;
//            }
//        }
//    }
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public int find_host(Host h) {
        int var = -1;
        for (int i = 0; i < this.hosts.size(); i++) {
            if (h.getDir_mac().equals(this.hosts.get(i).getHost().getDir_mac())) {
                var = i;
                break;
            }
        }
        return var;
    }

    public ArrayList<HostAnimation> getHosts() {
        return hosts;
    }

    public void setHosts(ArrayList<HostAnimation> hosts) {
        this.hosts = hosts;
    }

}
