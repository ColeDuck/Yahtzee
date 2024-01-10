
package yahtzee;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Player {
    
    private Map<String, Integer> scores = new HashMap<>();
    private String userName;
    private JLabel[] entreeNames;
    private JLabel[] pointDisplay;
    private JButton[] buttons;
            
    Player(String name) {
        userName = name;
        
        for (String entree : Yahtzee.ENTREES) {
            scores.put(entree, -1);
        }
    }
    
    public int getValue(String key) {
        return scores.get(key);
    }
    
    public String getName() {
        return userName;
    }
    
    public void setValue(String key, int score) {
        scores.put(key, score);
    }
    
    public boolean fullCard() {
        for (String entree : Yahtzee.ENTREES) {
            if (getValue(entree) == -1) {
                return false;
            }
        }
    return true;
    }
    
    public int getTotal() {
        int total = 0;
        for (String entree : Yahtzee.ENTREES) {
            total += getValue(entree);
        }
        return total;
    }
    
    public void createPlayerGUI(int x, int y) {
        
        // Create the JFrame
        JFrame playerFrame = new JFrame(userName + "'s Board");
        playerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the JPanel
        JPanel playerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        entreeNames = new JLabel[Yahtzee.ENTREES.length];
        pointDisplay = new JLabel[Yahtzee.ENTREES.length];
        buttons = new JButton[Yahtzee.ENTREES.length];
        
        for (int i = 0; i < Yahtzee.ENTREES.length; i++) {
            
            entreeNames[i] = new JLabel(Yahtzee.ENTREES[i]);
            pointDisplay[i] = new JLabel("0");
            buttons[i] = new JButton("Select");
            
            final String entreeName = entreeNames[i].getText();
            
            final Player thisInstance = this;
            
            gbc.gridy = i;
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(5, 0, 0, 40);
            entreeNames[i].setFont(new Font("Arial", Font.BOLD, 16));
            pointDisplay[i].setFont(new Font("Arial", Font.BOLD, 16));
            buttons[i].setFont(new Font("Arial", Font.BOLD, 16));
            //gbc.gridwidth = 1;
            //gbc.gridheight = 1;
            //gbc.fill = GridBagConstraints.BOTH;
            //gbc.weightx = 1.0;
            //gbc.weighty = 1.0;
            playerPanel.add(entreeNames[i], gbc);
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.gridx = 1;
            playerPanel.add(pointDisplay[i], gbc);
            gbc.gridx = 2;
            playerPanel.add(buttons[i], gbc);
            
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(entreeName);
                    Yahtzee.gameManager(entreeName, thisInstance);
                }
            });
        }
        
        playerFrame.getContentPane().add(playerPanel);
        playerFrame.setSize(400, 500);
        playerFrame.setLocation(x, y);
        playerFrame.setVisible(true);
        disableButtons();
    }
    
    public void disableButtons() {
        System.out.println("disable buttons" + userName + Yahtzee.currentPlayer);
        for (JButton button : buttons) {
            button.setEnabled(false);
        }
    }
    
    public void enableButtons() {
        System.out.println("enable buttons" + userName + Yahtzee.currentPlayer);
        for (int i = 0; i < entreeNames.length; i++) {
            //if score is equal to the defaul -1, then the user
            //hasn't selected that option yet, therefore, add the button
            if (getValue(entreeNames[i].getText()) == -1) {
                buttons[i].setEnabled(true);
            }
        }
    }

    public void displayValues() {
        
        for (int i = 0; i < Yahtzee.ENTREES.length; i++) {
            if (scores.get(Yahtzee.ENTREES[i]) == -1) {
                pointDisplay[i].setText("" + Yahtzee.currentPossibleValues[i]);
            } else {
                pointDisplay[i].setText("" + scores.get(Yahtzee.ENTREES[i]));
            }
       }
    }
}
