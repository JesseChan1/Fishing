package com.fish.manager;

import com.fish.src.Constant;
import com.fish.model.GamingInfo;
import com.fish.model.LoadProgress;
import com.fish.tools.Log;


/**
 * ��Ϸ��ʼ��������
 * @author Xiloerfan
 *
 */
public class GameInitManager {
	private static GameInitManager manager;
	private boolean initing = true;
	/**
	 * �Ƿ����ڳ�ʼ��
	 * @return
	 */
	public boolean isIniting(){
		return initing;
	}
	private GameInitManager(){}
	public static GameInitManager getGameInitManager(){
		if(manager == null){
			manager = new GameInitManager();
		}
		return manager;
	}
	public void init(){
		ImageManager.getImageMnagaer().initManager();
		initProgress();
		initGame();//��ʼ����Ϸ
    	try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	closeProgress();
    	beginGame();
    	initing = false;
	}
	/**
	 * ��ʼ������������
	 */
	private void initProgress(){
		while(LoadProgress.getLoadProgress()==null);
		GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.LOAD_PROGRESS_LAYER, LoadProgress.getLoadProgress());
	}

	/**
	 * �رս���������
	 */
	private void closeProgress(){
		try{
			if(GamingInfo.getGamingInfo().getSurface()!=null){
				GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Constant.LOAD_PROGRESS_LAYER, LoadProgress.getLoadProgress());
			}
		}catch(Exception e){
			Log.doLogForException(e);
		}
	}
	 /**
     * ��ʼ���������
     */
    private void initComponents(){
    	LayoutManager.getLayoutManager().init();
//
//    	// ������ߴ���������ť
//    	GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, new GreateGunQualityButton());
//    	// ���ƽ��ʹ���������ť
//    	GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, new LessGunQualityButton());
//    	// �������½ǼƷ����
//    	GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, new BottomLeftComponent());
//    	// �������½Ǽ������������
//    	GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, new BottomRightComponent());
    }

	/**
	 * ��ʼ����Ϸ
	 */
	private void initGame(){
			//��ʼ���������
			this.initComponents();
			//��ʼ���÷ֹ�����
			ScoreManager.getScoreManager().init();
			//��ʼ�����ӹ�����
			ParticleEffectManager.getParticleEffectManager();
			LoadProgress.getLoadProgress().setProgress(10);
			//��ʼ�����ڹ�����
			CannonManager.getCannonManager().init();
			LoadProgress.getLoadProgress().setProgress(20);
			//��ʼ���������
	        FishManager.getFishMananger().initFish();
	        LoadProgress.getLoadProgress().setProgress(40);
	        //��ʼ����Ⱥ������
			GamingInfo.getGamingInfo().setShoalManager(new ShoalManager());
			LoadProgress.getLoadProgress().setProgress(60);
			//��ʼ���ؿ�������
			GamePartManager.getManager().prepare();
			LoadProgress.getLoadProgress().setProgress(80);
			//��ʼ����Ч
			initSound();
	    	LoadProgress.getLoadProgress().setProgress(90);
			//��ʼ������
			CannonManager.getCannonManager().initCannon();
			LoadProgress.getLoadProgress().setProgress(100);
	}

	/**
	 * ֹͣ��Ϸ
	 */
	public void stop(){

		try {
			//������Ϸ����
			GamingInfo.getGamingInfo().setGaming(false);
			Thread.sleep(1000);
			//ע�����ֹ�����
			MusicManager.release();
			//ע���������
			FishManager.destroy();
			//ע����Ϸ�ؿ�������
			GamePartManager.getManager().destroy();
			//ע����Ч������
			SoundManager.release();
			//ע��������
			LoadProgress.getLoadProgress().destroy();
			//ע���Լ�
			manager = null;
		} catch (Exception e) {
			Log.doLogForException(e);
		}
	}

	/**
	 * ��ʼ��Ϸ
	 */
	private void beginGame(){
		//��ʼ
		GamePartManager.getManager().start();
	}

	/**
     * ��ʼ��������Ч
     */
	private void initSound(){
		GamingInfo.getGamingInfo().setSoundManager(SoundManager.getSoundManager());

	}
}
