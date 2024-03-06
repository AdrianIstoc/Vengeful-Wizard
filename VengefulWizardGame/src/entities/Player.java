package entities;

import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.GRAVITY;
import static utilz.HelpMethods.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class Player extends Entity{

    private boolean lastDirWasRight = true;

    private BufferedImage[][] animations;
    private int aniSpeed = 15;
    private boolean moving=false, attacking = false;
    private boolean left, right, jump;
    private int[][] lvlData;
    private float xDrawOffset = 28 * Game.SCALE;
    private float yDrawOffset = 6 * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

    // Status bar UI
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (32*7*Game.SCALE);
    private int statusBarHeight = (int) (32*Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 *Game.SCALE);

    private int healthWidth = statusBarWidth;

    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked=false;
    private Playing playing;

    //BG
    private BufferedImage BGimg;

    private int score = 0;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x,y,width,height);
        this.playing=playing;
        this.state = IDLE;
        this.maxHealth = 7;
        this.currentHealth = maxHealth;
        this.walkSpeed = 2.0f * Game.SCALE;
        loadAnimations();
        initHitbox(12, 54);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,(int) 20 *Game.SCALE, (int) 20*Game.SCALE);
    }

    public void update() {
        updateHealthBar();

        if(currentHealth<=0) {
            playing.setGameOver(true);
            System.out.println(score);
            LoadSave.insertB("Data", "LeaderScore", score);
            return;
        }

        updateAttackBox();

        updatePosition();
        if(attacking)
            checkAttack();
        updateAnimationTick();
        setAnimation();
    }

    public boolean checkPlayerOnSpikes() {
        if(IsEntityOnFloor(hitbox,lvlData)) {
            if(IsTileSpikes((int) hitbox.x, (int) hitbox.y, lvlData)) {
                return true;
            }
        }

        return false;
    }

    private void checkAttack() {
        if(attackChecked || aniIndex != 1)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
    }

    private void updateAttackBox() {
        if(right) {
            attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE *10);
        }else if(left) {
            attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE *18);
        }
        attackBox.y = hitbox.y + (Game.SCALE *10);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * statusBarWidth);
    }

    public void render(Graphics g, int lvlOffset) {
        if(state==IDLE && !lastDirWasRight)
            g.drawImage(animations[state][aniIndex],
                (int)(hitbox.x - xDrawOffset) - lvlOffset +flipX,
                (int)(hitbox.y - yDrawOffset),
                width * flipW,
                height,
                null);
        else
            g.drawImage(animations[state][aniIndex],
                    (int)(hitbox.x - xDrawOffset) - lvlOffset,
                    (int)(hitbox.y - yDrawOffset),
                    width,
                    height,
                    null);

        //drawHitbox(g,lvlOffset);
        //drawAttackBox(g, lvlOffset);

        drawUI(g);
    }

    public void drawBG(Graphics g) {
        g.drawImage(BGimg,0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
    }

    private void drawUI(Graphics g) {

        g.setColor(Color.red);
        g.fillRect(statusBarX,statusBarY,healthWidth,statusBarHeight);
        g.drawImage(statusBarImg,statusBarX,statusBarY,statusBarWidth,statusBarHeight, null);
    }

    private void updateAnimationTick() {
        aniTick++;
        if(aniTick>=aniSpeed)
        {
            aniTick=0;
            aniIndex++;
            if(!(state==JUMP_RIGHT || state==JUMP_LEFT)) {
                if (aniIndex >= GetSpriteAmount(state)) {
                    aniIndex = 0;
                    attacking = false;
                    attackChecked = false;
                }
            } else if (aniIndex > GetSpriteAmount(state)-1){
                aniIndex--;
            }
        }
    }

    public void setAnimation() {
        int startAni = state;

        if(moving) {
            if(left) {
                state = WALK_LEFT;
                lastDirWasRight=false;
            } else if(right) {
                state = WALK_RIGHT;
                lastDirWasRight=true;
            }
        }
        else {
            /*if(playerAction == WALK_LEFT || playerAction == SHOOT_LEFT) {
                playerAction = WALK_LEFT;
            }
            else {*/
            state = IDLE;
            //}
        }

        if(inAir) {
            if(!lastDirWasRight || left) {
                state = JUMP_LEFT;
                lastDirWasRight=false;
            } else {
                state = JUMP_RIGHT;
                lastDirWasRight=true;
            }
        }

        if(attacking)
        {
            if(inAir) {
                if (!lastDirWasRight || left) {
                    state = JUMP_SHOOT_LEFT;
                    lastDirWasRight=false;
                } else {
                    state = JUMP_SHOOT_RIGHT;
                    lastDirWasRight=true;
                }
            } else {
                if (!lastDirWasRight || left) {
                    state = SHOOT_LEFT;
                    lastDirWasRight=false;
                } else {
                    state = SHOOT_RIGHT;
                    lastDirWasRight=true;
                }
            }
        }

        if(startAni != state) {
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniIndex = 0;
    }

    private void updatePosition() {
        moving = false;
        if(jump)
            jump();


        /*if(!left && !right && !inAir)
            return;*/

        if(!inAir)
            if((!left && !right) || (right && left))
                return;

        float xSpeed=0;

        if(left) {
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
        if(right) {
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }

        if(!inAir) {
            if(!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }

        if(inAir) {
            if(CanMoveHere(hitbox.x,hitbox.y+airSpeed,hitbox.width,hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {
                if(IsEntityOnFloor(hitbox, lvlData)) {
                    hitbox.y -= 1;
                }
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox,airSpeed);
                if(airSpeed > 0) {
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }
        } else {
            updateXPos(xSpeed);
        }

        moving = true;
    }

    private void jump() {
        if(inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitbox.x+xSpeed,hitbox.y,hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    public void changeHealth(int value) {
        currentHealth += value;

        if(currentHealth<=0) {
            currentHealth = 0;
            //gameOver();
        } else if(currentHealth >= maxHealth)
            currentHealth = maxHealth;
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[9][7];
        for(int j = 0; j<animations.length;++j){
            for(int i = 0; i<animations[j].length; ++i)
            {
                animations[j][i] = img.getSubimage(i*64, j*64, 64, 64);
            }
        }

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.HEALTH_BAR);
        BGimg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitbox, lvlData)) {
            inAir = true;
        }
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isLeft(){
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        state= IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y= y;

        if(!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void setCurrentHealth(int health){
        currentHealth = health;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
