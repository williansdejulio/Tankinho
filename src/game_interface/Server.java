/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_interface;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class Server {

    private ServerSocket ss;
    private DataOutputStream out;
    private DataInputStream in;
    private ArrayList<Player> players;

    private void setPlayerInfo(Player p, String name, String password) throws InterruptedException {
        File bdContas = new File("bd.dat");
        boolean foundClient = false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(bdContas));
            while (br.ready()) {
                String str = br.readLine();
                StringTokenizer sT = new StringTokenizer(str, ";");
                String userName = sT.nextToken();
                String userPassword = sT.nextToken();
                if (name.equals(userName) && password.equals(userPassword)) {
                    boolean playerLogged = false;
                    for (Player player : players) {
                        if (player.getName().equals(userName)) {
                            playerLogged = true;
                            break;
                        }
                    }
                    if (!playerLogged) {
                        p.write(false);
                        p.write(false);
                        Thread.sleep(20);
                        p.write(userName);
                        p.setName(userName);
                        Thread.sleep(20);
                        p.write(userPassword);
                        p.setPassword(userPassword);
                        Thread.sleep(20);
                        int numOfWins = Integer.parseInt(sT.nextToken());
                        p.setNumOfGames(numOfWins);
                        p.write(numOfWins);
                        Thread.sleep(20);
                        int numOfGames = Integer.parseInt(sT.nextToken());
                        p.setNumOfGames(numOfGames);
                        p.write(numOfGames);
                        Thread.sleep(20);
                        int gold = Integer.parseInt(sT.nextToken());
                        p.setGold(gold);
                        p.write(gold);
                        Thread.sleep(20);
                        p.setState(State.SELECTING_OPPONENT);
                        foundClient = true;
                        break;
                    } else {
                        p.write(false);
                        p.write(true);
                    }
                }
            }
            if (!foundClient) {
                p.write(true);
                p.write(false);
            }
        } catch (IOException e) {

        }
    }

    private class PlayerListener implements Runnable {

        private Player p;
        private boolean sendAvailability;
        private boolean availability;
        private boolean exited;

        public PlayerListener(Player p) {
            this.p = p;
            this.sendAvailability = false;
            this.exited = false;
        }

        @Override
        public void run() {
            while (!exited) {
                if (p.getState() == State.LOGIN) {
                    try {
                        String request = p.readString();
                        if (request.equals(Request.LOGIN.request)) {
                            String name = p.readString();
                            String password = p.readString();
                            setPlayerInfo(p, name, password);
                        } else if (request.equals(Request.EXIT.request)) {
                            players.remove(p);
                            break;
                        } else if (request.equals(Request.SIGNUP.request)) {
                            boolean found = false;
                            String name = p.readString();
                            String password = p.readString();
                            File bdContas = new File("bd.dat");
                            BufferedReader br = new BufferedReader(new FileReader(bdContas));
                            while (br.ready()) {
                                String str = br.readLine();
                                StringTokenizer sT = new StringTokenizer(str, ";");
                                String userName = sT.nextToken();
                                if (userName.equals(name)) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                if (!bdContas.exists()) {
                                    bdContas.createNewFile();
                                }

                                p.setName(name);
                                p.setPassword(password);
                                p.setGold(100);
                                p.setNumOfGames(0);
                                p.setNumOfWins(0);
                                p.setState(State.SELECTING_OPPONENT);

                                String strNewPlayer = name + ";" + password + ";0;0;100;1,2;";
                                BufferedWriter output = new BufferedWriter(new FileWriter(bdContas, true));
                                output.write(strNewPlayer);
                                output.newLine();
                                output.close();
                            }

                            p.write(!found);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (p.getState() == State.SELECTING_OPPONENT) {
                    try {
                        byte[] requestPack = p.read();
                        String request = new String(requestPack);
                        request = request.replaceAll(" ", "");
                        String responsePack = "";
                        StringTokenizer sT = new StringTokenizer(request, ",");
                        while (sT.hasMoreTokens()) {
                            String token = sT.nextToken();
                            if (token.equals(Request.UPDATE.request)) {
                                responsePack += Response.UPDATE.response + ",";
                                int countPlayers = 0;
                                for (int i = 0; i < players.size(); i++){
                                    if (!players.get(i).isPlaying()){
                                        countPlayers++;
                                    }
                                }
                                countPlayers--; // NOS MESMOS
                                responsePack += countPlayers + ",";
                                for (Player player : players) {
                                    if (player != p) {
                                        if (!player.isPlaying()) {
                                            responsePack += player.getName() + ",";
                                        }
                                    }
                                }
                            } else if (token.equals(Request.END.request)) {
                                if (p.isAccepted()) {
                                    String playerName = p.getAcceptedPlayerHostname();
                                    responsePack += Response.GAME_ANSWER.response + ",";
                                    responsePack += playerName + ",";
                                    p.setAccepted(false);
                                }
                                if (p.invitationSent()) {
                                    String playerName = p.getInvitedPlayerName();
                                    String playerHostname = p.getInvitedPlayerHostname();
                                    responsePack += Response.GAME_INVITE.response + ",";
                                    responsePack += playerName + ",";
                                    responsePack += playerHostname + ",";
                                    p.setInvitationSent(false);
                                }
                                if (sendAvailability) {
                                    responsePack += Response.GAME_AVAILABLE.response + ",";
                                    responsePack += String.valueOf(availability) + ",";
                                    sendAvailability = false;
                                }
                                responsePack += Response.END.response + ",";
                                p.write(responsePack.getBytes());
                            } else if (token.equals(Request.GAME_INVITE.request)) {
                                String playerName = sT.nextToken();
                                for (Player player : players) {
                                    if (player.getName().equals(playerName)) {
                                        player.setInvitationSent(true);
                                        player.setInvitedPlayerName(p.getName());
                                        player.setInvitedPlayerHostname(p.getHostname());
                                        break;
                                    }
                                }
                            } else if (token.equals(Request.GAME_ANSWER.request)) {
                                String playerName = sT.nextToken();
                                for (Player player : players) {
                                    if (player.getName().equals(playerName)) {
                                        player.setAccepted(true);
                                        player.setAcceptedPlayerHostname(p.getHostname());
                                        p.setPlaying(true);
                                        player.setPlaying(true);
                                        break;
                                    }
                                }
                            } else if (token.equals(Request.GAME_AVAILABLE.request)) {
                                sendAvailability = true;
                                String playerName = sT.nextToken();
                                boolean foundPlayer = false;
                                for (Player player : players) {
                                    if (player.getName().equals(playerName)) {
                                        foundPlayer = true;
                                        if (!player.isPlaying()) {
                                            availability = true;
                                        } else {
                                            availability = false;
                                        }
                                    }
                                }
                                if (!foundPlayer) {
                                    availability = false;
                                }
                            } else if (token.equals(Request.EXIT.request)) {
                                exited = true;
                            } else if (token.equals(Request.UPDATE_GAME_ENDED.request)){
                                p.setPlaying(false);
                                String strWonGame = sT.nextToken();
                                if (strWonGame.equals("true")){
                                    p.incWins();
                                    p.incGolg();
                                }
                                p.incGames();
                            }
                        }
                    } catch (Exception ex) {
                    }
                }
            }
            players.remove(p);
        }
    }

    public Server() {
        try {
            ss = new ServerSocket(10515);
            players = new ArrayList();
            //new Updater();
            while (true) {

                Socket s = ss.accept();
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                DataInputStream in = new DataInputStream(s.getInputStream());
                Player p = new Player(s.getInetAddress().getHostAddress(), in, out);
                players.add(p);
                new Thread(new PlayerListener(p)).start();
            }
        } catch (Exception e) {

        }
    }
}
