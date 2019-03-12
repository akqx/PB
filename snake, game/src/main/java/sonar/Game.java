package sonar;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.Random;
import javax.swing.JFrame;

class Window extends JFrame {

    public Board board;

    public Window() {
        super("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(816, 519);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2,dim.height / 2 - this.getSize().height / 2);

        board = new Board(50, 30, 16);
        board.addKeyListener(board);
        board.setFocusable(true);
        this.add(board);
        
        this.setVisible(true);
    }

}

public class Game {

    public static Random rand = new Random();

    public static void main(String[] args) {
        Window window = new Window();
        window.board.run(.125);
    }

}
