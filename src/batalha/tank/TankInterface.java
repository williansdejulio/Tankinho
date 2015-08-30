/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batalha.tank;

import batalha.bullet.BulletInterface;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author u13181
 */
public interface TankInterface{
    
    public ArrayList<BulletInterface> getBullets();
    
    public void onHitByBullet(batalha.bullet.BulletInterface bullet);

    public void onFireBullet();

    public char getSide();

    public void setSide(char side);

    public int getSize();

    public double getSpeed();

    public int getX();

    public int getY();
    
    public boolean isDead();

    public long getFireRate();

    public int getFullLife();
    
    public int getBulletSize();
    
    public int getBulletDamage();
    
    public int getBulletSpeed();
    
    public int getID();

    public void paint(Graphics g);

    public void setY(int height);

    public void setX(int width);

    public int getLife();

    public void setLife(int life);

    public boolean isGoingDown();

    public void walkDown();

    public boolean isGoingUp();

    public void walkUp();

    public void setDead(boolean b);

    public void setGoingUp(boolean b);

    public void setGoingDown(boolean b);

    public boolean canShootAgain();

    public void setCanShootAgain(boolean b); 
    
    public void passive();
    
    public int getDebuff();
    
    public long getDebuffDuration();
    
    public void OnHitByBullet(int d, long dd);
}    
