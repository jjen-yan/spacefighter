import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import static java.lang.Character.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OuterSpace extends Canvas implements KeyListener, Runnable
{
  private Ship ship;
    private Bullets shots;
    private long lastShot = 0;
    private long lastAShot = 0;
    private long lastAMove = 0;
    private long lastRow = 0;
    private AlienHorde horde;
    private int score = 0;
    private int lives = 5;
    boolean paused = false;
    boolean ended = false;

  /* uncomment once you are ready for this part
   *
   
  
  */

  private boolean[] keys;
  private BufferedImage back;

  public OuterSpace()
  {
    setBackground(Color.black);

    keys = new boolean[5];
      

      ship = new Ship(150, 800, 50, 50, 2);
      horde = new AlienHorde(10);
      shots = new Bullets();

    //instantiate other instance variables
    //Ship, Alien

    this.addKeyListener(this);
    new Thread(this).start();

    setVisible(true);
  }

  public void update(Graphics window)
  {
    paint(window);
  }

  public void paint( Graphics window )
  {
      if(ended) return;
      if(paused){
          if(keys[4]){
              paused = false;
          } else return;
      }
    //set up the double buffering to make the game animation nice and smooth
    Graphics2D twoDGraph = (Graphics2D)window;

    //take a snap shop of the current screen and same it as an image
    //that is the exact same width and height as the current screen
    if (back==null)
      back = (BufferedImage)(createImage(getWidth(),getHeight()));

    //create a graphics reference to the back ground image
    //we will draw all changes on the background image
    Graphics graphToBack = back.createGraphics();

    graphToBack.setColor(Color.BLUE);
    graphToBack.drawString("StarFighter ", 25, 50 );
    graphToBack.setColor(Color.BLACK);
    graphToBack.fillRect(0,0,1000, 1000);
                            
    if (keys[0]){
      ship.move("L");
    } else if(keys[1]){
        ship.move("R");
    } else if(keys[2]){
        ship.move("U");
    } else if(keys[3]){
        ship.move("D");
    } else if(keys[4]){
        if(System.currentTimeMillis() - lastShot > 250){
            shots.add(new Ammo(ship.getX()+ship.getWidth()/2-3, ship.getY()-6, 6, 6, 2));
            lastShot = System.currentTimeMillis();
        }
    }
      ship.draw(graphToBack);
      if(System.currentTimeMillis() - lastAMove > 500){
          horde.move();
          lastAMove = System.currentTimeMillis();
      }
      score += horde.calcHits(shots.getList());
      horde.draw(graphToBack);
      shots.move();
      shots.draw(graphToBack);
      if(System.currentTimeMillis()-lastAShot > 500){
          shots.add(horde.randomShot());
          lastAShot = System.currentTimeMillis();
      }
      if(horde.crashesShip(ship) || ship.isHit(shots.getList())){
          lives--;
          paused = true;
          keys[4] = false;
          graphToBack.setColor(Color.RED);
          graphToBack.drawString("press space to continue", 500, 800);
      }
      
      if(System.currentTimeMillis() - lastRow > 3000){
          horde.addRow();
          lastRow = System.currentTimeMillis();
          

      }
        graphToBack.drawString("score: " + score, 25, 50 );
      graphToBack.drawString("lives: " + lives, 25, 75 );
      if(lives == 0){
            ended = true;
            graphToBack.setColor(Color.BLACK);
            graphToBack.fillRect(0,0,1000,1000);
            graphToBack.setColor(Color.RED);
            graphToBack.drawString("gg. final score: " + score, 500, 800);
        }



    //add code to move Ship, Alien, etc.


    //add in collision detection to see if Bullets hit the Aliens and if Bullets hit the Ship


    twoDGraph.drawImage(back, null, 0, 0);
  }


  public void keyPressed(KeyEvent e)
  {
    if (e.getKeyCode() == KeyEvent.VK_LEFT)
    {
      keys[0] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_RIGHT)
    {
      keys[1] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_UP)
    {
      keys[2] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_DOWN)
    {
      keys[3] = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_SPACE)
    {
      keys[4] = true;
    }
    repaint();
  }

  public void keyReleased(KeyEvent e)
  {
    if (e.getKeyCode() == KeyEvent.VK_LEFT)
    {
      keys[0] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_RIGHT)
    {
      keys[1] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_UP)
    {
      keys[2] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_DOWN)
    {
      keys[3] = false;
    }
    if (e.getKeyCode() == KeyEvent.VK_SPACE)
    {
      keys[4] = false;
    }
    repaint();
  }

  public void keyTyped(KeyEvent e)
  {
    //no code needed here
  }

  public void run()
  {
    try
    {
      while(true)
      {
        Thread.currentThread().sleep(5);
        repaint();
      }
    }catch(Exception e)
    {
    }
  }
}

