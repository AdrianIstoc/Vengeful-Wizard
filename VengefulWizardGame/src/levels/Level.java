package levels;

import entities.Mag;
import main.Game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.HelpMethods.GetLevelData;
import static utilz.HelpMethods.GetMags;

public class Level {

    private BufferedImage img;
    private int[][] level;
    private int[][] lvlData;
    private ArrayList<Mag> mags;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;

    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        calcLvlOffset();
    }

    /*public Level(int[][] level) {
        this.level = level;
        createLevelData();
        createEnemies();
        calcLvlOffset();
    }*/

    private void calcLvlOffset() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        mags = GetMags(img);
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLvlData() {
        return lvlData;
    }

    public int getLvlOffset() {
        return maxLvlOffsetX;
    }

    public ArrayList<Mag> getMags() {
        return mags;
    }
}
