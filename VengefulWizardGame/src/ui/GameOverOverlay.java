package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverOverlay {
    private Playing playing;

    public GameOverOverlay(Playing playing) {
        this.playing=playing;
    }

    public void draw(Graphics g) {
        g.setColor((new Color(0, 0, 0, 200)));
        g.fillRect(0,0, Game.GAME_WIDTH,Game.GAME_HEIGHT);

        g.setColor(Color.white);
        g.drawString("Game Over", (int) (Game.GAME_WIDTH/2-40* Game.SCALE), 150);
        g.drawString("Score: " + LoadSave.getB("Data", "LeaderScore") + " ", (int) (Game.GAME_WIDTH/2-40* Game.SCALE), 225);
        g.drawString("Press esc to enter Main Menu!", (int) (Game.GAME_WIDTH/2-90* Game.SCALE), 300);
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            playing.getPlayer().setScore(0);
            playing.resetAll();
            playing.levelManager.resetLevels();
            Gamestate.state=Gamestate.MENU;
            playing.levelManager.setLvlIndex(0);
        }
    }
}
