/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Brandon
 */
public class LargeBullet extends GameObject{

    public LargeBullet(int x, int y, ID id, ImageLoader imgl, short angle, ID id2, Handler handler) {
        super(x, y, id, angle, handler, imgl); //gameobject properties
        setID2(id2);     
    }

    @Override
    public void tick() {
        
        this.Forward(30);
        x += velX;
        y += velY;
        
        collision();
        
    }
    
    @Override
    public void render(Graphics g) {
        AffineTransform position = AffineTransform.getTranslateInstance(x, y);
        position.rotate(Math.toRadians(angle), img.getWidth() /2, img.getHeight() /2);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(img, position, null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle (x, y, 32, 32);
    }
    
    public void collision(){
        
        for (int i = 0; i < handler.objects.size(); i++) { //checking collision of large bullet with all other gameobjects 
            GameObject temp = handler.objects.get(i); 
            
            if (temp.getID() == ID.WallDe) { 
                if(getBounds().intersects(temp.getBounds())) {
                    handler.removeObject(this);
                    temp.setdoAction2(); //prepping for wall destruction
                }
            }
            
            if (temp.getID() == ID.WallIn) {
                if(getBounds().intersects(temp.getBounds())) {
                    handler.removeObject(this);
                }
            } 
        }
        
    }
}
