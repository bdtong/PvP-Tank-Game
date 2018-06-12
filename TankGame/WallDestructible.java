/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Brandon
 */
public class WallDestructible extends GameObject {
    
    int health = 3;
    
    public WallDestructible (int x, int y, ID id, Handler handler, ImageLoader imgl) {
        super(x, y, id, (short) 0, handler, imgl); //gameobject properties
        setID2(ID.WallDe2);
        setID3(ID.WallDe3);
        img2 = imgl.grabImage(id2);
        img3 = imgl.grabImage(id3);
    }
    
     public Rectangle getBounds() {
        return new Rectangle(x, y, 20, 20);
    }

     public void tick() {
         
        if (doAction == true) {
            action();
            resetdoAction();
        }
        
        if (doAction2 == true) {
            action2();
            resetdoAction2();
        }
        
        collision();
        
     }

    public void render(Graphics g) {
        
        Graphics2D g2 = (Graphics2D) g;
        
        if (health == 3) {
            g2.drawImage(img, x, y, null);
        } else if (health == 2) {
            g2.drawImage(img2, x, y, null);
        } else if (health == 1) {
            g2.drawImage(img3, x, y, null);
        }
        
        //health will never get to 0 here bc tick is before render 
        
    }
    
    public void collision() {
        if (health == 0) {
            handler.removeObject(this);
        }
    }
    
    public void action() {
        health--;
    }
    
    public void action2() {
        health = 0;
    }
}
