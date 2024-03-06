package utilz;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import com.sun.source.tree.WhileLoopTree;
import entities.Mag;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.MAG;

import java.sql.*;

public class LoadSave {

    public static final String PLAYER_ATLAS = "adi_sprite_sheet.png";
    public static final String LEVEL_ATLAS = "temp_world.png";
    public static final String MENU_BUTTONS = "menu_buttons.png";
    public static final String MENU_BACKGROUND = "Vengeful_Wizard.png";
    public static final String PAUSE_BACKGROUND = "pause.png";
    public static final String SOUND_BUTTONS = "sound_buttons.png";
    public static final String PAUSE_MENU_BUTTONS = "pause_buttons.png";
    public static final String VOLUME_SLIDER = "volume.png";
    public static final String MENU_BACKGROUND_IMG = "menu_background.png";
    public static final String ENEMY_SPRITE = "enemy_sprite_sheet.png";
    public static final String HEALTH_BAR = "health_bar.png";
    public static final String COMPLETED_IMG = "lvl_comp.png";


    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/lvls");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

        for (int i = 0; i < filesSorted.length; i++)
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i + 1) + ".png")) {
                    filesSorted[i] = files[j];
                }
            }

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        for (int i = 0; i < imgs.length; i++) {
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imgs;
    }

    public static void insertB(String nume_fisier, String nume_tabela, int a) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + nume_fisier + ".db");
            c.setAutoCommit(false);
            stmt = c.createStatement();

            String sql = "INSERT INTO " + nume_tabela + "(Score) " + "VALUES (" + a + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static int getB(String nume_fisier, String nume_tabela) {
        Connection c = null;
        Statement stmt = null;
        int value = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + nume_fisier + ".db");
            c.setAutoCommit(false);
            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM " + nume_tabela + ";");
            while (rs.next()) {
                value = rs.getInt("Score");
            }

            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return value;
    }

    public static int descending(String nume_fisier, String nume_tabela, int x) {
        int value = 0;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + nume_fisier + ".db");
            c.setAutoCommit(false);
            stmt = c.createStatement();

            String sql = "SELECT * FROM " + nume_tabela + " ORDER BY Score DESC;";
            //stmt.executeUpdate(sql);
            ResultSet rs = stmt.executeQuery(sql);
            for (int i = 0; i < x; i++) {
                rs.next();
                value = rs.getInt("Score");
            }
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return value;

    }



}
