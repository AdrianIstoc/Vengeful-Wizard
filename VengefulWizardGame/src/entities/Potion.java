package entities;

import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilz.Constants.EnemyConstants.GetEnemyDmg;
import static utilz.Constants.GRAVITY;
import static utilz.HelpMethods.*;

public class Potion extends Entity{

    private float XDrawOffset = 8 * Game.SCALE;
    private float YDrawOffset = 13 * Game.SCALE;
    private boolean active = false;
    private int activeTime = 1000;
    private boolean firstUpdate = true;

    private BufferedImage potionImg;
    public Potion(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadImg();
        initHitbox(16,15);
    }

    public void update(int[][] lvlData) {
        if(active)
            updateActiveTime();
        /*if(firstUpdate)
            potionFirstUpdateCheck(lvlData);

        if(inAir)
            updatePotionInAir(lvlData);*/
    }
    private void potionFirstUpdateCheck(int[][] lvlData) {
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
        firstUpdate = false;
    }

    private void updatePotionInAir(int[][] lvlData) {
        if(CanMoveHere(hitbox.x, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        }
        else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
        }
    }

    public void draw(Graphics g, int lvlOffset) {
        g.drawImage(potionImg, (int)(hitbox.x - lvlOffset - XDrawOffset), (int)(hitbox.y - YDrawOffset), width,height,null);
    }

    private void loadImg() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        potionImg = temp.getSubimage(4*32,2*32, 32, 32);
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    private void updateActiveTime(){
        activeTime--;

        if (activeTime==0) {
            active=false;
        }
    }
    public void potionIntersect(Rectangle2D.Float hitbox, Player player) {
        if(hitbox.intersects(player.hitbox)) {
            active = false;
            player.changeHealth(1);
        }
    }
}
