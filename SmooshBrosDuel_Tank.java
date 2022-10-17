
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.Timer;

/**
 *
 * @author nafea8846
 */
public class SmooshBrosDuel_Tank extends JComponent implements ActionListener {

    // Height and Width of our game
    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    //Title of the window
    String title = "Tank Test";

    // sets the framerate and delay for our game
    // this calculates the number of milliseconds per frame
    // you just need to select an approproate framerate
    int desiredFPS = 60;
    int desiredTime = Math.round((1000 / desiredFPS));

    // timer used to run the game loop
    // this is what keeps our time running smoothly :)
    Timer gameTimer;

    // YOUR GAME VARIABLES WOULD GO HERE
    boolean right = false;
    boolean left = false;
    boolean crouch = false;
    boolean jump = false;
    boolean normalPunch = false;
    boolean up = false;
    boolean punchRest = false;
    boolean facingRight = false;
    boolean facingLeft = false;
    Rectangle player = new Rectangle(40, 500, 110, 110);
    Rectangle punch = new Rectangle(player.x, player.y, 100, 50);
    int timer = desiredFPS * 3 / 4;
    int punchTime = timer;
    int moveSpeed = 10;
    int gravity = 1;
    int dy = 0;
    int dx = 0;
    boolean standing = true; // on the ground or not

    final int JUMP_FORCE = -20;
    int ground = 580;
    // GAME VARIABLES END HERE    

    // Constructor to create the Frame and place the panel in
    // You will learn more about this in Grade 12 :)
    public SmooshBrosDuel_Tank() {
        // creates a windows to show my game
        JFrame frame = new JFrame(title);

        // sets the size of my game
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(this);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);

        // add listeners for keyboard and mouse
        frame.addKeyListener(new Keyboard());
        Mouse m = new Mouse();
        this.addMouseMotionListener(m);
        this.addMouseWheelListener(m);
        this.addMouseListener(m);

        // Set things up for the game at startup
        setup();

        // Start the game loop
        gameTimer = new Timer(desiredTime, this);
        gameTimer.setRepeats(true);
        gameTimer.start();
    }

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.fillRect(0, ground, WIDTH, 120);
        Graphics2D g2d = (Graphics2D) g;
        // GAME DRAWING GOES HERE
        if (normalPunch) {
            g.setColor(Color.GREEN);
            g2d.fill(punch);
        }
        g.setColor(Color.RED);
        g2d.fill(player);

        // GAME DRAWING ENDS HERE
    }

    // This method is used to do any pre-setup you might need to do
    // This is run before the game loop begins!
    public void setup() {
        // Any of your pre setup before the loop starts should go here

    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void loop() {
        if (right) {
            if (player.x + player.width <= WIDTH) {
                player.x = player.x + moveSpeed;
            }
        } else if (left) {
            if (player.x >= 0) {

                player.x = player.x - moveSpeed;
            }
        }
        if (facingLeft) {
            dx = -1;
        } else if (facingRight) {
            dx = 1;
        }
        if (jump && standing) {
            // make it move upwards quickly
            dy = JUMP_FORCE;
            standing = false;
            // no longer standing

        }

        // add in gravity
        dy = dy + gravity;
        player.y = player.y + dy;

        if (player.y + player.height > ground) {
            player.y = ground - player.height;
            standing = true;
        }
        if (punch.x > player.x + player.width || punch.x < player.x - player.width) {
            punch.x = player.x;
        }
        if (facingRight) {
            if (punch.x < player.x + player.width && !punchRest) {
                punch.x = punch.x + 5;
                if (punch.x == player.x + player.width) {
                    punchRest = true;
                }
            }
            if (!normalPunch || punchRest) {
                if (punch.x > player.x) {
                    punch.x = punch.x - 10;
                }
            }
        }else if(facingLeft){
            if (punch.x > player.x - player.width && !punchRest) {
                punch.x = punch.x - 5;
                if (punch.x == player.x - player.width) {
                    punchRest = true;
                }
            }
            if (!normalPunch || punchRest) {
                if (punch.x < player.x) {
                    punch.x = punch.x + 10;
                }
            }
        }
    }

    // Used to implement any of the Mouse Actions
    private class Mouse extends MouseAdapter {

        // if a mouse button has been pressed down
        @Override
        public void mousePressed(MouseEvent e) {
            int click = e.getButton();
            if (click == MouseEvent.BUTTON1) {
                normalPunch = true;
            }
        }

        // if a mouse button has been released
        @Override
        public void mouseReleased(MouseEvent e) {
            int click = e.getButton();
            if (click == MouseEvent.BUTTON1) {
                normalPunch = false;
                punchRest = false;
            }
        }

        // if the scroll wheel has been moved
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

        }

        // if the mouse has moved positions
        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    // Used to implements any of the Keyboard Actions
    private class Keyboard extends KeyAdapter {

        // if a key has been pressed down
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_SPACE) {
                jump = true;
            }
            if (key == KeyEvent.VK_S) {
                crouch = true;
            }
            if (key == KeyEvent.VK_D) {
                right = true;
                facingRight = true;
                facingLeft = false;
            }
            if (key == KeyEvent.VK_A) {
                left = true;
                facingRight = false;
                facingLeft = true;
            }
            if (key == KeyEvent.VK_W) {
                up = true;
            }
        }

        // if a key has been released
        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP || key == KeyEvent.VK_SPACE) {
                jump = false;
            }
            if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
                crouch = false;
            }
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
                right = false;
            }
            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
                left = false;
            }
            if (key == KeyEvent.VK_W) {
                up = false;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        loop();
        repaint();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates an instance of my game
        SmooshBrosDuel_Tank game = new SmooshBrosDuel_Tank();
    }
}
