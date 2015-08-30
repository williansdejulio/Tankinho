/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package batalha.bullet;

import java.awt.Graphics;

/**
 *
 * @author u13165
 */
public interface BulletInterface {
    public void onWalk();
    public int getDamage();
    public boolean colides();

    public int getX();

    public int getY();

    public int getSize();

    public void paint(Graphics g);
    
    public long getDebuffTime();
    
    public Debuff getDebuffType();
}
