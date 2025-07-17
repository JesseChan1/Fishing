package com.fish.manager;

import com.fish.canvas.Matrix;
import com.fish.tools.Log;
import com.fish.src.Constant;
import com.fish.model.GamingInfo;
import com.fish.model.Fish;
import com.fish.threads.FishRunThread;
import com.fish.threads.PicActThread;

/**
 * ��Ⱥ������
 * @author Xiloer
 *
 */
public class ShoalManager{
	private String[] fish;							//�����ɵ���Ⱥ
	private int[] showProbability;					//������ɸ���
	private boolean createable;						//�Ƿ��������Ⱥ

	public ShoalManager(){
	}
	/**
	 * ������Ⱥ������
	 * @param gamePartInfo
	 */
	public void start(final GamePartInfo gamePartInfo){
		this.fish = gamePartInfo.getFishName();							//���ÿ��Դ�������Ⱥ
		this.showProbability = gamePartInfo.getShowProbability();
		createable = true;												//���ÿ��Դ�����Ⱥ
		new Thread(new Runnable(){
			public void run(){
				for(int i = 0;i<gamePartInfo.getShoalSumInScreen()&&GamingInfo.getGamingInfo().isGaming();i++){
					createShoal();
					try {
						Thread.sleep((long)(Math.random()*100*gamePartInfo.getShoalSumInScreen()));
					} catch (Exception e) {
						Log.doLogForException(e);
					}
				}
			}
		}).start();
	}
	/**
	 * ����һ����Ⱥ
	 */
	public void createShoal(){
		try {
			if(!createable){
				Log.w("ShoalManager", "������������Ⱥ");
				return;
			}
			HeadFish fish = birthHeadFish();
			createShoal(fish);
			fishRun(fish);
			startFishAct(fish.getFish());
			setRandomShoalPositionByHeadFish(fish);
		} catch (Exception e) {
			Log.doLogForException(e);
		}

	}



	/**
	 * ����һ����ͷ��
	 */
	private HeadFish birthHeadFish(){
		//����һ����ͷ��
		HeadFish headFish = new HeadFish();
		Fish fish = null;
		int probability = (int)(Math.random()*100+1);
		int sumProbability = 0;
		if(probability<=showProbability[0]){
			fish = FishManager.getFishMananger().birthFishByFishName(this.fish[0]);
		}else{
			sumProbability = showProbability[0];
			for(int index=1;index<this.showProbability.length;index++){
				sumProbability += this.showProbability[index];
				if(probability<=sumProbability){
					fish = FishManager.getFishMananger().birthFishByFishName(this.fish[index]);
					break;
				}
			}
		}
		if(fish==null){
			Log.w(this.getClass().getName(), "��ͷ������Ϊ�գ�fish=null!!ԭ��:����ֵ:"+probability+"��������ʼ���:"+showProbability);
			for(int p :showProbability){
				Log.w(this.getClass().getName(), "��������ʼ���:"+p);
			}
			Log.w(this.getClass().getName(), "��������ʼ����ܺ�:"+sumProbability);
			return null;
		}
		fish.setCanRun(true);
		GamingInfo.getGamingInfo().getFish().add(fish);
		headFish.setFish(fish);
		fish.setHeadFish(headFish);
		//������һ����ͷ��󣬽��������õ�������currentFromPoint�ϣ�����Ⱥʹ��
		headFish.setCurrentFromPoint(getFromPoint(headFish));
//		this.fishRun(fish,currentFromPoint);
		return headFish;
	}
	/**
	 * ���㿪ʼ�ζ�
	 * @param fish			��Ҫ�ζ�����
	 */
	private void fishRun(HeadFish fish){
		FishRunThread prt = new FishRunThread(fish);	// ������ǰ����ƶ��߳�
		fish.setFishRunThread(prt);
		prt.setRun(true);	//�����㿪ʼ�ƶ�
		prt.start();
	}
	/**
	 * ��ʼ������Ķ���
	 * @param fish			��Ҫ�ζ�����
	 */
	private void startFishAct(Fish fish){
		//����Ⱥ�������ͼ��
		GamingInfo.getGamingInfo().getSurface().putDrawablePic(fish.getFishInfo().getFishInLayer(), fish);
		PicActThread pat = new PicActThread(fish); 				// ������ǰ��Ķ����߳�
		fish.setPicActThread(pat);
		pat.start();// ��Ķ��������߳�����
	}
	/**
	 * ������Ⱥ
	 * @param fish	��ͷ��
	 */
	private void createShoal(HeadFish fish){
		try{
			if(GamingInfo.getGamingInfo().isGaming()&&fish.getFish().getFishInfo().getFishShoalMax()!=0){
				Fish flagFish;					//������Ⱥ�е������ʱ����
				int sum = (int)(Math.random()*fish.getFish().getFishInfo().getFishShoalMax()+1);
				for(int i = 0;i<sum;i++){
					flagFish = fish.getFish().getFish();
					flagFish.setHeadFish(fish);
					fish.getShoal().add(flagFish);
					GamingInfo.getGamingInfo().getFish().add(flagFish);
				}
			}
		}catch(Exception e){
			Log.doLogForException(e);
		}
	}
	/**
	 * ������ͷ���λ���������λ��,�������λ�õ�ƫ������ֵ��������Ⱥ
	 * @param headFish			��ͷ��
	 */
	private void setRandomShoalPositionByHeadFish(final HeadFish headFish){
		try{
		new Thread(new Runnable(){
			public void run() {
				try{
				float fromY = headFish.getFish_Y();
				int shoalIndex = 1;
				int sumAtOut = headFish.getShoal().size();//��Ⱥ�л��ж�����������Ļ��
				Fish fishFlag;
				float randomMinY;
				float randomMaxY;
				float flagFishX,flagFishY;//���ɵ��������
				Fish startFish = headFish.getFish();
				// TODO Auto-generated method stub
				while(GamingInfo.getGamingInfo().isGaming()){
					while(!GamingInfo.getGamingInfo().isPause()&&sumAtOut>1){
						fishFlag = headFish.getShoal().get(shoalIndex);
						randomMinY = (float)(fromY-fishFlag.getPicHeight());
						randomMaxY = (float)(fromY+fishFlag.getPicHeight());
						if(headFish.getCurrentFromPoint()<=Constant.FROM_BOTTOM_LEFT){
							flagFishX = 0-headFish.getFish().getPicWidth();
							flagFishY = (float)(randomMinY+Math.random()*(randomMaxY-randomMinY));

						}else{
							flagFishX = GamingInfo.getGamingInfo().getScreenWidth();
							flagFishY = (float)(randomMinY+Math.random()*(randomMaxY-randomMinY));

						}
						while(!canRun(startFish,flagFishX,flagFishY,headFish)&&GamingInfo.getGamingInfo().isGaming()){
							try{
								Thread.sleep(50);
							}catch(Exception e){

							}
						}
						fishFlag.setDistanceHeadFishX(headFish.getFish_X()-flagFishX);
						fishFlag.setDistanceHeadFishY(headFish.getFish_Y()-flagFishY);
						fishFlag.setCanRun(true);
						startFishAct(fishFlag);
						startFish = fishFlag;
						shoalIndex++;
						sumAtOut--;
						try{
							Thread.sleep(200);
						}catch(Exception e){

						}
					}
					break;
				}
			}catch(Exception e){}}}).start();
		}catch(Exception e){}
	}
	/**
	 * �ж����Ƿ�����ƶ���
	 * ����������Ǹ��ݲ������λ�ã�������׼���ƶ������Ƿ�����ƶ���
	 * @param firstFish		������
	 * @param fishFlag		׼���ƶ�����
	 * @param headFish		��ͷ��
	 * @return
	 */
	private boolean canRun(Fish firstFish,float fishX,float fishY,HeadFish headFish){
		//�����ʼ������Ļ���
		if(headFish.getCurrentFromPoint()<=Constant.FROM_BOTTOM_LEFT){
			if(fishY+firstFish.getPicHeight()<headFish.getFish_Y()-firstFish.getDistanceHeadFishY()
					||fishY>headFish.getFish_Y()-firstFish.getDistanceHeadFishY()+firstFish.getPicHeight()
					||headFish.getFish_X()-firstFish.getDistanceHeadFishX()>0){
				return true;
			}
		}else{
			if(fishY+firstFish.getPicHeight()<headFish.getFish_Y()-firstFish.getDistanceHeadFishY()
					||fishY>headFish.getFish_Y()-firstFish.getDistanceHeadFishY()+firstFish.getPicHeight()
					||headFish.getFish_X()-firstFish.getDistanceHeadFishX()+firstFish.getPicWidth()<GamingInfo.getGamingInfo().getScreenWidth()){
				return true;
			}
		}
		return false;
	}

	/**
	 * ֪ͨ��Ⱥ����������ͷ���Ѿ��뿪��Ļ
	 */
	public void notifyFishIsOutOfScreen(){
		try{
			createShoal();
		}catch(Exception e){
			Log.doLogForException(e);
		}
	}

	/**
	 * ���������ʼ���� ����Ļ�ֳ����ĸ���λ
	 */
	private int getFromPoint(HeadFish fish) {
		int mode = (int) (Math.random() * 4);
		Matrix matrix = fish.getFish().getPicMatrix();
		switch (mode) {
		case Constant.FROM_TOP_LEFT:
			// ����
			fish.setFish_X(-(float) fish.getFish().getPicWidth());
			fish.setFish_Y((float) Math.random()
					* (GamingInfo.getGamingInfo().getScreenHeight() / 2 - fish.getFish().getPicHeight() + 1));
			matrix.setTranslate(fish.getFish_X(), fish.getFish_Y());
			fish.setLastX(1);
			return Constant.FROM_TOP_LEFT;
		case Constant.FROM_BOTTOM_LEFT:
			// ����
			fish.setFish_X(-(float) fish.getFish().getPicWidth());
			fish.setFish_Y((float) (GamingInfo.getGamingInfo().getScreenHeight() / 2 + Math.random()
					* (GamingInfo.getGamingInfo().getScreenHeight() - fish.getFish().getPicHeight() + 1 - GamingInfo.getGamingInfo().getScreenHeight() / 2)));
			matrix.setTranslate(fish.getFish_X(), fish.getFish_Y());
			fish.setLastX(1);
			return Constant.FROM_BOTTOM_LEFT;
		case Constant.FROM_TOP_RIGHT:
			// ����
			fish.setFish_X((float) GamingInfo.getGamingInfo().getScreenWidth());
			fish.setFish_Y((float) Math.random()
					* (GamingInfo.getGamingInfo().getScreenHeight() / 2 - fish.getFish().getPicHeight() + 1));
			matrix.setTranslate(fish.getFish_X(), fish.getFish_Y());
			matrix.preRotate(180, fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y());
			fish.setCurrentRotate(180);
			fish.setLastX(-1);
			return Constant.FROM_TOP_RIGHT;
		case Constant.FROM_BOTTOM_RIGHT:
			// ����
			fish.setFish_X((float) GamingInfo.getGamingInfo().getScreenWidth());
			fish.setFish_Y((float) (GamingInfo.getGamingInfo().getScreenHeight() / 2 + Math.random()
					* (GamingInfo.getGamingInfo().getScreenHeight() - fish.getFish().getPicHeight() + 1 - GamingInfo.getGamingInfo().getScreenHeight() / 2)));
			matrix.setTranslate(fish.getFish_X(), fish.getFish_Y());
			matrix.preRotate(180, fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y());
			fish.setCurrentRotate(180);
			fish.setLastX(-1);
			return Constant.FROM_BOTTOM_RIGHT;
		}
		return 0;
	}


	/**
	 * ������Ⱥ
	 * @param fish
	 */
	public void stop(){
		this.createable = false;
	}

}
