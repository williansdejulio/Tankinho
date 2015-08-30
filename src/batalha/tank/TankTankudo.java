/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batalha.tank;

import batalha.bullet.BulletInterface;
import batalha.bullet.Bullet_Shatter;
import batalha.bullet.Debuff;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author u13181
 */
public class TankTankudo implements TankInterface {

    private double x, y;
    private double life;
    private char side;
    private boolean dead, goingUp, goingDown, canShootAgain;
    private int debuff;
    private long elapsedTime;
    private long debuffDuration;
    private ArrayList<BulletInterface> bullets;

    private final Random rPassive;
    private static final int BLOCK_BULLET_PERC = 12;
    private boolean blockNextBullet;
    public static final int ID = 4;
    public static final int BULLET_SPEED = 6;
    public static final int BULLET_SIZE = 20;
    public static final int BULLET_DAMAGE = 15;
    public static final int FULL_LIFE = 150;
    public static final double SPEED = 1.5;
    public static final int SIZE = 70;
    public static final int FIRE_RATE = 400;

    public TankTankudo() {
        dead = false;
        goingUp = false;
        goingDown = false;
        life = FULL_LIFE;
        canShootAgain = true;
        rPassive = new Random();
        blockNextBullet = false;
        debuff = Debuff.NOTHING.debuff;
        debuffDuration = 0;
        bullets = new ArrayList<>();
    }

    @Override
    public void onHitByBullet(BulletInterface bullet) {
        if (!blockNextBullet) {
            life -= bullet.getDamage();
            if (bullet.getDebuffType() != Debuff.NOTHING) {
                debuff = bullet.getDebuffType().debuff;
                debuffDuration = bullet.getDebuffTime();
                elapsedTime = System.currentTimeMillis();
            }
        } else {
            blockNextBullet = false;
        }
    }

    @Override
    public void onFireBullet() {
        if (debuff != Debuff.SILENCE.debuff) {
            double bSpeed = TankTankudo.BULLET_SPEED * batalha.Arena.getMaxX() / 1024.0;
            bullets.add(new Bullet_Shatter(this.side, this.getX() + BULLET_SIZE / 2, this.getY() + BULLET_SIZE / 2,
                    BULLET_DAMAGE, (int) bSpeed, 0, BULLET_SIZE, Debuff.NOTHING, 0, bullets));
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
        return TankTankudo.SIZE;
    }

    @Override
    public double getSpeed() {
        return TankTankudo.SPEED;
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
        return TankTankudo.FIRE_RATE;
    }

    @Override
    public int getFullLife() {
        return TankTankudo.FULL_LIFE;
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
        return TankTankudo.BULLET_SIZE;
    }

    @Override
    public int getBulletDamage() {
        return TankTankudo.BULLET_DAMAGE;

    }

    @Override
    public int getBulletSpeed() {
        return TankTankudo.BULLET_SPEED;

    }

    @Override
    public int getID() {
        return TankTankudo.ID;
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
            y += batalha.Attributes.MAX_BULLET_SLOW*(TankTankudo.SPEED * batalha.Arena.getMaxY() / 768.0);
        else if (debuff != Debuff.STUN.debuff)
            y += TankTankudo.SPEED * batalha.Arena.getMaxY() / 768.0;
    }

    @Override
    public boolean isGoingUp() {
        return goingUp;
    }

    @Override
    public void walkUp() {
        if (debuff == Debuff.SLOW.debuff)
            y -= batalha.Attributes.MAX_BULLET_SLOW*(TankTankudo.SPEED * batalha.Arena.getMaxY() / 768.0);
        else if (debuff != Debuff.STUN.debuff)
            y -= TankTankudo.SPEED * batalha.Arena.getMaxY() / 768.0;
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
        if (r < BLOCK_BULLET_PERC) {
            blockNextBullet = true;
        } else {
            blockNextBullet = false;
        }
        long now = System.currentTimeMillis();
        long elapsed = now - elapsedTime;
        if (elapsed >= debuffDuration) {
            debuff = Debuff.NOTHING.debuff;
        }
        if (debuff == Debuff.POISON.debuff) {
            this.life -= batalha.Attributes.MAX_BULLET_POISON;

        }
        if (this.life <= 0){
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
        return this.bullets;
    }
}
