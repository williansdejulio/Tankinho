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
public class Bullet_Wave implements BulletInterface {

    protected int x, y;

    private int walkPixelx, damage, size;
    private final char side; //** l for LEFT ---  r for RIGHT **//

    public Bullet_Wave() {
        this.side = '0';
        this.x = 0;
        this.y = 0;
    }

    public Bullet_Wave(char side, int x, int y, int damage, int walkPixelx, int walkPixely, int size, Debuff debuff, long debuffDuration) {
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
        if (side == 'l') {
            x += walkPixelx;
        } else {
            x -= walkPixelx;
        }

        y += (walkPixelx * waveState);
        waveTick++;
        if (waveTick == waveMax) {
            waveTick = 0;
            waveState = -waveState;
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

    private final int waveMax = 14;
    private int waveTick = 7;
    private int waveState = 1;
    
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
