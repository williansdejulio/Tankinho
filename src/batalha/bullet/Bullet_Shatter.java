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
public class Bullet_Shatter implements BulletInterface {

    private class Bullet_Shatter1 implements BulletInterface {

        protected int x, y;
        private int walkPixelx, damage, walkPixely, size;
        private final char side; //** l for LEFT ---  r for RIGHT **//
        private ArrayList<BulletInterface> tankBullets;

        public Bullet_Shatter1() {
            this.side = '0';
            this.x = 0;
            this.y = 0;
        }

        public Bullet_Shatter1(char side, int x, int y, int damage, int walkPixelx, int walkPixely, int size, Debuff debuff, long debuffDuration, ArrayList<BulletInterface> bullets) {
            this.side = side;
            this.x = x;
            this.y = y;
            this.walkPixelx = walkPixelx;
            this.walkPixely = walkPixely;
            this.damage = damage;
            this.size = size;
            this.debuff = Debuff.SLOW;
            this.debuffDuration = debuffDuration;
            this.tankBullets = bullets;
        }

        @Override
        public void onWalk() {
            if (side == 'l') {
                x += walkPixelx;
            } else {
                x -= walkPixelx;
            }
            y += walkPixely;

            shatterTick++;
            if (shatterTick >= shatter) {
                tankBullets.remove(this);
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

        private final int shatter = 15;
        private int shatterTick = 0;
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

    protected int x, y;

    private int walkPixelx, damage, size;
    private final char side; //** l for LEFT ---  r for RIGHT **//
    private final ArrayList<BulletInterface> tankBullets;

    public Bullet_Shatter() {
        this.side = '0';
        this.x = 0;
        this.y = 0;
        tankBullets = null;
    }

    public Bullet_Shatter(char side, int x, int y, int damage, int walkPixelx, int walkPixely, int size, Debuff debuff, long debuffDuration, ArrayList<BulletInterface> tankBullets) {
        this.side = side;
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.walkPixelx = walkPixelx;
        this.size = size;
        this.shatter = (int) (Arena.getMaxX() * 0.95);
        this.shatterTick = 0;
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
        shatterTick += walkPixelx;
        if (shatterTick >= shatter) {
            tankBullets.add(new Bullet_Shatter1(this.side, this.x + this.size / 2, this.y + size / 2, damage / 3, 6, 0, size / 2, debuff, 200, tankBullets));
            tankBullets.add(new Bullet_Shatter1(this.side, this.x + this.size / 2, this.y + size / 2, damage / 3, -6, 0, size / 2, debuff, 200, tankBullets));
            tankBullets.add(new Bullet_Shatter1(this.side, this.x + this.size / 2, this.y + size / 2, damage / 3, 0, 6, size / 2, debuff, 200, tankBullets));
            tankBullets.add(new Bullet_Shatter1(this.side, this.x + this.size / 2, this.y + size / 2, damage / 3, 0, -6, size / 2, debuff, 200, tankBullets));
            tankBullets.add(new Bullet_Shatter1(this.side, this.x + this.size / 2, this.y + size / 2, damage / 3, 4, 4, size / 2, debuff, 200, tankBullets));
            tankBullets.add(new Bullet_Shatter1(this.side, this.x + this.size / 2, this.y + size / 2, damage / 3, -4, 4, size / 2, debuff, 200, tankBullets));
            tankBullets.add(new Bullet_Shatter1(this.side, this.x + this.size / 2, this.y + size / 2, damage / 3, 4, -4, size / 2, debuff, 200, tankBullets));
            tankBullets.add(new Bullet_Shatter1(this.side, this.x + this.size / 2, this.y + size / 2, damage / 3, -4, -4, size / 2, debuff, 200, tankBullets));
            tankBullets.remove(this);
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

    private int shatter;
    private int shatterTick;
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
