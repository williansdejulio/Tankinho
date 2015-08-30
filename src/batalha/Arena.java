/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batalha;

import batalha.bullet.BulletInterface;
import batalha.tank.TankInterface;
import game_interface.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Willians
 */
public class Arena extends JPanel {

    private final JFrame selectOpponentFrame;
    private final JFrame frame;

    private class EnemyInfo {

        private static final int SIZE = 50;
        private final int[] Xs = new int[SIZE];
        private final int[] Ys = new int[SIZE];
        private final int[] Ss = new int[SIZE];
    }
    private static final long TIME_TO_WAIT = 2000, TIME_TO_SHRINK = 60000;
    private static final int MAX_SHRINK_PERCENTAGE = 40;
    private static final double SHRINK_INCREASER = 0.0075;
    private int maxUp, maxDown;
    private double shrinkPerc;
    private long initialShrinkTime;
    private boolean canShrink;
    private long lastSecond, lastFrame;
    private final long timeCalcFPS;
    private double fps;
    private int height, width, countBullets, frameCounter;
    private final int startX, startY, aHeight, aWidth;
    private static int maxX, maxY;
    private Dimension defaultLeft, defaultRight;
    private Color colorArena = Color.WHITE;
    private TankInterface me, player2;
    private final EnemyInfo enemy;
    private final Image imgArena, imgWin, imgLose;
    private long timeShoot;
    private Player p;
    private boolean bothDead, connectionClosed, shooting;

    public class Fisicas extends Thread {

        @Override
        public void run() {
            /**
             * **************FISICAS DO JOGO*******************
             */
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }

                long now = System.currentTimeMillis();
                if (now - timeShoot >= me.getFireRate()) {
                    me.setCanShootAgain(true);
                }
                if (!canShrink) {
                    if (now - initialShrinkTime >= TIME_TO_SHRINK) {
                        canShrink = true;
                    }
                }
                if (shooting) {
                    if (me.canShootAgain()) {
                        me.onFireBullet();
                        timeShoot = System.currentTimeMillis();
                        me.setCanShootAgain(false);
                    }
                }
                if (!me.isGoingDown() && !me.isGoingUp()) {
                    if (me.getY() + me.getSize() > maxDown) {
                        me.setY(maxDown - me.getSize());
                    }
                    if (me.getY() < maxUp) {
                        me.setY(maxUp);
                    }
                }
                if (me.isGoingDown()) {
                    if (me.getY() + me.getSize() > maxDown) {
                        me.setY(maxDown - me.getSize());
                    } else {
                        me.walkDown();
                    }
                }
                if (me.isGoingUp()) {
                    if (me.getY() < maxUp) {
                        me.setY(maxUp);
                    } else {
                        me.walkUp();
                    }
                }
                if (canShrink) {
                    if (shrinkPerc < MAX_SHRINK_PERCENTAGE) {
                        shrinkPerc += SHRINK_INCREASER;
                        maxDown = aHeight - (int) (aHeight * shrinkPerc / 100.0);
                        maxUp = (int) (aHeight * shrinkPerc / 100.0);
                    }
                }
                for (int i = 0; i < me.getBullets().size(); i++) {
                    BulletInterface bAtual = me.getBullets().get(i);
                    switch (me.getSide()) {
                        case 'l': {
                            if (bAtual.getX() >= aWidth + startX) {
                                me.getBullets().remove(bAtual);
                            } else {
                                bAtual.onWalk();
                                if (detectCollision(bAtual)) {
                                    player2.onHitByBullet(bAtual);
                                    me.getBullets().remove(bAtual);
                                }
                            }
                        }
                        break;
                        case 'r': {
                            if (bAtual.getX() <= startX) {
                                me.getBullets().remove(bAtual);
                            } else {
                                bAtual.onWalk();
                                if (detectCollision(bAtual)) {
                                    if (bAtual != null) {
                                        player2.onHitByBullet(bAtual);
                                        me.getBullets().remove(bAtual);
                                    }
                                }
                            }
                        }
                    }
                }
                me.passive();
                player2.passive();
            }
        }
    }

    public class Receptor extends Thread {

        @Override
        public void run() {
            try {
                byte[] b = new byte[844];
                while (!connectionClosed) {
                    int count = 0;
                    ConnectionManager.read(b);
                    int gameEnd = ConnectionManager.unpack(b, count);
                    count += 4;
                    if (gameEnd == 1) {
                        if (!connectionClosed) {
                            connectionClosed = true;
                            ConnectionManager.close();
                        }
                    } else if (!connectionClosed) {
                        int percY = ConnectionManager.unpack(b, count);
                        int y = (percY * maxY) / 1000000;
                        count += 4;
                        int life = ConnectionManager.unpack(b, count);
                        count += 4;
                        int debuff = (ConnectionManager.unpack(b, count));
                        count += 4;
                        int debuffDuration = (ConnectionManager.unpack(b, count));
                        count += 4;
                        countBullets = ConnectionManager.unpack(b, count);
                        count += 4;
                        for (int i = 0; i < countBullets; i++) {
                            int perX = ConnectionManager.unpack(b, count);
                            int xB = (perX * maxX) / 1000000;
                            count += 4;
                            int perY = ConnectionManager.unpack(b, count);
                            int yB = (perY * maxY) / 1000000;
                            count += 4;
                            int sB = ConnectionManager.unpack(b, count);
                            count += 4;
                            enemy.Xs[i] = xB;
                            enemy.Ys[i] = yB;
                            enemy.Ss[i] = sB;
                        }
                        if (y != 0) {
                            player2.setY(y);
                            me.setLife(life);
                            me.OnHitByBullet(debuff, debuffDuration);
                        }
                    }
                }
                if (me.isDead()) {
                    getGraphics().drawImage(imgLose, startX, startY, aWidth, aHeight, frame);
                } else {
                    getGraphics().drawImage(imgWin, startX, startY, aWidth, aHeight, frame);
                }
                p.setStoppedPlaying(true);
                Thread.sleep(TIME_TO_WAIT);
                frame.dispose();
                selectOpponentFrame.setVisible(true);
            } catch (IOException | InterruptedException ex) {
                if (connectionClosed) {
                    if (me.isDead()) {
                        getGraphics().drawImage(imgLose, startX, startY, aWidth, aHeight, frame);
                    } else {
                        getGraphics().drawImage(imgWin, startX, startY, aWidth, aHeight, frame);
                    }
                    try {
                        p.setStoppedPlaying(true);
                        Thread.sleep(TIME_TO_WAIT);
                    } catch (InterruptedException ex1) {
                    }
                    frame.dispose();
                    selectOpponentFrame.setVisible(true);
                }
            }
        }
    }

    public class Repainter extends Thread //MANDADORA
    {

        @Override
        public void run() {
            byte[] enemyPack = new byte[844];
            for (; !connectionClosed;) {
                try {
                    repaint();
                    int count = 0;
                    int countBullets = me.getBullets().size();
                    int percTankY = (1000000 * me.getY()) / maxY;
                    if (bothDead && !connectionClosed) {
                        ConnectionManager.pack(1, enemyPack, count);
                        count += 4;
                        ConnectionManager.write(enemyPack);
                        ConnectionManager.flush();
                        ConnectionManager.close();
                        connectionClosed = true;
                    } else if (!connectionClosed) {
                        ConnectionManager.pack(0, enemyPack, count);
                        count += 4;
                        ConnectionManager.pack(percTankY, enemyPack, count);
                        count += 4;
                        ConnectionManager.pack(player2.getLife(), enemyPack, count);
                        count += 4;
                        ConnectionManager.pack(player2.getDebuff(), enemyPack, count);
                        count += 4;
                        ConnectionManager.pack((int) player2.getDebuffDuration(), enemyPack, count);
                        count += 4;
                        ConnectionManager.pack(countBullets, enemyPack, count);
                        count += 4;
                        for (int i = 0; i < countBullets && countBullets == me.getBullets().size(); i++) {
                            BulletInterface b;
                            try {
                                b = me.getBullets().get(i);
                            } catch (IndexOutOfBoundsException ie) {
                                break;
                            }
                            if (b != null) {
                                int percentageX = (1000000 * b.getX()) / maxX;
                                int percentageY = (1000000 * b.getY()) / maxY;
                                ConnectionManager.pack(percentageX, enemyPack, count);
                                count += 4;
                                ConnectionManager.pack(percentageY, enemyPack, count);
                                count += 4;
                                ConnectionManager.pack(b.getSize(), enemyPack, count);
                                count += 4;
                            }
                        }
                        ConnectionManager.write(enemyPack);
                        ConnectionManager.flush();
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    Thread.sleep(16);
                } catch (InterruptedException ex) {
                }
            }
            if (me.isDead()) {
                getGraphics().drawImage(imgLose, startX, startY, aWidth, aHeight, frame);
            } else {
                getGraphics().drawImage(imgWin, startX, startY, aWidth, aHeight, frame);
            }
            try {
                p.setStoppedPlaying(true);
                Thread.sleep(TIME_TO_WAIT);
            } catch (InterruptedException ex) {
            }
            selectOpponentFrame.setVisible(true);
            frame.dispose();
        }
    }

    public static int getMaxX() {
        return maxX;
    }

    public static int getMaxY() {
        return maxY;
    }

    public Arena(Player p, int x, int y, final int width, final int height, TankInterface me1, TankInterface player2, JFrame frame) throws IOException {

        this.frame = new JFrame();
        this.frame.setTitle("TANKINHO");
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.frame.setUndecorated(true);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.add(this);
        this.frame.setVisible(true);

        this.bothDead = false;
        this.connectionClosed = false;
        this.p = p;
        this.selectOpponentFrame = frame;

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        maxX = dimension.width;
        maxY = dimension.height; //barra de tarefas
        startX = x;
        startY = y;

        this.me = me1;
        this.player2 = player2;

        this.shrinkPerc = 0;
        this.maxUp = 0;
        this.maxDown = height;
        this.canShrink = false;
        this.initialShrinkTime = System.currentTimeMillis();
        this.width = maxX;
        this.height = maxY;
        this.aWidth = width;
        this.aHeight = height;

        this.timeCalcFPS = 300;

        defaultLeft = new Dimension(startX + 10, startY + (height / 2));
        defaultRight = new Dimension(startX + width - 70, startY + (height / 2));
        if (me.getSide() == 'l') {
            this.me.setY(defaultLeft.height);
            this.me.setX(defaultLeft.width);
            this.player2.setY(defaultRight.height);
            this.player2.setX(defaultRight.width);
        } else {
            this.me.setY(defaultRight.height);
            this.me.setX(defaultRight.width);
            this.player2.setY(defaultLeft.height);
            this.player2.setX(defaultLeft.width);
        }

        enemy = new EnemyInfo();

        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    me.setGoingUp(true);
                    me.setGoingDown(false);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    me.setGoingDown(true);
                    me.setGoingUp(false);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    me.setGoingDown(true);
                    me.setGoingUp(false);
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    me.setGoingUp(true);
                    me.setGoingDown(false);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    shooting = true;
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    me.setGoingUp(false);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    me.setGoingDown(false);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    me.setGoingDown(false);
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    me.setGoingUp(false);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    shooting = false;
                }
            }
        });
        imgArena = Toolkit.getDefaultToolkit().createImage("imgs/arena.jpg");
        imgWin = Toolkit.getDefaultToolkit().createImage("imgs/youWin.jpg");
        imgLose = Toolkit.getDefaultToolkit().createImage("imgs/youLOSE.jpg");

        //TRYING TO CONNECT TO SERVER-----------------------------------------------------------------------------FSFFAFAS
        Repainter r = new Repainter();
        r.start();
        Receptor r1 = new Receptor();
        r1.start();
        Fisicas f = new Fisicas();
        f.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        if ((System.currentTimeMillis() - lastFrame) > 8) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, maxX, maxY);
            g.setColor(this.getColor());

            if (player2.isDead() || me.isDead()) {
                if (player2.isDead()) {
                    g.drawImage(imgWin, startX, startY, aWidth, aHeight, this);
                } else {
                    g.drawImage(imgLose, startX, startY, aWidth, aHeight, this);
                }
                bothDead = true;

            } else {
                g.drawImage(imgArena, startX, startY, aWidth, aHeight, this);

                g.setColor(Color.BLUE);
                me.paint(g);

                g.setColor(Color.RED);
                player2.paint(g);

                g.setColor(Color.BLUE);
                for (int i = 0; i < me.getBullets().size(); i++) {
                    me.getBullets().get(i).paint(g);
                }
                g.setColor(Color.RED);
                for (int i = 0; i < countBullets; i++) {
                    g.fillOval(enemy.Xs[i], enemy.Ys[i], enemy.Ss[i], enemy.Ss[i]);
                }
                g.setColor(Color.black);
                g.fillRect(0, 0, aWidth, maxUp);
                g.fillRect(0, maxDown, aWidth, aHeight);
            }
            calcFps();
            g.setColor(new Color(0, 0, 0, 200));
            g.setFont(new Font("Monospaced", Font.BOLD, 13));
            g.setColor(Color.BLACK);
            g.drawString("FPS: " + String.format(Locale.ENGLISH, "%.1f", fps), 15, 25);
            lastFrame = System.currentTimeMillis();
        }
    }

    private void calcFps() {
        long timeDiff = System.currentTimeMillis() - lastSecond;
        if (timeDiff > timeCalcFPS) {
            lastSecond = System.currentTimeMillis();
            fps = (frameCounter * (1000.0 / (timeDiff)));
            frameCounter = 0;
        } else {
            frameCounter++;
        }
    }

    public boolean detectCollision(BulletInterface bullet) {
        if (bullet.getX() + bullet.getSize() >= player2.getX()
                && bullet.getX() <= player2.getX() + player2.getSize()) {
            if (bullet.getY() + bullet.getSize() >= player2.getY()
                    && bullet.getY() <= player2.getY() + player2.getSize()) {
                return true;
            }
        }
        return false;
    }

    public Dimension getDefaultLeft() {
        return defaultLeft;
    }

    public void setDefaultLeft(Dimension defaultLeft) {
        this.defaultLeft = defaultLeft;
    }

    public Dimension getDefaultRight() {
        return defaultRight;
    }

    public void setDefaultRight(Dimension defaultRight) {
        this.defaultRight = defaultRight;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Color getColor() {
        return colorArena;
    }

    public void setColor(Color colorArena) {
        this.colorArena = colorArena;
    }

}
