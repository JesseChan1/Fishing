package com.fish.manager;

import java.util.ArrayList;
import java.util.HashMap;

import com.fish.canvas.Bitmap;
import com.fish.src.Constant;
import com.fish.model.Ammo;
import com.fish.model.FishingNet;
import com.fish.model.GamingInfo;
import com.fish.model.WaterRipple;
import com.fish.model.componets.Cannon;
import com.fish.model.componets.ChangeCannonEffect;
import com.fish.threads.ShotThread;
import com.fish.tools.Log;
import com.fish.tools.Tool;

/**
 * ���ڹ�����
 *
 * @author Xiloerfan
 */
public class CannonManager {
/**
 * �Ƿ���Ը�������
 */
private boolean canChangeCannon = true;
/**
 * �����ӵ�
 * key:��������ID��value:�ӵ�ͼƬ����
 */
private HashMap<Integer, Bitmap[]> bullet = new HashMap<Integer, Bitmap[]>();
/**
 * ���д���
 * key:��������ID��value:����ͼƬ����
 */
private HashMap<Integer, Cannon> cannon = new HashMap<Integer, Cannon>();
/**
 * ��������ͼƬ
 */
private Bitmap[] net;
/**
 * ˮ������Ч��ͼƬ
 */
private Bitmap[] waterRipple;
/**
 * �任���ڵ�Ч��ͼ
 */
private Bitmap[] changeCannonEffect;
/**
 * ������
 */
private Bitmap[] laser;
/**
 * �Ƿ���Է����ڵ�
 */
private boolean shotable;
/**
 * ��ǰʹ�õĴ���ID
 */
private int currentCannonIndex = 1;
private static CannonManager cannonManager;


private CannonManager() {

}

/**
 * ��ʼ�����ڹ�����
 */
public void init() {
    try {
        //��ȡ�����ļ�ָ��������ͼƬ
        HashMap<String, Bitmap> allImage = ImageManager.getImageMnagaer().getImagesMapByImageConfig(ImageManager.getImageMnagaer().createImageConfigByPlist("cannon/bulletandnet"), ImageManager.getImageMnagaer().scaleNum);
        allImage.putAll(ImageManager.getImageMnagaer().getImagesMapByImageConfig(ImageManager.getImageMnagaer().createImageConfigByPlist("cannon/fire"), ImageManager.getImageMnagaer().scaleNum));
        //��ʼ���������
        initGoldNum(allImage);
        //��ʼ���ӵ�
        initAmmo(allImage);
        //��ʼ������
        initNet(allImage);
        //��ʼ��ˮ����
        initWaterRipple(ImageManager.getImageMnagaer().getImagesMapByImageConfig(ImageManager.getImageMnagaer().createImageConfigByPlist("cannon/ripple"), ImageManager.getImageMnagaer().scaleNum));
        //��ʼ������
        initCannon(allImage);
        //��ʼ�����⣬Ŀǰû��ʵ��

        //��ʼ����������ʱ��Ч��
        initChangeCannonEffect();

    } catch (Exception e) {
        Log.doLogForException(e);
    }
}

/**
 * ��ʼ���������
 * д����������Ϊ����������ڵ�ͼƬ�����Ӻ��ӵ�����Դͼ�У���ʱǷ�������������
 *
 * @param allImage
 */
private void initGoldNum(HashMap<String, Bitmap> allImage) {
    //������ͼȫ��(num_9.png)
    StringBuffer numFullName = new StringBuffer();
    //�������ֱ��
    int num = 0;
    String numName = "num_";
    ArrayList<Bitmap> allNumList = new ArrayList<Bitmap>();
    //��ȡ��ǰ�ӵ������ж���
    while (GamingInfo.getGamingInfo().isGaming()) {
        numFullName.delete(0, numFullName.length());
        numFullName.append(numName + num + ".png");
        Bitmap numImg = allImage.get(numFullName.toString());
        //���û�н�����������
        if (numImg == null) {
            break;
        }
        allNumList.add(numImg);
        num++;
    }
    allNumList.add(allImage.get("num_x.png"));
    //������ת��Ϊ����
    Bitmap[] imgs = new Bitmap[allNumList.size()];
    for (int i = 0; i < allNumList.size(); i++) {
        imgs[i] = allNumList.get(i);
    }
    ScoreManager.getScoreManager().setGoldNum(imgs);
}

/**
 * ��ʼ���������ڵ�Ч��ͼ
 */
private void initChangeCannonEffect() {
    HashMap<String, Bitmap> allEffect = ImageManager.getImageMnagaer().getImagesMapByImageConfig(ImageManager.getImageMnagaer().createImageConfigByPlist("cannon/changefire"), ImageManager.getImageMnagaer().scaleNum);
    //Ч��ͼȫ��(paolizi_08.png)
    StringBuffer effectFullName = new StringBuffer();
    //�������ֱ��
    int effectNum = 1;
    String effectName = "paolizi";
    ArrayList<Bitmap> allEffectList = new ArrayList<Bitmap>();
    while (GamingInfo.getGamingInfo().isGaming()) {
        effectFullName.delete(0, effectFullName.length());
        if (effectNum < 10) {
            effectFullName.append(effectName + "_0" + effectNum + ".png");
        } else {
            effectFullName.append(effectName + "_" + effectNum + ".png");
        }
        Bitmap effect = allEffect.get(effectFullName.toString());
        if (effect == null) {
            break;
        }
        allEffectList.add(effect);
        effectNum++;
    }
    //������ת��Ϊ����
    changeCannonEffect = new Bitmap[allEffectList.size()];
    for (int i = 0; i < allEffectList.size(); i++) {
        changeCannonEffect[i] = allEffectList.get(i);
    }
}

/**
 * ��ʼ�����д���ͼƬ
 *
 * @param allImage
 */
private void initCannon(HashMap<String, Bitmap> allImage) {
    //���ڵ�ͼȫ��(net_11.png)
    StringBuffer cannonFullName = new StringBuffer();
    //�������ֱ��,�����Ʊ��
    int cannonNum = 1, subCannonNum = 1;
    String cannonName = "net";
    ArrayList<Bitmap> allCannonList = new ArrayList<Bitmap>();
    //��ȡ��ǰ�ӵ������ж���
    while (GamingInfo.getGamingInfo().isGaming()) {
        allCannonList.clear();
        subCannonNum = 1;
        cannonFullName.delete(0, cannonFullName.length());
        cannonFullName.append(cannonName + "_" + cannonNum);
        while (GamingInfo.getGamingInfo().isGaming()) {
            Bitmap cannon = allImage.get(cannonFullName.toString() + subCannonNum + ".png");
            if (cannon == null) {
                break;
            }
            allCannonList.add(cannon);
            subCannonNum++;
        }
        //���û�н�����������
        if (allCannonList.size() == 0) {
            break;
        }
        //������ת��Ϊ����
        Bitmap[] cannons = new Bitmap[allCannonList.size()];
        for (int i = 0; i < allCannonList.size(); i++) {
            cannons[i] = allCannonList.get(i);
        }
        //�����ڷ����������
        Cannon cannon_obj = new Cannon(cannons);
        cannon_obj.init();
        cannon.put(cannonNum, cannon_obj);
        cannonNum++;
    }
}

/**
 * ��ʼ������
 */
public void initCannon() {
    setShotable(false);
    currentCannonIndex = 1;
    resetCannonMatrix(getCannon(currentCannonIndex));
    LayoutManager.getLayoutManager().initCannon(getCannon(currentCannonIndex));
    setShotable(true);
}

/**
 * ��ʼ������
 */
private void initNet(HashMap<String, Bitmap> allImage) {
    //������ͼȫ��(net011.png)
    StringBuffer netFullName = new StringBuffer();
    //�������ֱ��
    int netNum = 1;
    String netName = "net";
    ArrayList<Bitmap> allNetList = new ArrayList<Bitmap>();
    //��ȡ��ǰ�ӵ������ж���
    while (GamingInfo.getGamingInfo().isGaming()) {
        netFullName.delete(0, netFullName.length());
        netFullName.append(netName + "0" + netNum + ".png");
        Bitmap net = allImage.get(netFullName.toString());
        //���û�н�����������
        if (net == null) {
            break;
        }
        allNetList.add(net);
        netNum++;
    }
    //������ת��Ϊ����
    net = new Bitmap[allNetList.size()];
    for (int i = 0; i < allNetList.size(); i++) {
        net[i] = allNetList.get(i);
    }
}

/**
 * ��ʼ������
 */
private void initWaterRipple(HashMap<String, Bitmap> allImage) {
    //������ͼȫ��(water_11.png)
    StringBuffer rippleFullName = new StringBuffer();
    //�������ֱ��
    int rippleNum = 1;
    String rippleName = "water_";
    ArrayList<Bitmap> allRippleList = new ArrayList<Bitmap>();
    //��ȡ��ǰ�ӵ������ж���
    while (GamingInfo.getGamingInfo().isGaming()) {
        rippleFullName.delete(0, rippleFullName.length());
        rippleFullName.append(rippleName + rippleNum + ".png");
        Bitmap ripple = allImage.get(rippleFullName.toString());
        //���û�н�����������
        if (ripple == null) {
            break;
        }
        allRippleList.add(ripple);
        rippleNum++;
    }
    //������ת��Ϊ����
    waterRipple = new Bitmap[allRippleList.size()];
    for (int i = 0; i < allRippleList.size(); i++) {
        waterRipple[i] = allRippleList.get(i);
    }
}

/**
 * ��ʼ�������ӵ�ͼƬ
 */
private void initAmmo(HashMap<String, Bitmap> allImage) {
    //�ӵ���ͼȫ��(bullet12.png),�ӵ�����(bullet12_01.png)
    StringBuffer ammoFullName = new StringBuffer();
    StringBuffer subAmmoFullName = new StringBuffer();
    //�������ֱ��,�����Ʊ��
    int ammoNum = 1, subAmmoNum = 1;
    String ammoName = "bullet";
    ArrayList<Bitmap> allAmmoList = new ArrayList<Bitmap>();
    //��ȡ��ǰ�ӵ������ж���
    while (GamingInfo.getGamingInfo().isGaming()) {
        allAmmoList.clear();
        ammoFullName.delete(0, ammoFullName.length());
        ammoFullName.append(ammoName + "0" + ammoNum + ".png");
        //����һ�����ڴ���ͼƬ������
        Bitmap ammo = allImage.get(ammoFullName.toString());
        //���ͼƬû���ҵ����˳�ѭ��
        if (ammo == null) {
            break;
        }
        allAmmoList.add(ammo);
        subAmmoNum = 1;
        //��ͼ���Կ�����û��ͬ������ͼƬ
        //����-4��ȥ��.png��������ַ����ټ���ƴд������
        ammoFullName.delete(ammoFullName.length() - 4, ammoFullName.length());
        while (GamingInfo.getGamingInfo().isGaming()) {
            subAmmoFullName.delete(0, subAmmoFullName.length());
            subAmmoFullName.append(ammoFullName.toString() + "_" + subAmmoNum + ".png");
            Bitmap subAmmo = allImage.get(subAmmoFullName.toString());
            if (subAmmo == null) {
                break;
            }
            allAmmoList.add(subAmmo);
            subAmmoNum++;
        }
        //������ת��Ϊ����
        Bitmap[] bullets = new Bitmap[allAmmoList.size()];
        for (int i = 0; i < allAmmoList.size(); i++) {
            bullets[i] = allAmmoList.get(i);
        }
        //���ӵ������������
        bullet.put(ammoNum, bullets);
        ammoNum++;
    }
}

public static CannonManager getCannonManager() {
    if (cannonManager == null) {
        cannonManager = new CannonManager();
    }
    return cannonManager;
}

/**
 * ���ݸ�������ID��ȡ����Ķ�Ӧ�ӵ���ʵ��
 *
 * @param id
 * @return
 */
private Ammo getAmmo(int id) {
    Ammo ammo = new Ammo(id);
    ammo.setCurrentPic(this.bullet.get(id), new FishingNet(this.net[id - 1], ammo));
    return ammo;
}

/**
 * ���ݸ�������ID��ȡ���ڵ�ʵ��
 *
 * @param id
 * @return
 */
private Cannon getCannon(int id) {
    return this.cannon.get(id);

}

/**
 * ��ߴ��ڵȼ�
 */
public void upCannon() {
    if (!canChangeCannon) {
        return;
    }
    canChangeCannon = false;//�����������
    setShotable(false);
    if (currentCannonIndex + 1 > cannon.size()) {
        currentCannonIndex = 1;
    } else {
        currentCannonIndex++;
    }
    resetCannonMatrix(getCannon(currentCannonIndex));
    playChangeCannonEffect();
    //���Ÿ������ڵ���Ч
    SoundManager.playSound(SoundManager.SOUND_BGM_CHANGE_CANNON);
    LayoutManager.getLayoutManager().updateCannon(getCannon(currentCannonIndex));
    canChangeCannon = true;
    setShotable(true);
}

/**
 * ���ʹ��ڵȼ�
 */
public void downCannon() {
    if (!canChangeCannon) {
        return;
    }
    canChangeCannon = false;//�����������
    setShotable(false);
    if (currentCannonIndex - 1 == 0) {
        currentCannonIndex = cannon.size();
    } else {
        currentCannonIndex--;
    }
    resetCannonMatrix(getCannon(currentCannonIndex));
    playChangeCannonEffect();
    //���Ÿ������ڵ���Ч
    SoundManager.playSound(SoundManager.SOUND_BGM_CHANGE_CANNON);
    LayoutManager.getLayoutManager().updateCannon(getCannon(currentCannonIndex));
    canChangeCannon = true;
    setShotable(true);
}

/**
 * ���Ŵ���ת��Ч��
 */
private void playChangeCannonEffect() {
    new Thread(new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            ChangeCannonEffect effect = new ChangeCannonEffect(changeCannonEffect);
            effect.playEffect();
        }
    }).start();

}

/**
 * ����ӵ�
 *
 * @param targetX Ŀ���X����
 * @param targetY Ŀ���y����
 */
public void shot(float targetX, float targetY) {
    if (shotable) {
        //����ˮ����Ч��
        playRipple(targetX, targetY);
        if (GamingInfo.getGamingInfo().getScore() >= currentCannonIndex) {
            waitReload();
            GamingInfo.getGamingInfo().setScore(GamingInfo.getGamingInfo().getScore() - currentCannonIndex);
            //���ڵ�����
            SoundManager.playSound(SoundManager.SOUND_BGM_FIRE);
            this.rotateCannon(targetX, targetY, getCannon(currentCannonIndex));
            //���Ŵ��ڷ���Ч��
            getCannon(currentCannonIndex).shot();
            //�����ڵ�
            Ammo ammo = getAmmo(currentCannonIndex);
            ShotThread st = new ShotThread(targetX - ammo.getPicWidth() / 2, targetY - ammo.getPicHeight() / 2, ammo, GamingInfo.getGamingInfo().getCannonLayoutX() - ammo.getPicWidth() / 2, GamingInfo.getGamingInfo().getCannonLayoutY() - ammo.getPicHeight() / 2);
            st.start();
        } else {
            //û�н�ҵ�����
            SoundManager.playSound(SoundManager.SOUND_BGM_NO_GOLD);

        }

    }
}

/**
 * �ϵ�ʱ��
 */
private void waitReload() {
    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                shotable = false;
                Thread.sleep(Constant.CANNON_RELOAD_TIME);
                shotable = true;
            } catch (Exception e) {
                Log.doLogForException(e);
            }

        }
    }).start();


}

/**
 * ����ˮ����Ч��
 *
 * @param targetX
 * @param targetY
 */
private void playRipple(final float targetX, final float targetY) {
    new Thread(new Runnable() {
        public void run() {
            WaterRipple wr = new WaterRipple(waterRipple);
            wr.getPicMatrix().setTranslate(targetX - wr.getPicWidth() / 2, targetY - wr.getPicHeight() / 2);
            GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.HUNDRED_WATER_RIPPLE_LAYER, wr);
            for (int i = 0; i < waterRipple.length; i++) {
                wr.setCurrentId(i);
                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                    Log.doLogForException(e);
                }
            }
            GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Constant.HUNDRED_WATER_RIPPLE_LAYER, wr);
        }
    }).start();
}

/**
 * ��ת����
 */
private void rotateCannon(float targetX, float targetY, Cannon cannon) {
    try {
        //��ȡ������Ҫ��ת�ĽǶ�
        float gun_angle = Tool.getAngle(targetX, targetY, GamingInfo.getGamingInfo().getScreenWidth() / 2, GamingInfo.getGamingInfo().getScreenHeight());
        cannon.getPicMatrix().reset();
        cannon.getPicMatrix().setTranslate(cannon.getX(), cannon.getY());
        //������ת���㷨
        if (gun_angle >= 90) {
            cannon.getPicMatrix().preRotate(90 - gun_angle, cannon.getGun_rotate_point_x(), cannon.getGun_rotate_point_y());
        } else {
            cannon.getPicMatrix().preRotate(-(gun_angle - 90), cannon.getGun_rotate_point_x(), cannon.getGun_rotate_point_y());
        }
    } catch (Exception e) {
        Log.doLogForException(e);
    }
}

/**
 * �ָ����ڵĳ�ʼ״̬
 */
private void resetCannonMatrix(Cannon cannon) {
    rotateCannon(GamingInfo.getGamingInfo().getScreenWidth() / 2, 0, cannon);
}

/**
 * �����Ƿ����������
 *
 * @param shotable true:���� false:������
 */
public void setShotable(boolean shotable) {
    this.shotable = shotable;
}
}
