package gamestates;

import entities.Enemy;
import entities.EnemyManager;
import entities.Player;
import entities.Potion;
import levels.LevelManager;
import main.Game;
import ui.GameOverOverlay;
import ui.GameWonOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class Playing extends State implements Statemethods{
    private Player player;
    public LevelManager levelManager;
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private GameWonOverlay gameWonOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private boolean paused = false;

    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_HEIGHT);
    private int maxLvlOffsetX;

    private boolean gameOver;
    private boolean lvlCompleted = false;
    private boolean gameWon;
    private boolean gameWonScore=false;

    public Playing(Game game) {
        super(game);
        initClasses();

        caclLvlOffset();
        loadStartLevel();
    }

    public void loadNextLevel() {
        int tempHealth = player.getCurrentHealth();
        resetAll();
        player.setCurrentHealth(tempHealth);
        levelManager.loadNextLevel();
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
    }

    private void caclLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = new Player(200*Game.SCALE, 200*Game.SCALE, (int) (2 * Game.TILES_SIZE),(int) (2 * Game.TILES_SIZE), this);
        player.loadLvlData(levelManager.getCurrentLevel().getLvlData());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        gameWonOverlay = new GameWonOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }

    @Override
    public void update() {
        if(paused) {
            pauseOverlay.update();
        }
        else if(lvlCompleted) {
            levelCompletedOverlay.update();
        }
        else if(!gameOver && !gameWon){
            levelManager.update();
            if(player.checkPlayerOnSpikes())
                //gameOver = true;
                player.changeHealth(-1);
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLvlData(), player);
            checkCloseToBorder();
        }
    }


    // if close to border move "camera"
    //lvlOffset creste sau scade in functie de directia in care se intreapta player-ul
    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if(diff > rightBorder)
            xLvlOffset += diff - rightBorder;

        else if(diff<leftBorder)
            xLvlOffset += diff - leftBorder;

        if(xLvlOffset > maxLvlOffsetX)
            xLvlOffset=maxLvlOffsetX;
        else if (xLvlOffset<0)
            xLvlOffset=0;


    }

    @Override
    public void draw(Graphics g) {
        player.drawBG(g);
        levelManager.draw(g,xLvlOffset);
        player.render(g,xLvlOffset);
        enemyManager.draw(g,xLvlOffset);

        if(paused) {
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if(gameOver)
            gameOverOverlay.draw(g);
        else if(gameWon)
            gameWonOverlay.draw(g);
        else if(lvlCompleted)
            levelCompletedOverlay.draw(g);
    }



    public void resetAll() {
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        gameWon = false;
        player.resetAll();
        enemyManager.resetAllEnemies();

    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!gameOver) {
            if(paused)
                pauseOverlay.mousePressed(e);
            else if(lvlCompleted)
                levelCompletedOverlay.mousePressed(e);
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseReleased(e);
            }
            else if (lvlCompleted)
                levelCompletedOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!gameOver) {
            if (paused)
                pauseOverlay.mouseMoved(e);
            else if (lvlCompleted)
                levelCompletedOverlay.mouseMoved(e);
        }
    }

    public void setLevelCompleted(boolean levelCompleted) {
        this.lvlCompleted = levelCompleted;
    }

    public void setMaxLvlOffset(int lvlOffset) {
        this.maxLvlOffsetX = lvlOffset;
    }

    public void unpauseGame() {
        paused = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver) {
            gameOverOverlay.keyPressed(e);
        }
        else if(gameWon){
            gameWonOverlay.keyPressed(e);
        }
        else
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_Z:
                case KeyEvent.VK_K:
                    player.setAttacking(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gameOver)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    player.setJump(false);
                    break;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
        }
    }

    public void mouseDragged(MouseEvent e) {
        if(!gameOver)
            if(paused)
                pauseOverlay.mouseDragged(e);
    }
    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    public EnemyManager getEnemyManager()  {
        return enemyManager;
    }
}
