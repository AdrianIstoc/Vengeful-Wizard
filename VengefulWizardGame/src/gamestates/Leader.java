package gamestates;

import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Leader extends State implements Statemethods {
    public Leader(Game game) {
        super(game);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(0,0,0));
        g.fillRect(0,0,Game.GAME_WIDTH,Game.GAME_HEIGHT);

        g.setColor(Color.white);
        g.drawString("Leader Board:", (int) (Game.GAME_WIDTH/2-40* Game.SCALE), 100);
        for(int i=0;i<3;++i){
            g.drawString(1+i + ". " + LoadSave.descending("Data", "LeaderScore", i+1), (int) (Game.GAME_WIDTH/2-40* Game.SCALE), 150+i*25);
        }

        g.drawString("Press esc to enter Main Menu!", (int) (Game.GAME_WIDTH/2-90* Game.SCALE), 300);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            Gamestate.state = Gamestate.MENU;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
