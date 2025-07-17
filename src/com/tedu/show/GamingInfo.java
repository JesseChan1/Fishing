package com.fish.model;

import java.util.ArrayList;

import com.fish.src.MainSurface;
import com.fish.manager.ShoalManager;
import com.fish.manager.SoundManager;

//��Ϸ������һЩ��Ҫ���õı���
public class GamingInfo {
private int screenWidth;
private int screenHeight;
private static GamingInfo gameInfo; // ����ģʽ��Ҫ
private boolean isGaming; // �Ƿ�����Ϸ״̬
private boolean isPause;//�Ƿ�����ͣ״̬
private MainSurface surface; // ����Ļ
private ArrayList<Fish> fish = new ArrayList<Fish>(); // ���е���
private ShoalManager shoalManager; // ��Ⱥ������
private SoundManager soundManager;//��Ч������
private float cannonLayoutX;            //������תX����
private float cannonLayoutY;            //������תY����
private int score = 100;                //��ǰ�ķ�

public int getScore() {
    return score;
}

public void setScore(int score) {
    this.score = score;
}

/**
 * ���GamingInfoʵ��
 */
public static void clearGameInfo() {
    gameInfo = null;
}

private GamingInfo() {
}

public static GamingInfo getGamingInfo() {
    if (gameInfo == null) {
        gameInfo = new GamingInfo();
    }
    return gameInfo;
}

public boolean isGaming() {
    return isGaming;
}

public void setGaming(boolean isGaming) {
    this.isGaming = isGaming;
}

public ArrayList<Fish> getFish() {
    return fish;
}

public void setFish(ArrayList<Fish> fish) {
    this.fish = fish;
}

public MainSurface getSurface() {
    return surface;
}

public void setSurface(MainSurface surface) {
    this.surface = surface;
}

public ShoalManager getShoalManager() {
    return shoalManager;
}

public void setShoalManager(ShoalManager shoalManager) {
    this.shoalManager = shoalManager;
}

public int getScreenWidth() {
    return screenWidth;
}

public void setScreenWidth(int screenWidth) {
    this.screenWidth = screenWidth;
}

public int getScreenHeight() {
    return screenHeight;
}

public void setScreenHeight(int screenHeight) {
    this.screenHeight = screenHeight;
}

public SoundManager getSoundManager() {
    return soundManager;
}

public void setSoundManager(SoundManager soundManager) {
    this.soundManager = soundManager;
}

public float getCannonLayoutX() {
    return cannonLayoutX;
}

public void setCannonLayoutX(float cannonLayoutX) {
    this.cannonLayoutX = cannonLayoutX;
}

public float getCannonLayoutY() {
    return cannonLayoutY;
}

public void setCannonLayoutY(float cannonLayoutY) {
    this.cannonLayoutY = cannonLayoutY;
}

public boolean isPause() {
    return isPause;
}

public void setPause(boolean isPause) {
    this.isPause = isPause;
}

}
