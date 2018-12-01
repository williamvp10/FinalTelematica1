/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectotelematicai;

import Modelo.Host;
import Modelo.HostAnimation;
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
        g.setColor(Color.BLACK);
        //paint images
        for (int i = 0; i < hosts.size(); i++) {
            g.drawString(hosts.get(i).getHost().getId(), hosts.get(i).getX() + 25, hosts.get(i).getY() - 10);
            g.drawImage(hosts.get(i).getR(), hosts.get(i).getX(), hosts.get(i).getY(), this);
        }
        ArrayList<enlace> enlaces = this.s.getEnlaces();
       
        for (int i = 0; i < enlaces.size(); i++) {
            HostAnimation h1=this.hosts.get(find_host(enlaces.get(i).getHost1()));
            HostAnimation h2=this.hosts.get(find_host(enlaces.get(i).getHost2()));
             g.setColor(Color.BLACK);
            g.drawLine(h1.getX()+25, h1.getY()+25, h2.getX()+25, h2.getY()+25);
             g.setColor(Color.RED);
            g.drawString(" "+enlaces.get(i).getTimestamp(),(h1.getX()+25+h2.getX()+25)/2, (h1.getY()+25+h2.getY()+25)/2);
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
//        g.setColor(Color.RED);
//        for (int i = 0; i < routers.size(); i++) {
//            g.drawString(routers.get(i).getTablaCon(), routers.get(i).getXt(), routers.get(i).getYt());
//            g.drawString(routers.get(i).getTablaCos(), routers.get(i).getXt() - 10, routers.get(i).getYt() + 10);
//        }

        //paint Mensaje
//        g.setColor(Color.BLUE);
//        for (int i = 0; i < routers.size(); i++) {
//            ArrayList<Mensaje> msg = routers.get(i).getMensajes();
//            for (int j = 0; j < msg.size(); j++) {
//                if (msg.get(j).isActivo()) {
//                    g.drawString(msg.get(j).getMsg(), msg.get(j).getX(), msg.get(j).getY());
//                    if (msg.get(j).getX() < msg.get(j).getX2()) {
//                        msg.get(j).setX(msg.get(j).getX() + 1);
//                    } else if (msg.get(j).getX() > msg.get(j).getX2()) {
//                        msg.get(j).setX(msg.get(j).getX() - 1);
//                    }
//                    if (msg.get(j).getY() < msg.get(j).getY2()) {
//                        msg.get(j).setY(msg.get(j).getY() + 1);
//                    } else if (msg.get(j).getY() > msg.get(j).getY2()) {
//                        msg.get(j).setY(msg.get(j).getY() - 1);
//                    }
//                    if (msg.get(j).getX() == msg.get(j).getX2() && msg.get(j).getY() == msg.get(j).getY2()) {
//                        msg.get(j).setActivo(false);
//                        if (msg.get(j).getMsg().split(":")[1].equals("hello")) {
//                            for (int k = 0; k < routers.size(); k++) {
//                                if (routers.get(k).getX() == msg.get(j).getX() && routers.get(k).getY() == msg.get(j).getY()) {
//                                    this.s.validarTabla(routers.get(k), routers.get(i));
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
        
        Toolkit.getDefaultToolkit().sync();
    }
    
    public void addHostAnimation(HostAnimation r) {
        this.hosts.add(r);
    }
    
    public void addMensaje(String id, String msg, int x2, int y2) {
        for (int i = 0; i < this.hosts.size(); i++) {
            if (this.hosts.get(i).getHost().getId().equals(id)) {
                this.hosts.get(i).addMensaje(msg, x2, y2);
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
    
    
    public int find_host(Host h){
        int var=-1;
        for (int i = 0; i < this.hosts.size(); i++) {
            if(h.getDir_mac().equals(this.hosts.get(i).getHost().getDir_mac())){
                var=i;
                break;
            }
        }
        return var;
    }
    
}
