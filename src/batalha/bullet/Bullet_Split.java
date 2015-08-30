/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batalha.bullet;

import batalha.Arena;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author u13165
 */
public class Bullet_Split implements BulletInterface {

    protected int x, y;

    private int walkPixelx, damage, size;
    private final char side; //** l for LEFT ---  r for RIGHT **//
    private ArrayList<BulletInterface> tankBullets;

    public Bullet_Split() {
        this.side = '0';
        this.x = 0;
        this.y = 0;
    }

    public Bullet_Split(char side, int x, int y, int damage, int walkPixelx, int walkPixely, int size, Debuff debuff, long debuffDuration, ArrayList<BulletInterface> tankBullets) {
        this.side = side;
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.walkPixelx = walkPixelx;
        this.size = size;
        this.split = (int)(Arena.getMaxX()*0.4);
        this.splitTick = 0;
        this.shot = false;
        this.debuff = debuff;
        this.debuffDuration = debuffDuration;
        this.tankBullets = tankBullets;
    }

    @Override
    public void onWalk() {
        if (side == 'l') {
            x += walkPixelx;
        } else {
            x -= walkPixelx;
        }
        splitTick += walkPixelx;
        if (splitTick >= split && !shot){
            shot = true;
            tankBullets.add(new Bullet(this.side, this.x + this.size / 2, this.y + size/2, damage, walkPixelx, 1, size, debuff, debuffDuration));
            tankBullets.add(new Bullet(this.side, this.x + this.size / 2, this.y + size/2, damage, walkPixelx, -1, size, debuff, debuffDuration));
            
        }
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

    private int split;
    private int splitTick;
    private boolean shot;
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
