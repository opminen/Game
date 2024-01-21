package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword extends Entity {
    public OBJ_Sword(GamePanel gp) {
        super(gp);

        name = "Normal Sword";
        down1 = setup("/objects/sword_normal.png");
        attackValue = 1;
    }
}
