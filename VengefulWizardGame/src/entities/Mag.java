package entities;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;

public class Mag extends Enemy{
    private int attackBoxOffsetX;
    private boolean firstDeath= true;
    public Potion potion = null;
    private int rand = new Random().nextInt(3);
    private boolean lastEnemy= false;

    public Mag(float x, float y) {
        super(x, y, MAG_SCALE, MAG_SCALE, MAG);
        initHitbox(30,44);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,(int) (65 *Game.SCALE), (int) (20*Game.SCALE));
        attackBoxOffsetX = (int) (Game.SCALE*20);
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    private void updateBehavior(int[][] lvlData, Player player) {
        if(firstUpdate)
            firstUpdateCheck(lvlData);

        if(inAir)
            updateInAir(lvlData);
        else {
            switch (state) {
                case IDLE:
                    if(canSeePlayer(lvlData,player)) {
                        turnTowordsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if(aniIndex == 0)
                        attackChecked = false;

                    if(aniIndex==3 && !attackChecked)
                        checkPlayerHit(attackBox, player);
                    break;
                case DEAD:

                    if(rand == 1 && firstDeath && !lastEnemy) {
                        potion = new Potion(hitbox.x, hitbox.y, 32, 32);
                        potion.setActive(true);
                        firstDeath= false;
                    }
                    break;
            }
        }
    }

    public int flipX() {
        if(walkDir == RIGHT)
            return width;
        else
            return 0;
    }

    public int flipW() {
        if(walkDir == RIGHT)
            return -1;
        else
            return 1;
    }

    public int getState() {
        return state;
    }

    public void setLastEnemy(boolean last) {
        this.lastEnemy = last;
    }

    public void resetMags() {
        firstDeath=true;
        lastEnemy=false;
        potion=null;
        rand = new Random().nextInt(3);
    }
}
