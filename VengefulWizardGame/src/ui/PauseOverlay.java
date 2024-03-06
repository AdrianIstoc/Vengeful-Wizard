package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.PauseButtons.*;
import static utilz.Constants.UI.VolumeSlider.*;

public class PauseOverlay {

    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private SoundButton musicButton, sfxButton;
    private PauseMenuButtons menuB, replayB, unpauseB;
    private VolumeSlider volumeSlider;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createSoundButtons();
        createPauseButtons();
        createVolumeSlider();
    }

    private void createVolumeSlider() {
        int vX = (int) (340 *Game.SCALE);
        int vY = (int) (200* Game.SCALE);
        volumeSlider = new VolumeSlider(vX,vY,SLIDER_WIDTH,VOLUME_HEIGHT);
    }

    private void createPauseButtons() {
        int menuX = (int) (345 * Game.SCALE);
        int replayX = (int) (400 * Game.SCALE);
        int unpauseX = (int) (455 * Game.SCALE);
        int bY = (int) (250 * Game.SCALE);

        menuB = new PauseMenuButtons(menuX, bY, PAUSE_SIZE, PAUSE_SIZE, 2);
        replayB = new PauseMenuButtons(replayX, bY, PAUSE_SIZE, PAUSE_SIZE, 1);
        unpauseB = new PauseMenuButtons(unpauseX, bY, PAUSE_SIZE, PAUSE_SIZE, 0);
    }

    private void createSoundButtons() {
        int soundX = (int)(430 * Game.SCALE);
        int musicY = (int)(70 * Game.SCALE);
        int sfxY = (int) (105 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, PAUSE_SIZE, PAUSE_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, PAUSE_SIZE, PAUSE_SIZE);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE / 2);
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE / 2);
        bgX = Game.GAME_WIDTH / 2 - bgW  / 2;
        bgY= (int) (60 * Game.SCALE);
    }

    public void update() {
        musicButton.update();
        sfxButton.update();

        menuB.update();
        replayB.update();
        unpauseB.update();

        volumeSlider.update();
    }

    public void draw(Graphics g) {
        // Background
        g.drawImage(backgroundImg, bgX,bgY,bgW,bgH,null);

        // Sound buttons
        musicButton.draw(g);
        sfxButton.draw(g);

        // Pause buttons
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);

        // Volume slider
        volumeSlider.draw(g);
    }

    public void mouseDragged(MouseEvent e ){
        if(volumeSlider.isMousePressed()) {
            volumeSlider.changeX(e.getX());
        }

    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e,musicButton))
            musicButton.setMousePressed(true);
        else if(isIn(e,sfxButton))
            sfxButton.setMousePressed(true);
        else if(isIn(e,menuB))
            menuB.setMousePressed(true);
        else if(isIn(e,replayB))
            replayB.setMousePressed(true);
        else if(isIn(e,unpauseB))
            unpauseB.setMousePressed(true);
        else if(isIn(e,volumeSlider))
            volumeSlider.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e) {
        if(isIn(e,musicButton)) {
            if(musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());
            }
        }
        else if(isIn(e,sfxButton)) {
            if(sfxButton.isMousePressed()){
                sfxButton.setMuted(!sfxButton.isMuted());
            }
        }
        else if(isIn(e,menuB)) {
            if(menuB.isMousePressed()){
                Gamestate.state = Gamestate.MENU;
                playing.unpauseGame();
            }
        }
        else if(isIn(e,replayB)) {
            if(replayB.isMousePressed()){
                playing.resetAll();
            }
        }
        else if(isIn(e,unpauseB)) {
            if(unpauseB.isMousePressed()){
                playing.unpauseGame();
            }
        }

        musicButton.resetBools();
        sfxButton.resetBools();
        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
        volumeSlider.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);
        volumeSlider.setMouseOver(false);

        if(isIn(e,musicButton))
            musicButton.setMouseOver(true);
        else if(isIn(e,sfxButton))
            sfxButton.setMouseOver(true);
        else if(isIn(e,menuB))
            menuB.setMouseOver(true);
        else if(isIn(e,replayB))
            replayB.setMouseOver(true);
        else if(isIn(e,unpauseB))
            unpauseB.setMouseOver(true);
        else if(isIn(e,volumeSlider))
            volumeSlider.setMouseOver(true);
    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
