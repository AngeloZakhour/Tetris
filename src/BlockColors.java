import java.awt.*;

public class BlockColors {

    private static final Color[] colors = new Color[7];

    static {
        colors[0] = new Color(17, 190, 241); //I
        colors[1] = new Color(183, 184, 28); //O
        colors[2] = new Color(0, 107, 182); //J
        colors[3] = new Color(213, 117, 7); //L
        colors[4] = new Color(119, 185, 10); //S
        colors[5] = new Color(181, 10, 19); //Z
        colors[6] = new Color(169, 15, 201); //T
    }

    public static Color getIColor(){return colors[0];}

    public static Color getOColor(){return colors[1];}

    public static Color getJColor(){return colors[2];}

    public static Color getLColor(){return colors[3];}

    public static Color getSColor(){return colors[4];}

    public static Color getZColor(){return colors[5];}

    public static Color getTColor(){return colors[6];}

    public static Color getColor(int i){
        return colors[i-1];
    }




}
