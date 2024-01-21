package main;

import entity.Entity;
import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    final int FPS = 60;

    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    Thread gameThread;
    KeyHandler keyHandler = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public Player player = new Player(this, keyHandler);
    public AssertSetter aSetter = new AssertSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    TileManager tileM = new TileManager(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Entity[] obj = new Entity[10];
    public Entity[] npc = new Entity[10];
    public Entity[] monster = new Entity[20];
    ArrayList<Entity> entityList = new ArrayList<>();

    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogState = 3;
    public final int characterState = 4;

    public GamePanel() {
        super.setPreferredSize(new Dimension(screenWidth, screenHeight));
        super.setBackground(Color.black);
        super.setDoubleBuffered(true);
        super.addKeyListener(keyHandler);
        super.setFocusable(true);
        super.setCursor(null);
    }

    public void setupGame() {
        this.aSetter.setObject();
        this.aSetter.setNPC();
        this.aSetter.setMonster();

//        this.playMusic(0);

        gameState = titleState;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lostTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lostTime) / drawInterval;
            timer += currentTime - lostTime;
            lostTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        if (gameState == playState) {
            player.update();
            for (Entity npcs : npc) {
                if (npcs != null) {
                    npcs.update();
                }
            }

            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    if (monster[i].alive) {
                        monster[i].update();
                    } else {
                        monster[i] = null;
                    }
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        super.paintComponent(g);

        if (gameState == titleState) {
            ui.draw(g2d);
        } else {

            tileM.draw(g2d);

            entityList.add(player);

            for (Entity ob : obj) {
                if (ob != null) entityList.add(ob);
            }

            for (Entity npcs : npc) {
                if (npcs != null) entityList.add(npcs);
            }

            for (Entity mon : monster) {
                if (mon != null) entityList.add(mon);
            }

            entityList.sort(Comparator.comparingInt(o -> o.worldY));

            entityList.forEach(el -> el.draw(g2d));

            entityList.clear();

            ui.draw(g2d);
        }

        g2d.dispose();
    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }
}
