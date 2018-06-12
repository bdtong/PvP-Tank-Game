/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.Color;
import java.awt.Graphics;
/**
 *
 * @author Owner
 */
public class EndScreen{
    
    String text;
    
    public EndScreen() {}
    
    public void tick() {}
    
    public void render(Graphics g, ID id) {
        
        if (id == ID.Player)
            text = "Player 2 wins!";
        if (id == ID.Player2)
            text = "Player 1 wins!";
        if (id == ID.tie) 
            text = "Tie!";
        
        g.setColor(Color.gray);
        g.fillRoundRect(245, 170, 140, 75, 25, 25);
        g.setColor(Color.white);
        
        if (id == ID.tie){
            g.drawString(text, 305, 210);    
        } else {
            g.drawString(text, 280, 210);
        }
        
        g.drawRoundRect(245, 170, 140, 75, 25, 25);

    }

}
