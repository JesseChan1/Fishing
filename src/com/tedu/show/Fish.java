package com.fish.model;


import com.fish.canvas.Bitmap;
import com.fish.tools.Log;
import com.fish.manager.HeadFish;
import com.fish.manager.ScoreManager;
import com.fish.threads.PicActThread;

public class Fish extends DrawableAdapter {
/**
 * ��������
 */
public static final int ROTATE_DIRECTION_LEFT = 1;    //��ת
public static final int ROTATE_DIRECTION_RIGHT = 2;    //��ת
/**
 * �����������Զ���
 */
private FishInfo fishInfo;                            //��ǰ���ϸ��������Ϣ
private Bitmap[] fishActs;                            //��ǰ������ж���
private Bitmap[] fishCatchActs;                        //��ǰ������б�������
private PicActThread picActThread;                    // ������ǰ��Ķ����߳�
/**
 * ���������Զ���
 */
private int currentPicAct = 0;                        //��ǰ��������ֵ
private int currentCatchPicAct = 0;                    //��ǰ����׽��������ֵ
private boolean isAlive = true;                        //��ǰ���Ƿ����
private float distanceHeadFishX;                    //����ͷ��Xƫ����
private float distanceHeadFishY;                    //����ͷ��Yƫ����
private HeadFish headFish;                            //��ͷ��
private boolean canRun;                        //���Ƿ�����ƶ�
private int[] fishOutlinePoint = new int[4];        //�����Ӿ��Σ�x����Сֵ�����ֵ��Y����Сֵ�����ֵ

public Fish() {

}

public Fish(Bitmap[] fishActs, Bitmap[] fishCatchActs, FishInfo fishInfo) {
    this.fishActs = fishActs;
    this.fishCatchActs = fishCatchActs;
    this.fishInfo = fishInfo;
    this.getPicMatrix().setTranslate(GamingInfo.getGamingInfo().getScreenWidth(), GamingInfo.getGamingInfo().getScreenHeight());
}

//�Ƿ��ڻ״̬������Ļ�����ţ�
public boolean isAlive() {
    // TODO Auto-generated method stub
    return isAlive;
}

//�����Ƿ��ڻ״̬
public void setAlive(boolean isAlive) {
    // TODO Auto-generated method stub
    this.isAlive = isAlive;
}

/**
 * ����ת���X����
 */
public int getFishRotatePoint_X() {
    return getCurrentPic().getWidth() / 2;
}

/**
 * ����ת���Y����
 */
public int getFishRotatePoint_Y() {
    return getCurrentPic().getHeight() / 2;
}

public PicActThread getPicActThread() {
    return picActThread;
}

public void setPicActThread(PicActThread picActThread) {
    this.picActThread = picActThread;
}


public float getDistanceHeadFishX() {
    return distanceHeadFishX;
}

public void setDistanceHeadFishX(float distanceHeadFishX) {
    this.distanceHeadFishX = distanceHeadFishX;
}

public float getDistanceHeadFishY() {
    return distanceHeadFishY;
}

public void setDistanceHeadFishY(float distanceHeadFishY) {
    this.distanceHeadFishY = distanceHeadFishY;
}

/**
 * ��ȡ���еĶ�������
 *
 * @return
 */
public int getFishActs() {
    // TODO Auto-generated method stub
    if (isAlive()) {
        return fishActs.length;
    } else {
        return fishCatchActs.length;
    }
}

/**
 * ���õ�ǰ����ͼƬ����ԴID
 *
 * @param picId
 */
public void setCurrentPicId(int picId) {
    if (isAlive()) {
        this.currentPicAct = picId;
    } else {
        this.currentCatchPicAct = picId;
    }

}

public int getCurrentPicId() {
    if (isAlive()) {
        return currentPicAct;
    } else {
        return currentCatchPicAct;
    }

}

public Bitmap getCurrentPic() {
    // TODO Auto-generated method stub
    if (isAlive()) {
        return fishActs[currentPicAct];
    } else {
        return fishCatchActs[currentCatchPicAct];
    }

}

public int getPicWidth() {
    // TODO Auto-generated method stub
    return getCurrentPic().getWidth();
}

public int getPicHeight() {
    // TODO Auto-generated method stub
    return getCurrentPic().getHeight();
}

/**
 * ����������ж���
 *
 * @param fishActs
 */
public void setFishActs(Bitmap[] fishActs) {
    // TODO Auto-generated method stub
    this.fishActs = fishActs;
}

/**
 * ����������б�������
 *
 * @param fishCatchActs
 */
public void setFishCatchActs(Bitmap[] fishCatchActs) {
    // TODO Auto-generated method stub
    this.fishCatchActs = fishCatchActs;
}


public FishInfo getFishInfo() {
    return fishInfo;
}

public void setFishInfo(FishInfo fishInfo) {
    this.fishInfo = fishInfo;
}

/**
 * ���ݵ�ǰ���ȡͬ����ʵ��
 *
 * @return
 */
public Fish getFish() {
    return new Fish(this.fishActs, this.fishCatchActs, this.fishInfo);
}

/**
 * ������׽�¼�����Ӧ����
 */
public void onCatch(Ammo ammo, final float targetX, final float targetY) {
//		System.out.println("�㱻��׽�ˣ�����û�в�׽��");
}

/**
 * �����ѱ���׽�¼�����Ӧ����
 * �����������������˵���������Ѿ�����׽��
 */
public void onCatched(Ammo ammo, final float targetX, final float targetY) {
    this.setAlive(false);
    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                float fishX = getHeadFish().getFish_X() - getDistanceHeadFishX();
                float fishY = getHeadFish().getFish_Y() - getDistanceHeadFishY();
                GamingInfo.getGamingInfo().getFish().remove(Fish.this);
                Thread.sleep(1800);
                //�������ӷ�������
                ScoreManager.getScoreManager().addScore(getFishInfo().getWorth(), fishX, fishY);
                Thread.sleep(200);
                Fish.this.getPicActThread().stopPlay();
                GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Fish.this.getFishInfo().getFishInLayer(), Fish.this);
            } catch (Exception e) {
                Log.e("Fish_onCatched", e.toString());
            }
        }
    }).start();
}

public HeadFish getHeadFish() {
    return headFish;
}

public void setHeadFish(HeadFish headFish) {
    this.headFish = headFish;
}

public int[] getFishOutlinePoint() {
    return fishOutlinePoint;
}

public boolean isCanRun() {
    return canRun;
}

public void setCanRun(boolean canRun) {
    this.canRun = canRun;
}

}
