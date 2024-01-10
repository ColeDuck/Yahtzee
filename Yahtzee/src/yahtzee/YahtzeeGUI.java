
package yahtzee;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class YahtzeeGUI {
    
    public static String diceOneIcon = "Images/d1.png";
    public static String diceTwoIcon = "Images/d2.png";
    public static ImageIcon blank = new ImageIcon("Images/d0.png");
    public static boolean[] diceHeld = {false, false, false, false, false};
    public static JLabel[] diceLabels = new JLabel[5];
    private static JLabel playerTurnDisplay = new JLabel();
    private static JLabel playerRollsLeftDisplay = new JLabel();
    private static JLabel lblDiceOne = new JLabel();
    private static JLabel lblDiceTwo = new JLabel();
    private static JLabel lblDiceThree = new JLabel();
    private static JLabel lblDiceFour = new JLabel();
    private static JLabel lblDiceFive = new JLabel();
    
    private static int[] playerBoardPositionsX = {0, 0, 1500, 1500, 0, 800, 800, 1500};
    private static int[] playerBoardPositionsY = {0, 560, 0, 560, 300, 0, 560, 560};
    
    public static void main(String[] args) {
        
        Yahtzee.initializeArrays();
        
        SwingUtilities.invokeLater(() -> {
            createStartPanel();
        });
    }

    private static void createStartPanel() {
        
        JFrame startFrame = new JFrame("Yahtzee Start Page");
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel startPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JTextArea nameDisplayTextArea = new JTextArea();
        nameDisplayTextArea.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        startPanel.add(new JScrollPane(nameDisplayTextArea), gbc);
        
        JButton addPlayerButton = new JButton("Add Player");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        startPanel.add(addPlayerButton, gbc);
        
        JButton startGameButton = new JButton("Start Game");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        startPanel.add(startGameButton, gbc);
        
        JTextField nameEnterTextField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        startPanel.add(nameEnterTextField, gbc);
        
        // Add action listeners to the buttons
        addPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameEnterTextField.getText();
                String display = addPlayer(name);
                nameDisplayTextArea.append(display);
            }
        });

        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startFrame.dispose();
                startGame();
            }
        });
        
        startFrame.getContentPane().add(startPanel);
        startFrame.setSize(600, 300);
        startFrame.setLocationRelativeTo(null);
        startFrame.setVisible(true);
    }
    
    public static void createGamePanel() {
        // Create the JFrame
        JFrame gameFrame = new JFrame("Yahtzee");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the JPanel
        JPanel gamePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        
        JLabel lblDiceOne = new JLabel();
        lblDiceOne.setIcon(blank);
        diceLabels[0] = lblDiceOne;
        gamePanel.add(lblDiceOne, gbc);
        
        gbc.gridx = 1;
        JLabel lblDiceTwo = new JLabel();
        lblDiceTwo.setIcon(blank);
        diceLabels[1] = lblDiceTwo;
        gamePanel.add(lblDiceTwo, gbc);
        
        gbc.gridx = 2;
        JLabel lblDiceThree = new JLabel();
        lblDiceThree.setIcon(blank);
        diceLabels[2] = lblDiceThree;
        gamePanel.add(lblDiceThree, gbc);
        
        gbc.gridx = 3;
        JLabel lblDiceFour = new JLabel();
        lblDiceFour.setIcon(blank);
        diceLabels[3] = lblDiceFour;
        gamePanel.add(lblDiceFour, gbc);
        
        gbc.gridx = 4;
        lblDiceFive.setIcon(blank);
        diceLabels[4] = lblDiceFive;
        gamePanel.add(lblDiceFive, gbc);
        
        JButton rollButton = new JButton("Roll!");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gamePanel.add(rollButton, gbc);
        
        
        playerTurnDisplay.setFont(new Font("Arial", Font.BOLD, 30));
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gamePanel.add(playerTurnDisplay, gbc);
        
        playerRollsLeftDisplay.setFont(new Font("Arial", Font.BOLD, 30)); 
        gbc.gridy = 6;
        gamePanel.add(playerRollsLeftDisplay, gbc);
        
        rollButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Yahtzee.gameManager("Roll", null);
            }
        });
        
        lblDiceOne.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleDiceHeld(0);
            }
        });
        
        lblDiceTwo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleDiceHeld(1);
            }
        });
        
        lblDiceThree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleDiceHeld(2);
            }
        });
        
        lblDiceFour.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleDiceHeld(3);
            }
        });
        
        lblDiceFive.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleDiceHeld(4);
            }
        });

        gameFrame.getContentPane().add(gamePanel);
        gameFrame.setSize(1100, 400);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
    }
     
    private static void showErrorDialog(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    private static String addPlayer(String name) {
        
        //if name is empty, display error
        if (name.isEmpty()) {
           showErrorDialog("Player name must not be empty", "Error");
            return "";
        }
                
        //if name is greater than 15 charactrs, display error
        if (name.length() > 15) {
            showErrorDialog("Name must be less than 15 characters", "Error");
            return "";
        }
        
        //loop over all names and check if player name already exists
        boolean passed = true;        
        for (Player player : Yahtzee.players) {
            if (player.getName().equals(name)) {
                passed = false;
            }
        }
             
        //if player exists, display error, otherwise add new player and display to screen
        if (passed) {
            Yahtzee.players.add(new Player(name));
            return(name + "\n");
        } else {
            showErrorDialog("This player already exists", "Error");
            return "";
        }  
    }
    
    private static void startGame() {
        
        //open game panel
        createGamePanel();
                
        //create score cards for each player
        for (int i = 0; i < Yahtzee.players.size(); i++) {
            int x = playerBoardPositionsX[i];
            int y = playerBoardPositionsY[i];
            Yahtzee.players.get(i).createPlayerGUI(x,y);
        }
        
        Yahtzee.gameManager("Start", null);
    }
    
    public static void animateDice() {
        Random rand = new Random();
        Timer timer = new Timer(50, new ActionListener() {
            
            int rolls = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rolls <= 20) {
                    //go through 5 dice and animate them
                    System.out.println("made it here!" + rolls);
                    for (int i = 0; i < 5; i++) {
                        //if the dice is being held, do not animate it, otherwise, animate it
                        if (!diceHeld[i]) {
                            //get random number
                            int randomNumber = rand.nextInt(6) + 1;
                            //set label to dice based on random number
                            diceLabels[i].setIcon(new ImageIcon("Images/d" + randomNumber + ".png"));
                        } 
                    }
                    rolls++;
                } else {
                    ((Timer) e.getSource()).stop();
                    
                    for (int i = 0; i < 5; i++) {
                        //if the dice is being held, do update it
                        if (!diceHeld[i]) {
                            
                            //set label to dice based on random number
                            diceLabels[i].setIcon(new ImageIcon("Images/d" + Yahtzee.diceValues[i] + ".png"));
                        } 
                    }
                    Yahtzee.rolling = false;
                }
            }
        });
        
        timer.start();
    }
    
    private static void toggleDiceHeld(int index) {
        
        //if rolling, the user should not be able to interact with the dice, so do nothing
        if (Yahtzee.rolling) {
            System.out.println("return");
            return;     
        }

        //flip current state.
        diceHeld[index] = !diceHeld[index];
        
        //change icon
        if (diceHeld[index]) {
            System.out.println("Images/d" + Yahtzee.diceValues[index] + "held.png");
            diceLabels[index].setIcon(new ImageIcon("Images/d" + Yahtzee.diceValues[index] + "held.png"));
        } else {
            diceLabels[index].setIcon(new ImageIcon("Images/d" + Yahtzee.diceValues[index] + ".png"));
        }
    }
    
    public static void updateVisuals() {
        playerTurnDisplay.setText(Yahtzee.players.get(Yahtzee.currentPlayer).getName() + "'s turn");
        playerRollsLeftDisplay.setText(Yahtzee.rollsLeft + " rolls left");
        
        for (int i = 0; i < Yahtzee.diceValues.length; i++) {
            toggleDiceHeld(i);
        }

    }
    
    public static void createEndScreen() {
        
        // Create the JFrame
        JFrame endFrame = new JFrame("Yahtzee");
        endFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the JPanel
        JPanel endPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        for (int i = 0; i < Yahtzee.players.size(); i++) {
            gbc.gridy = i;
            JLabel scoreDisplay = new JLabel(Yahtzee.players.get(i).getName() + ": " + Yahtzee.players.get(i).getTotal());
            endPanel.add(scoreDisplay, gbc);
        }
    }
}