
import jaco.mp3.player.MP3Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.Timer;


/**
 *
 * @author nafea8846
 */
public class SmooshBrosDuel_TheGame extends JComponent implements ActionListener {

    // Height and Width of our game
    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    //Title of the window
    String title = "SmooshBrosDuel";

    // sets the framerate and delay for our game
    // this calculates the number of milliseconds per frame
    // you just need to select an approproate framerate
    int desiredFPS = 60;
    int desiredTime = Math.round((1000 / desiredFPS));

    // timer used to run the game loop
    // this is what keeps our time running smoothly :)
    Timer gameTimer;

    // YOUR GAME VARIABLES WOULD GO HERE
    Rectangle startButton = new Rectangle(300, 300, 200, 140);
    Rectangle backButton = new Rectangle(300, 400, 150, 30);
    Rectangle startGameButton = new Rectangle(300, 500, 200, 140);
    boolean pauseScreen = false;
    boolean menuScreen = true;
    boolean characterSelScreen = false;
    boolean mapOne = false;
    boolean player1Intersecting = false;
    int gravity = 1;
    int ground = 580;

    BufferedImage startButtonImage;
    BufferedImage playButton;
        BufferedImage mrLamontImage;
        BufferedImage tankImage;
        BufferedImage dwarfImage;
    Rectangle player1 = new Rectangle(100, 500, 50, 140);
    Rectangle player1Punch = new Rectangle(player1.x, player1.y, 100, 30);
    Rectangle[] player1Button = new Rectangle[3];
    Boolean[] player1Bool = new Boolean[3];
    boolean player1Right = false;
    boolean player1Left = false;
    boolean player1Down = false;
    boolean player1Jump = false;
    boolean player1PunchBool = false;
    boolean player1PunchRest = false;
    boolean player1Up = false;
    boolean player1Standing = true; // on the ground or not
    boolean player1FacingRight = true;
    int player1Dy = 0;
    int player1Dx = 0;
    int player1JumpForce = -20;
    int player1MoveSpeed = 10;

    Rectangle[] player2Button = new Rectangle[3];
    Rectangle player2 = new Rectangle(500, 500, 50, 140);
    Rectangle player2Punch = new Rectangle(player1.x, player1.y, 100, 30);
    boolean[] player2Bool = new boolean[3];
    boolean player2Right = false;
    boolean player2Left = false;
    boolean player2Down = false;
    boolean player2Jump = false;
    boolean player2PunchBool = false;
    boolean player2PunchRest = false;
    boolean player2Up = false;
    boolean player2Standing = true;
    boolean player2FacingRight = false;
    int player2Dy = 0;
    int player2Dx = 0;
    int player2JumpForce = -20;
    int player2MoveSpeed = 10;

    public BufferedImage load(String filename) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
    // GAME VARIABLES END HERE    
    // Constructor to create the Frame and place the panel in
    // You will learn more about this in Grade 12 :)
    public SmooshBrosDuel_TheGame() {
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

        // Set things player1Up for the game at startup
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
        if (mapOne) {
            g.fillRect(0, ground, WIDTH, 120);
            if (player1PunchBool && !player1Bool[2]) {
                g2d.fill(player1Punch);
            }
            g.fillRect(0, ground, WIDTH, 120);
            g.setColor(Color.RED);
            g2d.fill(player1);
            if (player2PunchBool && !player2Bool[2]) {
                g2d.fill(player2Punch);
            }

            g.setColor(Color.BLUE);
            g2d.fill(player2);
        }
        if (menuScreen) {
            g.setColor(new Color(0, 0, 0));
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(new Color(0, 0, 30));
            g.drawImage(playButton,startButton.x, startButton.y, this);
           
        }
        if (pauseScreen) {
            g.setColor(new Color(0, 0, 0));
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(new Color(0, 0, 40));
            g2d.fill(backButton);
        }
        if (characterSelScreen) {
            g.setColor(new Color(0, 0, 30));
            for (int i = 0; i < 3; i++) {
                g.drawImage(startButtonImage,200, 450, this);
                g.setColor(Color.RED);
                g2d.fill(player1Button[i]);
                g.setColor(Color.BLUE);
                g2d.fill(player2Button[i]);
            }
        }
        // GAME DRAWING GOES HERE
        // GAME DRAWING ENDS HERE
    }

    // This method is used to do any pre-setup you might need to do
    // This is run before the game loop begins!
    public void setup() {
        // Any of your pre setup before the loop starts should go here
        startButtonImage = load("startgameButton.png");
        playButton = load("playButton.png");

        player1Button[0] = new Rectangle(100, 250, 50, 50);
        player1Button[1] = new Rectangle(160, 250, 50, 50);
        player1Button[2] = new Rectangle(220, 250, 50, 50);

        player1Bool[0] = false;
        player1Bool[1] = false;
        player1Bool[2] = false;

        player2Button[0] = new Rectangle(500, 250, 50, 50);
        player2Button[1] = new Rectangle(560, 250, 50, 50);
        player2Button[2] = new Rectangle(620, 250, 50, 50);

        player2Bool[0] = false;
        player2Bool[1] = false;
        player2Bool[2] = false;

    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void loop() {
        if (!mapOne) {
            playerSetter();
        }
        playerMovement();
        if ((player1PunchBool && (player1Bool[0] || player1Bool[1])) || (player2PunchBool && (player2Bool[0] || player2Bool[1]))) {
            playerSetter();
            playerPunch();
        }
        if ((player1PunchBool && player1Bool[2]) || (player2PunchBool && player2Bool[2])) {
            playerStretch();
        }
        if (player1.intersects(player2) && (!player2Left && !player2Right)) {
            player1Intersecting = true;
            playerColliosion(player1Intersecting);
        }
        if (player2.intersects(player1) && (!player1Left && !player1Right)) {
            player1Intersecting = false;
            playerColliosion(player1Intersecting);
        }
        if (player1Punch.intersects(player2)) {
            player1PunchRest = true;
            punchColliosion(true);
            player1Punch.width = 0;
        }
        if (player2Punch.intersects(player1)) {
            player2PunchRest = true;
            punchColliosion(false);
            player2Punch.width = 0;
        }
        /*if(player2Punch.intersects(player1)){
        punchColliosion(false);
        }*/

    }

    public void playerMovement() {
        if (player1Right) {
            if (player1.x + player1.width <= WIDTH) {
                player1.x = player1.x + player1MoveSpeed;
            }
        } else if (player1Left) {
            if (player1.x >= 0) {
                player1.x = player1.x - player1MoveSpeed;
            }
        }
        if (player1Jump && player1Standing) {
            // make it move upwards quickly
            player1Dy = player1JumpForce;
            player1Standing = false;
            // no longer player1Standing
        }

        // add in gravity
        player1Dy = player1Dy + gravity;
        player1.y = player1.y + player1Dy;

        if (player1.y + player1.height > ground) {
            player1.y = ground - player1.height;
            player1Standing = true;
        }
        if (player2Right) {
            if (player2.x + player2.width <= WIDTH) {
                player2.x = player2.x + player2MoveSpeed;
            }
        } else if (player2Left) {
            if (player2.x >= 0) {

                player2.x = player2.x - player2MoveSpeed;
            }
        }
        if (player2Jump && player2Standing) {
            // make it move upwards quickly
            player2Dy = player2JumpForce;
            player2Standing = false;
            // no longer player1Standing
        }

        // add in gravity
        player2Dy = player2Dy + gravity;
        player2.y = player2.y + player2Dy;

        if (player2.y + player2.height > ground) {
            player2.y = ground - player2.height;
            player2Standing = true;
        }
    }

    public void playerSetter() {
        if (player1Bool[0]) {
            player1.width = 110;
            player1.height = 110;
            player1Punch.height = 50;
            player1Punch.width = 100;
            player1MoveSpeed = 3;
            player1JumpForce = 0;

        } else if (player1Bool[1]) {
            player1.width = 50;
            player1.height = 140;
            player1Punch.height = 25;
            player1Punch.width = 100;
            player1MoveSpeed = 10;
            player1JumpForce = -5;

        } else if (player1Bool[2]) {
            player1.width = 50;
            player1.height = 50;
            player1MoveSpeed = 15;
            player1JumpForce = -25;

        }
        if (player2Bool[0]) {
            player2.width = 110;
            player2.height = 110;
            player2Punch.height = 50;
            player2Punch.width = 100;
            player2MoveSpeed = 3;
            player2JumpForce = 0;

        } else if (player2Bool[1]) {
            player2.width = 50;
            player2.height = 140;
            player2Punch.height = 25;
            player2Punch.width = 100;
            player2MoveSpeed = 10;
            player2JumpForce = -5;

        } else if (player2Bool[2]) {
            player2.width = 50;
            player2.height = 50;
            player2MoveSpeed = 15;
            player2JumpForce = -25;
        }
    }

    public void playerPunch() {
        if (player1PunchBool) {
            if (player1Punch.x < player1.x - player1Punch.width || player1Punch.x > player1.x + player1.width) {
                player1Punch.x = player1.x;
            }
            if (!player1Down && !player1Up) {
                player1Punch.y = player1.y + player1.height / 2 - player1Punch.height / 2;
            }
            if (player1Down) {
                player1Punch.y = player1.y + player1.height - player1Punch.height / 2;
                if (player1Punch.y + player1Punch.height > ground) {
                    player1Punch.y = ground - player1Punch.height;
                }
            }
            if (player1Up) {
                player1Punch.y = player1.y - player1Punch.height;
            }
            if (player1FacingRight) {
                if (player1Punch.x < player1.x + player1.width && !player1PunchRest) {
                    player1Punch.x = player1Punch.x + 5;
                    if (player1Punch.x == player1.x + player1.width) {
                        player1PunchRest = true;
                    }
                }
                if (player1PunchRest) {
                    if (player1Punch.x > player1.x) {
                        player1Punch.x = player1Punch.x - 10;
                    }
                }
            } else {
                if (player1Punch.x > player1.x - player1.width && !player1PunchRest) {
                    player1Punch.x = player1Punch.x - 5;
                    if (player1Punch.x == player1.x - player1.width) {
                        player1PunchRest = true;
                    }
                }
                if (player1PunchRest) {
                    if (player1Punch.x < player1.x) {
                        player1Punch.x = player1Punch.x + 5;
                    }
                }
            }
        }
        //player2Puching

        if (player2PunchBool) {
            if (player2Punch.x < player2.x - player2Punch.width || player2Punch.x > player2.x + player2.width) {
                player2Punch.x = player2.x;
            }
            if (!player2Down && !player2Up) {
                player2Punch.y = player2.y + player2.height / 2 - player2Punch.height / 2;
            }
            if (player2Down) {
                player2Punch.y = player2.y + player2.height - player2Punch.height / 2;
                if (player2Punch.y + player2Punch.height > ground) {
                    player2Punch.y = ground - player2Punch.height;
                }
            }
            if (player2Up) {
                player2Punch.y = player2.y - player2Punch.height;
            }
            if (player2FacingRight) {
                if (player2Punch.x < player2.x + player2.width && !player2PunchRest) {
                    player2Punch.x = player2Punch.x + 5;
                    if (player2Punch.x == player2.x + player2.width) {
                        player2PunchRest = true;
                    }
                }
                if (player2PunchRest) {
                    if (player2Punch.x > player2.x) {
                        player2Punch.x = player2Punch.x - 10;
                    }
                }
            } else {
                if (player2Punch.x > player2.x - player2.width && !player2PunchRest) {
                    player2Punch.x = player2Punch.x - 5;
                    if (player2Punch.x == player2.x - player2.width) {
                        player2PunchRest = true;
                    }
                }
                if (player2PunchRest) {
                    if (player2Punch.x < player2.x) {
                        player2Punch.x = player2Punch.x + 5;
                    }
                }
            }
        }
    }

    public void playerStretch() {
        if (player1Bool[2]) {
            if (!player1Down && !player1Up) {
                if (player1.height > 50) {
                    player1.height = player1.height - 2;
                    player1.y = player1.y + 2;
                } else if (player1.height < 50) {
                    player1.height++;
                    player1.y--;
                } else if (player1.width > 50) {
                    player1.width = player1.width - 2;
                } else if (player1.width < 50) {
                    player1.width = player1.width + 1;
                }
            } else if (player1Down && !player1Up) {
                if (player1.height > 4 && player1.height <= player1.width) {
                    player1.width = player1.width + 2;
                    player1.height--;
                    player1.y++;
                }
            } else if (player1Up && !player1Down) {
                if (player1.width > 4 && player1.width <= player1.height) {
                    player1.width--;
                    player1.height = player1.height + 2;
                    player1.y = player1.y - 2;
                } else {
                    player1.width = player1.width++;
                }
            }
        }
        if (player2Bool[2]) {
            if (!player2Down && !player2Up) {
                if (player2.height > 50) {
                    player2.height = player2.height - 2;
                    player2.y = player2.y + 2;
                } else if (player2.height < 50) {
                    player2.height++;
                    player2.y--;
                } else if (player2.width > 50) {
                    player2.width = player2.width - 2;
                } else if (player2.width < 50) {
                    player2.width = player2.width + 1;
                }
            } else if (player2Down && !player2Up) {
                if (player2.height > 4 && player2.height <= player2.width) {
                    player2.width = player2.width + 2;
                    player2.height--;
                    player2.y++;
                }
            } else if (player2Up && !player2Down) {
                if (player2.width > 4 && player2.width <= player2.height) {
                    player2.width--;
                    player2.height = player2.height + 2;
                    player2.y = player2.y - 2;
                } else {
                    player2.width = player2.width++;
                }
            }
        }
    }

    public void playerColliosion(boolean player1Intersecting) {
        Rectangle collision = player1.intersection(player2);

        if (player1Intersecting) {
            if (collision.width > collision.height) {
                if (player1.y > player2.y) {
                    player1.y = player1.y - collision.height;
                } else {
                    player1.y = player1.y + collision.height;
                }

            }
            if (collision.width < collision.height) {
                if (player1.x < player2.x) {
                    player1.x = player1.x - collision.width;
                } else {
                    player1.x = player1.x + collision.width;
                }
            }
        } else {
            if (collision.width > collision.height) {
                if (player2.y > player1.y) {
                    player2.y = player2.y - collision.height;
                } else {
                    player2.y = player2.y + collision.height;
                }

            }
            if (collision.width < collision.height) {
                if (player2.x < player1.x) {
                    player2.x = player2.x - collision.width;
                } else {
                    player2.x = player2.x + collision.width;
                }
            }
        }
    }

    public void punchColliosion(boolean punch1Intersecting) {

        if (punch1Intersecting) {
            if (player2.x > player1Punch.x) {
                player2.x = player2.x + 150;
            } else {
                player2.x = player2.x - 150;
            }

        } else {
            if (player1.x > player2Punch.x) {

                player1.x = player1.x + 150;
            } else {
                player1.x = player1.x - 40;
            }

        }
    }

    // Used to implement any of the Mouse Actions
    private class Mouse extends MouseAdapter {

        // if a mouse button has been pressed down
        @Override
        public void mousePressed(MouseEvent e) {
            int click = e.getButton();
            int mouseX = e.getX();
            int mouseY = e.getY();
            if (click == MouseEvent.BUTTON1) {

            }
            if (menuScreen) {
                if (startButton.contains(mouseX, mouseY)) {
                    characterSelScreen = true;
                    menuScreen = false;
                }
            }
            if (characterSelScreen) {
                if (player1Button[0].contains(mouseX, mouseY)) {
                    player1Bool[0] = true;
                    player1Bool[1] = false;
                    player1Bool[2] = false;
                } else if (player1Button[1].contains(mouseX, mouseY)) {
                    player1Bool[1] = true;
                    player1Bool[0] = false;
                    player1Bool[2] = false;

                } else if (player1Button[2].contains(mouseX, mouseY)) {
                    player1Bool[2] = true;
                    player1Bool[1] = false;
                    player1Bool[0] = false;
                }

                if (player2Button[0].contains(mouseX, mouseY)) {
                    player2Bool[0] = true;
                    player2Bool[1] = false;
                    player2Bool[2] = false;
                } else if (player2Button[1].contains(mouseX, mouseY)) {
                    player2Bool[1] = true;
                    player2Bool[0] = false;
                    player2Bool[2] = false;

                } else if (player2Button[2].contains(mouseX, mouseY)) {
                    player2Bool[2] = true;
                    player2Bool[1] = false;
                    player2Bool[0] = false;
                }
                if (startGameButton.contains(mouseX, mouseY)) {
                    characterSelScreen = false;
                    mapOne = true;
                }
            }
        }

        // if a mouse button has been released
        @Override
        public void mouseReleased(MouseEvent e) {

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
                player1Jump = true;
            }
            if (key == KeyEvent.VK_S) {
                player1Down = true;
            }
            if (key == KeyEvent.VK_D) {
                player1Right = true;
                player1FacingRight = true;
            }
            if (key == KeyEvent.VK_A) {
                player1Left = true;
                player1FacingRight = false;
            }
            if (key == KeyEvent.VK_W) {
                player1Up = true;
            }
            if (key == KeyEvent.VK_E) {
                player1PunchBool = true;
            }
            
            //player 2 controls
            if (key == KeyEvent.VK_M) {
                player2Jump = true;
            }
            if (key == KeyEvent.VK_DOWN) {
                player2Down = true;
            }
            if (key == KeyEvent.VK_RIGHT) {
                player2Right = true;
                player2FacingRight = true;
            }
            if (key == KeyEvent.VK_LEFT) {
                player2Left = true;
                player2FacingRight = false;
            }
            if (key == KeyEvent.VK_UP) {
                player2Up = true;
            }
            if (key == KeyEvent.VK_N) {
                player2PunchBool = true;
            }
        }

        // if a key has been released
        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_SPACE) {
                player1Jump = false;
            }
            if (key == KeyEvent.VK_S) {
                player1Down = false;
            }
            if (key == KeyEvent.VK_D) {
                player1Right = false;
            }
            if (key == KeyEvent.VK_A) {
                player1Left = false;
            }
            if (key == KeyEvent.VK_W) {
                player1Up = false;
            }
            if (key == KeyEvent.VK_E) {
                player1PunchBool = false;
                player1PunchRest = false;
            }
            if (key == KeyEvent.VK_M) {
                player2Jump = false;
            }
            if (key == KeyEvent.VK_DOWN) {
                player2Down = false;
            }
            if (key == KeyEvent.VK_RIGHT) {
                player2Right = false;
            }
            if (key == KeyEvent.VK_LEFT) {
                player2Left = false;
            }
            if (key == KeyEvent.VK_UP) {
                player2Up = false;
            }
            if (key == KeyEvent.VK_N) {
                player2PunchBool = false;
                player2PunchRest = false;
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
        SmooshBrosDuel_TheGame game = new SmooshBrosDuel_TheGame();
    }
}
