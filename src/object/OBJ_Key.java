package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity {
    public OBJ_Key(GamePanel gp) {
        super(gp);

        super.name = "Key";
        down1 = setup("/objects/key.png");
    }
}
