/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectotelematicai;

import Modelo.Conecciones;
import Modelo.Host;
import Modelo.HostAnimation;
import Modelo.Router;
import Modelo.enlace;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author willi
 */
public final class Servidor {

    private ServerSocket echoServer;
    private ArrayList<Host> hosts;
    private ArrayList<enlace> enlaces;
    private Ventana v;
    private animation animacion;

    public Servidor() {

        this.hosts = new ArrayList<>();
        this.enlaces = new ArrayList<>();
        v = new Ventana();
        echoServer = null;
        try {
            echoServer = new ServerSocket(9999);
        } catch (IOException e) {
            System.out.println(e);
        }
// Create a socket object from the ServerSocket to listen and accept 
// connections.
// Open input and output streams
        System.out.println(" activo ");
        nuevaAnimacion();
        this.v.start();
        try {
            while (true) {

                Socket socket;
                socket = echoServer.accept();
                //System.out.println("Nueva conexión entrante: " + socket);
                ((ServidorHilo) new ServidorHilo(socket)).start();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void agregarEnlace(Host h1, Host h2) {
        if (buscarHost(h1) != -1 && buscarHost(h2) != -1 && !h1.getDir_mac().equals(h2.getDir_mac())) {
            int timestamp = 1;
            if (enlaces.size() != 0) {
                timestamp = enlaces.get(enlaces.size() - 1).getTimestamp() + 2;
            }
            enlace e = new enlace(timestamp, h1, h2);
            if (!ExisteEnlace(e)) {
                this.enlaces.add(e);
                this.enlaces.get(this.enlaces.size() - 1).Start(this);
            }
        }
    }

    public boolean ExisteEnlace(enlace e) {
        boolean existe = false;
        for (int i = 0; i < this.enlaces.size(); i++) {
            if (this.enlaces.get(i).getHost1().getDir_mac().equals(e.getHost1().getDir_mac()) && this.enlaces.get(i).getHost2().getDir_mac().equals(e.getHost2().getDir_mac())
                    || this.enlaces.get(i).getHost1().getDir_mac().equals(e.getHost2().getDir_mac()) && this.enlaces.get(i).getHost2().getDir_mac().equals(e.getHost1().getDir_mac())) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    public void borrarEnlace(enlace e) {
        this.enlaces.remove(e);
    }

    public String validarPeticion(String val) {
        String res = "";
        String[] peticion = val.split(" ");
        if (peticion.length != 0) {
            if (peticion[0].equals("host")) {
                Host host = new Host(peticion[1], peticion[2]);
                for (int i = 3; i < peticion.length; i++) {
                    Host host2 = new Host();
                    host2.setDir_mac(peticion[i]);
                    host.addVecinos(host2);
                }
                res = "HOST conectado";
                this.hosts.add(host);
                for (int i = 0; i < host.getVecinos().size(); i++) {
                    agregarEnlace(host, host.getVecinos().get(i));
                }
                nuevoHost(host);

            } else if (peticion[0].equals("enlacevivo")) {
                Host e = new Host();
                e.setDir_mac(peticion[1]);
                sendEnlaceVivo(e);
            } else if (peticion[0].equals("updatetabla")) {
                // UpdateRouter(peticion[1], peticion[2].split(","), peticion[3].split(","));
            }

        } else {
            System.out.println(" vacio ");
        }
        animacion.revalidate();
        animacion.repaint();
        return res;
    }

    public void sendEnlaceVivo(Host h) {
        System.out.println("---------------enlacevivo");
        int pos = buscarHost(h);
        if (pos != -1) {
            h = this.hosts.get(pos);
            ArrayList<Host> Vecinos = h.getVecinos();
            for (int i = 0; i < Vecinos.size(); i++) {
                animacion.addMensaje(h, this.hosts.get(pos).getId() + ":enlace vivo", Vecinos.get(i));
            }
            for (int i = 0; i < enlaces.size(); i++) {
                if (this.enlaces.get(i).getHost1().getDir_mac().equals(h.getDir_mac())
                        || this.enlaces.get(i).getHost2().getDir_mac().equals(h.getDir_mac())) {
                    this.enlaces.get(i).validar(h);
                }
            }
        }
    }

    public void nuevaAnimacion() {
        this.animacion = new animation(this);
        this.animacion.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.animacion.setBounds(0, 0, 500, 500);
        this.animacion.setLayout(null);
        v.frame.add(this.animacion);
    }

    public void nuevoHost(Host r) {
        int x = 0, y = 0;
        if (this.hosts.size() == 1) {
            x = 300;
            y = 100;
        } else if (this.hosts.size() == 2) {
            x = 100;
            y = 350;
        } else {
            x = 500;
            y = 350;
        }
        HostAnimation r2 = new HostAnimation(r, x, y);
        this.animacion.addHostAnimation(r2);
        animacion.revalidate();
        animacion.repaint();
    }

    public void enviarReps(HostAnimation a, HostAnimation b) {
        int posb = buscarHost(b.getHost());
        int posa = buscarHost(a.getHost());
        if (posb != -1 && posa != -1) {
            animacion.addMensaje(this.hosts.get(posa), this.hosts.get(posa).getId() + ":resp enlace vivo", this.hosts.get(posb));
        }
        animacion.revalidate();
        animacion.repaint();
    }

    public void validarTablaInfoEnlace(HostAnimation a, HostAnimation b) {
        boolean modificacion = false;
        int posb = buscarHost(b.getHost());
        int posa = buscarHost(a.getHost());
        if (posb != -1 && posa != -1) {;
            //enlace
            for (int i = 0; i < this.enlaces.size(); i++) {
                Conecciones c = null;
                if (this.hosts.get(posa).getDir_mac().equals(this.enlaces.get(i).getHost1().getDir_mac())) {
                    c = this.hosts.get(posa).buscarConeccion(this.enlaces.get(i).getHost2());
                    System.out.println(c);
                    if (c == null) {
                        c = new Conecciones(this.enlaces.get(i).getHost2(), this.enlaces.get(i).getTimestamp(), this.enlaces.get(i).getHost2());
                        this.hosts.get(posa).addConecciones(c);
                        this.animacion.addConeccion(this.hosts.get(posa), c);
                        modificacion = true;
                    } else {
                        if (c.getTimestamp() > this.enlaces.get(i).getTimestamp()) {
                            this.hosts.get(posa).updateConeccion(this.enlaces.get(i).getHost2(), this.enlaces.get(i).getHost2(), this.enlaces.get(i).getTimestamp());
                            this.animacion.updateConeccion(this.hosts.get(posa), this.enlaces.get(i).getHost2(), this.enlaces.get(i).getHost2(), this.enlaces.get(i).getTimestamp());
                            modificacion = true;
                        }
                    }
                } else if (this.hosts.get(posa).getDir_mac().equals(this.enlaces.get(i).getHost2().getDir_mac())) {
                    c = this.hosts.get(posa).buscarConeccion(this.enlaces.get(i).getHost1());
                    System.out.println(c);
                    if (c == null) {
                        c = new Conecciones(this.enlaces.get(i).getHost1(), this.enlaces.get(i).getTimestamp(), this.enlaces.get(i).getHost1());
                        this.hosts.get(posa).addConecciones(c);
                        this.animacion.addConeccion(this.hosts.get(posa), c);
                        modificacion = true;
                    } else {
                        if (c.getTimestamp() > this.enlaces.get(i).getTimestamp()) {
                            this.hosts.get(posa).updateConeccion(this.enlaces.get(i).getHost1(), this.enlaces.get(i).getHost1(), this.enlaces.get(i).getTimestamp());
                            this.animacion.updateConeccion(this.hosts.get(posa), this.enlaces.get(i).getHost1(), this.enlaces.get(i).getHost1(), this.enlaces.get(i).getTimestamp());
                            modificacion = true;
                        }
                    }
                }
            }

            //enviar mensaje de update a sus vecinos excepto del que llego la confirmacion de existecia
            if (modificacion) {
                animacion.addMensaje(this.hosts.get(posa), this.hosts.get(posa).getId() + ":info vecinos", this.hosts.get(posb));
            }
        } else {
            System.out.println(" error");
        }
        animacion.revalidate();
        animacion.repaint();
    }

    public void validarTablaInfoVecino(HostAnimation a, HostAnimation b) {
        boolean modificacion = false;
        int posb = buscarHost(b.getHost());
        int posa = buscarHost(a.getHost());
        ArrayList<Conecciones> conec = a.getHost().getConecciones();
        if (posb != -1 && posa != -1) {
            // nuevas rutas con respecto al otro host
            ArrayList<Conecciones> conb = b.getHost().getConecciones();
            for (int i = 0; i < conb.size(); i++) {
                Conecciones c = null;
                if (!this.hosts.get(posa).getDir_mac().equals(conb.get(i).getHost().getDir_mac())
                        && !this.hosts.get(posa).getDir_mac().equals(conb.get(i).getNext().getDir_mac())) {
                    c = this.hosts.get(posa).buscarConeccion(conb.get(i).getHost());
                    if (c == null) {
                        //valor del enlace + el valor de la ruta de b
                        int time = valEnlace(this.hosts.get(posa), b.getHost()) + conb.get(i).getTimestamp();
                        c = new Conecciones(conb.get(i).getHost(), time, b.getHost());
                        this.hosts.get(posa).addConecciones(c);
                        this.animacion.addConeccion(this.hosts.get(posa), c);
                        modificacion = true;
                    } else {
                        //valor del enlace + el valor de la ruta de b
                        int time = valEnlace(this.hosts.get(posa), b.getHost()) + conb.get(i).getTimestamp();
                        if (time < c.getTimestamp()) {
                            this.hosts.get(posa).updateConeccion(c.getHost(), b.getHost(), time);
                            this.animacion.updateConeccion(this.hosts.get(posa), c.getHost(), b.getHost(), time);
                            modificacion = true;
                        }
                    }
                }
            }
            //enviar mensaje de update a sus vecinos excepto del que llego la confirmacion de existecia
            if (modificacion) {
                ArrayList<Host> Vecinos = this.hosts.get(posa).getVecinos();
                for (int i = 0; i < Vecinos.size(); i++) {
                    animacion.addMensaje(this.hosts.get(posa), this.hosts.get(posa).getId() + ":update", Vecinos.get(i));
                }
            }
        } else {
            System.out.println(" error");
        }

        animacion.revalidate();
        animacion.repaint();
    }

    public ArrayList<Host> getHosts() {
        return hosts;
    }

    public ArrayList<enlace> getEnlaces() {
        return enlaces;
    }

    public int buscarHost(Host h) {
        int var = -1;
        for (int i = 0; i < this.hosts.size(); i++) {
            System.out.println("" + this.hosts.get(i).getDir_mac().trim());
            if (this.hosts.get(i).getDir_mac().trim().equals(h.getDir_mac().trim())) {
                var = i;
                break;
            }
        }
        return var;
    }

    public int valEnlace(Host a, Host b) {
        int val = 0;
        for (int i = 0; i < this.enlaces.size(); i++) {
            if (this.enlaces.get(i).getHost1().getDir_mac().equals(a.getDir_mac()) && this.enlaces.get(i).getHost2().getDir_mac().equals(b.getDir_mac())
                    || this.enlaces.get(i).getHost1().getDir_mac().equals(b.getDir_mac()) && this.enlaces.get(i).getHost2().getDir_mac().equals(a.getDir_mac())) {
                val = this.enlaces.get(i).getTimestamp();
                break;
            }
        }
        return val;
    }

    /*main*/
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
    }


    /*
    clase hilo servidor 
    
     */
    public class ServidorHilo extends Thread {

        private Socket socket;
        private DataOutputStream dos;
        private DataInputStream dis;

        public ServidorHilo(Socket socket) {
            this.socket = socket;
            try {
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void desconnectar() {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            String accion = "";
            try {
                accion = dis.readUTF();
                System.out.println("cliente: " + accion);
                String response = validarPeticion(accion);
                System.out.println("res: " + response);
                dos.writeUTF(response);
//            accion = dis.readUTF();
//            if(accion.equals("hola")){
//                System.out.println("El cliente con idSesion "+this.idSessio+" saluda");
//                dos.writeUTF("adios");
//            }
            } catch (IOException ex) {
                Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
            }
            //desconnectar();
        }

    }

}
