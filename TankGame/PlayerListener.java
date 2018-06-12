/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;

/**
 *
 * @author Owner
 */
public class PlayerListener extends Observable implements KeyListener{
        
    private Handler handler;

    public PlayerListener (Handler handler) {
        this.handler = handler;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_W) 
            handler.player.toggleUp();
        if (key == KeyEvent.VK_S) 
            handler.player.toggleDown();
        if (key == KeyEvent.VK_A) 
            handler.player.toggleLeft();
        if (key == KeyEvent.VK_D) 
            handler.player.toggleRight();
        
        if (key == KeyEvent.VK_UP) 
            handler.player2.toggleUp();
        if (key == KeyEvent.VK_DOWN) 
            handler.player2.toggleDown();
        if (key == KeyEvent.VK_LEFT) 
            handler.player2.toggleLeft();
        if (key == KeyEvent.VK_RIGHT) 
            handler.player2.toggleRight();
                
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();     
        
        if (key == KeyEvent.VK_W) 
            handler.player.unToggleUp();
        if (key == KeyEvent.VK_S) 
            handler.player.unToggleDown();
        if (key == KeyEvent.VK_A) 
            handler.player.unToggleLeft();
        if (key == KeyEvent.VK_D) 
            handler.player.unToggleRight();
        if (key == KeyEvent.VK_SPACE)
            handler.player.setdoAction();
        if (key == KeyEvent.VK_B)
            handler.player.setdoAction2();
                
        
        if (key == KeyEvent.VK_UP) 
            handler.player2.unToggleUp();
        if (key == KeyEvent.VK_DOWN) 
            handler.player2.unToggleDown();
        if (key == KeyEvent.VK_LEFT) 
            handler.player2.unToggleLeft();
        if (key == KeyEvent.VK_RIGHT) 
            handler.player2.unToggleRight();
        if (key == KeyEvent.VK_NUMPAD0)
            handler.player2.setdoAction();
        if (key == KeyEvent.VK_NUMPAD2)
            handler.player2.setdoAction2();
                
    }
    
}
