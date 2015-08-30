/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package batalha;

import game_interface.Player;
import game_interface.SelectOpponentScreen;
import java.io.IOException;

/**
 *
 * @author Usuario
 */
public class TestMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Player p = new Player();
        SelectOpponentScreen selectOpponentScreen = new SelectOpponentScreen(p);
        new TankSelection(p, null);
    }
    
}
