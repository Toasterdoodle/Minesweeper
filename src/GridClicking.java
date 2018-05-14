import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.InputStream;

/**

 _  _ _ _  _ ____ ____ _ _ _ ____ ____ ___  ____ ____
 |\/| | |\ | |___ [__  | | | |___ |___ |__] |___ |__/
 |  | | | \| |___ ___] |_|_| |___ |___ |    |___ |  \


 */

//------------------------------------------------

//I WOULD LIKE TO GIVE A SHOUTOUT TO MY MAN ROBERT GLAZER

//ROBERT GLAZER, YOU THE MAN

//------------------------------------------------

//Michael Chen

public class GridClicking extends JPanel {

    //info:
    //-1 is a mine
    //0 means there are 0 mines around the tile
    //1-8 means that there are 1-8 mines around

    //instance fields
    private int[][] board = new int[15][15];//board is the board containing
    private boolean[][] revealedBoard = new boolean[15][15];//this board keeps track of whether the area has been revealed or not
    private boolean[][] flagged = new boolean[15][15];//checks if the tile is flagged
    private int size;
    int clickcounter = 0;
    boolean flagging = false;
    int flagsLeft = 50;
    int time = 0;
    
    //// TODO: 3/28/18 Write code to run the mine planter and mine checker AFTER your first click 

    //-------------------------------------

    public GridClicking(int width, int height) {
        //initializes the board to be completely blank
        //initializes the revealedBoard to be all false
        //initializes the flagged board to be completely false

        Timer timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                time++;

                repaint();

            }//end actionPerformed

        });//end timer

        timer.start();

        setSize(width, height);

        for (int r = 0; r < board.length; r++) {

            for (int c = 0; c < board[0].length; c++) {

                board[r][c] = 0;

                revealedBoard[r][c] = false;

                flagged[r][c] = false;

            }//end for

        }//end for

        size = 30;

        setupMouseListener();

        addKeyListener(keyListener);

        plantMines(50);

        //=======background music=========

        try {
            // open the sound file as a Java input stream
            String hop = "res/music1.wav";
            InputStream in = new FileInputStream(hop);
            // create an audiostream from the inputstream
            AudioStream audioStream = new AudioStream(in);
            // play the audio clip with the audioplayer class
            AudioPlayer.player.start(audioStream);
        } //end try
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading sound file.");
        }//end catch

        //===========looper========

        Timer music = new Timer(201000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {
                    // open the sound file as a Java input stream
                    String hop = "res/music1.wav";
                    InputStream in = new FileInputStream(hop);
                    // create an audiostream from the inputstream
                    AudioStream audioStream = new AudioStream(in);
                    // play the audio clip with the audioplayer class
                    AudioPlayer.player.start(audioStream);
                } //end try
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error loading sound file.");
                }//end catch

            }});//end timer

        music.start();

        //--------------------------------

        for (int r = 0; r < board.length; r++) {
            //initializes the tiles to contain the number of mines around them

            for (int c = 0; c < board[0].length; c++) {

                numMinesAround(r, c);

            }//end for

        }//end for

        String space;

        for (int r = 0; r < board.length; r++) {
            //prints an answer key if ur a piece of scum and don't want to play like a real man

            for (int c = 0; c < board[0].length; c++) {

                if(board[r][c] == -1){

                    space = "  ";

                }//end if
                else{

                    space = "   ";

                }//end else

                System.out.print("" + board[r][c] + space);

            }//end for

            System.out.println();

        }//end for

    }//end GridClicking

    //-------------------------------------

    public void plantMines(int num){
        //initializes random tiles to be mines
        //mines are -1

        for (int r = 0; r < num; r++) {

            int x = (int)(Math.random() * 15);

            int y = (int)(Math.random() * 15);

            if(board[x][y] != -1) {
                //checks if the tile is already a mine

                board[x][y] = -1;

            }//end if

        }//end for

    }//end plantMines

    //-------------------------------------

    public void numMinesAround(int r, int c){
        //the board to contain the number of tiles around it

        if (board[r][c] != -1) {
            //checks if the tile is not a mine

            //-----INITIATE NEIGHBOR COUNTING SEQUENCE------

            int count = 0;

            //-----ROW R-1-----

            if (r - 1 >= 0) {

                if (c - 1 >= 0) {

                    if (board[r - 1][c - 1] == -1) {

                        count++;

                    }//end if

                }//end if

                if (board[r - 1][c] == -1) {

                    count++;

                }//end if

                if (c + 1 < board[0].length) {

                    if (board[r - 1][c + 1] == -1) {

                        count++;

                    }//end if

                }//end if

            }//if

            //----ROW R-----

            if (c - 1 >= 0) {

                if (board[r][c - 1] == -1) {

                    count++;

                }//end if

            }//end if

            if (c + 1 < board.length) {

                if (board[r][c + 1] == -1) {

                    count++;

                }//end if

            }//end if

            //----ROW R+1-----

            if (r + 1 < board.length) {

                if (c - 1 >= 0) {

                    if (board[r + 1][c - 1] == -1) {

                        count++;

                    }//end if

                }//end if

                if (board[r + 1][c] == -1) {

                    count++;

                }//end if

                if (c + 1 < board.length) {

                    if (board[r + 1][c + 1] == -1) {

                        count++;

                    }//end if

                }//end if

            }//end if

            //-------------OK WE DONE-------------

            board[r][c] = count;

        }//end if

    }//end numMinesAround

    //-------------------------------------

    public void revealCell(int r, int c){
        //if a cell is clicked, it will reveal all the cell

        boolean wasRevealed;

        if(r < board.length && c < board[0].length && r > -1 && c > -1){

            wasRevealed = revealedBoard[r][c];

            revealedBoard[r][c] = true;

            if(board[r][c] == 0 && !wasRevealed){

                if(r + 1 < board.length) {

                    revealCell(r + 1, c);

                }//end if

                if(r - 1 > -1) {

                    revealCell(r - 1, c);

                }//end if

                if(c - 1 > -1) {

                    revealCell(r, c - 1);

                }//end if

                if(c + 1 < board[0].length){

                    revealCell(r, c + 1);

                }//end if

            }//end if

        }//end if

    }//end revealCell

    //-------------------------------------

    public void flagCell(int r, int c){
        //flags the clicked cell

        if(!revealedBoard[r][c]) {
            //chceks that the clicked cell is not already revealed

            if(!flagged[r][c]) {

                flagged[r][c] = true;

                flagsLeft--;

            }//end if
            else{

                flagged[r][c] = false;

                flagsLeft++;

            }//end else

        }//end if

    }//end flagCell

    //-------------------------------------

    public void paintComponent(Graphics g) {

        String numNeighbors;

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(190, 190, 190));

        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(new Color(73, 109, 207));

        g2.setFont(new Font("Comic Sans", Font.BOLD, 40));

        g2.drawString("MINE", 455, 35);

        g2.setFont(new Font("Comic Sans", Font.BOLD, 25));

        g2.drawString("SWEEPER", 455, 65);

        for (int r = 0; r < board.length; r++) {

            for (int c = 0; c < board[0].length; c++) {

                if(revealedBoard[r][c]) {

                    if (board[r][c] == 0){
                        //if the tile is a blank tile and it is clicked (no mines around it)

                        g2.setColor(new Color(214, 214, 214));

                        g2.fillRect(c * size, r * size, size, size);

                        g2.setColor(new Color(40, 40, 40));

                        g2.drawRect(c * size, r * size, size, size);

                    }//end if
                    else if(board[r][c] == -1){

                        g2.setColor(new Color(214, 214, 214));
                        //draw the tile

                        g2.fillRect(c * size, r * size, size, size);

                        g2.setColor(new Color(40, 40, 40));

                        g2.drawRect(c * size, r * size, size, size);

                        g2.setColor(new Color(218, 0, 8));

                        g2.setFont(new Font("Comic Sans", Font.BOLD, 25));

                        g2.drawString("B", c * size + size / 3 - 2, r * size + size / 3 + 15);

                    }//end else if
                    else{
                        //if the tile has a mine around it

                        g2.setColor(new Color(214, 214, 214));
                        //draw the tile

                        g2.fillRect(c * size, r * size, size, size);

                        g2.setColor(new Color(40, 40, 40));

                        g2.drawRect(c * size, r * size, size, size);

                        g2.setColor(new Color(0, 165, 8));

                        numNeighbors = ("" + board[r][c]);
                        //draw the number of neighbors

                        g2.setFont(new Font("Comic Sans", Font.BOLD, 25));

                        g2.drawString(numNeighbors, c * size + size / 3 - 2, r * size + size / 3 + 15);

                    }//end else

                }//end if
                else if(!revealedBoard[r][c]){
                    //if the tile is unclicked

                    if(!flagged[r][c]) {
                        //if the tile has not been flagged

                        g2.setColor(new Color(90, 90, 90));

                        g2.fillRect(c * size, r * size, size, size);

                        g2.setColor(new Color(40, 40, 40));

                        g2.drawRect(c * size, r * size, size, size);

                    }//end if
                    else{

                        g2.setColor(new Color(227, 136, 0));

                        g2.fillRect(c * size, r * size, size, size);

                        g2.setColor(new Color(40, 40, 40));

                        g2.drawRect(c * size, r * size, size, size);

                    }//end else

                }//end else

            }//end for

        }//end for

        g2.setColor(new Color(40, 40, 40));

        g2.fillOval(50, 470, 40, 40);

        if(!flagging){

            g2.setColor(new Color(220, 26, 2));

            g2.fillOval(55, 475, 30, 30);

        }//end if
        else{

            g2.setColor(new Color(49, 203, 45));

            g2.fillOval(55, 475, 30, 30);

        }//end if

        g2.setColor(new Color(73, 109, 207));

        g2.setFont(new Font("Comic Sans", Font.BOLD, 25));

        g2.drawString("Flagging Mode", 100, 500);

        g2.setColor(new Color(227, 136, 0));

        g2.fillRect(50, 540, 40, 40);

        g2.setColor(new Color(209, 210, 0));

        g2.setFont(new Font("Comic Sans", Font.BOLD, 25));

        g2.drawString("" + flagsLeft, 53, 570);

        g2.setColor(new Color(73, 109, 207));

        g2.setFont(new Font("Comic Sans", Font.BOLD, 25));

        g2.drawString("Number of Flags Left", 100, 570);

        g2.setColor(new Color(50, 50, 50));

        g2.fillRect(335, 470, 55, 40);

        g2.setColor(new Color(227, 227, 227));

        g2.setFont(new Font("Comic Sans", Font.BOLD, 25));

        g2.drawString("" + time, 338, 500);

        g2.setColor(new Color(73, 109, 207));

        g2.setFont(new Font("Comic Sans", Font.BOLD, 25));

        g2.drawString("Time Spent", 400, 500);

    }//end paintComponenet

    //-------------------------------------


    public void setupMouseListener(){
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }//end mouseClicked

            @Override
            public void mousePressed(MouseEvent e) {

                clickcounter++;

                int x = e.getX();
                int y = e.getY();

                int r = y / size;
                int c = x / size;

                if(!flagging) {

                    if(r < 15 && c < 15) {

                        if (!flagged[r][c]) {

                            revealCell(r, c);

                        }//end if

                    }//end if

                }//end if
                else{

                    if(r < 15 && c < 15) {

                        flagCell(r, c);

                    }//end if

                }//end if

                repaint();

                System.out.println(e.getX() + ", " + e.getY());

            }//end mousePressed

            @Override
            public void mouseReleased(MouseEvent e) {

            }//end mouseReleased

            @Override
            public void mouseEntered(MouseEvent e) {

            }//end mouseEntered

            @Override
            public void mouseExited(MouseEvent e) {

            }//end mouseExited

        });//end mouseListener

    }//end setupMouseListener

    //-------------------------------------

    KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {

        }//end keyTyped

        @Override
        public void keyPressed(KeyEvent e) {

            int keyCode = e.getKeyCode();

            if(keyCode == e.VK_SPACE){
                //if the space bar is pressed, it sets the cursor into flagging mode

                if(flagging){

                    flagging = false;

                    System.out.println("=== FLAGGING DISABLED ===");

                }//end if
                else{

                    flagging = true;

                    System.out.println("=== FLAGGING ENABLED ===");

                }//end else

                System.out.println(flagging);

                repaint();

            }//end if

        }//end keyPressed

        @Override
        public void keyReleased(KeyEvent e) {

        }//end keyReleased

    };//end keyListener

    //-------------------------------------

    //sets ups the panel and frame.  Probably not much to modify here.
    public static void main(String[] args) {

        JFrame window = new JFrame("DAS MINESCHWEEPEN");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(0, 0, 600, 600 + 22); //(x, y, w, h) 22 due to title bar.

        GridClicking panel = new GridClicking(600, 600);

        panel.setFocusable(true);
        panel.grabFocus();

        window.add(panel);
        window.setVisible(true);
        window.setResizable(false);

    }//end psvm

    //-------------------------------------

}//end class