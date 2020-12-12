import java.awt.*;
import java.util.HashMap;
import java.util.Set;

public class BlockColors {

    private static HashMap<Character, Color> colors = new HashMap<>();
    private static final int alpha = 100;

    static {
        colors.put('I', new Color(17, 190, 241));
        colors.put('O', new Color(183, 184, 28));
        colors.put('J', new Color(0, 107, 182));
        colors.put('L', new Color(213, 117, 7));
        colors.put('S', new Color(119, 185, 10));
        colors.put('Z', new Color(181, 10, 19));
        colors.put('T', new Color(169, 15, 201));

        Object[] keys = colors.keySet().toArray();

        for(Object key : keys){
            char newKey = key.toString().toLowerCase().toCharArray()[0];
            int red = colors.get(key).getRed();
            int green = colors.get(key).getGreen();
            int blue = colors.get(key).getBlue();

            colors.put(newKey, new Color(red, green, blue, alpha));
        }
    }

    public static Color getColor(char i){
        return colors.get(i);
    }






}
