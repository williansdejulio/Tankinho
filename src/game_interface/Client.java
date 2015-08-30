/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_interface;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Leonardo
 */
public class Client extends JFrame{
    private Player p;
    
    public Client() throws IOException{
        String serverHostname = JOptionPane.showInputDialog("Digite o IP do servidor!");
        Socket s = new Socket(serverHostname,10515);
        InetAddress localAddress = InetAddress.getLocalHost();
        
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        DataInputStream in = new DataInputStream(s.getInputStream());
        p = new Player(localAddress.getHostAddress(),in, out);
        Login l = new Login(p);
    }
}
