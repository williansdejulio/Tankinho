/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batalha.bullet;

import java.awt.Graphics;

/**
 *
 * @author Willians
 */
public class Bullet implements BulletInterface{

    protected int x, y;
   
    private int walkPixelx,walkPixely, damage, size;
    private final char side; //** l for LEFT ---  r for RIGHT **//

    public Bullet() {
        this.side = '0';
        this.x = 0;
        this.y = 0;
    }

    public Bullet(char side, int x, int y, int damage, int walkPixelx, int walkPixely, int size, Debuff debuff, long debuffDuration) {
        this.side = side;
        this.x = x;
        this.y = y;
        this.walkPixelx = walkPixelx;
        this.walkPixely = walkPixely;
        this.damage = damage;
        this.size = size;
        this.debuff = debuff;
        this.debuffDuration = debuffDuration;
    }

    @Override
    public void onWalk() {
        if (side == 'l')
            x += walkPixelx;
        else
            x -= walkPixelx;
        y += walkPixely;
    }

    @Override
    public int getDamage() {
        return this.damage;
    }
    
    public boolean colides() {
        return true;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public void paint(Graphics g) {
        g.fillOval(x, y, size, size);
    }
    
    private Debuff debuff;
    private long debuffDuration;
    
    @Override
    public long getDebuffTime() {
        return debuffDuration;
    }

    @Override
    public Debuff getDebuffType() {
        return debuff;
    }
    
}
