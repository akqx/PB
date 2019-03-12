
package sonar;

import java.awt.Color;
import java.awt.List;
import java.awt.Point;
import java.util.LinkedList;

public class Apple {
    
    public Point position;
    public Color color;
    
    public Apple() {
    }
    
    public void create(LinkedList<Point> snake, int sizeX, int sizeY) {
        boolean created = true;
        do {
            created = true;
            this.position.x = Game.rand.nextInt(sizeX);
            this.position.y = Game.rand.nextInt(sizeY);
            
            for (Point point : snake) {
                if (point.x == this.position.x && point.y == this.position.y) {
                    created = false;
                    break;
                }
            }
        } while(!created);
        
    }
}
