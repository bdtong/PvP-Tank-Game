/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;
/**
 *
 * @author Brandon
 */
public class Camera {
    
    private float x, y; //top left point of camera
    
    public Camera(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public void tick(GameObject object) {
        
        x += ((object.getX() - x) - 310/2) * 0.12f; //screen width slighty adjusted for center of split screen
        y += ((object.getY() - y) - 440/2) * 0.12f; //screen height slighty adjusted for center of split screen
        
        if (x < 0) { //border checking
            x = 0;
        }
        if (x >= 960) {
            x = 960;
        }
        if (y < 0) {
            y = 0;
        }
        if (y >= 485) {
            y = 485;
        }
    
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
    
    
}
