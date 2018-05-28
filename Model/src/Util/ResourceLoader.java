/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;


import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.imageio.ImageIO;

/**
 * @author Philipp Schweizer
 */
public class ResourceLoader {


    public static Cursor getCursor(String cursor) {

        Toolkit t = Toolkit.getDefaultToolkit();

        Cursor c = null;
        try {
            c = t.createCustomCursor(
                    ImageIO.read(ResourceLoader.class.getResource("/Karten/" + cursor))
                    , new Point(0, 0),
                    "");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return c;
    }

    public static Image getImage(String imageName, int width, int height) {

        Image image = null;

        try {
            image = ImageIO.read(ResourceLoader.class.getResource("/Karten/" + imageName)).getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            Logger.getLogger(ResourceLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }
}
