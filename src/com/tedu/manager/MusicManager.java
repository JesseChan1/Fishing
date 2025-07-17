package com.fish.manager;

import java.io.File;

import javax.sound.sampled.*;

import com.fish.model.GamingInfo;

/**
 * ��������
 */
public class MusicManager {
private static MusicManager manager;
private AudioInputStream audioInputStream;

private AudioFormat audioFormat;

private SourceDataLine sourceDataLine;

private PlayThread currentPlayThread;

public static MusicManager getMusicManager() {
    if (manager == null) {
        manager = new MusicManager();
    }
    return manager;
}

private MusicManager() {

}

public void playMusicByR(String resId, boolean isLoop) {
    try {
        // 停止当前播放的音乐
        stopCurrentMusic();
        
        File file = new File("bgm" + File.separator + resId);
        System.out.println(file);
        currentPlayThread = new PlayThread(file, isLoop);
        Thread playThread = new Thread(currentPlayThread);
        playThread.start();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

/**
 * 停止当前播放的音乐
 */
private void stopCurrentMusic() {
    if (currentPlayThread != null) {
        currentPlayThread.stopPlaying();
        currentPlayThread = null;
    }
}

public static void release() {
    if (manager != null) {
        manager.stopCurrentMusic();
        manager = null;
    }
}


class PlayThread extends Thread {
    public boolean isLoop;
    File musicFile;
    byte tempBuffer[] = new byte[1024];
    private volatile boolean shouldStop = false;

    public PlayThread(File musicFile, boolean isLoop) {
        this.musicFile = musicFile;
        this.isLoop = isLoop;
    }
    
    public void stopPlaying() {
        shouldStop = true;
    }

    public void run() {
        try {
            int cnt;
            do {
                System.out.println(musicFile);
                audioInputStream = AudioSystem.getAudioInputStream(musicFile);

                audioFormat = audioInputStream.getFormat();
                if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                    audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                            audioFormat.getSampleRate(), 16,
                            audioFormat.getChannels(),
                            audioFormat.getChannels() * 2,
                            audioFormat.getSampleRate(),
                            false);
                    audioInputStream = AudioSystem.getAudioInputStream(audioFormat, audioInputStream);
                }
                DataLine.Info dataLineInfo = new DataLine.Info(
                        SourceDataLine.class, audioFormat,
                        AudioSystem.NOT_SPECIFIED);
                sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                sourceDataLine.open();
                sourceDataLine.start();
                while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                    if (cnt > 0) {
                        sourceDataLine.write(tempBuffer, 0, cnt);
                    }
                }

                sourceDataLine.drain();
                sourceDataLine.close();
            } while (isLoop && GamingInfo.getGamingInfo().isGaming() && !shouldStop);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
}
