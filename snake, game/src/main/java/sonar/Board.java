package sonar;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Board extends JPanel implements KeyListener {

    public int sizeX = 50, sizeY = 30;
    public int tileSize = 16;

    public int score = 30, scoreTmp = 30;

    public boolean runFlag = false;

    public Color background = new Color(0, 111, 83);

    public Apple apple;
    public Snake snake;

    public Board(int sizeX, int sizeY, int tileSize) {
        this.setLayout(null);
        this.setBackground(background);

        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.tileSize = tileSize;

        this.snake = new Snake();
        snake.addLast(new Point(sizeX / 2 - 1, sizeY / 2));
        snake.addLast(new Point(sizeX / 2, sizeY / 2));
        snake.addLast(new Point(sizeX / 2 + 1, sizeY / 2));
        snake.sizeX = sizeX;
        snake.sizeY = sizeY;
        snake.color = new Color(251, 192, 45);
        snake.direction = 3;

        this.apple = new Apple();
        apple.color = new Color(248, 58, 58);
        apple.position = new Point();
        apple.create(this.snake.snake, sizeX, sizeY);

    }

    public void run(double delta) {
        runFlag = true;

        double nextTime = (double) System.nanoTime() / 1000000000.0;
        while (runFlag) {
            double currTime = (double) System.nanoTime() / 1000000000.0;
            if (currTime >= nextTime) {
                nextTime += delta;
                update();
                repaint();
            } else {
                int sleepTime = (int) (1000.0 * (nextTime - currTime));
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    public void stop() {
        runFlag = false;
    }

    public void update() {
        if(!snake.update()) {
            System.out.println("GAME OVER ! żeś frajer ");
            stop();
        }
    
        if (snake.get(0).equals(apple.getPosition())) {
            snake.grow();
            apple.create(snake.snake, sizeX, sizeY);
            score += 10;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (runFlag) {
            g.setColor(apple.color);
            g.fillRect(apple.position.x * tileSize, apple.position.y * tileSize, tileSize, tileSize);

            int p = 255;
            g.setColor(snake.color);
            g.setFont(new Font("Arial", Font.PLAIN, 21));
            Thread scoreThread;
            if (scoreTmp != score) {
                scoreThread = new Thread() {
                    public void run() {
                        try {
                            scoreTmp++;
                            if (scoreTmp == score) {
                                this.stop();
                            }
                            Thread.sleep(150);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
                scoreThread.start();
            }

            g.drawString(Integer.toString(scoreTmp), 15, 30);
            for (Point point : snake.snake) {
                g.fillRect(point.x * tileSize, point.y * tileSize, tileSize, tileSize);
                g.setColor(
                        new Color(
                                snake.color.getRed(),
                                snake.color.getGreen(),
                                snake.color.getBlue(),
                                p
                        ));
                p -= 255 / (snake.snake.size()) / 2;
            }
        } else {
            g.setFont(new Font("Arial", Font.PLAIN, 64));
            g.setColor(Color.white);
            g.drawString("Przegrałeś frajerze!", 25, 150);
            g.setFont(new Font("Arial", Font.PLAIN, 32));
            g.drawString("Kiedy idziemy na wixe?", 25, 300);
            g.drawString("Wynik: " + score, 25, 400);
            Thread t = new Thread() {
            int x = 0;
              public void run() {
                  try {
                      x++;
                      if (getBackground() == Color.RED) {
                          setBackground(Color.BLUE);
                      } else {
                        setBackground(Color.RED);
                      }
                      Thread.sleep(500);
                  } catch (InterruptedException ex) {}
              }  
            };
            t.start();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if ((keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) && snake.direction != 2) {
            snake.direction = 0;
        } else if ((keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) && snake.direction != 3) {
            snake.direction = 1;
        } else if ((keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) && snake.direction != 0) {
            snake.direction = 2;
        } else if ((keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) && snake.direction != 1) {
            snake.direction = 3;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
