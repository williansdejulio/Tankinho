/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batalha.tank;

import batalha.bullet.BulletInterface;
import batalha.bullet.Bullet;
import batalha.bullet.Debuff;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author u13181
 */
public class TankRambo implements TankInterface {

    private double x, y;
    private double life;
    private double speed;
    private char side;
    private boolean dead, goingUp, goingDown, canShootAgain;
    private int debuff;
    private long elapsedTime;
    private long debuffDuration;
    private final ArrayList<BulletInterface> bullets;

    public static final int ID = 0;
    public static final int BULLET_SPEED = 8;
    public static final int BULLET_SIZE = 15;
    public static final int BULLET_DAMAGE = 12;
    public static final int FULL_LIFE = 130;
    public static final int SPEED = 3;
    public static final int SIZE = 60;
    public static final int FIRE_RATE = 200;
    public static final int SLOW_TIME = 500;

    public TankRambo() {
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
            double bSpeed = TankRambo.BULLET_SPEED * batalha.Arena.getMaxX() / 1024.0;
            bullets.add(new Bullet(this.side, this.getX() + BULLET_SIZE / 2, this.getY() + BULLET_SIZE / 2,
                    BULLET_DAMAGE, (int) bSpeed, 0, BULLET_SIZE, Debuff.SLOW, TankRambo.SLOW_TIME));
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
        return TankRambo.SIZE;
    }

    @Override
    public double getSpeed() {
        return TankRambo.SPEED;
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
        return TankRambo.FIRE_RATE;
    }

    @Override
    public int getFullLife() {
        return TankRambo.FULL_LIFE;
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
        return TankRambo.BULLET_SIZE;
    }

    @Override
    public int getBulletDamage() {
        return TankRambo.BULLET_DAMAGE;

    }

    @Override
    public int getBulletSpeed() {
        return TankRambo.BULLET_SPEED;

    }

    @Override
    public int getID() {
        return TankRambo.ID;
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
        return (int)this.life;
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
        if (debuff == Debuff.SLOW.debuff)
            y += batalha.Attributes.MAX_BULLET_SLOW*(TankRambo.SPEED * batalha.Arena.getMaxY() / 768.0);
        else if (debuff != Debuff.STUN.debuff)
            y += TankRambo.SPEED * batalha.Arena.getMaxY() / 768.0;
    }

    @Override
    public boolean isGoingUp() {
        return goingUp;
    }

    @Override
    public void walkUp() {
        if (debuff == Debuff.SLOW.debuff)
            y -= batalha.Attributes.MAX_BULLET_SLOW*(TankRambo.SPEED * batalha.Arena.getMaxY() / 768.0);
        else if (debuff != Debuff.STUN.debuff)
            y -= TankRambo.SPEED * batalha.Arena.getMaxY() / 768.0;
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
        if (this.life <= 0)
            this.dead = true;
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
