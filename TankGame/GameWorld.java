/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import sun.util.logging.PlatformLogger;

/**
 *
 * @author Owner
 */
public class GameWorld extends Canvas implements Runnable{
    
    private static final long serialVersionUID = 2L;

    private Thread thread;
    private boolean isRunning = false;
    
    private BufferedImage world; //world size 1920 x 1080
    private BufferedImage background; //background size 1280 x 960
    static final int WORLD_WIDTH = 1920;
    static final int WORLD_HEIGHT = 1080;
    static final int SCREEN_WIDTH = 640;
    static final int SCREEN_HEIGHT = 480;
    
    private Handler handler;
    
    private HUD hud1;
    private HUD hud2;
    
    private EndScreen endscreen;
    
    private Camera camera1;
    private Camera camera2;
    
    private ImageLoader imgloader;
    
    private File soundtrack;

    public GameWorld() {
        
        world = new BufferedImage(WORLD_WIDTH, WORLD_HEIGHT, BufferedImage.TYPE_INT_RGB); //instantiating world
        
        soundtrack = new File("./Audio/music.mid"); //loading music
        sun.util.logging.PlatformLogger platformLogger = PlatformLogger.getLogger("java.util.prefs"); 
        platformLogger.setLevel(PlatformLogger.Level.OFF); //hiding potentially bugged warning from playSoundtrack()
        //warning deals with accessing java preferences system tree or java incorrectly touching the Windows Registry
        //more info found here https://stackoverflow.com/questions/5354838/java-java-util-preferences-failing
        
        imgloader = new ImageLoader(); //loads images into hashmap
        imgloader.loader(); //init function
        background = imgloader.grabImage(ID.Backg); //loading background
        
        playSoundtrack();
        
        handler = new Handler(); 
        camera1 = new Camera(0,0); //initializing camera1 position
        camera2 = new Camera(0,0); //initializing camera2 position
        this.addKeyListener(new PlayerListener(handler));//registering component to our game world object
        //adding our keyListener PlayerListener, passing in the handler to the constructor
        //our keyListener PlayerListener modifies the velocities of objects within our handler 
        //in our thread, tick() will continue to execute repeatedly portraying movement
        
        new Window(SCREEN_WIDTH, SCREEN_HEIGHT, "Tank Game", this);   
        
        //[HERE] after exiting from start(). a new thread has begun
        hudBuilder(); //old thread will add our players and etc  
        powerUpBuilder();
        playerBuilder(); 
        wallBuilder();
        endScreenBuilder();

    }
    
    public synchronized void start(){
        thread = new Thread(this);
        thread.start(); //creating new thread calling run()
        isRunning = true;
    }
    
    public synchronized void stop(){
        try{
            thread.join();
            isRunning = false;
        } catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    private void tick() {
        
        camera1.tick(handler.player); //camera calculating
        camera2.tick(handler.player2);
        
        handler.tick();
        
        hud1.tick();
        hud2.tick();
        
    }
    
     private void render() {
      
        //creates a place for the image to be rendered behind the screen. then it is shown
        BufferStrategy bs = this.getBufferStrategy(); //gets reference to current bufferstrategy object.
        if (bs == null) {
            this.createBufferStrategy(3); //adding to the Jframe. buffered 3 "screens"
            return;
        }
        
        Graphics g = world.getGraphics(); //creating graphics for world
        Graphics g2 = bs.getDrawGraphics(); //creating graphics for render. linking it to the buffer strategy
 
        g.drawImage(background, 0, 0, null); //draws background to world
   
        handler.render(g); //draws all gameObjects onto world

        BufferedImage righthalf = world.getSubimage((int) camera2.getX(), (int) camera2.getY(), SCREEN_WIDTH/2, SCREEN_HEIGHT); //creating split screen from world
        BufferedImage lefthalf = world.getSubimage((int) camera1.getX(), (int) camera1.getY(), SCREEN_WIDTH/2, SCREEN_HEIGHT);
        
        g2.drawImage(lefthalf, 0,0, null); //rendering screens
        g2.drawImage(righthalf, SCREEN_WIDTH/2,0,null);
        
        hud1.render(g2); //rendering huds
        hud2.render(g2);
        
        checkWin(g2); //checks for win
        
        Graphics2D g3 = (Graphics2D) g2; //creating g2d for minimap scale
        
        BufferedImage minimap = world.getSubimage(0, 0, 1280, 940); //passing width and height of background
        g3.scale(.1,.1);
        g3.drawImage(minimap, SCREEN_WIDTH + 1900, SCREEN_HEIGHT + 2850, null);
        
        g.dispose(); //freeing graphics
        g2.dispose(); 
        g3.dispose();
        
        bs.show();
            
     }
    
    private void hudBuilder() {
        
        hud1 = new HUD(ID.hud1, imgloader);
        hud2 = new HUD(ID.hud2, imgloader);
        
    }
    
    private void powerUpBuilder () {
        
        //health PUs
        handler.addObject(new HealthPowerUp(500, 360, ID.HealthPU, handler, imgloader)); //handler passed but not used for all PUs
        handler.addObject(new HealthPowerUp(740, 360, ID.HealthPU, handler, imgloader));
        
        //tripleshot PU
        handler.addObject(new TriplePowerUp(500, 500, ID.tripleshot, handler, imgloader));
        
        //speed PU
        handler.addObject(new SpeedPowerUp(620, 500, ID.fast, handler, imgloader));
        
        //largeB PU
        handler.addObject(new LargeBPowerUp(740, 500, ID.tankshot, handler, imgloader));
      
    }
    
    private void playerBuilder() {
        
        handler.addObject(new Player(150, 420, ID.Player, handler, hud1, imgloader, (short) 0)); 
        handler.addObject(new Player(1120, 420, ID.Player2, handler, hud2, imgloader, (short) 180));
        
    }
    
    private void wallBuilder() {
        
        //left wall
        int increment = 0;
        for (int i = 0; i < 48; i ++) {
            handler.addObject(new WallIndestructible(0, increment, ID.WallIn, handler, imgloader)); //handler passed but not used for all Indestructible Walls
            increment = increment + 20;
        }
        
        //top wall
        int increment2 = 0;
        for (int j = 0; j < 63; j ++) {
            handler.addObject(new WallIndestructible(20 + increment2, 920, ID.WallIn, handler, imgloader));
            increment2 = increment2 + 20;
        }
  
        //bottom wall
        int increment3 = 0;
        for (int j = 0; j < 63; j ++) {
            handler.addObject(new WallIndestructible(20 + increment3, 0, ID.WallIn, handler, imgloader));
            increment3 = increment3 + 20;
        }
  
        //right wall
        int increment4 = 0;
        for (int i = 0; i < 45; i ++) {
            handler.addObject(new WallIndestructible(1260, 20 + increment4, ID.WallIn, handler, imgloader));
            increment4 = increment4 + 20;
        }
        
        //inner left wall
        int increment5 = 0;
        for (int i = 0; i < 18; i ++) {
            handler.addObject(new WallDestructible(320, 280 + increment5, ID.WallDe, handler, imgloader)); //handler IS used for Destructible Walls
            increment5 = increment5 + 20;
        }
        
        //inner right wall
        int increment6 = 0;
        for (int i = 0; i < 18; i ++) {
            handler.addObject(new WallDestructible(920, 280 + increment6, ID.WallDe, handler, imgloader));
            increment6 = increment6 + 20;
        }
        
        //inner top wall
        int increment7 = 0;
        for (int i = 0; i < 19; i ++) {
            handler.addObject(new WallDestructible(440 + increment7, 200, ID.WallDe, handler, imgloader));
            increment7 = increment7 + 20;
        }
        
        //inner bottom wall
        int increment8 = 0;
        for (int i = 0; i < 19; i ++) {
            handler.addObject(new WallDestructible(440 + increment8, 700, ID.WallDe, handler, imgloader));
            increment8 = increment8 + 20;
        }
    }
    
    private void endScreenBuilder(){
        endscreen = new EndScreen();
    }
    
    private void checkWin(Graphics g2) {
        
        if (hud1.lives == 0 && hud2.lives == 0) {
            endscreen.render(g2, ID.tie);
        }
        else if (hud1.lives == 0) {
            endscreen.render(g2, ID.Player);
        }       
        else if (hud2.lives == 0) {
            endscreen.render(g2, ID.Player2);
        }
        
    }
    
    public void run()
    {
        long lastTime = System.nanoTime(); // get current time to the nanosecond
        double amountOfTicks = 60.0; // set the number of ticks 
        double ns = 1000000000 / amountOfTicks;
        double delta = 0; // change in time
        long timer = System.currentTimeMillis(); // get current time
        int frames = 0; 
        while(isRunning)
        {
                    long now = System.nanoTime();  // get current time in nonoseconds durring current loop
                    delta += (now - lastTime) / ns; // add the amount of change since the last loop
                    lastTime = now; // set lastTime to now to prepare for next loop
                    while(delta >=1)
                            {
                                tick();
                                delta--;  // lower our delta back to 0 to start our next frame wait
                            }
                            if(isRunning)
                                render();
                            frames++; // note that a frame has passed
                            
                            if(System.currentTimeMillis() - timer > 1000) // if one second has passed
                            {
                                timer += 1000;
                                frames = 0;
                            }
        }
                stop(); // no longer running stop the thread
    }
    
     private void playSoundtrack() {
        Clip mainSoundClip = null;
        try {
            mainSoundClip = AudioSystem.getClip();
        } catch (LineUnavailableException ex) {
            System.out.println("AudioSystem.getClip failed");
        }
        
        try {
            mainSoundClip.open(AudioSystem.getAudioInputStream(soundtrack));
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("Unsupported Audio File");
        } catch (IOException ex) {
            System.out.println("IO exception");
        } catch (LineUnavailableException ex) {
            System.out.println("Line Unavailable");
        }
        
        FloatControl gainControl = (FloatControl) mainSoundClip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-20.0f); //Reduce Soundtrack volume 20 decibels.
        mainSoundClip.loop(5);
        
    }
    
    public static void main(String args[]){
        new GameWorld();
    }

}
