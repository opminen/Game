package entity;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword;

import java.awt.*;


public class Player extends Entity {
    KeyHandler keyH;

    public int screenX;
    public int screenY;
    public boolean attackCanceled = false;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        super.worldX = gp.tileSize * 23;
        super.worldY = gp.tileSize * 21;
        super.speed = 4;
        this.keyH = keyH;

        maxLife = 6;
        life = maxLife;

        screenX = gp.screenWidth / 2 - gp.tileSize / 2;
        screenY = gp.screenHeight / 2 - gp.tileSize / 2;

        super.solidArea = new Rectangle(8, 16, 32, 32);
        this.solidAreaDefaultX = 8;
        this.solidAreaDefaultY = 16;
        super.attackSolidArea.width = 36;
        super.attackSolidArea.height = 36;

        level = 1;
        strength = 1;
        dexterity = 1;
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentWeapon = new OBJ_Sword(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        attack = getAttack();
        defense = getDefense();

        getPlayerImage();
    }

    private int getAttack() {
        return strength * currentWeapon.attack;
    }

    private int getDefense() {
        return dexterity * currentShield.defense;
    }

    private void getPlayerImage() {
        super.up1 = setup("/player/boy_up_1.png");
        super.up2 = setup("/player/boy_up_2.png");
        super.down1 = setup("/player/boy_down_1.png");
        super.down2 = setup("/player/boy_down_2.png");
        super.left1 = setup("/player/boy_left_1.png");
        super.left2 = setup("/player/boy_left_2.png");
        super.right1 = setup("/player/boy_right_1.png");
        super.right2 = setup("/player/boy_right_2.png");

        super.attackUp1 = uTool.scaleImage(setup("/player/boy_attack_up_1.png"), gp.tileSize, gp.tileSize * 2);
        super.attackUp2 = uTool.scaleImage(setup("/player/boy_attack_up_2.png"), gp.tileSize, gp.tileSize * 2);
        super.attackDown1 = uTool.scaleImage(setup("/player/boy_attack_down_1.png"), gp.tileSize, gp.tileSize * 2);
        super.attackDown2 = uTool.scaleImage(setup("/player/boy_attack_down_2.png"), gp.tileSize, gp.tileSize * 2);
        super.attackLeft1 = uTool.scaleImage(setup("/player/boy_attack_left_1.png"), gp.tileSize * 2, gp.tileSize);
        super.attackLeft2 = uTool.scaleImage(setup("/player/boy_attack_left_2.png"), gp.tileSize * 2, gp.tileSize);
        super.attackRight1 = uTool.scaleImage(setup("/player/boy_attack_right_1.png"), gp.tileSize * 2, gp.tileSize);
        super.attackRight2 = uTool.scaleImage(setup("/player/boy_attack_right_2.png"), gp.tileSize * 2, gp.tileSize);
    }

    public void pickUpObject(int index) {
        if (index != 999) {
            //
        }
    }

    public void interactNPC(int index) {
        if (keyH.enterPressed) {
            if (index != 999) {
                attackCanceled = true;
                gp.gameState = gp.dialogState;
                gp.npc[index].speak();
                keyH.enterPressed = false;
            }
        }
    }

    private void contactMonster(int index) {
        if (index != 999 && !invincible) {
            gp.playSE(6);
            life--;
            invincible = true;
        }
    }

    private void damageMonster(int monsterIndex) {
        if (monsterIndex != 999) {
            if (!gp.monster[monsterIndex].invincible) {
                gp.playSE(5);
                gp.monster[monsterIndex].damageReaction();
                gp.monster[monsterIndex].life--;
                gp.monster[monsterIndex].invincible = true;

                if (gp.monster[monsterIndex].life <= 0) {
                    gp.monster[monsterIndex].dying = true;
                }
            }
        }
    }

    @Override
    public void update() {
        if (attacking) {
            attacking();
        } else if (keyH.upPressed || keyH.downPressed || keyH.rightPressed || keyH.leftPressed || keyH.enterPressed) {
            if (keyH.upPressed) {
                super.directory = "up";
            }
            if (keyH.downPressed) {
                super.directory = "down";
            }
            if (keyH.rightPressed) {
                super.directory = "right";
            }
            if (keyH.leftPressed) {
                super.directory = "left";
            }

            this.collisionOn = false;

            this.interactNPC(gp.cChecker.checkEntity(this, gp.npc));
            this.pickUpObject(gp.cChecker.checkObject(this, true));
            this.contactMonster(gp.cChecker.checkEntity(this, gp.monster));

            gp.eHandler.checkEvent();

            if (keyH.enterPressed && !attackCanceled) {
                gp.playSE(7);
                attacking = true;
                spriteCount = 0;
            }

            attackCanceled = false;
            keyH.enterPressed = false;

            super.update();
        }
    }

    private void attacking() {
        spriteCount++;

        if (spriteCount <= 5) {
            spriteNum = 1;
        }
        if (spriteCount > 5 && spriteCount <= 25) {
            spriteNum = 2;

            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            switch (directory) {
                case "up" -> worldY -= attackSolidArea.height;
                case "down" -> worldY += attackSolidArea.height;
                case "left" -> worldX -= attackSolidArea.width;
                case "right" -> worldX += attackSolidArea.width;
            }

            solidArea.width = attackSolidArea.width;
            solidArea.height = attackSolidArea.height;

            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex);

            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        if (spriteCount > 25) {
            spriteNum = 1;
            spriteCount = 0;
            attacking = false;
        }
    }
}
