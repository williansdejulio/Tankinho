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
public class Bullet_Isaac implements BulletInterface {

    protected double x, y, walkPixely;

    private int walkPixelx, damage, size;
    private final char side; //** l for LEFT ---  r for RIGHT **//

    public Bullet_Isaac() {
        this.side = '0';
        this.x = 0;
        this.y = 0;
    }

    public Bullet_Isaac(char side, int x, int y, int damage, int walkPixelx, double walkPixely, int size, Debuff debuff, long debuffDuration) {
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
        if (side == 'l') {
            x += walkPixelx;
        } else {
            x -= walkPixelx;
        }
        y += walkPixely;
    }

    @Override
    public int getDamage() {
        return this.damage;
    }

    @Override
    public boolean colides() {
        return true;
    }
    @Override
    public int getX() {
        return (int)this.x;
    }

    @Override
    public int getY() {
        return (int)this.y;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public void paint(Graphics g) {
        g.fillOval((int)x, (int)y, size, size);
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
