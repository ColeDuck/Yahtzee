package yahtzee;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Yahtzee {
    
    static Scanner scan = new Scanner(System.in);
        
    static Random rand = new Random();
    
    static final int NUMBER_OF_DICE = 5;
    
    static ArrayList<Player> players = new ArrayList<>();

    static int[] diceValues = new int[NUMBER_OF_DICE];
    static boolean[] diceHeld = new boolean[NUMBER_OF_DICE];

    static final String[] ENTREES = {"Ones", "Twos","Threes", "Fours", "Fives", "Sixes", "Three Of A Kind", "Four Of A Kind", "Full House",
                               "Small Straight", "Large Straight", "Yahtzee", "Chance"};
    
    static int[] currentPossibleValues = new int[ENTREES.length];

    public static void main(String[] args) {
        
        for (int i = 0; i < NUMBER_OF_DICE; i++) {
            diceValues[i] = 0;
            diceHeld[i] = false;
        }
        
        for (int i = 0; i < ENTREES.length; i++) {
            currentPossibleValues[i] = 0;
        }
        
        System.out.println("Add Player by typing their name! Otherwise, type \"Start\" to start playing!");
        
        String input = "";
        
        while (true) {
            input = scan.nextLine();
            
            if ("Start".equals(input)) {
                break;
            }
            
            players.add(new Player(input));
        }
        
        RunGame();
    }
        
    
    static public void RunGame() {
        
        String input;
        String output;
        int rollsLeft; // 3 rolls in yahtzee
        
        System.out.println("Welcome to Yahtzee!");
        
        boolean loop = true;
        
        //loop until all players are out of entrees
        while (loop) {
            
            //go through each player and let them play their turn
            for (Player player : players) {
                
                //if a player has a full card, skip their turn
                if (player.fullCard()) {
                    continue;
                }
                //reset all variables for new turn
                resetHeld();
            
                rollsLeft = 3;
                
                System.out.println("It is " + player.getName() + "'s turn! Rolling...");
                
                rollDice();
                
                while (rollsLeft > 0) {

                    System.out.println("Dices: " + Arrays.toString(diceValues));
                    output = calculateChoices(player);
                    System.out.println(output);
                
                    System.out.println("Please choose to either reroll with \"Roll\", choose to hold dice with \"124\", or choose points to enter by typing its name");
                
                    input = scan.nextLine();
                    
                    boolean roll = false;
                    //get dice held
                    for (int i = 0; i < diceHeld.length; i++) {
                        if (input.contains(String.valueOf(i + 1))) {
                            diceHeld[i] = true;
                            //automatically roll
                            roll = true;
                        }
                    }

                   
                    if (input.equals("Roll") || roll) {
                        
                        rollDice();
                        
                        //reset variables and decrease remaining roles
                        resetHeld();
                        rollsLeft--;
                        
                        //loop back
                        continue;
                    }
                    
                    //check all entrees
                    for (int i = 0; i < ENTREES.length; i++) {
                        
                        if (input.equals(ENTREES[i])) {
                            if (player.getValue(ENTREES[i]) == -1) {
                                //update users score card
                                player.setValue(ENTREES[i], currentPossibleValues[i]);
                                //set rollsLeft to 0 to exit loop
                                rollsLeft = 0;
                                
                            } else {
                                System.out.println("You already have this entree on your card");
                            }
                        }
                    }
                }
            }
            
            //check if any players have 
            loop = false;
            
            for (Player player : players) {
                if (!player.fullCard()) {
                    loop = true;
                }
            }
        }
        
        for (Player player : players) {
            System.out.println(player.getTotal());
        }
    }
    
    static public void resetHeld() {
        Arrays.fill(diceHeld, false);
    }
    
    static public void rollDice() {
        //go through each dice  
        for (int i = 0; i < NUMBER_OF_DICE; i++) {
            //if the dice isn't being held back, reroll it
            if (!diceHeld[i]) {
                diceValues[i] = rand.nextInt(6) + 1;
            }
        }
    }

    static public String calculateChoices(Player player) {
        String output = "";

        //Upper Section
        output += getNumber(1, "Ones", 0, player);
        output += getNumber(2, "Twos", 1, player);
        output += getNumber(3, "Threes", 2, player);
        output += getNumber(4, "Fours", 3, player);
        output += getNumber(5, "Fives", 4, player);
        output += getNumber(6, "Sixes", 5, player);
        
        //Lower Section
        
        //Three of a Kind
        output += getOfAKind(3, "Three Of A Kind", 6, player);
        
        //Four Of A Kind
        output += getOfAKind(4, "Four Of A Kind", 7, player);
        
        //Full House
        output += getFullHouse(player);
        
        //Small Straight
        output += getSmallStraight(player);
        
        //Large Straight
        output += getLargeStraight(player);
        
        //Yahtzee!!
        output += getOfAKind(5, "Yahtzee", 11, player);
        
        //Chance
        output += getChance(player);
        
        return output;
    }
    
    static public String getNumber(int value, String name, int index, Player player) {
        int amount = 0;
        
        if (player.getValue(name) == -1) {
            for (int dice : diceValues) {
                if (dice == value) {
                    amount += value;
                }
            }        
            currentPossibleValues[index] = amount;
            return " " + name + ": " + amount;
            
        }
        return "";
    }
    
    static public String getOfAKind(int max, String name, int index, Player player) {
        int amount = 0;
        int one = 0;
        int two = 0;
        int three = 0;
        int four = 0;
        int five = 0;
        int six = 0;
        
        if (player.getValue(name) == -1) {
            for (int dice : diceValues) {
                if (dice == 1) {
                    one++;
                }
                if (dice == 2) {
                    two++;
                }
                if (dice == 3) {
                    three++;
                }
                if (dice == 4) {
                    four++;
                }
                if (dice == 5) {
                    five++;
                }
                if (dice == 6) {
                    six++;
                }
            }

            if (one >= max || two >= max || three >= max || four >= max || five >= max || six >= max) {
                for (int dice : diceValues) {
                    amount += dice;
                }
            }
            currentPossibleValues[index] = amount;
            return "\n" + name + ": " + amount;
        }
    return "";
    }
            
    public static String getFullHouse(Player player) {
        int one = 0;
        int oneAmount = 0;
        int two = 0;
        int twoAmount = 0;
        if (player.getValue("Full House") == -1) {
    
            one = diceValues[0];
            
            for (int dice : diceValues) {
                if (dice == one) {
                    oneAmount++;
                } else {
                    if (two == 0) {
                        two = dice;
                    }
                }
                if (dice == two) {
                    twoAmount++;
                }
            }
            
            //in this context, a full house is anytime you have 2 of one value, and number of dice - 2 of another.
            if (oneAmount == NUMBER_OF_DICE - 2 && twoAmount == 2 || oneAmount == 2 && twoAmount == NUMBER_OF_DICE - 2) {
                currentPossibleValues[8] = 25;
                return "\nFull House: 25";
            } else {
                currentPossibleValues[8] = 0;
                return "\nFull House: 0";
            } 
        }

        return "";
    }
    
    public static String getSmallStraight(Player player) {
        
        boolean one = false;
        boolean two = false;
        boolean three = false;
        boolean four = false;
        boolean five = false;
        boolean six = false;
        
        if (player.getValue("Small Straight") == -1) {
            
            for (int dice : diceValues) {
                if (dice == 1) {
                    one = true;
                }
                if (dice == 2) {
                    two = true;
                }
                if (dice == 3) {
                    three = true;
                }
                if (dice == 4) {
                    four = true;
                }
                if (dice == 5) {
                    five = true;
                }
                if (dice == 6) {
                    six = true;
                }
            }
            
            if (one && two && three && four || two && three && four && five || three && four && five && six) {
                currentPossibleValues[9] = 30;
                return ("\nSmall Straight: 30");
            } else {
                currentPossibleValues[9] = 0;
                return ("\nSmall Straight: 0");
            }
        }
        return "";
    }
    
    public static String getLargeStraight(Player player){
        boolean one = false;
        boolean two = false;
        boolean three = false;
        boolean four = false;
        boolean five = false;
        boolean six = false;
        
        if (player.getValue("Large Straight") == -1) {
            
            for (int dice : diceValues) {
                if (dice == 1) {
                    one = true;
                }
                if (dice == 2) {
                    two = true;
                }
                if (dice == 3) {
                    three = true;
                }
                if (dice == 4) {
                    four = true;
                }
                if (dice == 5) {
                    five = true;
                }
                if (dice == 6) {
                    six = true;
                }
            }
            
            if (one && two && three && four && five || two && three && four && five && six) {
                currentPossibleValues[9] = 40;
                return ("\nLarge Straight: 40");
            } else {
                currentPossibleValues[9] = 0;
                return ("\nLarge Straight: 0");
            }
        }
        return "";
    }
    
    public static String getChance(Player player) {
        int amount = 0;
        if (player.getValue("Chance") == -1) {
            for (int dice : diceValues) {
                amount += dice;
            }
            currentPossibleValues[12] = amount;
            return ("\nChance: " + amount);
        }
        return "";
    }
 }



