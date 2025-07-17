package com.fish.threads;

import com.fish.model.GamingInfo;
import com.fish.model.Fish;
import com.fish.tools.Log;

/**
 * ������Ķ������߳�
 * һ����Ķ����Ƕ���ͼƬ
 *
 * @author Xiloer
 */
public class PicActThread extends Thread {
private Fish fish;                            //�����Ƶ���
private boolean notPause = true;            //�Ƿ���Ҫ��ͣ����
private boolean isAct = true;                //��Ҫ����
int len;                                    //��ȡ������ж���
private boolean isPause = false;            //�߳��Ƿ��Ѿ���ͣ

public PicActThread(Fish fish) {
    this.fish = fish;
    len = fish.getFishActs();
    isPause = false;
}

public void run() {
    try {
        while (GamingInfo.getGamingInfo().isGaming()) {
            while (!GamingInfo.getGamingInfo().isPause() && isAct) {
                while (notPause) {
                    if (fish.getCurrentPicId() + 1 >= fish.getFishActs()) {
                        //ѭ������һ����������ĵ�ǰ����
                        fish.setCurrentPicId(0);
                    } else {
                        //ѭ������һ����������ĵ�ǰ����
                        fish.setCurrentPicId(fish.getCurrentPicId() + 1);
                    }

                    Thread.sleep(fish.getFishInfo().getPicActSpeed());
                }
                isPause = true;
            }
            break;
        }
    } catch (Exception e) {
        Log.doLogForException(e);
    }
}

/**
 * ����
 *
 * @param mode
 */
public void reset() {
    len = fish.getFishActs();
    notPause = true;
    isAct = true;
}

/**
 * ����
 */
public void play() {
    notPause = true;
    isPause = false;
}

/**
 * ��ͣ����
 */
public void pausePlay() {
    notPause = false;
    while (!isPause) {
    }
}

/**
 * ֹͣ����
 */
public void stopPlay() {
    notPause = false;
    isAct = false;
}

}
