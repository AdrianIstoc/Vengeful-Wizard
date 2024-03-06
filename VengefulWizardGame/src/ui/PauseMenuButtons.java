package ui;

import utilz.LoadSave;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.PauseButtons.*;

public class PauseMenuButtons extends PauseButton {

    private BufferedImage[] imgs;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;
    private PauseMenuButtons menuB, replayB, unpauseB;

    public PauseMenuButtons(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_MENU_BUTTONS);
        imgs = new BufferedImage[3];
        for(int i= 0; i<imgs.length; i++)
            imgs[i] = temp.getSubimage(i * PAUSE_SIZE_DEFAULT, rowIndex * PAUSE_SIZE_DEFAULT, PAUSE_SIZE_DEFAULT, PAUSE_SIZE_DEFAULT);
    }

    public void update() {
        index=0;
        if(mouseOver)
            index=1;
        if(mousePressed)
            index =2;
    }

    public void draw(Graphics g) {
        g.drawImage(imgs[index], x, y, PAUSE_SIZE, PAUSE_SIZE, null);
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
}
