/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_interface;

import batalha.Champions;
import batalha.tank.TankInterface;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author u13181
 */
public class Player {

    private boolean stoppedPlaying;
    private boolean gameWon;
    private boolean playing;
    private String name;
    private String password;
    private String hostname;
    private int numOfWins;
    private int numOfGames;
    private int gold;
    private final ArrayList<batalha.tank.TankInterface> tanks;
    private DataOutputStream out;
    private DataInputStream in;
    private State state;
    private boolean invitationSent;
    private String invitedPlayerName;
    private String invitedPlayerHostname;
    private boolean accepted;
    private String acceptedPlayerName;
    private boolean exit;

    public boolean exit() {
        return this.exit;
    }

    public void exited() {
        this.exit = true;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void write(byte[] pack) throws IOException {
        out.write(pack);
        out.flush();
    }

    public byte[] read() throws IOException {
        byte[] bytes = new byte[1024];
        in.read(bytes);
        return bytes;
    }

    public void write(String s) throws IOException {
        out.writeUTF(s);
        out.flush();
    }

    public void write(boolean b) throws IOException {
        out.writeBoolean(b);
        out.flush();
    }

    public void write(int i) throws IOException {
        out.writeInt(i);
        out.flush();
    }

    public String readString() throws IOException {
        return in.readUTF();
    }

    public boolean readBoolean() throws IOException {
        return in.readBoolean();
    }

    public int readInt() throws IOException {
        return in.readInt();
    }

    private void addTank(String s) {
        tanks.add(Champions.getTank(s));
    }

    public TankInterface[] getTanks() {
        TankInterface[] array = new TankInterface[tanks.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = tanks.get(i);
        }
        return array;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNumOfWins() {
        return numOfWins;
    }

    public void setNumOfWins(int numOfWins) {
        this.numOfWins = numOfWins;
    }

    public int getNumOfGames() {
        return numOfGames;
    }

    public void setNumOfGames(int numOfGames) {
        this.numOfGames = numOfGames;
    }

    public void incGames() {
        this.numOfGames++;
    }

    public void incWins() {
        this.numOfWins++;
    }

    public void incGolg() {
        this.gold += 25;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Player() {
        this.name = "test";
        this.password = "";
        this.gold = 0;
        this.numOfGames = 0;
        this.numOfWins = 0;
        tanks = new ArrayList();
        for (int i = 0; i < Champions.tankNames.length; i++) {
            this.addTank(Champions.tankNames[i]);
        }
        this.state = State.LOGIN;
        this.hostname = "";
        this.exit = false;
        this.stoppedPlaying = false;
        this.playing = false;
    }

    public Player(String hostname, DataInputStream in, DataOutputStream out) throws IOException {
        this.name = "";
        this.password = "";
        this.gold = 0;
        this.numOfGames = 0;
        this.numOfWins = 0;
        tanks = new ArrayList();
        for (int i = 0; i < Champions.tankNames.length; i++) {
            this.addTank(Champions.tankNames[i]);
        }
        this.out = out;
        this.in = in;
        this.state = State.LOGIN;
        this.hostname = hostname;
        this.exit = false;
        this.stoppedPlaying = false;
        this.playing = false;
    }

    public boolean invitationSent() {
        return invitationSent;
    }

    public void setInvitationSent(boolean invitationSent) {
        this.invitationSent = invitationSent;
    }

    public String getInvitedPlayerName() {
        return invitedPlayerName;
    }

    public void setInvitedPlayerName(String invitedPlayerName) {
        this.invitedPlayerName = invitedPlayerName;
    }

    public String getInvitedPlayerHostname() {
        return invitedPlayerHostname;
    }

    public void setInvitedPlayerHostname(String invitedPlayerHostname) {
        this.invitedPlayerHostname = invitedPlayerHostname;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getAcceptedPlayerHostname() {
        return acceptedPlayerName;
    }

    public void setAcceptedPlayerHostname(String acceptedPlayerName) {
        this.acceptedPlayerName = acceptedPlayerName;
    }

    public boolean stoppedPlaying() {
        return stoppedPlaying;
    }

    public void setStoppedPlaying(boolean playing) {
        this.stoppedPlaying = playing;
    }

    public void setGameWon(boolean b) {
        this.gameWon = b;
    }

    public boolean wonGame() {
        return this.gameWon;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

}
