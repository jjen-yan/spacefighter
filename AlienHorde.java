import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

public class AlienHorde
{
  private List<Alien> aliens;
    boolean L = true;
    int s;

  public AlienHorde(int size)
  {
      aliens = new ArrayList<Alien>();
      for(int i = 0; i < 2; i++){
          for(int j = 0; j < size; j++){
              add(new Alien(100+50*j, 50+50*i, 50, 50, 5));
          }
      }
      s = size;
    }

  public void add(Alien al)
  {
      aliens.add(al);
  }

    public void addRow(){
        for(Alien a : aliens) a.move("D");
        for(int i = 0; i < s; i++){
            add(new Alien(100+50*i, 50, 50, 50, 5));
        }
    }

  public void draw(Graphics window)
  {
      for(Alien a : aliens){
          a.draw(window);
      }
  }

  public void move()
  {
      for(Alien a : aliens){
          a.move(L ? "L" : "R");
      }
      L = !L;
  }

    public int num(){ return aliens.size(); }

  // calulate if Aliens are hit by shots, if so remove the shot and alien and return the number of hits
  public int calcHits(List<Ammo> shots)
  {
      int hits = 0;
      for(int i = 0; i < aliens.size(); i++){
          for(int j = 0; j < shots.size(); j++){
              Alien al = aliens.get(i);
              Ammo am = shots.get(j);
              if(al.didCollide(am)){
                  aliens.remove(i--);
                  shots.remove(j--);
                  hits++;
                  break;
              }
          }
      }
      return hits;
  }

    public boolean crashesShip(Ship s){
        boolean crashes = false;
        for(int i = 0; i < aliens.size(); i++){
            if(aliens.get(i).didCollide(s)){
                aliens.remove(i--);
                crashes = true;
            }
        }
        return crashes;
    }

    public Ammo randomShot(){
        if(aliens.size() == 0) return null;
        // only allow bottom row to shoot
        int maxY = -1;
        for(Alien a : aliens){
            maxY = Math.max(maxY, a.getY());
        }
        Alien a;
        do{
            a = aliens.get((int)(aliens.size()*Math.random()));
        } while(a.getY() != maxY);
        return new Ammo(a.getX()+a.getWidth()/2-3, a.getY()+a.getHeight()+6, 6, 6, -2);
    }

  public String toString()
  {
    return "";
  }
}
