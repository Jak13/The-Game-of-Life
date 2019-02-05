/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jack
 */
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author Jack
 */
public class Game extends JPanel {

    private final int columns, rows;
    private boolean isLiving;
    public static Random random = new Random();

    public Game(int r, int c) {
        this.columns = c;
        this.rows = r;
        MouseListener listener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isLiving = !isLiving;
                repaint();
            }
        };
        this.addMouseListener(listener);
        isLiving = random.nextBoolean();
    }

    //isAlive method is a boolean set to ask whether a given cell is alive while asking given scenarios.
    public boolean isAlive(int neighbors) {
        boolean alive = false;
        if (this.isLiving) {
            if (neighbors < 2) {    //Scenario 1: Underpopulation: If a live cell has less than two neighbours, the cell will die.
                alive = false;
            } else if (neighbors == 2 || neighbors == 3) { //Scenario 3: Survival: If a live cell has two or three neighbours, the cell will stay alive.
                alive = true;
            } else if (neighbors > 3) { //Scenario 2: Overcrowding: if a live cell has more than 3 neighbours, the cell will die.
                alive = false;
            }
        } else {
            if (neighbors == 3) { //Scenario 4: Creation of Life: if a empty postion has three existing neighbours, the cell wil be created. NOTE: you will need at click on a cell near these neighbours to make this scenario.
                alive = true;
            }
        }
        return alive;
    }

    //setAlive is when we will create a cell in a given postion.  
    public void setAlive(boolean alive) {
        isLiving = alive;
    }

    //isLiving is set asking whether a given cell in still living on the grid.
    public boolean isLiving() {
        return this.isLiving;
    }

    @Override //paintComponent draws the grid.
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.isLiving) {
            g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        } else {
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }

    public static void main(String[] args) {
        final int s = 15; //Sets the number of rows and columns to add on the grid. Could have add in a textfield lisener to given users to add in the rows and columns themsevles, but couldn't due to time.
        final Game[][] board = new Game[s][s]; //Here we are setting up the space of the panel which we are using to show the grid.
        final JPanel gui = new JPanel(new GridLayout(s, s, 2, 2)); //Here we are setting up the layout of the panel, with the number of rows and columns.
        for (int ii = 0; ii < s; ii++) {
            for (int jj = 0; jj < s; jj++) {
                Game cell = new Game(ii, jj);
                cell.setPreferredSize(new Dimension(10, 10));
                gui.add(cell);
                board[ii][jj] = cell;
            }
        }

        ActionListener al = (ActionEvent ae) -> {
            boolean[][] living = new boolean[s][s]; //Here we are setting up an array of cells that are living on the grid to check.
            int bottom;
            int top;
            int left;
            int right;
            int neighbors = 0;
            for (int ii = 0; ii < s; ii++) {
                for (int jj = 0; jj < s; jj++) {
                    if (jj > 0) {
                        top = jj - 1;
                    } else {
                        top = s - 1;
                    }
                    if (jj < s - 1) {
                        bottom = jj + 1;
                    } else {
                        bottom = 0;
                    }
                    if (ii > 0) {
                        left = ii - 1;
                    } else {
                        left = s - 1;
                    }
                    if (ii < s - 1) {
                        right = ii + 1;
                    } else {
                        right = 0;
                    }

                    if (board[ii][top].isLiving()) {
                        neighbors++;
                    }
                    if (board[ii][bottom].isLiving()) {
                        neighbors++;
                    }
                    if (board[left][top].isLiving()) {
                        neighbors++;
                    }
                    if (board[left][right].isLiving()) {
                        neighbors++;
                    }
                    if (board[left][jj].isLiving()) {
                        neighbors++;
                    }
                    if (board[right][jj].isLiving()) {
                        neighbors++;
                    }
                    if (board[right][top].isLiving()) {
                        neighbors++;
                    }
                    if (board[right][bottom].isLiving()) {
                        neighbors++;
                    }
                    living[ii][jj] = board[ii][jj].isAlive(neighbors); //Here we are asking the array of living cells to check with the main grid if any cells can be alive based on the given scenarios.
                }
            }
            for (int ii = 0; ii < s; ii++) {
                for (int jj = 0; jj < s; jj++) {
                    board[ii][jj].setAlive(living[ii][jj]); //We are setting up any random cell within a given position to be alive.
                }
            }
            gui.repaint();
        };

        Timer timer = new Timer(50, al);
        timer.start();

        JOptionPane.showMessageDialog(null, gui);
        timer.stop();
    }
}
