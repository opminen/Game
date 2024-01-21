package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Heart extends Entity {
    public OBJ_Heart(GamePanel gp) {
        super(gp);
        super.name = "Heart";

        image = setup("/objects/heart_full.png");
        image1 = setup("/objects/heart_half.png");
        image2 = setup("/objects/heart_blank.png");

    }
}
