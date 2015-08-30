/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batalha.tank;

import batalha.bullet.BulletInterface;
import batalha.bullet.Bullet_Isaac;
import batalha.bullet.Debuff;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author u13181
 */
public class TankIsaac implements TankInterface {

    private double x, y;
    private double life;
    private char side;
    private boolean dead, goingUp, goingDown;
    private boolean canShootAgain;
    private int debuff;
    private long elapsedTime;
    private long debuffDuration;
    private final ArrayList<BulletInterface> bullets;

    private final Random rPassive;
    private static final int CRIT_BULLET_PERC = 20;
    private boolean critNextBullet;
    public static final int ID = 2;
    public static final int BULLET_SPEED = 8;
    public static final int BULLET_SIZE = 35;
    public static final int BULLET_DAMAGE = 20;
    public static final int FULL_LIFE = 80;
    public static final int SPEED = 4;
    public static final int SIZE = 40;
    public static final int FIRE_RATE = 400;

    public TankIsaac() {
        dead = false;
        goingUp = false;
        goingDown = false;
        life = FULL_LIFE;
        canShootAgain = true;
        rPassive = new Random();
        critNextBullet = false;
        debuff = Debuff.NOTHING.debuff;
        debuffDuration = 0;
        bullets = new ArrayList<>();
    }

    @Override
    public void onHitByBullet(BulletInterface bullet) {
        life -= bullet.getDamage();
        if (bullet.getDebuffType() != Debuff.NOTHING) {
            debuff = bullet.getDebuffType().debuff;
            debuffDuration = bullet.getDebuffTime();
            elapsedTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onFireBullet() {
        if (debuff != Debuff.SILENCE.debuff) {
            double walkPixelY = 0.0;
            double speed = TankIsaac.SPEED * batalha.Arena.getMaxY() / 768.0;
            double bSpeed = TankIsaac.BULLET_SPEED * batalha.Arena.getMaxX() / 1024.0;
            if (this.goingDown) {
                walkPixelY = speed / 4.0;
            } else if (this.goingUp) {
                walkPixelY = -speed / 4.0;
            }
            int damage = critNextBullet ? TankIsaac.BULLET_DAMAGE * 2 : TankIsaac.BULLET_DAMAGE;
            bullets.add(new Bullet_Isaac(this.side, this.getX() + TankIsaac.BULLET_SIZE / 2, this.getY(),
                    damage, (int) bSpeed, walkPixelY, TankIsaac.BULLET_SIZE, Debuff.NOTHING, 0));
        }
    }

    @Override
    public char getSide() {
        return this.side;
    }

    @Override
    public void setSide(char side) {
        this.side = side;
    }

    @Override
    public int getSize() {
        return TankIsaac.SIZE;
    }

    @Override
    public double getSpeed() {
        return TankIsaac.SPEED;
    }

    @Override
    public int getX() {
        return (int) this.x;
    }

    @Override
    public int getY() {
        return (int) this.y;
    }

    @Override
    public boolean isDead() {
        return this.dead;
    }

    @Override
    public long getFireRate() {
        return TankIsaac.FIRE_RATE;
    }

    @Override
    public int getFullLife() {
        return TankIsaac.FULL_LIFE;
    }

    @Override
    public void paint(Graphics g) {
        g.fillRect(this.getX(), this.getY(), this.getSize(), this.getSize());
        g.setColor(Color.WHITE); //LIFE
        g.fillRect(this.getX(), this.getY() - 15, this.getSize(), 10);
        g.setColor(Color.GREEN);
        g.fillRect(this.getX(), this.getY() - 15, (int) (this.getSize() * ((double) this.getLife() / this.getFullLife())), 10);
    }

    @Override
    public int getBulletSize() {
        return TankIsaac.BULLET_SIZE;
    }

    @Override
    public int getBulletDamage() {
        return TankIsaac.BULLET_DAMAGE;

    }

    @Override
    public int getBulletSpeed() {
        return TankIsaac.BULLET_SPEED;

    }

    @Override
    public int getID() {
        return TankIsaac.ID;
    }

    @Override
    public void setY(int height) {
        this.y = height;
    }

    @Override
    public void setX(int width) {
        this.x = width;
    }

    @Override
    public int getLife() {
        return (int) this.life;
    }

    @Override
    public void setLife(int life) {
        this.life = life;
    }

    @Override
    public boolean isGoingDown() {
        return goingDown;
    }

    @Override
    public void walkDown() {
        if (debuff == Debuff.SLOW.debuff) {
            y += batalha.Attributes.MAX_BULLET_SLOW * (TankIsaac.SPEED * batalha.Arena.getMaxY() / 768.0);
        } else if (debuff != Debuff.STUN.debuff) {
            y += TankIsaac.SPEED * batalha.Arena.getMaxY() / 768.0;
        }
    }

    @Override
    public boolean isGoingUp() {
        return goingUp;
    }

    @Override
    public void walkUp() {
        if (debuff == Debuff.SLOW.debuff) {
            y -= batalha.Attributes.MAX_BULLET_SLOW * (TankIsaac.SPEED * batalha.Arena.getMaxY() / 768.0);
        } else if (debuff != Debuff.STUN.debuff) {
            y -= TankIsaac.SPEED * batalha.Arena.getMaxY() / 768.0;
        }
    }

    @Override
    public void setDead(boolean b) {
        this.dead = b;
    }

    @Override
    public void setGoingUp(boolean b) {
        this.goingUp = b;
    }

    @Override
    public void setGoingDown(boolean b) {
        this.goingDown = b;
    }

    @Override
    public boolean canShootAgain() {
        return canShootAgain;
    }

    @Override
    public void setCanShootAgain(boolean b) {
        this.canShootAgain = b;
    }

    @Override
    public void passive() {
        int r = rPassive.nextInt(100);
        if (r < CRIT_BULLET_PERC) {
            critNextBullet = true;
        } else {
            critNextBullet = false;
        }
        long now = System.currentTimeMillis();
        long elapsed = now - elapsedTime;
        if (elapsed >= debuffDuration) {
            debuff = Debuff.NOTHING.debuff;
        }
        if (debuff == Debuff.POISON.debuff) {
            this.life -= batalha.Attributes.MAX_BULLET_POISON;
        }
        if (this.life <= 0) {
            this.dead = true;
        }
    }

    @Override
    public int getDebuff() {
        return this.debuff;
    }

    @Override
    public long getDebuffDuration() {
        return this.debuffDuration;
    }

    @Override
    public void OnHitByBullet(int d, long dd) {
        if (dd != 0 && d != 0){
            debuff = d;
            debuffDuration = dd;
            elapsedTime = System.currentTimeMillis();
        }
    }

    @Override
    public ArrayList<BulletInterface> getBullets() {
        return bullets;
    }
}
