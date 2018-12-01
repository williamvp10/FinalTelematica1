/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectotelematicai;

import Modelo.Host;
import Modelo.Router;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author willi
 */
public class Cliente {
    private Socket smtpSocket;
    private DataOutputStream os;
    private DataInputStream is;
    private Host host;
    private Timer timer;

    public Cliente(Host h) {
        this.host = h;
        this.timer = new Timer();
        // declaration section:
        // smtpClient: our client socket
        // os: output stream
        // is: input stream
        smtpSocket = null;
        os = null;
        is = null;
        connect();
        // If everything has been initialized then we want to write some data
        // to the socket we have opened a connection to on port 25
        addtask(new Task(this), 20000);
        if (smtpSocket != null && os != null && is != null) {
            try {
                //conect router 

                os.writeUTF(enviarinfoHost()+ "\n");
                // keep on reading from/to the socket till we receive the "Ok" from SMTP,
                // once we received that then we want to break.
                String responseLine;
                while ((responseLine = is.readUTF()) != null) {
                    System.out.println("Servidor: " + responseLine);

                }
                // clean up:
                // close the output stream
                // close the input stream
                // close the socket
                os.close();
                is.close();
                smtpSocket.close();
            } catch (UnknownHostException e) {
                System.err.println("Trying to connect to unknown host: " + e);
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }

    private String enviarinfoHost() {
        String info = "host ";
        info += this.host.getId()+ " ";
        info += this.host.getDir_mac()+ " ";
        for (int i = 0; i < this.host.getVecinos().size(); i++) {
            info += this.host.getVecinos().get(i).getDir_mac()+ " ";
        }
       
        return info;
    }

    public void enviarEnlaceVivo() {
        String info = "enlacevivo ";
        info += this.host.getDir_mac() + " ";
        connect();
        try {
            os.writeUTF(info + "\n");
            
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addtask(Task task, int time) {
        this.timer.schedule(task, time);
    }

    public void purgeTimer() {
        this.timer = new Timer();
    }

    public void cancelTimer() {
        this.timer.cancel();
    }

    public void connect() {
        try {
            smtpSocket = new Socket("127.0.0.1", 9999);
            os = new DataOutputStream(smtpSocket.getOutputStream());
            is = new DataInputStream(smtpSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host");
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: hostname");
        }
    }
    
    
    
    //enviar coneccion enlace vivo (medir timestamp)
    
    
    
    
}
