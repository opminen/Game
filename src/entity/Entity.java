package entity;

import main.GamePanel;
import main.UtilityTools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {

    public GamePanel gp;
    public int worldX, worldY;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public BufferedImage image, image1, image2;

    public boolean collision = false;
    public boolean invincible = false;
    public boolean collisionOn = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    public boolean hpBarOn = false;

    public int invincibleCounter = 0;
    public int spriteCount = 0;
    public int actionCounter = 0;
    public int dyingCounter = 0;
    public int spriteNum = 1;
    public int hpBarCounter = 0;
    int dialogIndex = 0;

    public int type;
    public String name;
    public int speed;
    public int maxLife;
    public int life;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentShield;

    public int attackValue;
    public int defenseValue;


    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackSolidArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public String directory = "down";

    public UtilityTools uTool = new UtilityTools();
    String[] dialogues = new String[20];

    public Entity(GamePanel gp) {
        this.gp = gp;
        solidAreaDefaultY = 0;
        solidAreaDefaultX = 0;
    }
    public void setAction() {}
    public void damageReaction() {}
    public void speak() {
        if (dialogues[dialogIndex] == null) dialogIndex = 0;
        gp.ui.currentDialog = dialogues[dialogIndex];
        dialogIndex++;

        switch (gp.player.directory) {
            case "up" -> directory = "down";
            case "down" -> directory = "up";
            case "left" -> directory = "right";
            case "right" -> directory = "left";
        }
    }
    public void update() {
        setAction();

        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        if (gp.cChecker.checkPlayer(this) && type == 2) {
            if (!gp.player.invincible) {
                gp.player.life--;
                gp.player.invincible = true;
            }
        }

        if (!this.collisionOn && !dying) {
            switch (this.directory) {
                case "up" -> this.worldY -= this.speed;
                case "down" -> this.worldY += this.speed;
                case "left" -> this.worldX -= this.speed;
                case "right" -> this.worldX += this.speed;
            }
        }

        spriteCount++;
        if (spriteCount > 12) {
            spriteNum = spriteNum == 1 ? 2 : 1;
            spriteCount = 0;
        }

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        this.collisionOn = false;
    }

    private void dyingAnimation(Graphics2D g2d) {
        dyingCounter++;

        int i = 5;

        if (dyingCounter <= i) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        } else if (dyingCounter <= i*2) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        } else if (dyingCounter <= i*3) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        } else if (dyingCounter <= i*4) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        } else if (dyingCounter <= i*5) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        } else if (dyingCounter <= i*6) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        } else if (dyingCounter <= i*7) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        }
        if (dyingCounter > i*8) {
            dying = false;
            alive = false;
        }

    }

    public void draw(Graphics2D g2d) {
        int tempScreenX = gp.player.screenX;
        int tempScreenY = gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
                && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
                && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            BufferedImage image = null;
            switch (directory) {
                case "up" -> {
                    if (!attacking) {
                        image = spriteNum == 1 ? up1 : up2;
                    }
                    else {
                        tempScreenY = gp.player.screenY - gp.tileSize;
                        image = (spriteNum == 1) ? attackUp1 : attackUp2;
                    }
                }
                case "down" -> {
                    if (!attacking) {
                        image = spriteNum == 1 ? down1 : down2;
                    }
                    else {
                        image = (spriteNum == 1) ? attackDown1 : attackDown2;
                    }
                }
                case "right" -> {
                    if (!attacking) {
                        image = spriteNum == 1 ? right1 : right2;
                    }
                    else {
                        image = (spriteNum == 1) ? attackRight1 : attackRight2;
                    }
                }
                case "left" -> {
                    if (!attacking) {
                        image = spriteNum == 1 ? left1 : left2;
                    }
                    else {
                        tempScreenX = gp.player.screenX - gp.tileSize;
                        image = (spriteNum == 1) ? attackLeft1 : attackLeft2;
                    }
                }
            };

            int screenX = worldX - gp.player.worldX + tempScreenX;
            int screenY = worldY - gp.player.worldY + tempScreenY;

            if (type == 2 && hpBarOn) {
                double oneScale = (double) gp.tileSize / maxLife;
                double hpBarValue = oneScale * life;

                g2d.setColor(new Color(35, 35, 35));
                g2d.fillRect(screenX - 1, screenY - 16, gp.tileSize+2, 10);
                g2d.setColor(new Color(255, 0, 30));
                g2d.fillRect(screenX, screenY - 15, (int) hpBarValue, 8);

                hpBarCounter++;

                if (hpBarCounter > 600) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if (invincible) {
                hpBarOn = true;
                hpBarCounter = 0;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            }

            if (dying) {
                dyingAnimation(g2d);
            }

            g2d.drawImage(image, worldX, worldY, null);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        }
    }

    protected BufferedImage setup(String imagePath) {
        BufferedImage img = null;
        try {
            img = uTool.scaleImage(ImageIO.read(getClass().getResourceAsStream(imagePath)), gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
}
