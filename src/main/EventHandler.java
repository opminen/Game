package main;

import java.awt.*;

public class EventHandler {
    GamePanel gp;
    EventRect[][] eventRect;
    int previousEventX, previousEventY;
    boolean canTouchEvent = false;

    public EventHandler(GamePanel gp) {
        this.gp = gp;
        eventRect = new EventRect[gp.maxWorldRow][gp.maxWorldCol];
        for (int row = 0; row < gp.maxWorldRow; row++) {
            for (int col = 0; col < gp.maxWorldCol; col++) {
                eventRect[row][col] = new EventRect(23, 23, 2 , 2);
                eventRect[row][col].eventRectDefaultX = 23;
                eventRect[row][col].eventRectDefaultY = 23;
            }
        }
    }

    public void checkEvent() {
        int distance = Math.max(Math.abs(gp.player.worldX - previousEventX), Math.abs(gp.player.worldY - previousEventY));

        if (distance > gp.tileSize) {
            canTouchEvent = true;
        }

        if (canTouchEvent) {
            if (hit(27, 16, "right")) {
                damagePit(27, 16, gp.dialogState);
            } else if (hit(23, 12, "up")) {
                healingPool(gp.dialogState);
            }
        }
    }

    private void damagePit(int col, int row, int gameState) {
        if (!eventRect[row][col].eventDone) {
            gp.gameState = gameState;
            gp.ui.currentDialog = "You fall into a pit!";
            gp.player.life--;
//            eventRect[row][col].eventDone = true;
            canTouchEvent = false;
        }
    }

    private void healingPool(int gameState) {
        if (gp.keyHandler.enterPressed) {
            gp.player.attackCanceled = true;
            gp.gameState = gameState;
            gp.ui.currentDialog = "You drink the water.\nYour life has been recovered.";
            gp.player.life = gp.player.maxLife;
        }
        gp.keyHandler.enterPressed = false;
    }

    public boolean hit(int eventCol, int eventRow, String reqDirection) {
        boolean hit = false;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        this.eventRect[eventRow][eventCol].x = eventCol * gp.tileSize + this.eventRect[eventRow][eventCol].x;
        this.eventRect[eventRow][eventCol].y = eventRow * gp.tileSize + this.eventRect[eventRow][eventCol].y;

        if (gp.player.solidArea.intersects(this.eventRect[eventRow][eventCol])) {
            if (gp.player.directory.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                hit = true;
                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldY;
            }
        }

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        this.eventRect[eventRow][eventCol].x = eventRect[eventRow][eventCol].eventRectDefaultX;
        this.eventRect[eventRow][eventCol].y = eventRect[eventRow][eventCol].eventRectDefaultY;

        return hit;
    }
}
