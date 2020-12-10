package sounds;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.util.HashMap;

public class SoundManager {
    private final static HashMap<SoundType, String> sounds = new HashMap<>();
    private static float volume = 0.1f;

    static{
        sounds.put(SoundType.SOUNDTRACK, "src/sounds/tracks/tetris-soundtrack.wav");
        sounds.put(SoundType.MOVE, "src/sounds/tracks/moveBlock.wav");
        sounds.put(SoundType.ROTATE, "src/sounds/tracks/rotateBlock.wav");
        sounds.put(SoundType.DROP, "src/sounds/tracks/dropBlock.wav");
    }

    public static void playSound(SoundType type, boolean loop){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(sounds.get(type)).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            if(type == SoundType.SOUNDTRACK) gainControl.setValue(20f * (float) Math.log10(volume/3));
            else gainControl.setValue(20f * (float) Math.log10(volume));

            if(loop){
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }

            clip.start();
        }catch(Exception e){
            System.out.println("Problem with playing the sound: "+type.toString());
            e.printStackTrace();
        }
    }

    public static void playSound(SoundType  type){
        playSound(type, false);
    }

    public void setVolume(float vol) {
        volume = vol;
    }

    public float getVolume(){
        return volume;
    }
}

