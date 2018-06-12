/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Brandon
 */
public class HUD{
    
    protected ID id;
    public int health = 100;
    public int lives = 3;
    ImageLoader imgl;
    BufferedImage img;
    
    public HUD (ID id, ImageLoader imgl) {
        this.id = id;
        this.imgl = imgl;
        img = imgl.grabImage(ID.Player); //grabbing tankImage
    }
    
    public void tick () {
        
        health = checkborder(health, 0, 100);
         
    }
    
    public void render (Graphics g) {
        
        Graphics2D g2 = (Graphics2D) g;
        
        if (id == ID.hud1) {
            g.setColor(Color.gray);
            g.fillRect(15,15, 200, 32);
            g.setColor(Color.green);
            g.fillRect(15, 15, health * 2, 32);
            g.setColor(Color.white);
            g.drawRect(15, 15, 200, 32);
            
            if (lives == 3 || lives == 2 || lives == 1) 
                g2.drawImage(img, 15, 60, 20, 20, null);
            if (lives == 3 || lives == 2)
                g2.drawImage(img, 45, 60, 20, 20, null);
            if (lives == 3)
                g2.drawImage(img, 75, 60, 20, 20, null);
            
            
        }
        if (id == ID.hud2) {
            g.setColor(Color.gray);
            g.fillRect(420,15, 200, 32); //1060, 15, 200, 32
            g.setColor(Color.green);
            g.fillRect(420, 15, health * 2, 32); //1060, 15, health * 2, 32
            g.setColor(Color.white);
            g.drawRect(420, 15, 200, 32); //1060, 15,200, 32
            
            if (lives == 3 || lives == 2 || lives == 1) 
                g2.drawImage(img, 600, 60, 20, 20, null);
            if (lives == 3 || lives == 2)
                g2.drawImage(img, 570, 60, 20, 20, null);
            if (lives == 3)
                g2.drawImage(img, 540, 60, 20, 20, null);
        }
    }
    
    public int checkborder(int var, int min, int max) {
        
        //health bar wont exceed its borders
        if (var >= max) 
            return max;
        else if (var <= min)
            return min;
        else 
            return var;
        
    }
}
