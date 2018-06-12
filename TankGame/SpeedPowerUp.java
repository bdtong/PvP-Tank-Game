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
public class SpeedPowerUp extends GameObject{
    
    public SpeedPowerUp(int x, int y, ID id, Handler handler, ImageLoader imgl) {
        super(x, y, id, (short) 0, handler, imgl); //gameobject properties
    }

    public void tick() {}
      
    public void render(Graphics g) {
        
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(img, x, y, null);
        
    }
        
    public Rectangle getBounds() {
        return new Rectangle(x, y, 20, 20);
    }
}
