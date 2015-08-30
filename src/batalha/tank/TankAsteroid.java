/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batalha.tank;

import batalha.bullet.BulletInterface;
import batalha.bullet.Bullet_Split;
import batalha.bullet.Debuff;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author u13181
 */
public class TankAsteroid implements TankInterface {

    private double x, y;
    private double life;
    private double speed;
    private double lifeLost;
    private char side;
    private boolean dead, goingUp, goingDown, canShootAgain;
    private int debuff;
    private long debuffDuration;
    private long elapsedTime;
    private ArrayList<BulletInterface> bullets;
    
    @Override
    public ArrayList<BulletInterface> getBullets() {
        return bullets;
    }

    public static final int ID = 3;
    public static final int BULLET_SPEED = 12;
    public static final int BULLET_SIZE = 15;
    public static final int BULLET_DAMAGE = 5;
    public static final int FULL_LIFE = 50;
    public static final double SPEED = 3;
    public static final int SIZE = 50;
    public static final int FIRE_RATE = 300;
    public static final int POISON_TIME = 4000;

    public TankAsteroid() {
        dead = false;
        goingUp = false;
        goingDown = false;
        life = FULL_LIFE;
        canShootAgain = true;
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
            double bSpeed = TankAsteroid.BULLET_SPEED * batalha.Arena.getMaxX() / 1024.0;
            bullets.add(new Bullet_Split(this.side, this.getX() + BULLET_SIZE / 2, this.getY() + BULLET_SIZE / 2,
                    BULLET_DAMAGE, (int) bSpeed, 0, BULLET_SIZE, Debuff.POISON, TankAsteroid.POISON_TIME, bullets));
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
        return TankAsteroid.SIZE;
    }

    @Override
    public double getSpeed() {
        return TankAsteroid.SPEED;
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
        return TankAsteroid.FIRE_RATE;
    }

    @Override
    public int getFullLife() {
        return TankAsteroid.FULL_LIFE;
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
        return TankAsteroid.BULLET_SIZE;
    }

    @Override
    public int getBulletDamage() {
        return TankAsteroid.BULLET_DAMAGE;

    }

    @Override
    public int getBulletSpeed() {
        return TankAsteroid.BULLET_SPEED;

    }

    @Override
    public int getID() {
        return TankAsteroid.ID;
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
            y += batalha.Attributes.MAX_BULLET_SLOW * (TankAsteroid.SPEED * batalha.Arena.getMaxY() / 768.0);
        } else if (debuff != Debuff.STUN.debuff) {
            y += TankAsteroid.SPEED * batalha.Arena.getMaxY() / 768.0;
        }
    }

    @Override
    public boolean isGoingUp() {
        return goingUp;
    }

    @Override
    public void walkUp() {
        if (debuff == Debuff.SLOW.debuff) {
            y -= batalha.Attributes.MAX_BULLET_SLOW * (TankAsteroid.SPEED * batalha.Arena.getMaxY() / 768.0);
        } else if (debuff != Debuff.STUN.debuff) {
            y -= TankAsteroid.SPEED * batalha.Arena.getMaxY() / 768.0;
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
        if (dd != 0 && d != 0) {
            debuff = d;
            debuffDuration = dd;
            elapsedTime = System.currentTimeMillis();
        }
    }
}
