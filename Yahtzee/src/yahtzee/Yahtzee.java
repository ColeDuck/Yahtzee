package yahtzee;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Yahtzee {
    
    static Random rand = new Random();
    
    public static boolean rolling = true;
    
    static int rollsLeft = 3;
    
    static final int NUMBER_OF_DICE = 5;
    
    static int currentPlayer;
    
    static ArrayList<Player> players = new ArrayList<>();

    static int[] diceValues = new int[NUMBER_OF_DICE];

    static final String[] ENTREES = {"Ones", "Twos","Threes", "Fours", "Fives", "Sixes", "Three Of A Kind", "Four Of A Kind", "Full House",
                               "Small Straight", "Large Straight", "Yahtzee", "Chance"};
    
    static int[] currentPossibleValues = new int[ENTREES.length];

    public static void initializeArrays() {
        
        //populate arrays with their default state
        for (int i = 0; i < NUMBER_OF_DICE; i++) {
            diceValues[i] = 1;
        }
        
        //populate array with its default state
        for (int i = 0; i < ENTREES.length; i++) {
            currentPossibleValues[i] = 0;
        }
    }
        
    static public void gameManager(String function, Player player) {
        
        if (function.equals("Start")) {
            currentPlayer = 0;
            players.get(currentPlayer).enableButtons();
            YahtzeeGUI.updateVisuals();
            System.out.println(players);
            return;
        }
        
        if (function.equals("Roll")) {
            
            if (rollsLeft <= 0) {
                return;
            }
            
            System.out.println("hello");
            rollsLeft--;
            toggleButtons("Roll");
            rollDice();
            calculateChoices(players.get(currentPlayer));
            players.get(currentPlayer).displayValues();
            YahtzeeGUI.updateVisuals();
        } else {
            for (int i = 0; i < ENTREES.length; i++) {
                System.out.println(function + " " + ENTREES[i]);
                if (function.equals(ENTREES[i])) {
                    System.out.println("HELLOOOOOOO");
                    player.setValue(ENTREES[i], currentPossibleValues[i]);
                    Arrays.fill(currentPossibleValues, 0);
                    player.displayValues();
                    player.disableButtons();
                    switchTurn();
                    players.get(currentPlayer).enableButtons();
                    return;
                }
            }
        }
    }
    
    static public void switchTurn() {
        int startingPlayer = currentPlayer;
        
        nextPlayer();
        
        //go through each player and check if they have a full card
        //if they don't, then it is their turn.
        //if all players have a full card, then the game is over
        do {
            if (players.get(currentPlayer).fullCard()) {
                System.out.println(players.get(currentPlayer).fullCard());
                nextPlayer();
            } else {
                rollsLeft = 3;
                Arrays.fill(diceValues, 0);
                Arrays.fill(YahtzeeGUI.diceHeld, true);
                YahtzeeGUI.updateVisuals();
                return;
            }     
        } while (currentPlayer != startingPlayer);
        
        //add game over code here
        return;
    }
    
    static public void nextPlayer() {
        if (currentPlayer == players.size() - 1) {
            currentPlayer = 0;
        } else {
            currentPlayer++;
        }
    }
    
    static public void toggleButtons(String function) {
        if (function.equals("Roll")) {
            rolling = true;
            //currentPlayer.disableButtons();
        } else if (function.equals("Finished Rolling")) {
            rolling = false;
            //currentPlayer.enableButtons();
        }
    }
    
    static public void resetHeld() {
        Arrays.fill(YahtzeeGUI.diceHeld, false);
    }
    
    static public void rollDice() {
        //go through each dice  
        for (int i = 0; i < NUMBER_OF_DICE; i++) {
            //if the dice isn't being held back, reroll it
            if (!YahtzeeGUI.diceHeld[i]) {
                diceValues[i] = rand.nextInt(6) + 1;
            }
        }
        
        YahtzeeGUI.animateDice();
    }

    static public String calculateChoices(Player player) {
        String output = "";
        
        int[] unsortedArray = diceValues.clone();
        
        //Sorting the dice makes it easier to test for "of a kinds" (3,4,yahtzee), full house, and the straights
        int[] sortedDice = bubbleSort(unsortedArray, false);
        
        //Upper Section
        output += getNumber(1, "Ones", 0, player, sortedDice);
        output += getNumber(2, "Twos", 1, player, sortedDice);
        output += getNumber(3, "Threes", 2, player, sortedDice);
        output += getNumber(4, "Fours", 3, player, sortedDice);
        output += getNumber(5, "Fives", 4, player, sortedDice);
        output += getNumber(6, "Sixes", 5, player, sortedDice);
        
        //Lower Section
        //Three of a Kind
        output += getOfAKind(3, "Three Of A Kind", 6, player, sortedDice);
        
        //Four Of A Kind
        output += getOfAKind(4, "Four Of A Kind", 7, player, sortedDice);
        
        //Full House
        output += getFullHouse(player, sortedDice);
        
        //Small Straight
        output += getStraight(player, NUMBER_OF_DICE - 1, "Small Straight", 30, sortedDice, 9);
        
        //Large Straight
        output += getStraight(player, NUMBER_OF_DICE, "Large Straight", 40, sortedDice, 10);
        
        //Yahtzee!!
        output += getOfAKind(NUMBER_OF_DICE, "Yahtzee", 11, player, sortedDice);
        
        //Chance
        output += getChance(player);
        
        return output;
    }
    
    public static int[] bubbleSort(int[] numbers, boolean ascending) {
        
        //loop over each index in the array
        for (int i = 0; i < numbers.length - 1; i++) {
            //loop over each index in the array
            //due to how this sorting algorithm works, the right most value will always be correctly sorted
            //this is because the largest / smallest value will be "carried" all the way to the right (due to it always being the smallest / largest value)
            //therefore, the upper bound can subtract i for each iteration.
            for (int j = 0; j < numbers.length - 1 - i; j++) {
                
                if (ascending) {
                    
                    //swap values if j is less than j + 1
                    if (numbers[j] < numbers[j + 1]) {
                        
                        Swap(numbers, j, j + 1);
                    }
                } else {
                    //swap values if j is greater than j + 1
                    if (numbers[j] > numbers[j + 1]) {
                        Swap(numbers, j, j + 1);
                    }
                }
            }
        }
        return numbers;
    }
    
    /*
    pre: one and two are within the boundaries of numbers. numbers.length is greater than 0
    function: swaps the values of one and two in numbers
    post: swapped numbers[one] and numbers[two]
    */
    public static void Swap(int[] numbers, int one, int two) {
        int temp = numbers[one];
        numbers[one] = numbers[two];
        numbers[two] = temp;
    }
            
    static public String getNumber(int value, String name, int index, Player player, int[] sortedDice) {
        int amount = 0;
        
        if (player.getValue(name) != -1) {
            return "";
        }
        
        //loops through dice, adding any dice to the total that equal "value"
        for (int dice : sortedDice) {
            //since the dices are sorted, a dice being a higher value than
            //our value means that it must have already gone past, meaning
            //that breaking from the loop is possible.
            if (dice > value) {
                break;
            }
            
            if (dice == value) {
                amount += value;
            }
        } 
        
        currentPossibleValues[index] = amount;
        return name + ": " + amount + " ";
            
        
    }
    
    static public String getOfAKind(int max, String name, int index, Player player, int[] sortedDice) {
        int amount = 0;
        int currentTracker = 0;
        int currentAmount = 0;
        int highest = 0;
        
        if (player.getValue(name) != -1) {
            return "";
        }
        
        //finds the length of the largest chain of same value dice
        for (int dice : sortedDice) {
            
            if (dice == currentTracker) {
                currentAmount++;
                continue;
            }
                    
            if (highest < currentAmount) {
                 highest = currentAmount; 
            }
            
            currentAmount = 1;
            currentTracker = dice;
        }
        
        if (highest < currentAmount) {
            highest = currentAmount; 
        }

        //if highest is greater than max, there must be a max of a kind!
        if (highest >= max) {
            
            //having all dice the same is a yahtzee, aka max == number_of_dice. Otherwise, sum the dice
            if (max == NUMBER_OF_DICE) {
                amount = 50;
            } else {
                amount = sumDice();
            }
        }
            
        currentPossibleValues[index] = amount;
        return "\n" + name + ": " + amount;
    }
     
    public static String getFullHouse(Player player, int[] sortedDice) {
        int one;
        int oneAmount = 1;
        int two;
        int twoAmount = 1;
        int i = 0;
        
        //if player already has full house in their card, ignore
        if (player.getValue("Full House") != -1) {
            return "";
        }
        
        //find the length of the first chain
        one = sortedDice[i];
        i++;
        while (i < NUMBER_OF_DICE && sortedDice[i] == one) {
            oneAmount++;
            i++;
        }
       
        //find the length of the second chain
        two = sortedDice[i];
        i++;
        while (i < NUMBER_OF_DICE && sortedDice[i] == two) {
            twoAmount++;
            i++;
        }
    
        //with five dice, the first chain must be 2/3 and the second chain must be 3/2 in order for it to be a full house
        if (oneAmount == 2 && twoAmount == NUMBER_OF_DICE - 2 || twoAmount == 2 && oneAmount == NUMBER_OF_DICE - 2) {
            currentPossibleValues[8] = 25;
            return("\nFull House: 25");
        }
        
        //if not a full house, return 0
        currentPossibleValues[8] = 0;
        return("\nFull House: 0");
    }
    
    public static String getStraight(Player player, int length, String name, int value, int[] sortedDice, int index) {
         
        int highest = 0;
        int currentAmount = 1;
        
        //if player already has this on their card, ignore
        if (player.getValue(name) != -1) {
            return "";
        }
        
        //this finds the length of the largest chain of dice where dice(i + 1) = dice(i) + 1
        //which basically finds the length of the largest straight present in the dice
        int i = 0;
        while (i < NUMBER_OF_DICE - 1) {
            if (sortedDice[i + 1] == sortedDice[i] + 1) {
                currentAmount++;
                i++;
                continue;
            }
            
            if (highest < currentAmount) {
                highest = currentAmount;
            }
            
            i++;
            currentAmount = 1;
        }
        
        if (highest < currentAmount) {
            highest = currentAmount;
        }
        
        //if the largest straight is greater or equal to the length required, return points
        if (highest >= length) {
            currentPossibleValues[index] = value;
            return("\n" + name + ": " + value);
        }
        
        //otherwise, return 0
        currentPossibleValues[index] = 0;
        return("\n" + name + ": 0");
    }
    
    public static String getChance(Player player) {
        
        if (player.getValue("Chance") != -1) {
            return "";
        }
        
        return("\nChance: " + sumDice());
    }
    
    public static int sumDice() {
        int sum = 0;
        
        for (int dice : diceValues) {
            sum += dice;
        }
        
        return sum;
    }
 }



