import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SoundDisplay {

    private static final ImageIcon soundOn = new ImageIcon("src/images/volume.png");
    private static final ImageIcon soundMute = new ImageIcon("src/images/mute.png");
    private static boolean mute = false;

    //Positioning different elements
    // -Icon
    private static final int soundIconXPos = 10;
    private static final int soundIconYPos = Main.frameHeight-110;
    private static final int soundIconSize = soundOn.getIconWidth(); //its a square
    // -Bar
    private static final int soundBarXPos = soundIconXPos + soundIconSize + 10;
    private static final int soundBarYPos = soundIconYPos;
    private static final int soundBarWidth = 135;
    private static final int soundBarHeight = soundIconSize;

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
        System.out.println(x + " / " + y);

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

    private static int getVolumeBarSize(){
        return (int)(soundBarWidth*(SoundManager.getVolume()/SoundManager.getMaxVolume()));
    }

}
