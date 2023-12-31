import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{

  static final int SCREEN_WIDTH = 600;
  static final int SCREEN_HEIGHT = 600;
  static final int UNIT_SIZE = 20;
  static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
  static final int DELAY = 75;
  final int x[] = new int[GAME_UNITS];
  final int y[] = new int[GAME_UNITS];
  int bodyParts = 6;
  int applesEaten;
  int appleX;
  int appleY;
  char direction = 'R';
  boolean running = false;
  Timer timer;
  Random random;

  GamePanel(){

    //Game Panel Design
    random = new Random();
    this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
    this.setBackground(Color.black);
    this.setFocusable(true);
    this.addKeyListener(new MyKeyAdapter());
    startGame();

  }

  public void startGame(){
    
    //Setting the time as to when the apple will appear when the game is running
    newApple();
    running = true;
    timer = new Timer(DELAY,this);
    timer.start();

  }

  public void paintComponent(Graphics g){

    //Just setting the class for the graphics of the snake and apple
    super.paintComponent(g);
    draw(g);

  }

  public void draw(Graphics g){

  if(running){

    //Gridlines
    for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){

      g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
      g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);      

    }

    //Color and design of the apple
    g.setColor(Color.red);
    g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

    //Color of the snake
    for(int i = 0; i < bodyParts; i++){

      //Color of head
      if(i == 0){

        g.setColor(Color.blue);
        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

      }

      //Color of body
      else{

        g.setColor(new Color(0,128,180));
        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

      }

    }

    //Displaying score while playing
    g.setColor(Color.pink);
    g.setFont( new Font("Ink Free",Font.BOLD, 30));
    FontMetrics metrics = getFontMetrics(g.getFont());
    g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());

  }

  //Attaching the class for when game will be over
  else{

    gameOver(g);

  }

}

  //Generating apple on a random spot of the panel
  public void newApple() {
    boolean collision;
    
    do {
        collision = false;
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

        // Check if the new apple coordinates collide with the snake
        for (int i = 0; i < bodyParts; i++) {
            if (appleX == x[i] && appleY == y[i]) {
                collision = true;
                break;
            }
        }
    } while (collision);
}

  //Snake movement 
  public void move(){

    //Body of the snake tracing the path of its head
    for(int i = bodyParts; i > 0; i--){

      x[i] = x[i - 1];
      y[i] = y[i - 1];

    }

    //Determining the directions of the snake movement
    switch(direction){

      case 'U':
        y[0] = y[0] - UNIT_SIZE;
        break;
      case 'D':
        y[0] = y[0] + UNIT_SIZE;
        break;
      case 'L':
        x[0] = x[0] - UNIT_SIZE;
        break;
      case 'R':
        x[0] = x[0] + UNIT_SIZE;
        break;

    }

  }

  //Increasing body size when apple is eaten and also creating a new one
  public void checkApple(){

    if((x[0] == appleX) && (y[0] == appleY)){

      bodyParts++;
      applesEaten++;
      newApple();

    }

  }

  //Stopping the game under the following circumstances
  public void checkCollisions(){

    //Checks if head collides with body
    for(int i = bodyParts; i > 0; i--){

      if((x[0] == x[i]) && (y[0] == y[i])){

        running = false;

      }

    }

    //Check if head touches left border
    if(x[0] < 0){

      running = false;

    }

    //Check if head touches right border
    if(x[0] > SCREEN_WIDTH){

      running = false;

    }

    //Check if head touches top border
    if(y[0] < 0){

      running = false;

    }

    //Check if head touches bottom border
    if(y[0] > SCREEN_HEIGHT){

      running = false;

    }

    //if game not running then stop timer
    if(!running){

      timer.stop();

    }

  }

  //What to do when game over
  public void gameOver(Graphics g){

    //Score
    g.setColor(Color.red);
    g.setFont( new Font("Ink Free",Font.BOLD, 30));
    FontMetrics metrics1 = getFontMetrics(g.getFont());
    g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());

    //Game Over Text
    g.setColor(Color.red);
    g.setFont( new Font("Ink Free",Font.BOLD, 75));
    FontMetrics metrics2 = getFontMetrics(g.getFont());
    g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

  }

  //The various actions performed during the game
  @Override
  public void actionPerformed(ActionEvent e) {
  
    if(running){

      move();
      checkApple();
      checkCollisions();

    }

    repaint();

  }

  //Controlling Keys
  public class MyKeyAdapter extends KeyAdapter{
    @Override
    public void keyPressed(KeyEvent e){
      
      //Controlling the snake via arrow keys
      switch(e.getKeyCode()){

        case KeyEvent.VK_LEFT:
          if(direction != 'R'){

            direction = 'L';

          }
          break;
        case KeyEvent.VK_RIGHT:
          if(direction != 'L'){

            direction = 'R';

          }
          break;
        case KeyEvent.VK_UP:
          if(direction != 'D'){

            direction = 'U';

          }
          break;
        case KeyEvent.VK_DOWN:
          if(direction != 'U'){

            direction = 'D';

          }
          break;

      }

    }
  }
  
}
