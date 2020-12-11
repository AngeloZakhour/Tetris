import java.awt.*;
import java.util.HashMap;

public class BlockColors {

    private static final HashMap<Character, Color> colors = new HashMap<>();
    private static final HashMap<Character, Color> shadows = new HashMap<>();
    private static final int alpha = 127;

    static {
        colors.put('I', new Color(17, 190, 241));
        colors.put('O', new Color(183, 184, 28));
        colors.put('J', new Color(0, 107, 182));
        colors.put('L', new Color(213, 117, 7));
        colors.put('S', new Color(119, 185, 10));
        colors.put('Z', new Color(181, 10, 19));
        colors.put('T', new Color(169, 15, 201));

        colors.forEach((k, v) -> shadows.put(k, new Color(v.getRed(), v.getGreen(), v.getBlue(), alpha)));
    }

    public static Color getColor(char i){
        return colors.get(i);
    }

    public static Color getShadow(char i){
        return shadows.get(i);
    }





}
