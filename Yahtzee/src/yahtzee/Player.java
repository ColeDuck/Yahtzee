
package yahtzee;

import java.util.HashMap;
import java.util.Map;

public class Player {
    
    private Map<String, Integer> scores = new HashMap<>();
    private String Username;
            
    Player(String name) {
        Username = name;
        
        for (String entree : Yahtzee.ENTREES) {
            scores.put(entree, -1);
        }
    }
    
    public int getValue(String key) {
        return scores.get(key);
    }
    
    public String getName() {
        return Username;
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
}
