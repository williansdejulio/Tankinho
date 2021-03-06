/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_interface;

import batalha.ConnectionManager;
import batalha.TankSelection;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Leonardo
 */
public class SelectOpponentScreen {

    private Player player;
    private List list;
    private JButton btnInvite;
    private JFrame frame;
    private boolean accepted, sendAnswer;
    private String acceptedPlayerName, acceptedPlayerHostname;
    private String selectedPlayer;

    private class ServerRequestSender implements Runnable {

        @Override
        public void run() {
            boolean exited = false;
            while (true) {
                String requestString = "";
                requestString += Request.UPDATE.request + ",";
                if (player.invitationSent()) {
                    String playerName = player.getInvitedPlayerName();
                    requestString += Request.GAME_INVITE.request + ",";
                    requestString += playerName + ",";
                    player.setInvitationSent(false);
                }
                if (accepted) {
                    requestString += Request.GAME_AVAILABLE.request + ",";
                    requestString += acceptedPlayerName + ",";
                    accepted = false;
                }
                if (sendAnswer) {
                    requestString += Request.GAME_ANSWER.request + ",";
                    requestString += acceptedPlayerName + ",";
                    sendAnswer = false;
                }
                if (player.stoppedPlaying()) {
                    requestString += Request.UPDATE_GAME_ENDED.request + ",";
                    requestString += String.valueOf(player.wonGame());
                    player.setStoppedPlaying(false);
                }
                if (player.exit()) {
                    requestString += Request.EXIT.request + ",";
                    exited = true;
                }
                requestString += Request.END.request;
                byte[] requestPack = requestString.getBytes();

                try {
                    player.write(requestPack);
                    Thread.sleep(900);
                    if (exited) {
                        System.exit(0);
                    }
                } catch (IOException | InterruptedException ex) {

                }
            }
        }

    }

    private class ServerResponseListener implements Runnable {

        @Override
        public void run() {
            while (!player.exit()) {
                try {
                    byte[] responsePack = player.read();
                    String responseStr = new String(responsePack);
                    responseStr = responseStr.replaceAll(" ", "");

                    StringTokenizer sT = new StringTokenizer(responseStr, ",");
                    while (sT.hasMoreTokens()) {
                        String token = sT.nextToken();
                        if (token.equals(Response.UPDATE.response)) {
                            list.removeAll();
                            int countPlayers = Integer.parseInt(sT.nextToken());
                            for (int i = 0; i < countPlayers; i++) {
                                list.add(sT.nextToken());
                            }
                        } else if (token.equals(Response.END.response)) {
                            ;
                        } else if (token.equals(Response.GAME_INVITE.response)) {
                            String playerName = sT.nextToken();
                            String playerHostname = sT.nextToken();
                            if (JOptionPane.showConfirmDialog(null, "O player " + playerName + " quer jogar com voce!!!", "Voce aceita?", JOptionPane.YES_NO_OPTION)
                                    == JOptionPane.YES_OPTION) {
                                acceptedPlayerName = playerName;
                                acceptedPlayerHostname = playerHostname;
                                accepted = true;
                            }
                        } else if (token.equals(Response.GAME_ANSWER.response)) {
                            String playerHostname = sT.nextToken();
                            ConnectionManager.createConnection(playerHostname);
                            TankSelection tS = new TankSelection(player, frame);
                            frame.setVisible(false);
                        } else if (token.equals(Response.GAME_AVAILABLE.response)) {
                            String availability = sT.nextToken();
                            System.out.println(availability);
                            if (availability.equals("true")) {
                                sendAnswer = true;
                                ConnectionManager.createConnection(acceptedPlayerHostname);
                                TankSelection tS = new TankSelection(player, frame);
                                frame.setVisible(false);
                            } else {
                                JOptionPane.showMessageDialog(frame, "o player " + acceptedPlayerName + " nao está mais disponível para jogar!", "Deu ruim", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(SelectOpponentScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public SelectOpponentScreen(Player p) throws IOException {

        initGUI(p.getName());
        this.player = p;
        
        frame.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent we) {
            }

            @Override
            public void windowClosing(WindowEvent we) {
                player.exited();
            }

            @Override
            public void windowClosed(WindowEvent we) {

            }

            @Override
            public void windowIconified(WindowEvent we) {
            }

            @Override
            public void windowDeiconified(WindowEvent we) {
            }

            @Override
            public void windowActivated(WindowEvent we) {
            }

            @Override
            public void windowDeactivated(WindowEvent we) {
            }
        });

        this.list.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (list.getSelectedIndex() != -1) {
                    selectedPlayer = list.getSelectedItem();

                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });

        this.btnInvite.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                String playerName = selectedPlayer;
                player.setInvitationSent(true);
                player.setInvitedPlayerName(playerName);
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                btnInvite.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        new Thread(new ServerResponseListener()).start();
        new Thread(new ServerRequestSender()).start();
    }

    public void initGUI(String playerName) {
        
        Font font = new Font("Monospaced", Font.BOLD, 18);
        
        frame = new JFrame("Tankinho - " + playerName);
        JLabel lbl = new JLabel("Players:");
        lbl.setFont(font);
        
        
        JPanel panelLbl = new JPanel();
        panelLbl.setBackground(new Color(0, 0, 0, 0));
        panelLbl.setLayout(new BoxLayout(panelLbl,BoxLayout.X_AXIS));
        
        JPanel panelPrincipal = new JPanel();
        BoxLayout layout1 = new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS);
        panelPrincipal.setLayout(layout1);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.setBackground(new Color(227, 242, 253));

        JPanel panelButton = new JPanel();
        BoxLayout layout2 = new BoxLayout(panelButton, BoxLayout.X_AXIS);
        panelButton.setLayout(layout2);
        panelButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelButton.setBackground(new Color(0, 0, 0, 0));

        frame.setSize(300, 500);
        btnInvite = new JButton("Invite player");
        btnInvite.setBorderPainted(false);
        btnInvite.setBackground(Color.WHITE);
        btnInvite.setFocusPainted(false);
        btnInvite.setFont(font);
        list = new List();
        Random r = new Random();
        
        for (int i = 0; i < 20; i++) {
            String nome = "bunda "+(i+1)+":";
            for (int j = nome.length(); j < 15; j++) {
                nome += " ";
            }
            list.add(nome+r.nextInt(50)+"/"+r.nextInt(50));
        }
        JList jlist = new JList(list.getItems());
        jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //jlist.setLayoutOrientation(JList.VERTICAL_WRAP);
        jlist.setSelectedIndex(0);
        jlist.setVisibleRowCount(4);
        //DefaultListCellRenderer renderer = (DefaultListCellRenderer) jlist.getCellRenderer();
        //renderer.setHorizontalAlignment(SwingConstants.CENTER);
        jlist.setBackground(new Color(227, 242, 253));
        jlist.setFont(font);
        jlist.setForeground(Color.BLACK);
        jlist.setSelectionBackground(new Color(29, 152, 235,150));
        jlist.setSelectionForeground(Color.WHITE);
        UIManager.put("List.focusCellHighlightBorder", BorderFactory.createEmptyBorder());
        //UIManager.put("Button.focus", BorderFactory.createEmptyBorder());
        JScrollPane scrollPane = new JScrollPane(jlist);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panelButton.add(btnInvite);
        panelLbl.add(lbl);
        panelPrincipal.add(panelButton);
        panelPrincipal.add(panelLbl);
        panelPrincipal.add(scrollPane);
        
        
        frame.add(panelPrincipal);
        
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        
        this.accepted = false;
        this.sendAnswer = false;
    }
}
