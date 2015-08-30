/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batalha;

import batalha.tank.TankInterface;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author u13181
 */
public class TankSelection extends JFrame {

    private TankInterface[] tanks;
    private PanelStats panelStats;
    private TankInterface tankSelecionado;
    private TankInterface me, player2;
    private int player2ID, myID;
    private JButton pick1;
    private boolean lockedPlayer2 = false, meLocked = false;
    private JFrame frame;
    private game_interface.Player p;
    
    public class Receptor extends Thread {

        @Override
        public void run() {
            try {
                byte[] b = new byte[4];
                ConnectionManager.read(b);
                player2ID = ConnectionManager.unpack(b, 0);
                lockedPlayer2 = true;
            } catch (IOException ex) {

            }
            if (meLocked) {
                createArena();
            }
        }
    }

    public TankSelection(game_interface.Player p, JFrame frame) {
        this.p = p;
        tanks = p.getTanks();
        
        this.frame = frame;
        //this.frame.setVisible(false);
        
        initGUI();
    }

    public void initGUI() {
        TratadorMouse trMouse = new TratadorMouse();
        Receptor r = new Receptor();

        pick1 = new JButton("YOUR PICK");
        this.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent we) {
            }

            @Override
            public void windowClosing(WindowEvent we) {
                p.exited();
                //AVISAR O OUTRO JOGADOR QUE QUITAMOS;
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

        this.setTitle("TANK SELECTION");
        JPanel panelChampions = new JPanel();
        JPanel panelLeft = new JPanel();
        JPanel panelPrincipal = new JPanel();
        JPanel panelPlayers = new JPanel();
        panelStats = new PanelStats();

        GridLayout layoutChampions = new GridLayout(4, 4, 12, 12);
        panelChampions.setPreferredSize(new Dimension(300, 300));
        
        
        Color color = new Color(227, 242, 253); //~~~~~~~~
        
        
        panelChampions.setLayout(layoutChampions);
        panelChampions.setBackground(color);
        for (int i = 0;i < tanks.length ;i++){
            JButton b = new JButton();
            b.setText(Champions.tankNames[tanks[i].getID()]);
            b.addMouseListener(trMouse);
            panelChampions.add(b);
        }

        panelPlayers.setLayout(new BoxLayout(panelPlayers, BoxLayout.X_AXIS));
        panelPlayers.setPreferredSize(new Dimension(200, 150));
        panelPlayers.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelPlayers.add(pick1);
        panelPlayers.setBackground(color);

        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        panelLeft.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelLeft.add(panelPlayers);
        panelLeft.add(panelChampions);
        panelLeft.setBackground(color);

        panelPrincipal.setLayout(new GridLayout(1, 2));
        panelPrincipal.add(panelLeft);
        panelPrincipal.add(panelStats);
        panelPrincipal.setBackground(color);

        this.add(panelPrincipal);
        this.pack();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {

        }
        this.setVisible(true);
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - this.getSize().width / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getSize().height / 2);
        this.setResizable(false);

        r.start();
    }

    private class TratadorMouse implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent me) {
        }

        @Override
        public void mousePressed(MouseEvent penes) {
            if (!meLocked) {
                meLocked = true;
                try {
                    JButton buttonEntered = (JButton) penes.getSource();
                    String champName = buttonEntered.getText();
                    tankSelecionado = Champions.getTank(champName);
                    pick1.setText(champName);
                    panelStats.paintComponent(panelStats.getGraphics());
                    myID = tankSelecionado.getID();
                    
                    byte[] pack = new byte[4];
                    ConnectionManager.pack(myID, pack, 0);
                    ConnectionManager.write(pack);
                    ConnectionManager.flush();
                    
                } catch (IOException ex) {
                    Logger.getLogger(TankSelection.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (lockedPlayer2) {
                    createArena();
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent me
        ) {
        }

        @Override
        public void mouseEntered(MouseEvent me
        ) {
            if (!meLocked) {
                JButton buttonEntered = (JButton) me.getSource();
                String champName = buttonEntered.getText();
                tankSelecionado = Champions.getTank(champName);
                pick1.setText(champName);
                panelStats.paintComponent(panelStats.getGraphics());
                myID = tankSelecionado.getID();
            }
        }

        @Override
        public void mouseExited(MouseEvent me
        ) {
        }

    }

    private void createArena() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int maxX = dimension.width;
        int maxY = dimension.height; //barra de tarefas
        if (!ConnectionManager.isServer()) {
            me = tankSelecionado;
            me.setSide('r');
            player2 = Champions.getTank(Champions.getName(player2ID));
            player2.setSide('l');
        } else {
            me = tankSelecionado;
            me.setSide('l');
            player2 = Champions.getTank(Champions.getName(player2ID));
            player2.setSide('r');
        }
        try {
            Arena arena = new Arena(p, 0, 0, (int) (maxX), (int) (maxY), me, player2, frame);
            
        } catch (IOException ex) {
        }
        this.setVisible(false);
    }

    private class PanelStats extends JPanel {

        @Override
        public void paintComponent(Graphics g) {
            int startY = 173;
            int x = 20;
            int size = 18;
            int esp = 20;
            //g.setFont(new Font("Monospaced", Font.BOLD, size));
            g.setFont(new Font("Monospaced", Font.PLAIN, size));
            g.drawString("TANK ATTRIBUTES:", x, startY);
            g.drawString("  Size     :", x, startY + size + esp);
            g.drawString("  Life     :", x, startY + 2 * +size + esp);
            g.drawString("  Speed    :", x, startY + 3 * +size + esp);
            g.drawString("  Fire rate:", x, startY + 4 * +size + esp);
            g.drawString("BULLET ATTRIBUTES:", x, startY + 5 * +size + esp * 2);
            g.drawString("  Damage   :", x, startY + 6 * +size + esp * 3);
            g.drawString("  Speed    :", x, startY + 7 * +size + esp * 3);
            g.drawString("  Size     :", x, startY + 8 * +size + esp * 3);

            g.setColor(Color.WHITE);

            g.fillRect(170, startY + 4 + esp, 150, size - 2);
            g.fillRect(170, startY + 4 + size + esp, 150, size - 2);
            g.fillRect(170, startY + 4 + 2 * size + esp, 150, size - 2);
            g.fillRect(170, startY + 4 + 3 * size + esp, 150, size - 2);

            g.fillRect(170, startY + 8 + 7 * size + esp, 150, size - 2);
            g.fillRect(170, startY + 8 + 8 * size + esp, 150, size - 2);
            g.fillRect(170, startY + 8 + 9 * size + esp, 150, size - 2);

            if (tankSelecionado != null) {
                g.setColor(Color.BLACK);
                g.fillRect(170, startY + 4 + esp, (int) (150 * (tankSelecionado.getSize() / (double) batalha.Attributes.MAX_TANK_SIZE)), size - 2);
                g.fillRect(170, startY + 4 + size + esp, (int) (150 * (tankSelecionado.getLife() / (double) batalha.Attributes.MAX_TANK_LIFE)), size - 2);
                g.fillRect(170, startY + 4 + 2 * size + esp, (int) (150 * ((tankSelecionado.getSpeed()) / (double) batalha.Attributes.MAX_TANK_SPEED)), size - 2);
                g.fillRect(170, startY + 4 + 3 * size + esp, (int) (150 * (batalha.Attributes.MAX_TANK_FIRE_RATE / (double) tankSelecionado.getFireRate())), size - 2);

                g.fillRect(170, startY + 8 + 7 * size + esp, (int) (150 * (tankSelecionado.getBulletDamage() / (double) batalha.Attributes.MAX_BULLET_DAMAGE)), size - 2);
                g.fillRect(170, startY + 8 + 8 * size + esp, (int) (150 * (tankSelecionado.getBulletSpeed() / (double) batalha.Attributes.MAX_BULLET_SPEED)), size - 2);
                g.fillRect(170, startY + 8 + 9 * size + esp, (int) (150 * (tankSelecionado.getBulletSize() / (double) batalha.Attributes.MAX_BULLET_SIZE)), size - 2);
            }
        }
    }
}
