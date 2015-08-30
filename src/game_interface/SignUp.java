/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_interface;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.swing.*;
import javax.swing.JFrame;

/**
 *
 * @author Willians
 */

public class SignUp extends JFrame {

    private JTextField nome;
    private JPasswordField password;
    private JPasswordField confirmPassword;
    private JButton cadastro;
    private final Player p;

    /**
     * @param args the command line arguments
     */
    public SignUp(Player p) {
        this.p = p;
        initGUI();
    }

    public boolean validacao() {
        if (nome.getText().equals("") || password.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Nome ou senha inválido");
            return false;
        }

        if (nome.getText().contains(";") || password.getText().contains(";")) {
            JOptionPane.showMessageDialog(this, "Nome ou senha inválido");
            return false;
        }
        
        if (!password.getText().equals(confirmPassword.getText())){
            return false;
        }
        return true;
    }

    public void sendServer(String name, String password) throws IOException {
        p.write(Request.SIGNUP.request);
        try {
            Thread.sleep(20);
        } catch (InterruptedException ex) {
        }
        
        p.write(name);
        try {
            Thread.sleep(20);
        } catch (InterruptedException ex) {
        }
        
        p.write(password);
        try {
            Thread.sleep(20);
        } catch (InterruptedException ex) {
        }
        
        try {
            boolean signUpConfirmed = p.readBoolean();
            if (signUpConfirmed) {
                
                p.setState(State.SELECTING_OPPONENT);
                p.setName(name);
                p.setPassword(password);
                p.setNumOfWins(0);
                p.setNumOfGames(0);
                p.setGold(100);
                
                JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso!", "", JOptionPane.INFORMATION_MESSAGE);
                SelectOpponentScreen SOS = new SelectOpponentScreen(p);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Nome ja existente!", "Tente novamente", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
        }
    }

    public void initGUI() {
        this.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent we) {
            }

            @Override
            public void windowClosing(WindowEvent we) {
                try {
                    p.write(Request.EXIT.request);
                    Thread.sleep(20);
                    java.lang.System.exit(0);
                } catch (IOException | InterruptedException ex) {
                }
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
        this.setTitle("SIGN UP");

        JPanel panelTextFields = new JPanel();
        JPanel panelPrincipal = new JPanel();
        JPanel panelNome = new JPanel();
        JPanel panelPassword = new JPanel();
        JPanel panelButton = new JPanel();
        JPanel panelConfirmPassword = new JPanel();

        nome = new JTextField();
        password = new JPasswordField();
        confirmPassword = new JPasswordField();
        cadastro = new JButton("Sign up");

        cadastro.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (validacao()) {
                    try {
                        sendServer(nome.getText(), password.getText());
                    } catch (IOException ex) {
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Verifique os campos e tente novamente!", "Cadastro nao realizado", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        JLabel label1 = new JLabel(" Nome: ", SwingConstants.RIGHT);
        JLabel label2 = new JLabel("Senha: ", SwingConstants.RIGHT);
        JLabel label3 = new JLabel("Confirmar senha: ", SwingConstants.RIGHT);

        panelNome.setLayout(new BoxLayout(panelNome, BoxLayout.X_AXIS));
        panelNome.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelNome.setPreferredSize(new Dimension(200, 50));
        panelNome.add(label1);
        panelNome.add(nome);

        panelPassword.setLayout(new BoxLayout(panelPassword, BoxLayout.X_AXIS));
        panelPassword.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelNome.setPreferredSize(new Dimension(200, 50));
        panelPassword.add(label2);
        panelPassword.add(password);
        
        panelConfirmPassword.setLayout(new BoxLayout(panelConfirmPassword, BoxLayout.X_AXIS));
        panelConfirmPassword.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelNome.setPreferredSize(new Dimension(200, 50));
        panelConfirmPassword.add(label3);
        panelConfirmPassword.add(confirmPassword);

        panelTextFields.setLayout(new BoxLayout(panelTextFields, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panelTextFields.add(panelNome);
        panelTextFields.add(panelPassword);
        panelTextFields.add(panelConfirmPassword);

        panelButton.add(cadastro);

        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setPreferredSize(new Dimension(200, 200));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 50, 300, 20));
        panelPrincipal.add(panelTextFields);
        panelPrincipal.add(panelButton);

        this.add(panelPrincipal);
        this.setSize(new Dimension(350, 200));
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {

        }
        this.setLocationByPlatform(true);
        this.revalidate();
        this.setVisible(true);
        this.setMinimumSize(new Dimension(263, 160));
    }

}
