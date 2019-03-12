package sonar;

import java.awt.Color;
import java.awt.List;
import java.awt.Point;
import java.util.LinkedList;

public class Snake {

    public LinkedList<Point> snake = new LinkedList<Point>();
    public Color color;
    public int sizeX, sizeY;
    /*
     0 - top
     1 - right
     2 - down
     3 - left
     */
    public int direction;

    public Snake() {

    }

    public boolean update() {
        snake.removeLast();

        if (direction == 0){
            snake.addFirst(new Point(snake.getFirst().x, snake.getFirst().y - 1));
        }
        if (direction == 1){
            snake.addFirst(new Point(snake.getFirst().x + 1, snake.getFirst().y));
        }
        if (direction == 2){
            snake.addFirst(new Point(snake.getFirst().x, snake.getFirst().y + 1));
        }
        if (direction == 3){
            snake.addFirst(new Point(snake.getFirst().x - 1, snake.getFirst().y));
        }

        if (snake.get(0).x < 0 || snake.get(0).x > sizeX || snake.get(0).y < 0 || snake.get(0).y > sizeY) {
            return false;
        }
        
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(i).equals(snake.getFirst())) {
                return false;
            }
        }
        
        return true;
    }

    public void grow() {
        if (direction == 0){
            snake.addLast(new Point(snake.getLast().x, snake.getLast().y + 1));
        }
        if (direction == 1){
            snake.addLast(new Point(snake.getLast().x - 1, snake.getLast().y));
        }
        if (direction == 2){
            snake.addLast(new Point(snake.getLast().x, snake.getLast().y - 1));
        }
        if (direction == 3){
            snake.addLast(new Point(snake.getLast().x + 1, snake.getLast().y));
        }
    }
}
