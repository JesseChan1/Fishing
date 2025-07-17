package com.fish.manager;

import java.util.HashMap;

/**
 * ͼƬ��������Ϣ��
 *
 * @author Xiloerfan
 */
public class ImageConfig {
private String srcImageFileName;//Դͼ����
private int srcImageWidth;//Դͼ���
private int srcImageHeight;//Դͼ�߶�
private HashMap<String, ActConfig> allActs = new HashMap<String, ActConfig>();//ÿһ��ͼ��ϸ����Ϣ

/**
 * ÿһ��ͼ��ϸ����Ϣ
 *
 * @author Xiloerfan
 */
public static class ActConfig {
    //����Դͼ����Ϣ
    private ImageConfig config;
    //ͼƬ����
    private String imageName;
    //ͼƬ��Դͼ��X����
    private int imageX = 0;
    //ͼƬ��Դͼ��Y����
    private int imageY = 0;
    //ͼƬ��Դͼ�Ŀ��
    private int imageWidth = 0;
    //ͼƬ��Դͼ�ĸ߶�
    private int imageHeight = 0;
    //ͼƬ��Դʼ���
    private int originalWidth = 0;
    //ͼƬ��Դʼ�߶�
    private int originalHeight = 0;
    //ƫ����X
    private float offsetX = 0;
    //ƫ����Y
    private float offsetY = 0;

    public int getImageX() {
        return imageX;
    }

    public void setImageX(int imageX) {
        this.imageX = imageX;
    }

    public int getImageY() {
        return imageY;
    }

    public void setImageY(int imageY) {
        this.imageY = imageY;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getOriginalWidth() {
        return originalWidth;
    }

    public void setOriginalWidth(int originalWidth) {
        this.originalWidth = originalWidth;
    }

    public int getOriginalHeight() {
        return originalHeight;
    }

    public void setOriginalHeight(int originalHeight) {
        this.originalHeight = originalHeight;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public ImageConfig getConfig() {
        return config;
    }

    public void setConfig(ImageConfig config) {
        this.config = config;
    }

}

public String getSrcImageFileName() {
    return srcImageFileName;
}

public void setSrcImageFileName(String srcImageFileName) {
    this.srcImageFileName = srcImageFileName;
}

public int getSrcImageWidth() {
    return srcImageWidth;
}

public void setSrcImageWidth(int srcImageWidth) {
    this.srcImageWidth = srcImageWidth;
}

public int getSrcImageHeight() {
    return srcImageHeight;
}

public void setSrcImageHeight(int srcImageHeight) {
    this.srcImageHeight = srcImageHeight;
}

public HashMap<String, ActConfig> getAllActs() {
    return allActs;
}

public void setAllActs(HashMap<String, ActConfig> allActs) {
    this.allActs = allActs;
}


}
