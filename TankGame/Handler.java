package TankGame;

import java.awt.Graphics;
import java.util.LinkedList;

/**
 *
 * @author Owner
 */
 public class Handler {
     
    //handles all gameObjects 
     LinkedList<GameObject> objects = new LinkedList<GameObject>();
     
    //eases access to players
     GameObject player;
     GameObject player2;
     
     public void tick() {
         for (int i = 0; i < objects.size(); i++) {
             GameObject temp = objects.get(i);
             temp.tick(); //ticking all gameObjects
         }
     }
     
     public void render(Graphics g) {
           for (int i = 0; i < objects.size(); i++) { 
             GameObject temp = objects.get(i);
             temp.render(g); //rendering all gameObjects
         }
     }
     
     public void addObject(GameObject object) {
         objects.add(object);
         
         if (object.getID() == ID.Player) {
             player = object;
         }
         
         if (object.getID() == ID.Player2) {
             player2 = object;
         }
     
     }
     
     public void removeObject(GameObject object) {
         objects.remove(object);
         //cannot set player and player2 to null bc camera is still ticking after a players death
     }
     
}
