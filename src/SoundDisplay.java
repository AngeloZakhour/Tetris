import javax.swing.*;
import java.awt.*;

public class SoundDisplay {

    private static final ImageIcon soundOn = new ImageIcon("assets/images/volume.png");
    private static final ImageIcon soundMute = new ImageIcon("assets/images/mute.png");
    private static boolean mute = false;

    //Positioning different elements
    // -Icon
    private static int soundIconXPos;
    private static int soundIconYPos;
    private static int soundIconSize; //its a square
    // -Bar
    private static int soundBarXPos;
    private static int soundBarYPos;
    private static int soundBarWidth;
    private static int soundBarHeight;

    static {
        setPositions(0, 0, 150, 0);
    }

    public static void draw(JPanel caller, Graphics2D g){
        if(mute) soundMute.paintIcon(caller, g, soundIconXPos, soundIconYPos);
        else soundOn.paintIcon(caller, g, soundIconXPos, soundIconYPos);

        g.setColor(Color.GRAY);
        g.fillRect(soundBarXPos, soundBarYPos, soundBarWidth, soundBarHeight);

        g.setColor(Color.GREEN);
        g.fillRect(soundBarXPos, soundBarYPos, getVolumeBarSize(), soundBarHeight);
    }

    public static boolean mouseClicked(Point point){
        boolean redraw = false;
        double x = point.getX();
        double y = point.getY();

        boolean onButtonY = y >= soundIconYPos && y <= soundIconYPos+soundIconSize;
        boolean onButtonX = x >= soundIconXPos && x <= soundIconXPos+soundIconSize;
        boolean onBarX = x >= soundBarXPos && x <= soundBarXPos + soundBarWidth;

        if(onButtonY && onButtonX) {
            mute = !mute;
            SoundManager.mute();
            redraw = true;
        }
        else if(onButtonY && onBarX){
            double xRel = x - soundBarXPos;
            double percentVolume = (xRel/soundBarWidth) * 100;
            SoundManager.setVolumePercent((float)percentVolume);
            redraw = true;
        }

        return redraw;
    }

   public static void setPositions(int x, int y, int width, int xPadding){

       // -Icon
       soundIconXPos = x+xPadding;
       soundIconYPos = y;
       soundIconSize = soundOn.getIconWidth(); //its a square
       // -Bar
       soundBarXPos = soundIconXPos + soundIconSize + 10;
       soundBarYPos = soundIconYPos;
       soundBarWidth = width - (2*xPadding) - (soundBarXPos-soundIconXPos);
       soundBarHeight = soundIconSize;

   }

    private static int getVolumeBarSize(){
        return (int)(soundBarWidth*(SoundManager.getVolume()/SoundManager.getMaxVolume()));
    }

}
