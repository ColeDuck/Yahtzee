package yahtzee;


import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Yahtzee {
    
    static Scanner scan = new Scanner(System.in);
        
    static Random rand = new Random();
        
    static boolean gameOn = true;
    static boolean diceOneHeld = false;
    static boolean diceTwoHeld = false;
    static boolean diceThreeHeld = false;
    static boolean diceFourHeld = false;
    static boolean diceFiveHeld = false;
    static String input;
    static String output;
    static boolean hasRolled = false;
        
    // order is 0: Ones, 1: Twos, 2: Threes, 3: Fours, 4: Fives, 5: Sixes, 6: Three Of A Kind, 7: Four Of A Kind, 8: Full House, 9: Small Straight, 10: Large Straight, 11: Yahtzee, 12: Chance
    static int[] userScores = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
    static int[] currentScores = {0,0,0,0,0,0,0,0,0,0,0,0,0};
    static int[] diceValues = {0,0,0,0,0};

    public static void main(String[] args) {
        System.out.println("Welcome to Yahtzee!");

        while (gameOn) {
            
            int rollsLeft = 3;
                  
            System.out.println("New Round. Type \"Roll\" to roll!");
            input = scan.nextLine();
            
            if (input.equals("Roll")) {
                rollDice();
            } else {
                System.out.println("invalid!");
                continue;
            }
            
            
            rollsLeft--;
            
            diceOneHeld = false;
            diceTwoHeld = false;
            diceThreeHeld = false;
            diceFourHeld = false;
            diceFiveHeld = false;
            boolean picked = false;
            
            while (!picked) {
                
                output = calculateChoices();
                System.out.println("Dices:" + Arrays.toString(diceValues));
                System.out.println(output);
                
                System.out.println("Please Enter your choice. Either enter \"Roll\" to roll. Enter numbers 1 2 3 etc to hold a dice, or select points to win. You have " + rollsLeft + " rolls left");
                
                input = scan.nextLine();
                
                if (input.contains(String.valueOf(1))) {
                    diceOneHeld = true;
                }
                if (input.contains(String.valueOf(2))) {
                    diceTwoHeld = true;
                }
                if (input.contains(String.valueOf(3))) {
                    diceThreeHeld = true;
                }
                if (input.contains(String.valueOf(4))) {
                    System.out.println("4!");
                    diceFourHeld = true;
                }
                if (input.contains(String.valueOf(5))) {
                    diceFiveHeld = true;
                    
                }
                
                if (diceOneHeld || diceTwoHeld || diceThreeHeld || diceFourHeld || diceFiveHeld) {
                    
                    if (rollsLeft > 0) {
                        rollDice();
                    
                        diceOneHeld = false; 
                        diceTwoHeld = false; 
                        diceThreeHeld = false; 
                        diceFourHeld = false; 
                        diceFiveHeld = false;
                        rollsLeft--;
                        continue;
                    } else {
                        System.out.println("You don't have any rolls left this turn.");
                    }
                }
                
                switch (input) {
                    case "Roll":  
                        if (rollsLeft > 0) {
                            rollDice();
                    
                            diceOneHeld = false; 
                            diceTwoHeld = false; 
                            diceThreeHeld = false; 
                            diceFourHeld = false; 
                            diceFiveHeld = false;
                            rollsLeft--;
                            break;
                        } else {
                            System.out.println("You don't have any rolls left this turn.");
                        }
                        
                    case "Ones": addValues(0); rollsLeft = 0; picked = true; break;
                    case "Twos": addValues(1); rollsLeft = 0; picked = true; break;
                    case "Threes": addValues(2); rollsLeft = 0; picked = true; break;
                    case "Fours": addValues(3); rollsLeft = 0; picked = true; break;
                    case "Fives": addValues(4); rollsLeft = 0; picked = true; break;
                    case "Sixes": addValues(5); rollsLeft = 0; picked = true; break;
                    case "Three Of A Kind": addValues(6); rollsLeft = 0; picked = true; break;
                    case "Four Of A Kind": addValues(7); rollsLeft = 0; picked = true; break;
                    case "Full House": addValues(8); rollsLeft = 0; picked = true; break;
                    case "Small Straight": addValues(9); rollsLeft = 0; picked = true; break;
                    case "Large Straight": addValues(10); rollsLeft = 0; picked = true; break;
                    case "Yahtzee": addValues(11); rollsLeft = 0; picked = true; break;
                    case "Chance": addValues(12); rollsLeft = 0; picked = true; break;
                    default: break;
                    }
                }
            
            System.out.println(Arrays.toString(userScores));
            }  
        }
    
    static public void addValues(int index) {
        if (userScores[index] == -1) {
            userScores[index] = currentScores[index];
        } else {
            System.out.println("You already have this.");
        }
    }
    
    static public void rollDice() {
        if (!diceOneHeld) {
            diceValues[0] = rand.nextInt(6) + 1;
        }
        if (!diceTwoHeld) {
            diceValues[1] = rand.nextInt(6) + 1;
        }
        if (!diceThreeHeld) {
            diceValues[2] = rand.nextInt(6) + 1;
        }
        if (!diceFourHeld) {
            diceValues[3] = rand.nextInt(6) + 1;
        }
        if (!diceFiveHeld) {
            diceValues[4] = rand.nextInt(6) + 1;
        }

    }

    static public String calculateChoices() {
        String output = "";

        //Upper Section
        output += getNumber(1, "Ones", 0);
        output += getNumber(2, "Twos", 1);
        output += getNumber(3, "Threes", 2);
        output += getNumber(4, "Fours", 3);
        output += getNumber(5, "Fives", 4);
        output += getNumber(6, "Sixes", 5);
        
        //Lower Section
        
        //Three of a Kind
        output += getOfAKind(3, "Three Of A Kind", 6);
        
        //Four Of A Kind
        output += getOfAKind(4, "Four Of A Kind", 7);
        
        //Full House
        output += getFullHouse();
        
        //Small Straight
        output += getSmallStraight();
        
        //Large Straight
        output += getLargeStraight();
        
        //Yahtzee!!
        output += getOfAKind(5, "Yahtzee!", 11);
        
        //Chance
        output += getChance();
        
        return output;
    }
    
    static public String getNumber(int value, String name, int index) {
        int amount = 0;
        
        if (userScores[index] == -1) {
            for (int dice : diceValues) {
                if (dice == value) {
                    amount += value;
                }
            }        
            currentScores[index] = amount;
            return " " + name + ": " + amount;
            
        }
        return "";
    }
    
    static public String getOfAKind(int max, String name, int index) {
        int amount = 0;
        int one = 0;
        int two = 0;
        int three = 0;
        int four = 0;
        int five = 0;
        int six = 0;
        
        if (userScores[index] == -1) {
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
            currentScores[index] = amount;
            return "\n" + name + ": " + amount;
        }
    return "";
    }
            
    public static String getFullHouse() {
        int one = 0;
        int oneAmount = 0;
        int two = 0;
        int twoAmount = 0;
        if (userScores[8] == -1) {
    
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
            
            if (oneAmount == 2 && twoAmount == 3 || oneAmount == 3 && twoAmount == 2) {
                currentScores[8] = 25;
                return "\nFull House: 25";
            } else {
                currentScores[8] = 0;
                return "\nFull House: 0";
            } 
        }

        return "";
    }
    
    public static String getSmallStraight() {
        
        boolean one = false;
        boolean two = false;
        boolean three = false;
        boolean four = false;
        boolean five = false;
        boolean six = false;
        
        if (userScores[9] == -1) {
            
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
                currentScores[9] = 30;
                return ("\nSmall Straight: 30");
            } else {
                currentScores[9] = 0;
                return ("\nSmall Straight: 0");
            }
        }
        return "";
    }
    
    public static String getLargeStraight(){
        boolean one = false;
        boolean two = false;
        boolean three = false;
        boolean four = false;
        boolean five = false;
        boolean six = false;
        
        if (userScores[10] == -1) {
            
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
                currentScores[10] = 40;
                return ("\nLarge Straight: 40");
            } else {
                currentScores[10] = 0;
                return ("\nLarge Straight: 0");
            }
        }
        return "";
    }
    
    public static String getChance() {
        int amount = 0;
        if (userScores[12] == -1) {
            for (int dice : diceValues) {
                amount += dice;
            }
            return ("\nChance: " + amount);
        }
        return "";
    }
 }



