/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batalha;

import batalha.tank.TankAsteroid;
import batalha.tank.TankForrest;
import batalha.tank.TankInterface;
import batalha.tank.TankIsaac;
import batalha.tank.TankTankudo;
import batalha.tank.TankRambo;

/**
 *
 * @author WILL E LEUUUUUUUU
 */
public class Champions {

    public static final String[] tankNames = {"Rambo", "Forrest", "Isaac", "Asteroid", "Tankudo"};

    public static TankInterface getTank(String champ) {
        switch (champ) {
            case "Rambo": {
                return new TankRambo();
            }
            case "Forrest": {
                return new TankForrest();
            }
            case "Isaac": {
                return new TankIsaac();
            }
            case "Asteroid": {
                return new TankAsteroid();
            }
            case "Tankudo": {
                return new TankTankudo();
            }
        }
        return null;
    }

    public static String getName(int id) {
        return tankNames[id];
    }
}
