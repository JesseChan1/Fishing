package com.fish.manager;

/**
 * ����ÿһ�ص���ϸ��Ϣ
 *
 * @author Xiloerfan
 */
public class GamePartInfo {
//�ؿ�����
private String partName;
//���ֵ��������
private String[] fishName;
//��ĳ��ּ���
private int[] showProbability;
//�ɳ��ֵ���Ⱥ����
private int shoalSumInScreen;
//��һ�ص�����
private String nextPart;
//��������
private String bgMusic;
//����ͼƬ
private String background;
//��ǰ�ؿ�ʱ��(����Ϊ��λ)
private int partTime;
//��ǰ�ؿ��Ŀ��ּ���
private int targetScore;

public String[] getFishName() {
    return fishName;
}

public void setFishName(String[] fishName) {
    this.fishName = fishName;
}

public String getNextPart() {
    return nextPart;
}

public void setNextPart(String nextPart) {
    this.nextPart = nextPart;
}

public int getPartTime() {
    return partTime;
}

public void setPartTime(int partTime) {
    this.partTime = partTime;
}

public String getPartName() {
    return partName;
}

public void setPartName(String partName) {
    this.partName = partName;
}

public String getBgMusic() {
    return bgMusic;
}

public void setBgMusic(String bgMusic) {
    this.bgMusic = bgMusic;
}

public String getBackground() {
    return background;
}

public void setBackground(String background) {
    this.background = background;
}

public int getShoalSumInScreen() {
    return shoalSumInScreen;
}

public void setShoalSumInScreen(int shoalSumInScreen) {
    this.shoalSumInScreen = shoalSumInScreen;
}

public int[] getShowProbability() {
    return showProbability;
}

public void setShowProbability(int[] showProbability) {
    this.showProbability = showProbability;
}

public int getTargetScore() {
    return targetScore;
}

public void setTargetScore(int targetScore) {
    this.targetScore = targetScore;
}


}
