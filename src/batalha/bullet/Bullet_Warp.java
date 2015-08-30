/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batalha.bullet;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author u13165
 */
public class Bullet_Warp implements BulletInterface {

    protected int x, y;

    private int walkPixelx, damage, size;
    private final char side; //** l for LEFT ---  r for RIGHT **//

    public Bullet_Warp() {
        this.side = '0';
        this.x = 0;
        this.y = 0;
    }

    public Bullet_Warp(char side, int x, int y, int damage, int walkPixelx, int walkPixely, int size, Debuff debuff, long debuffDuration) {
        this.side = side;
        this.x = x;
        this.y = y;
        this.walkPixelx = walkPixelx;
        this.damage = damage;
        this.size = size;
        this.debuff = debuff;
        this.debuffDuration = debuffDuration;
    }

    @Override
    public void onWalk() {
        if (warpTick < warpStart) {
            if (side == 'l') {
                x += walkPixelx / 2;
            } else {
                x -= walkPixelx / 2;
            }
        } else if (warpTick >= warpLenght) {
            if (side == 'l') {
                x += walkPixelx * 5;
            } else {
                x -= walkPixelx * 5;
            }
        } else {
            if (side == 'l') {
                x += walkPixelx / 3;
            } else {
                x -= walkPixelx / 3;
            }
        }

        warpTick++;
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

    private final int warpStart  = 100;
    private final int warpLenght = 200;
    private int warpTick   = 0;
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
