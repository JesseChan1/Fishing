package com.fish.manager;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import com.fish.canvas.Bitmap;
import com.fish.tools.Log;
import com.fish.src.Constant;
import com.fish.model.BackGround;
import com.fish.model.GamingInfo;
import com.fish.model.componets.BottomTime;

/**
 * ��Ϸ�ؿ�������
 *
 * @author Xiloerfan
 */
public class GamePartManager {
/**
 * ����ģʽʹ��
 */
private static GamePartManager manager;
/**
 * ��������йؿ�
 * key		Ϊ�ؿ���
 * value	Ϊ�ؿ�����
 */
private ArrayList<com.fish.manager.GamePartInfo> games = new ArrayList<com.fish.manager.GamePartInfo>();
/**
 * ��ǰ���еĹؿ�
 */
private com.fish.manager.GamePartInfo part;
/**
 * ��ǰ���еĹؿ��ı���ͼƬ
 */
private BackGround background;
/**
 * �Ƿ�׼�����
 */
private boolean prepared;

/**
 * ������
 */
private GamePartManager() {
    try {
        XmlPullParser xml = com.fish.manager.XmlManager.getXmlParser("config/GamePart", "UTF-8");
        initGamePart(xml);
    } catch (Exception e) {
        e.printStackTrace();
    }
}


/**
 * ׼��
 */
public void prepare() {
    try {
        //���ñ���
        setBg();
        //���������
        com.fish.manager.FishManager.getFishMananger().updateFish(this.part.getFishName());
        prepared = true;
    } catch (Exception e) {
        Log.doLogForException(e);
    }

}

/**
 * �����ؿ�������
 */
public void start() {
    if (!prepared) {
        Log.e("GamePartManager", "������û��׼�����Ƿ���ù�prepare������");
        return;
    }
    //���ű�������
    com.fish.manager.MusicManager.getMusicManager().playMusicByR(this.part.getBgMusic(), true);
    //������Ⱥ��������֪ͨ�����ɵ���Ⱥ����
    GamingInfo.getGamingInfo().getShoalManager().start(this.part);
    //����������߳�
    startGiveGoldThrad();
}

/**
 * ������ʱ������߳�
 */
private void startGiveGoldThrad() {
    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                int time = Constant.GIVE_GOLD_TIME;
                BottomTime bt = com.fish.manager.LayoutManager.getLayoutManager().getBottomTime();
                while (GamingInfo.getGamingInfo().isGaming()) {
                    while (!GamingInfo.getGamingInfo().isPause()) {
                        if (time == 0) {
                            giveGold();
                            time = Constant.GIVE_GOLD_TIME;
                        }
                        bt.updateNumIndex(time);
                        time--;
                        Thread.sleep(1000);
                    }
                    break;
                }
            } catch (Exception e) {
                Log.doLogForException(e);
            }

        }

        private void giveGold() {
            if (GamingInfo.getGamingInfo().getScore() < Constant.GIVE_GOLD_LESS) {
                GamingInfo.getGamingInfo().setScore(Constant.GIVE_GOLD);
            }
        }
    }).start();
}

/**
 * ���ñ���
 */
private void setBg() {
    try {
        if (background == null) {
            background = new BackGround();
            try {
                background.setCurrentPic(com.fish.manager.ImageManager.getImageMnagaer().sacleImageByWidthAndHeight(com.fish.manager.ImageManager.getImageMnagaer().getBitmapByAssets(this.part.getBackground()), GamingInfo.getGamingInfo().getScreenWidth(), GamingInfo.getGamingInfo().getScreenHeight()));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.doLogForException(e);
            }
            GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.BACK_GROUND_LAYER, background);
        } else {
            try {
                background.setCurrentPic(Bitmap.createScaledBitmap(com.fish.manager.ImageManager.getImageMnagaer().getBitmapByAssets(this.part.getBackground()), GamingInfo.getGamingInfo().getScreenWidth(), GamingInfo.getGamingInfo().getScreenHeight(), false));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.doLogForException(e);
            }
        }
    } catch (Exception e) {
        Log.doLogForException(e);
    }

}

/**
 * ��ʼ�����еĹؿ�
 *
 * @param xml ��Ҫ������xml�ļ�
 */
private void initGamePart(XmlPullParser xml) {
    //ѭ�����еĹؿ�
    while (GamingInfo.getGamingInfo().isGaming() && com.fish.manager.XmlManager.gotoTagByTagName(xml, "key")) {
        //�����ؿ�������
        com.fish.manager.GamePartInfo gamePartInfo = new com.fish.manager.GamePartInfo();
        //��ȡ�ؿ�����
        com.fish.manager.XmlManager.gotoTagByTagName(xml, "string");
        gamePartInfo.setPartName(com.fish.manager.XmlManager.getValueByCurrentTag(xml));
        //��ȡ�ؿ����ֵ���
        com.fish.manager.XmlManager.gotoTagByTagName(xml, "string");
        gamePartInfo.setFishName(com.fish.manager.XmlManager.getValueByCurrentTag(xml).split(";"));
        //��ĳ��ָ���
        com.fish.manager.XmlManager.gotoTagByTagName(xml, "string");
        String probability[] = com.fish.manager.XmlManager.getValueByCurrentTag(xml).split(";");
        int[] showProbability = new int[probability.length];
        for (int i = 0; i < probability.length; i++) {
            showProbability[i] = Integer.parseInt(probability[i]);
        }
        gamePartInfo.setShowProbability(showProbability);
        //��ȡ�ɳ��ֵ���Ⱥ����
        com.fish.manager.XmlManager.gotoTagByTagName(xml, "integer");
        gamePartInfo.setShoalSumInScreen(Integer.parseInt(com.fish.manager.XmlManager.getValueByCurrentTag(xml)));
        //��ȡ�ؿ�ʱ��
        com.fish.manager.XmlManager.gotoTagByTagName(xml, "integer");
        gamePartInfo.setPartTime(Integer.parseInt(com.fish.manager.XmlManager.getValueByCurrentTag(xml)));
        //��ȡ��һ�ص�����
        com.fish.manager.XmlManager.gotoTagByTagName(xml, "string");
        gamePartInfo.setNextPart(com.fish.manager.XmlManager.getValueByCurrentTag(xml));
        //��ȡ��������
        com.fish.manager.XmlManager.gotoTagByTagName(xml, "string");
        gamePartInfo.setBgMusic(com.fish.manager.XmlManager.getValueByCurrentTag(xml));
        //��ȡ����ͼƬ
        com.fish.manager.XmlManager.gotoTagByTagName(xml, "string");
        gamePartInfo.setBackground(com.fish.manager.XmlManager.getValueByCurrentTag(xml));
        //��ȡ��ؿ��Ŀ��ּ���
        com.fish.manager.XmlManager.gotoTagByTagName(xml, "integer");
        gamePartInfo.setTargetScore(Integer.parseInt(com.fish.manager.XmlManager.getValueByCurrentTag(xml)));
        this.games.add(gamePartInfo);
    }
    //����йؿ���Ĭ�ϵ�һ��Ԫ��Ϊ��ʼ�ؿ���������ϲ�Ӧ��Ϊ�ռ���
    if (this.games.size() > 0) {
        this.part = this.games.get(0);
    }

}

/**
 * ��ȡ�ؿ�������ʵ��
 *
 * @return
 */
public static GamePartManager getManager() {
    if (manager == null) {
        manager = new GamePartManager();
    }
    return manager;
}

public com.fish.manager.GamePartInfo getCurrentPart() {
    return this.part;
}

/**
 * 检查分数是否达到目标，如果达到则切换到下一关
 */
public void checkScoreForLevelUp() {
    if (this.part != null) {
        int currentScore = GamingInfo.getGamingInfo().getScore();
        int targetScore = this.part.getTargetScore();
        
        if (currentScore >= targetScore) {
            switchToNextPart();
        }
    }
}

/**
 * 切换到下一关
 */
private void switchToNextPart() {
    try {
        String nextPartName = this.part.getNextPart();
        
        // 查找下一关的信息
        com.fish.manager.GamePartInfo nextPart = null;
        for (com.fish.manager.GamePartInfo gamePartInfo : this.games) {
            if (gamePartInfo.getPartName().equals(nextPartName)) {
                nextPart = gamePartInfo;
                break;
            }
        }
        
        if (nextPart != null) {
            this.part = nextPart;
            
            // 重新准备新关卡
            prepare();
            
            // 重新启动新关卡的音乐
            com.fish.manager.MusicManager.getMusicManager().playMusicByR(this.part.getBgMusic(), true);
            
            // 更新UI显示
            updateUIComponents();
        }
    } catch (Exception e) {
        Log.doLogForException(e);
    }
}

/**
 * 更新UI组件显示
 */
private void updateUIComponents() {
    try {
        // 更新关卡显示和目标分数显示
        if (com.fish.manager.LayoutManager.getLayoutManager() != null) {
            com.fish.manager.LayoutManager.getLayoutManager().updateLevelAndTarget();
        }
    } catch (Exception e) {
        Log.doLogForException(e);
    }
}

/**
 * ע������
 */
public void destroy() {
    manager = null;
    System.gc();
}

}
