package entity;

import main.GamePanel;

import java.util.Random;

public class NPC_OldMan extends Entity {
    public NPC_OldMan(GamePanel gp) {
        super(gp);

        speed = 1;

        getPlayerImage();
        setDialog();
    }

    private void getPlayerImage() {
        super.up1 = setup("/npc/oldman_up_1.png");
        super.up2 = setup("/npc/oldman_up_2.png");
        super.down1 = setup("/npc/oldman_down_1.png");
        super.down2 = setup("/npc/oldman_down_2.png");
        super.left1 = setup("/npc/oldman_left_1.png");
        super.left2 = setup("/npc/oldman_left_2.png");
        super.right1 = setup("/npc/oldman_right_1.png");
        super.right2 = setup("/npc/oldman_right_2.png");
    }

    public void setDialog() {
        dialogues[0] = "Привіт вояче!";
        dialogues[1] = "Ти тепер знаходшся на острові.\nЩоб вибратися ти повинен...\nУбити короля монстрів!";
        dialogues[2] = "Будь обережним.\nМонстри по всюду.";
        dialogues[3] = "Удачі тобі в битвах!";
    }

    @Override
    public void setAction() {
        actionCounter++;

        if (actionCounter > 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if (i <= 25) {
                directory = "up";
            } else if (i <= 50) {
                directory = "down";
            } else if (i <= 75) {
                directory = "left";
            } else {
                directory = "right";
            }
            actionCounter = 0;
        }
    }

    @Override
    public void speak() {

        super.speak();
    }
}
