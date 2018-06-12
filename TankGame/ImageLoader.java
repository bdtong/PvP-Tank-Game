/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 *
 * @author Brandon
 */
public class ImageLoader {
    
    HashMap <ID, BufferedImage> images;
    
    public ImageLoader() {
        images = new HashMap();
    }
    
    public void loader() {

        //IMPORTANT: CURRENT DIRECTORY MUST BE SET TO THE PACKAGE TankGame BEFORE GAME RUN
       
        try {
            images.put(ID.Player, ImageIO.read(new File("./images/tankImage.png"))); //Items in subdirectory
            images.put(ID.Player2, ImageIO.read(new File("./images/tankImage.png")));
            images.put(ID.WallIn, ImageIO.read(new File("./images/Wall1Image.png")));
            images.put(ID.WallDe, ImageIO.read(new File("./images/Wall2Image.png")));
            images.put(ID.WallDe2, ImageIO.read(new File("./images/Wall2Image2.png")));
            images.put(ID.WallDe3, ImageIO.read(new File("./images/Wall2Image3.png")));
            images.put(ID.Backg, ImageIO.read(new File("./images/Background.png")));
            images.put(ID.Bullet, ImageIO.read(new File("./images/Shell.png")));
            images.put(ID.LargeBullet, ImageIO.read(new File("./images/tankImage.png")));
            images.put(ID.HealthPU, ImageIO.read(new File("./images/healthpack.png")));
            images.put(ID.tripleshot, ImageIO.read(new File("./images/tripleshot.png")));
            images.put(ID.fast, ImageIO.read(new File("./images/fast.png")));
            images.put(ID.tankshot, ImageIO.read(new File("./images/moartanks.png")));
        } catch (IOException e) {
            System.out.println("excpetion " + e);      
        }
        
    }
    
    public BufferedImage grabImage(ID id) {
        return images.get(id);
    }
    
    
}
