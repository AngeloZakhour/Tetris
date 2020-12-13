import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.util.HashMap;

public class SoundManager {
    private final static HashMap<SoundType, String> sounds = new HashMap<>();
    private static float volume = 0.1f;
    private static long clipTime;
    private static Clip soundtrackClip;
    private static boolean mute = false;

    private static final float MAX_VOLUME = 0.2f;
    private static final float MIN_VOLUME = 0.0f;
    private static final float MIN_SOUND_VALUE = -80.0f;
    private static final float SOUND_INCREMENT = 0.01f;

    static{
        sounds.put(SoundType.SOUNDTRACK, "src/sounds/tetris-soundtrack.wav");
        sounds.put(SoundType.MOVE, "src/sounds/moveBlock.wav");
        sounds.put(SoundType.ROTATE, "src/sounds/rotateBlock.wav");
        sounds.put(SoundType.DROP, "src/sounds/dropBlock.wav");
    }

    public static void playSound(SoundType type){
        if(mute) return;

        try{
            AudioInputStream audioInputStream =
                    AudioSystem.getAudioInputStream(new File(sounds.get(type)).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            float soundValue = 20f * (float) Math.log10(volume);

            gainControl.setValue(Math.max(soundValue, MIN_SOUND_VALUE));


            clip.start();
        }catch(Exception e){
            System.out.println("Problem with playing the sound: "+type.toString());
            e.printStackTrace();
        }
    }

    public static void playMusic(){
        try{
            if(soundtrackClip != null){
                soundtrackClip.stop();
            }
            SoundType type = SoundType.SOUNDTRACK;
            AudioInputStream audioInputStream =
                    AudioSystem.getAudioInputStream(new File(sounds.get(type)).getAbsoluteFile());
            soundtrackClip = AudioSystem.getClip();
            soundtrackClip.open(audioInputStream);

            updateMusicVolume();

            soundtrackClip.loop(Clip.LOOP_CONTINUOUSLY);

            soundtrackClip.start();
        }catch(Exception e){
            System.out.println("Problem with playing the sound: SoundTrack");
            e.printStackTrace();
        }
    }

    public static void setVolume(float vol) {
        volume = vol;
        if(!mute) updateMusicVolume();
    }

    public static void setVolumePercent(float percent){
        volume = MAX_VOLUME * (percent/100);

        if(!mute) updateMusicVolume();
    }

    public static float getVolume(){
        return volume;
    }

    public static float getMaxVolume(){
        return MAX_VOLUME;
    }

    public static void decreaseVolume(){
        if(volume <= MIN_VOLUME) return;

        volume -= SOUND_INCREMENT;

        if(!mute) updateMusicVolume();
    }

    public static void increaseVolume(){
        if(volume >= MAX_VOLUME) return;

        volume += SOUND_INCREMENT;

        if(!mute) updateMusicVolume();
    }

    public static void pauseMusic(){
        clipTime = soundtrackClip.getMicrosecondPosition();
        soundtrackClip.stop();
    }

    public static void resumeMusic(){
        soundtrackClip.setMicrosecondPosition(clipTime);
        soundtrackClip.loop(Clip.LOOP_CONTINUOUSLY);
        soundtrackClip.start();
    }

    private static void updateMusicVolume(){
        FloatControl gainControl = (FloatControl) soundtrackClip.getControl(FloatControl.Type.MASTER_GAIN);

        float soundValue = 20f * (float) Math.log10(volume/3);

        gainControl.setValue(Math.max(soundValue, MIN_SOUND_VALUE));
    }

    public static void mute(){
        mute = !mute;

        FloatControl gainControl = (FloatControl) soundtrackClip.getControl(FloatControl.Type.MASTER_GAIN);

        if(mute){
            gainControl.setValue(-80.0f);
        }
        else{
            float soundValue = 20f * (float) Math.log10(volume/3);
            gainControl.setValue(Math.max(soundValue, MIN_SOUND_VALUE));
        }
    }
}

