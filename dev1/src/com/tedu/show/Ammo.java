package com.fish.model;

import com.fish.canvas.Bitmap;

/**
 * �ӵ�
 */
public class Ammo extends DrawableAdapter {
private int ammoQuality;//�ӵ�Ʒ��
private FishingNet net;//��ǰ�ӵ���Ӧ������
private Bitmap[] pic;
//��ǰͼƬ������
private int currentId;

public Ammo(int ammoQuality) {
    this.ammoQuality = ammoQuality;
}

public void setCurrentPic(Bitmap[] pic, FishingNet net) {
    this.net = net;
    this.pic = pic;
}

public Bitmap getCurrentPic() {
    // TODO Auto-generated method stub
    return pic[currentId];
}

public FishingNet getNet() {
    return net;
}

public int getAmmoPicLenght() {
    return pic.length;
}

/**
 * ���ñ�ʾ��ǰͼƬ������ֵ,���ֵ��ͼƬ���������ֵ
 *
 * @param currentId
 */
public void setCurrentId(int currentId) {
    this.currentId = currentId;
}

public int getPicWidth() {
    // TODO Auto-generated method stub
    return pic[currentId].getWidth();
}

public int getPicHeight() {
    // TODO Auto-generated method stub
    return pic[currentId].getHeight();
}

public int getAmmoQuality() {
    return ammoQuality;
}

}
