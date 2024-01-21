package tile;

import main.GamePanel;
import main.UtilityTools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TileManager {
    GamePanel gp;
    public Tile[] tiles;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tiles = new Tile[50];
        getTileImage();

        mapTileNum = new int[gp.maxWorldRow][gp.maxWorldCol];
        loadMap("/maps/map1.txt");
    }

    private void getTileImage() {
        setup(0, "grass00", false);
        setup(1, "grass00", true);
        setup(2, "grass00", true);
        setup(3, "grass00", false);
        setup(4, "grass00", true);
        setup(5, "grass00", false);
        setup(6, "grass00", false);
        setup(7, "grass00", false);
        setup(8, "grass00", false);
        setup(9, "grass00", false);

        setup(10, "grass00", false);
        setup(11, "grass01", false);
        setup(12, "water00", true);
        setup(13, "water01", true);
        setup(14, "water02", true);
        setup(15, "water03", true);
        setup(16, "water04", true);
        setup(17, "water05", true);
        setup(18, "water06", true);
        setup(19, "water07", true);
        setup(20, "water08", true);
        setup(21, "water09", true);
        setup(22, "water10", true);
        setup(23, "water11", true);
        setup(24, "water12", true);
        setup(25, "water13", true);
        setup(26, "road00", false);
        setup(27, "road01", false);
        setup(28, "road02", false);
        setup(29, "road03", false);
        setup(30, "road04", false);
        setup(31, "road05", false);
        setup(32, "road06", false);
        setup(33, "road07", false);
        setup(34, "road08", false);
        setup(35, "road09", false);
        setup(36, "road10", false);
        setup(37, "road11", false);
        setup(38, "road12", false);
        setup(39, "earth", false);
        setup(40, "wall", true);
        setup(41, "tree", true);
    }

    private void setup(int index, String imageName, boolean collision) {
        UtilityTools uTool = new UtilityTools();

        try {
            this.tiles[index] = new Tile();
            this.tiles[index].image = uTool.scaleImage(ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png")), gp.tileSize, gp.tileSize);
            this.tiles[index].collision = collision;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String map) {
        try {
            InputStream is = getClass().getResourceAsStream(map);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (int row = 0; row < gp.maxWorldRow; row++) {
                String line = br.readLine();
                for (int col = 0; col < gp.maxWorldCol; col++) {
                    String[] numbers = line.split(" ");

                    this.mapTileNum[row][col] = Integer.parseInt(numbers[col]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2d) {
        for (int row = 0; row < gp.maxWorldRow; row++) {
            for (int col = 0; col < gp.maxWorldCol; col++) {
                if (col * gp.tileSize + gp.tileSize > gp.player.worldX - gp.player.screenX
                    && col * gp.tileSize - gp.tileSize < gp.player.worldX + gp.player.screenX
                    && row * gp.tileSize + gp.tileSize > gp.player.worldY - gp.player.screenY
                        && row * gp.tileSize - gp.tileSize < gp.player.worldY + gp.player.screenY)
                    g2d.drawImage(tiles[this.mapTileNum[row][col]].image, col * gp.tileSize - gp.player.worldX + gp.player.screenX, row * gp.tileSize - gp.player.worldY + gp.player.screenY, null);
            }
        }
    }
}
