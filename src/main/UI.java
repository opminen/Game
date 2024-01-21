package main;

import entity.Entity;
import object.OBJ_Heart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI {
    GamePanel gp;
    Graphics2D g2d;
    Font joystix;
    BufferedImage heart_full, heart_half, heart_blank;
    public boolean debuging = false;
    public String currentDialog = "";
    public int commandNum = 0;
    private int speedX, speedY;

    public UI(GamePanel gp) {
        this.gp = gp;

        gp.player.worldX = gp.tileSize * 8;
        gp.player.worldY = gp.tileSize * 6;

        speedX = 2;
        speedY = 1;

        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image1;
        heart_blank = heart.image2;

        getFonts();
    }

    public void draw(Graphics2D g2d) {
        this.g2d = g2d;

        g2d.setFont(joystix);
        g2d.setColor(Color.white);

        if (debuging) {
            debug();
        }
        
        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        else if (gp.gameState == gp.playState) {
            drawPlayerLife();
        } else if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
            drawPlayerLife();
        } else if (gp.gameState == gp.dialogState) {
            drawPlayerLife();
            drawDialogOnScreen();
        }

    }

    private void drawPlayerLife() {
        int x = gp.tileSize / 4;
        int y = gp.tileSize / 4;

        for (int i = 0; i < gp.player.maxLife / 2; i++) {
            g2d.drawImage(this.heart_blank, x, y, null);
            x += gp.tileSize;
        }

        x = gp.tileSize / 4;

        for (int i = 0; i < gp.player.life; i++) {
            g2d.drawImage(this.heart_half, x, y, null);
            i++;
            if (i < gp.player.life) {
                g2d.drawImage(this.heart_full, x, y, null);
            }
            x += gp.tileSize;
        }
    }

    private void drawTitleScreen() {
        gp.tileM.draw(g2d);
        if (gp.player.worldX >= gp.maxWorldCol * (gp.tileSize - 8) - 50 || gp.player.worldX < gp.tileSize * 8) speedX = -speedX;
        if (gp.player.worldY >= gp.maxWorldRow * (gp.tileSize - 6) - 50 || gp.player.worldY < gp.tileSize * 6) speedY = -speedY;
        gp.player.worldX += speedX;
        gp.player.worldY += speedY;

        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 44f));
        String text = "Blue Boy Adventure";
        int x = getXForCenterText(text);
        int y = gp.tileSize * 3;

        g2d.setColor(Color.gray);
        g2d.drawString(text, x + 5, y + 5);

        g2d.setColor(Color.white);
        g2d.drawString(text, x, y);

        x = gp.screenWidth / 2 - gp.tileSize;
        y += (int) (gp.tileSize * 1.5);

        g2d.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 24f));

        text = "NEW GAME";
        x = getXForCenterText(text);
        y += gp.tileSize * 4;
        g2d.drawString(text, x, y);
        if (commandNum == 0) {
            g2d.drawString(">", x - gp.tileSize, y);
        }

        text = "LOAD GAME";
        x = getXForCenterText(text);
        y += gp.tileSize - 10;
        g2d.drawString(text, x, y);
        if (commandNum == 1) {
            g2d.drawString(">", x - gp.tileSize, y);
        }

        text = "QUIT";
        x = getXForCenterText(text);
        y += gp.tileSize - 10;
        g2d.drawString(text, x, y);
        if (commandNum == 2) {
            g2d.drawString(">", x - gp.tileSize, y);
        }
    }

    private void drawDialogOnScreen() {
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - gp.tileSize * 4;
        int height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 18f));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialog.split("\n")) {
            g2d.drawString(line, x, y);
            y += 40;
        }
    }

    private void drawSubWindow(int x, int y, int width, int height) {
        Color bg = new Color(0, 0, 0, 200);
        g2d.setColor(bg);
        g2d.fillRoundRect(x, y, width, height, 35, 35);

        bg = new Color(255, 255, 255);
        g2d.setColor(bg);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    private void drawPauseScreen() {
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 40f));

        String text = "PAUSED";
        int x = getXForCenterText(text);
        int y = gp.screenHeight / 2;

        g2d.drawString(text, x, y);
    }

    public int getXForCenterText(String text) {
        return gp.screenWidth / 2 - (int)g2d.getFontMetrics().getStringBounds(text, g2d).getWidth() / 2;
    }

    public void debug() {
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 16f));

        String text = "invincible: " + gp.player.invincibleCounter;
        int x = 10;
        int y = 400;

        g2d.drawString(text, x, y);

        text = "WorldX: " + gp.player.worldX;
        y += 20;

        g2d.drawString(text, x, y);

        text = "WorldY: " + gp.player.worldY;
        y += 20;

        g2d.drawString(text, x, y);

        text = "Row: " + gp.player.worldY / gp.tileSize;
        y += 20;

        g2d.drawString(text, x, y);

        text = "Col: " + gp.player.worldX / gp.tileSize;
        y += 20;

        g2d.drawString(text, x, y);
    }

    private void getFonts() {
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/joystix.ttf");
            joystix = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
