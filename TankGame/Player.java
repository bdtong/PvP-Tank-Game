package TankGame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Player extends GameObject{
    
    int homeX; //home position
    int homeY;
    short homeAngle;
    
    HUD hud; //hud
    
    int capacityTriple = 0; //power-ups
    int capacityTank = 0;
    boolean speedUp = false;
    
    File firingSound; //sound

    public Player(int x, int y, ID id, Handler handler, HUD hud, ImageLoader imgl , short angle) {
        super(x, y, id, angle, handler, imgl); //gameobject properties       

        this.homeX = x; //player properties
        this.homeY = y;
        this.homeAngle = angle;
        this.hud = hud;
        firingSound = new File("./Audio/Tank Firing.wav");
        
        //In java, for objects, what is passed to the function is a copy of the reference
        //(not a copy of the object). Therefore, we can manipulate the object, although now we have two references
        //pointing to the object. Intuitively, removing or changing the copy reference does not affect the outside
        //reference. 
        //Copy constructors can be used for copying objects.
        //In java, primitives are passed by copy. So changes inside are not reflected outside.
    }

    public void tick() {
        
        int lastPositionX = x;
        int lastPositionY = y;
        
        if (hud.health == 0){
            die();
        }
        
        //forward movement
        if (this.UpPressed && speedUp == false) {
            this.Forward(6);
        } else if (this.UpPressed && speedUp == true) {
            this.Forward(12);
        }
        
        //backward movement
        else if (this.DownPressed && speedUp == false) {
            this.Backward(6);
        } else if (this.DownPressed && speedUp == true) {
            this.Backward(12);
        }
        
        //stop movement
        else {
            this.StopMoving();
        }
        
        //rotational movement
        if (this.LeftPressed) {
            this.rotateCounter();
        }
        if (this.RightPressed) {
            this.rotateClock();
        }

        x += velX; //adding increments of position to display movement (velocity)
        y += velY;
        
        collision(lastPositionX, lastPositionY);
        
        if(doAction == true) {
            action();
            resetdoAction();
            speedUp = false; //speed is lost upon actions
        }
        
        if(doAction2 == true) {
            action2();
            resetdoAction2();
            speedUp = false; //speed is lost upon actions
        }
        
        checkBorder(); //final boundary check

    }
    
    public void action() {
        
        if (capacityTank > 0) { //bullet order priority established
            handler.addObject(new LargeBullet(x , y ,ID.LargeBullet, imgl, angle, this.getID(), handler)); 
            capacityTank--;
        }
        else if (capacityTriple > 0) {
            handler.addObject(new Bullet(x + 10, y + 10 ,ID.Bullet, imgl, angle, this.getID(), handler)); 
            handler.addObject(new Bullet(x + 10, y + 10 ,ID.Bullet, imgl, (short)(angle+10), this.getID(), handler)); 
            handler.addObject(new Bullet(x + 10, y + 10 ,ID.Bullet, imgl, (short)(angle-10), this.getID(), handler)); 
            capacityTriple--;
        } else {
            handler.addObject(new Bullet(x + 10, y + 10 ,ID.Bullet, imgl, angle, this.getID(), handler)); //adjusted bullet position for accurate center
        }
        playFiringSound();
    }
    
    public void action2() {
        //pending functionality. pls do not touch.
        System.out.println("action2 called");
    }

    public void collision(int lastPositionX, int lastPositionY) {
        
        for (int i = 0; i < handler.objects.size(); i++) { //checking collision of player with all other gameobjects 
            
            GameObject temp = handler.objects.get(i); 
            
            if (temp.getID() == ID.Player2 && temp.getID() != this.getID()) { //cancels checking itself and checks against other player
                if(getBounds().intersects(temp.getBounds())) {
                   x = lastPositionX; //moves player to avoid getting stuck
                   y = lastPositionY;
                }
            }
            
            if (temp.getID() == ID.Player && temp.getID() != this.getID()) { //cancels checking itself and checks against other player
                if(getBounds().intersects(temp.getBounds())) {
                   x = lastPositionX;
                   y = lastPositionY;
                }
            }
            
            if (temp.getID() == ID.WallDe) { 
                if(getBounds().intersects(temp.getBounds())) {
                    x = lastPositionX;
                    y = lastPositionY;
                }
            }
            
            if (temp.getID() == ID.WallIn) {
                if(getBounds().intersects(temp.getBounds())) {
                    x = lastPositionX;
                    y = lastPositionY;
                }
            }  
            
            if (temp.getID() == ID.Bullet) {
                if(getBounds().intersects(temp.getBounds()) && temp.getID2() != this.getID()) { //checks if bullet is not from itself
                    hud.health -= 5;
                    handler.removeObject(temp);
                }
            }  
            
            if (temp.getID() == ID.LargeBullet) {
                if(getBounds().intersects(temp.getBounds()) && temp.getID2() != this.getID()) { //checks if bullet is not from itself
                    hud.health -= 20;
                    handler.removeObject(temp);
                }
            }  
            
            if (temp.getID() == ID.HealthPU) {
                if(getBounds().intersects(temp.getBounds())) {
                    if (hud.health < 100){
                        hud.health += 20;
                        handler.removeObject(temp);
                    }
                }
            }  
            
            if (temp.getID() == ID.tripleshot) {
                if(getBounds().intersects(temp.getBounds())) {
                    capacityTriple = 10;
                    handler.removeObject(temp);       
                }
            }  
            
            if (temp.getID() == ID.fast) {
                if(getBounds().intersects(temp.getBounds())) {
                    speedUp = true;
                    handler.removeObject(temp);       
                }
            }
            
             if (temp.getID() == ID.tankshot) {
                if(getBounds().intersects(temp.getBounds())) {
                    capacityTank = 10;
                    handler.removeObject(temp);       
                }
            }
        }
        
    }
    
    public Rectangle getBounds() {
        
        //Rotating bounds (BUGGED MIGHT WANT TO FIX)
        /*Rectangle bounds = new Rectangle(x, y, 32, 32);
        AffineTransform transform = new AffineTransform(); //creating AffineTransform
        transform.rotate(Math.toRadians(angle), x + bounds.getWidth() / 2, y + bounds.getHeight() / 2); //rotating based on angle.
        //angle does not need to be adjusted because there is not "front" of hitbox. paramater 2 and 3 represent positions
        //relative to origin.
        Shape rect = transform.createTransformedShape(bounds); //instantiating shape from transform.
        return rect.getBounds();*/
       
        //stationary bounds
        return new Rectangle(x, y, 32, 32);
          
    }

    public void render(Graphics g) {
        
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle + 90), img.getWidth() / 2, img.getHeight() / 2);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(img, rotation, null);
        
        //Rotating bounds render
        /*Rectangle bounds = new Rectangle(x, y, 32, 32);
        Graphics2D g4 = (Graphics2D) g;
        AffineTransform r2 = new AffineTransform();
        r2.rotate(Math.toRadians(angle), x + bounds.getWidth() / 2, y + bounds.getHeight() / 2);
        Shape s2 = r2.createTransformedShape(bounds);
        g4.fill(s2);*/
        
        //Staionary bounds render
        /*Graphics2D g3 = (Graphics2D) g;
        g3.drawRect(x, y, 32, 32);*/
   
    }
    
    public void checkBorder() {
        if (x < 0) {
            x = 0;
        }
        if (x >= 1250) {
            x = 1250;
        }
        if (y < 0) {
            y = 0;
        }
        if (y >= 910) {
            y = 910;
        }
    }

    private void playFiringSound() {
        Clip firing = null;
        try {
            
            firing = AudioSystem.getClip();
            firing.open(AudioSystem.getAudioInputStream(firingSound));
        } catch (LineUnavailableException ex) {
            System.out.println("Line unavailable");
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("Aunsupported audio file");
        } catch (IOException ex) {
            System.out.println("IO exception");
        }
        FloatControl firingVolume = (FloatControl) firing.getControl(FloatControl.Type.MASTER_GAIN);
        firingVolume.setValue(-15.0f);
        firing.start();
    }

    private void die() {
        
        hud.lives -= 1;
        
        if (hud.lives > 0){
            hud.health = 100;
            x = homeX;
            y = homeY;
            angle = homeAngle;
            capacityTriple = 0;
            capacityTank = 0;
            speedUp = false;
        } else if (hud.lives == 0) {
            handler.removeObject(this);
        }
           
        
    }
    
}
