package monster;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class MON_GreenSlime extends Entity {
    public MON_GreenSlime(GamePanel gp) {
        super(gp);

        name = "Green Slime";
        speed = 1;
        maxLife = 4;
        life = maxLife;
        type = 2;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    private void getImage() {
        up1 = setup("/monster/greenslime_down_1.png");
        up2 = setup("/monster/greenslime_down_2.png");
        down1 = setup("/monster/greenslime_down_1.png");
        down2 = setup("/monster/greenslime_down_2.png");
        left1 = setup("/monster/greenslime_down_1.png");
        left2 = setup("/monster/greenslime_down_2.png");
        right1 = setup("/monster/greenslime_down_1.png");
        right2 = setup("/monster/greenslime_down_2.png");
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
    public void damageReaction() {
        actionCounter = 0;
        directory = gp.player.directory;
    }
}
