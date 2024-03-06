package levels;

import gamestates.Gamestate;
import main.Game;
import ui.GameWonOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int lvlIndex = 0;

    public LevelManager(Game game) {
        this.game=game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    public void resetLevels() {
        lvlIndex = -1;
        loadNextLevel();
    }

    public void loadNextLevel() {
        lvlIndex++;
        if(lvlIndex<=levels.size())
            game.getPlaying().getPlayer().addScore(lvlIndex*1000);
        if(lvlIndex>=levels.size()) {
            for(int i = 0; i<game.getPlaying().getPlayer().getCurrentHealth(); ++i)
            {
                game.getPlaying().getPlayer().addScore(100);
            }
            game.getPlaying().setGameWon(true);
            lvlIndex=0;
        }

        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLvlData());
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
    }


    //cream array cu toate nivelele
    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for(BufferedImage img : allLevels)
            levels.add(new Level(img));
    }

    //cream vector cu tiles-uri
    public void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[24];
        for(int j = 0; j<4; j++) {
            for(int i = 0; i<6; i++) {
                int index = j*6 + i;
                levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
            }
        }
    }

    public void draw(Graphics g, int lvlOffset) {
        for(int j = 0; j<Game.TILES_IN_HEIGHT;j++) {
            for(int i = 0; i<levels.get(lvlIndex).getLvlData()[0].length;i++) {
                int index = levels.get(lvlIndex).getSpriteIndex(i, j);
                g.drawImage(levelSprite[index],Game.TILES_SIZE*i - lvlOffset, Game.TILES_SIZE*j, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
        }
    }

    public void update() {

    }

    public Level getCurrentLevel() {
        return levels.get(lvlIndex);
    }

    public int getAmountOfLevels() {
        return levels.size();
    }

    public void setLvlIndex(int lvlIndex) {
        this.lvlIndex = lvlIndex;
    }

}
