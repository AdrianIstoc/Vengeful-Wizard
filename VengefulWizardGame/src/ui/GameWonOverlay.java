package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameWonOverlay {
    private Playing playing;

    public GameWonOverlay(Playing playing){
        this.playing = playing;
    }

    public void draw(Graphics g) {
        g.setColor((new Color(0, 0, 0)));
        g.fillRect(0,0, Game.GAME_WIDTH,Game.GAME_HEIGHT);

        g.setColor(Color.white);
        g.drawString("Game Won!",(int) (Game.GAME_WIDTH/2-40* Game.SCALE), 150);
        g.drawString("Score: " + playing.getPlayer().getScore() + " ", (int) (Game.GAME_WIDTH/2-40* Game.SCALE), 225);

        g.drawString("Press esc to enter Main Menu!", (int) (Game.GAME_WIDTH/2-90* Game.SCALE), 300);
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            LoadSave.insertB("Data", "LeaderScore", playing.getPlayer().getScore());
            playing.getPlayer().setScore(0);
            playing.resetAll();
            playing.levelManager.resetLevels();
            Gamestate.state=Gamestate.MENU;
            playing.levelManager.setLvlIndex(0);
        }
    }
}
