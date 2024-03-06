package entities;
import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;
import static utilz.Constants.GRAVITY;
public abstract class Enemy extends Entity{

    protected int enemyType;
    protected int aniSpeed = 25;
    protected boolean firstUpdate = true;
    protected int walkDir = LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;


    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        this.state = IDLE;
        maxHealth=GetMaxHealt(enemyType);
        currentHealth = maxHealth;
        walkSpeed = 0.5f * Game.SCALE;
    }

    protected void firstUpdateCheck(int[][] lvlData) {
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
        firstUpdate = false;
    }

    protected void updateInAir(int[][] lvlData) {
        if(CanMoveHere(hitbox.x, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        }
        else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            tileY = (int) (hitbox.y/Game.TILES_SIZE);
        }
    }

    protected void move(int[][] lvlData) {
        float xSpeed = 0;
        if(walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if(IsFloor(hitbox,xSpeed,lvlData)) {
                hitbox.x += xSpeed;
                return;
            }

        changeWalkDir();
    }

    protected void turnTowordsPlayer(Player player) {
        if(player.hitbox.x > hitbox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        int playerTileY =(int) (player.getHitbox().y/Game.TILES_SIZE);
        if(playerTileY == tileY)
           if(isPlayerInRange(player)) {
               if (IsSightClear(lvlData, hitbox,player.hitbox,tileY))
                   return true;
           }

        return false;
    }

    protected boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance*5;
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance;
    }

    protected void newState(int enemyState) {
        this.state = enemyState;
        aniTick=0;
        aniIndex=0;
    }

    public void hurt(int amount) {
        currentHealth -= amount;
        if(currentHealth <=0)
            newState(DEAD);
    }

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
        if(attackBox.intersects(player.hitbox))
            player.changeHealth(-GetEnemyDmg(enemyType));

        attackChecked = true;
    }

    protected void updateAnimationTick() {
        aniTick++;
        if(aniTick >= aniSpeed) {
            aniTick=0;
            aniIndex++;
            if(aniIndex >=GetSpriteAmount(enemyType,state)) {
                aniIndex=0;

                switch (state){
                    case ATTACK -> state = IDLE;
                    case DEAD -> active = false;
                }

                /*if(enemyState==ATTACK)
                    enemyState = IDLE;
                else if(enemyState==DEAD)
                    active = false;*/
            }
        }
    }

    protected void changeWalkDir() {
        if(walkDir == LEFT)
            walkDir =RIGHT;
        else
            walkDir=LEFT;
    }

    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y=y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active= true;
        airSpeed = 0;
    }

    public boolean isActive() {
        return active;
    }
}