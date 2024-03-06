package entities;


import gamestates.Playing;
import levels.Level;
import ui.LevelCompletedOverlay;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] magArr;
    private ArrayList<Mag> mags = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        mags = level.getMags();
    }

    public void update(int[][] lvlData, Player player) {
        boolean isAnyActive = false;
        for(Mag m : mags) {
            int nr = numberActivEnemies();
            if(nr == 1)
                m.setLastEnemy(true);
            if (m.isActive()) {
                m.update(lvlData, player);
                isAnyActive = true;
            }
            if(m.potion != null) {
                if (m.potion.isActive()) {
                    m.potion.update(lvlData);
                    m.potion.potionIntersect(m.potion.getHitbox(), player);
                }
                else
                    m.potion = null;
            }
        }


        if(!isAnyActive)
            playing.setLevelCompleted(true);
    }

    public void draw(Graphics g,int xLvlOffset) {
        drawMags(g,xLvlOffset);
        drawPotions(g,xLvlOffset);
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for(Mag m : mags)
            if(m.potion != null)
                if(m.potion.isActive())
                {
                    m.potion.draw(g,xLvlOffset);
                    //m.potion.drawHitbox(g,xLvlOffset);
                }
    }

    private void drawMags(Graphics g,int xLvlOffset) {
        for(Mag m : mags)
            if(m.isActive())
            {
                g.drawImage(magArr[m.getEnemyState()][m.getAniIndex()],
                    (int) m.getHitbox().x - MAG_DRWOFFSET_X - xLvlOffset + m.flipX(),
                    (int) m.getHitbox().y - MAG_DRWOFFSET_Y,
                    MAG_SCALE * m.flipW(),
                    MAG_SCALE,
                    null);
                //m.drawHitbox(g,xLvlOffset);
                //m.drawAttackBox(g,xLvlOffset);
            }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for(Mag m : mags)
            if(m.getEnemyState() != DEAD)
                if(m.isActive())
                    if(attackBox.intersects(m.getHitbox())) {
                        m.hurt(1);
                        return;
                    }
    }

    private void loadEnemyImgs() {
        magArr = new BufferedImage[3][6];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.ENEMY_SPRITE);
        for(int j = 0; j<magArr.length; j++)
            for(int i = 0; i<magArr[j].length;i++)
                magArr[j][i]=temp.getSubimage(i*MAG_SCALE_DEFALUT, j*MAG_SCALE_DEFALUT, MAG_SCALE_DEFALUT, MAG_SCALE_DEFALUT);
    }

    public void resetAllEnemies() {
        for(Mag m : mags) {
            m.resetEnemy();
            m.resetMags();
        }
    }

    public int numberActivEnemies() {
        int temp = 0;
        for(Mag m : mags)
            if(m.isActive())
                temp++;

        return temp;
    }
}
