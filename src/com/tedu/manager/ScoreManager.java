package com.fish.manager;

import com.fish.canvas.Bitmap;
import com.fish.src.Constant;
import com.fish.model.*;
import com.fish.tools.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * �÷ֹ�����
 *
 * @author Xiloerfan
 */
public class ScoreManager {
private static ScoreManager scoreManager;
private int gold_speed = 1000 / Constant.ON_DRAW_SLEEP;
/**
 * ��ҵ����ͼƬ
 */
private Bitmap[] gold;
/**
 * ��Ҷ�Ӧ�Ľ�����ͼƬ
 * ������ԱȽ����⣬�ڴ��ڹ�������ʼ���ģ����ﵱ��Ƿ������
 */
private Bitmap[] goldNum;
/**
 * �߷ֵ����ͼƬ
 * key:���� value:��Ӧ��ͼƬ
 */
private HashMap<Integer, Bitmap[]> highPoint = new HashMap<Integer, Bitmap[]>();
/**
 * �ٷֵ����ͼƬ
 * key:���� value:��Ӧ��ͼƬ
 */
private HashMap<Integer, Bitmap[]> hundredPoint = new HashMap<Integer, Bitmap[]>();

private ScoreManager() {
}

public static ScoreManager getScoreManager() {
    if (scoreManager == null) {
        scoreManager = new ScoreManager();
    }
    return scoreManager;
}

/**
 * �ӷֲ���
 *
 * @param value
 * @param showX ��ʾλ�õ�x����
 * @param showY ��ʾλ�õ�Y����
 */
public void addScore(int value, final float showX, final float showY) {
    GamingInfo.getGamingInfo().setScore(GamingInfo.getGamingInfo().getScore() + value);
    
    // 检查分数是否达到目标，如果达到则切换关卡
    com.fish.manager.GamePartManager.getManager().checkScoreForLevelUp();
    
    //��ͬ�ķ����в�ͬ����ʾЧ��
    switch (value) {
        case 40:
            showHighPoint(40, showX, showY);
            break;
        case 50:
            showHighPoint(50, showX, showY);
            break;
        case 60:
            showHighPoint(60, showX, showY);
            break;
        case 70:
            showHighPoint(70, showX, showY);
            break;
        case 80:
            showHighPoint(80, showX, showY);
            break;
        case 90:
            showHighPoint(90, showX, showY);
            break;
        case 100:
            showHundredPoint(100, showX, showY);
            break;
        case 120:
            showHundredPoint(120, showX, showY);
            break;
        case 150:
            showHundredPoint(150, showX, showY);
            break;
        default:
            showGoldNum(value, showX, showY);
            goldRun(showX, showY);
    }
}

/**
 * ��ʾ��õĽ����
 *
 * @param score
 * @param goldFromX
 * @param goldFromY
 */
private void showGoldNum(final int score, final float goldFromX, final float goldFromY) {
    //��ʾ�õ��ķ���
    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                FishGold fg = new FishGold(goldNum, score, goldFromX, goldFromY);
                GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.GOLD_LAYER, fg);
                Thread.sleep(1000);
                GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Constant.GOLD_LAYER, fg);
            } catch (Exception e) {
                e.printStackTrace();
                Log.doLogForException(e);
            }

        }
    }).start();
}

/**
 * ������ң��ƶ�����Ļ�ײ����м�λ��
 * ����Ժ���Ա�������ļ�������λ��
 *
 * @param goldFromX
 * @param goldFromY
 */
private void goldRun(final float goldFromX, final float goldFromY) {
    //����һ���̣߳����ý�ҿ�ʼ�ƶ�
    new Thread(new Runnable() {
        @Override
        public void run() {
            final Gold gold = getGold();
            float x = Math.abs(GamingInfo.getGamingInfo().getScreenWidth() / 2 - gold.getPicWidth() / 2 - goldFromX);
            float y = Math.abs(GamingInfo.getGamingInfo().getScreenHeight() - gold.getPicHeight() / 2 - goldFromY);
            float len = (float) Math.sqrt(x * x + y * y);                            // Ŀ���ʼ����֮��ľ���
            float time = len / (Constant.Gold_SPEED / Constant.ON_DRAW_SLEEP);        // ����Ŀ����ʼ��֮���ӵ���Ҫ���ߵ�֡��
            x = x / time;                                                            // �����ӵ���X���н�������
            y = y / time;
            if (GamingInfo.getGamingInfo().getScreenWidth() / 2 < goldFromX) {
                x = -x;
            }
            float goldX = goldFromX - gold.getPicWidth() / 2, goldY = goldFromY - gold.getPicHeight() / 2;
            //������ҵĶ����߳�
            Runnable goldActThread = new Runnable() {
                @Override
                public void run() {
                    int picIndex = 0;
                    gold.setPlayGoldPicAct(true);
                    while (GamingInfo.getGamingInfo().isGaming()) {
                        while (!GamingInfo.getGamingInfo().isPause() && gold.isPlayGoldPicAct()) {
                            gold.setCurrentPicId(picIndex);
                            picIndex++;
                            if (picIndex == gold.getPicLength()) {
                                picIndex = 0;
                            }
                            try {
                                Thread.sleep(100);
                            } catch (Exception e) {
                                Log.doLogForException(e);
                            }
                        }
                        break;
                    }
                }
            };
            gold.setGoldActThread(goldActThread);
            gold.getPicMatrix().setTranslate(goldFromX, goldFromY);
            //����ҷ���ͼ��
            GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.GOLD_LAYER, gold);
            GoldParticleEffect effect = com.fish.manager.ParticleEffectManager.getParticleEffectManager().getGoldEffect();
//				float angle = Tool.getAngle(GamingInfo.getGamingInfo().getScreenWidth()/2, GamingInfo.getGamingInfo().getScreenHeight(), goldFromX, goldFromY);
//				int ammoRedius = gold.getPicHeight()/2;//����뾶�����������ڼ�����β�ʹ���������ʹ��
//				effect.playEffect((float)(ammoRedius*Math.cos(Math.toRadians(angle+180))),-(float)(ammoRedius*Math.sin(Math.toRadians(angle+180))),goldFromX-gold.getPicWidth()/2, goldFromY-gold.getPicHeight()/2, x, y);
            float gw = gold.getPicWidth() / 2, gh = gold.getPicHeight() / 2;//�������ƫ��
            effect.playEffect(0, 0, goldFromX + gw, goldFromY + gh, x, y);
            new Thread(gold.getGoldActThread()).start();
            //��ʼ�ƶ����
            while (GamingInfo.getGamingInfo().isGaming()) {
                goldX = goldX + x;
                goldY = goldY + y;
                gold.getPicMatrix().setTranslate(goldX, goldY);
                effect.setEffectMatrix(goldX + gw, goldY + gh);
                if (goldY > GamingInfo.getGamingInfo().getScreenHeight()) {
                    gold.setPlayGoldPicAct(false);//ֹͣ���Ž�Ҷ���
                    GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Constant.GOLD_LAYER, gold);
                    effect.stopEffect();
                    break;
                }
                try {
                    Thread.sleep(gold_speed);
                } catch (Exception e) {
                    Log.doLogForException(e);
                }
            }
        }
    }).start();
    //���Ž����Ч
    com.fish.manager.SoundManager.playSound(com.fish.manager.SoundManager.SOUND_BGM_GOLD);
}

/**
 * ��ʾ�߷���
 * 40-90�ֵ�����
 *
 * @param score
 * @param goldFromX
 * @param goldFromY
 */
private void showHighPoint(final int score, final float goldFromX, final float goldFromY) {
    new Thread(new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            HighPoint hp = new HighPoint(highPoint.get(score));
            hp.getPicMatrix().setTranslate(goldFromX, goldFromY);
            GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.HIGH_POINT_LAYER, hp);
            com.fish.manager.SoundManager.playSound(com.fish.manager.SoundManager.SOUND_BGM_HIGH_POINT);
            try {
                int showTime = 0;//��ʾ5��ѭ��
                int currentId = 0;
                while (GamingInfo.getGamingInfo().isGaming()) {
                    while (!GamingInfo.getGamingInfo().isPause()) {
                        hp.setCurrentPicId(currentId);
                        currentId++;
                        if (currentId == hp.getActPicLength()) {
                            currentId = 0;
                            showTime++;
                        }
                        if (showTime == 5) {
                            break;
                        }
                        Thread.sleep(100);
                    }
                    break;
                }
            } catch (Exception e) {
                Log.doLogForException(e);
            }
            GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Constant.HIGH_POINT_LAYER, hp);
        }
    }).start();
}

/**
 * ��ʾ�߷���
 * 40-90�ֵ�����
 *
 * @param score
 * @param goldFromX
 * @param goldFromY
 */
private void showHundredPoint(final int score, final float goldFromX, final float goldFromY) {
    new Thread(new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            HundredPoint hp = new HundredPoint(hundredPoint.get(score));
            hp.getPicMatrix().setTranslate(goldFromX, goldFromY);
            GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.HUNDRED_POINT_LAYER, hp);
            com.fish.manager.SoundManager.playSound(com.fish.manager.SoundManager.SOUND_BGM_HUNDRED_POINT);
            try {
                int showTime = 0;//��ʾ5��ѭ��
                int currentId = 0;
                while (GamingInfo.getGamingInfo().isGaming()) {
                    while (!GamingInfo.getGamingInfo().isPause()) {
                        hp.setCurrentPicId(currentId);
                        currentId++;
                        if (currentId == hp.getActPicLength()) {
                            currentId = 0;
                            showTime++;
                        }
                        if (showTime == 5) {
                            break;
                        }
                        Thread.sleep(100);
                    }
                    break;
                }
            } catch (Exception e) {
                Log.doLogForException(e);
            }
            GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Constant.HUNDRED_POINT_LAYER, hp);
        }
    }).start();
}

/**
 * ��ȡһ�����
 *
 * @return
 */
private Gold getGold() {
    return new Gold(this.gold);
}

/**
 * ���ٲ���
 */
public static void destroy() {
    scoreManager = null;
}

/**
 * ��ʼ������
 */
public void init() {
    try {

        //��ʼ�����
        initGold(com.fish.manager.ImageManager.getImageMnagaer().getImagesMapByImageConfig(com.fish.manager.ImageManager.getImageMnagaer().createImageConfigByPlist("score/goldItem"), com.fish.manager.ImageManager.getImageMnagaer().scaleNum));
        //��ʼ���߷�
        initHighPoint(com.fish.manager.ImageManager.getImageMnagaer().getImagesMapByImageConfig(com.fish.manager.ImageManager.getImageMnagaer().createImageConfigByPlist("score/highPoint"), com.fish.manager.ImageManager.getImageMnagaer().scaleNum));
        //��ʼ���ٷ�
        initHundredPoint(com.fish.manager.ImageManager.getImageMnagaer().getImagesMapByImageConfig(com.fish.manager.ImageManager.getImageMnagaer().createImageConfigByPlist("score/hundred"), com.fish.manager.ImageManager.getImageMnagaer().scaleNum));
    } catch (Exception e) {
        Log.doLogForException(e);
    }
}

/**
 * ��ʼ�����
 *
 * @param golds
 */
private void initGold(HashMap<String, Bitmap> golds) {
    //Ч��ͼȫ��(gold01.png)
    StringBuffer goldFullName = new StringBuffer();
    //�������ֱ��
    int goldtNum = 1;
    String goldName = "gold";
    ArrayList<Bitmap> allGoldList = new ArrayList<Bitmap>();
    while (GamingInfo.getGamingInfo().isGaming()) {
        goldFullName.delete(0, goldFullName.length());
        goldFullName.append(goldName + "0" + goldtNum + ".png");
        Bitmap gold = golds.get(goldFullName.toString());
        if (gold == null) {
            break;
        }
        allGoldList.add(gold);
        goldtNum++;
    }
    //������ת��Ϊ����
    gold = new Bitmap[allGoldList.size()];
    for (int i = 0; i < allGoldList.size(); i++) {
        gold[i] = com.fish.manager.ImageManager.getImageMnagaer().rotateImage(90, allGoldList.get(i));
    }
}

/**
 * ��ʼ���߷�
 *
 * @param imgs
 */
private void initHighPoint(HashMap<String, Bitmap> highPoint) {
    //Ч��ͼȫ��(40_1.png)
    StringBuffer highPointFullName = new StringBuffer();
    //�������ֱ��
    int highPointBaseNum = 40;
    int highPointNum;
    ArrayList<Bitmap> allHighPointList = new ArrayList<Bitmap>();
    while (GamingInfo.getGamingInfo().isGaming()) {
        highPointNum = 1;
        while (GamingInfo.getGamingInfo().isGaming()) {
            highPointFullName.delete(0, highPointFullName.length());
            highPointFullName.append(highPointBaseNum + "_" + highPointNum + ".png");
            Bitmap highPointImg = highPoint.get(highPointFullName.toString());
            if (highPointImg == null) {
                break;
            }
            highPointNum++;
            allHighPointList.add(highPointImg);
        }
        if (allHighPointList.size() == 0) {
            break;
        }
        //������ת��Ϊ����
        Bitmap[] highPointArr = new Bitmap[allHighPointList.size()];
        for (int i = 0; i < allHighPointList.size(); i++) {
            highPointArr[i] = allHighPointList.get(i);
        }
        allHighPointList.clear();
        this.highPoint.put(highPointBaseNum, highPointArr);
        highPointBaseNum += 10;
    }

}

/**
 * ��ʼ���ٷ�
 *
 * @param imgs
 */
private void initHundredPoint(HashMap<String, Bitmap> hundredPoint) {
    //Ч��ͼȫ��(40_1.png)
    StringBuffer hundredPointFullName = new StringBuffer();
    //�������ֱ��
    int hundredPointBaseNum = 100;
    int hundredPointNum;
    ArrayList<Bitmap> allHundredPointList = new ArrayList<Bitmap>();
    while (GamingInfo.getGamingInfo().isGaming()) {
        hundredPointNum = 1;
        while (GamingInfo.getGamingInfo().isGaming()) {
            hundredPointFullName.delete(0, hundredPointFullName.length());
            hundredPointFullName.append(hundredPointBaseNum + "_" + hundredPointNum + ".png");
            Bitmap hundredPointImg = hundredPoint.get(hundredPointFullName.toString());
            if (hundredPointImg == null) {
                break;
            }
            hundredPointNum++;
            allHundredPointList.add(hundredPointImg);
        }
        if (allHundredPointList.size() > 0) {
            //������ת��Ϊ����
            Bitmap[] hundredPointArr = new Bitmap[allHundredPointList.size()];
            for (int i = 0; i < allHundredPointList.size(); i++) {
                hundredPointArr[i] = allHundredPointList.get(i);
            }
            allHundredPointList.clear();
            this.hundredPoint.put(hundredPointBaseNum, hundredPointArr);
        }
        hundredPointBaseNum += 10;
        if (hundredPointBaseNum >= 150) {
            break;
        }
    }

}

/**
 * ���ý�Ҷ�Ӧ������ͼƬ
 *
 * @param goldNum
 */
public void setGoldNum(Bitmap[] goldNum) {
    this.goldNum = goldNum;
}

}
