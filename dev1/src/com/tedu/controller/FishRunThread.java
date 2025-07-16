package com.fish.threads;

import com.fish.src.Constant;
import com.fish.manager.HeadFish;
import com.fish.manager.PathManager;
import com.fish.model.GamingInfo;
import com.fish.model.Fish;
import com.fish.tools.Log;

public class FishRunThread extends Thread {
	private boolean isRun; 												// ���Ƿ�����ζ�
	private HeadFish fish; 												// ��ǰ�߳���Ҫ���Ƶ���ͷ��
	private boolean fishIsOut;											// ���Ƿ�����Ļ��
	private int fish_speed = 1000/Constant.ON_DRAW_SLEEP;				// ������ٶ�,�������Ļˢ���ٶ�һ��
	private float fishrunSpeed;										// ����ת����
	public FishRunThread(HeadFish fish) {
		super();
		this.fish = fish;
		fishrunSpeed = (fish.getFish().getFishInfo().getFishRunSpeed() / Constant.ON_DRAW_SLEEP);//����ֻ�Ǹ����ٶ�����һ������������ת�ٶȿ������Ƚ�����
	}

	public void run() {
		int[][] path = PathManager.getDefaultPath(fish);
		while (GamingInfo.getGamingInfo().isGaming()) {
			while(isRun &&!GamingInfo.getGamingInfo().isPause()){
				for (int[] pathMode : path) {
					if(fishIsOut||!isRun){
						break;
					}
					// ���·��Ϊ��תģʽ
					if (pathMode[0] == PathManager.PATH_MODE_ROTATE) {
						/**
						 * ��������һ���������Ƿ�����ͷ���������λ��������в�ͬ����ת·��
						 */
						// ����㴦�ڵ�1���2����
						if (fish.getFish_X() <= GamingInfo.getGamingInfo().getScreenWidth() / 2
								&& fish.getFish_Y() <= GamingInfo.getGamingInfo().getScreenHeight() / 2
								|| fish.getFish_X() > GamingInfo.getGamingInfo().getScreenWidth() / 2
								&& fish.getFish_Y() <= GamingInfo.getGamingInfo().getScreenHeight() / 2) {
							// �����ͷ�ǳ���x�ĸ����귽��
							if (fish.getCurrentRotate() >= 90
									&& fish.getCurrentRotate() <= 270
									|| fish.getCurrentRotate() <= -90
									&& fish.getCurrentRotate() >= -270) {
								rotateLeftFish(pathMode[1]);
							} else {
								rotateRightFish(pathMode[1]);
							}
							// ����㴦�ڵ�3���4����
						} else {
							// �����ͷ�ǳ���x�ĸ����귽��
							if (fish.getCurrentRotate() >= 90
									&& fish.getCurrentRotate() <= 270
									|| fish.getCurrentRotate() <= -90
									&& fish.getCurrentRotate() >= -270) {
								rotateRightFish(pathMode[1]);
							} else {
								rotateLeftFish(pathMode[1]);
							}
						}
						// ���·��Ϊֱ��ģʽ
					} else {
						goStraight(pathMode[1]);
					}
				}
				if(!fishIsOut){
				// ���������е�·�������»�ȡ��·��
				path = PathManager.getDefaultPath(fish);
				}else{
					while(isRun && GamingInfo.getGamingInfo().isGaming()){
						//���������Ļ����һ��ֱ��
						goStraight(100);
					}

				}
			}
			break;
		}
	}
	/**
	 * �ƶ���Ⱥ
	 */
	private void moveShoal(){
		try{
			if(fish.getShoal()==null){
				return;
			}
			for(Fish fishFlag:fish.getShoal()){
				if(!fishFlag.isCanRun()||!fishFlag.isAlive()){
					continue;
				}
				fishFlag.getFishOutlinePoint()[0] = (int)(fish.getFishOutlinePoint()[0]-fishFlag.getDistanceHeadFishX());
				fishFlag.getFishOutlinePoint()[1] = (int)(fish.getFishOutlinePoint()[1]-fishFlag.getDistanceHeadFishX());
				fishFlag.getFishOutlinePoint()[2] = (int)(fish.getFishOutlinePoint()[2]-fishFlag.getDistanceHeadFishY());
				fishFlag.getFishOutlinePoint()[3] = (int)(fish.getFishOutlinePoint()[3]-fishFlag.getDistanceHeadFishY());
				fishFlag.getPicMatrix().setTranslate(fish.getFish_X()-fishFlag.getDistanceHeadFishX(), fish.getFish_Y()-fishFlag.getDistanceHeadFishY());
				fishFlag.getPicMatrix().preRotate(-fish.getCurrentRotate(),fishFlag.getFishRotatePoint_X(),fishFlag.getFishRotatePoint_Y());
			}
		}catch(Exception e){

		}
	}
	/**
	 * ���ݸ���������ֱ��
	 *
	 * @param len
	 * @return true:������Ļ
	 */
	private void goStraight(int len) {
		// ������³������㣬��ֱ�ߣ�������ת�Ƕ�
		if (fish.isNew()) {
			fish.setNew(false);
			while (GamingInfo.getGamingInfo().isGaming()) {
				while(isRun &&!GamingInfo.getGamingInfo().isPause()){
					setFishOutlintPoint(0);
					if (fish.getCurrentFromPoint() <= 1) {
						fish.setFish_X(fish.getFish_X() + fishrunSpeed);
					} else {
						fish.setFish_X(fish.getFish_X() - fishrunSpeed);
					}
					moveShoal();
					// �Ƿ��߳�����Ļ
					if (isAtOut()&&!fishIsOut) {
						setFishAtOut();
						break;
					}
					// �߹��˳��ȣ���ֹͣѭ��
					if ((len-=fishrunSpeed) <= 0) {
						break;
					}
					try {
						Thread.sleep(fish_speed);
					} catch (Exception e) {

					}
				}
				break;
			}
		} else {
			fishRun(0,len);
		}
	}

	/**
	 * ����������תangle�Ƕȣ����ƶ���
	 *
	 * @param angle
	 */
	private void rotateLeftFish(int angle) {
		fishRun(1,angle);
	}

	/**
	 * ����������תangle�Ƕȣ����ƶ���
	 *
	 * @param angle
	 */
	private void rotateRightFish(int angle) {
		fishRun(-1,angle);
	}
	/**
	 * ����ж�����
	 * @param mode	1:��ת  -1����ת   0:ֱ��
	 * @param len   �ƶ�����
	 */
	private void fishRun(int mode,int len){
		int sumLen = 0;
		while (GamingInfo.getGamingInfo().isGaming()) {
			while(isRun &&!GamingInfo.getGamingInfo().isPause()){
				// ���������Ӿ���
				setFishOutlintPoint(fish.getCurrentRotate());
				// ���õ�ǰ��ת�Ƕ�
				fish.setCurrentRotate(fish.getCurrentRotate() + mode);
				// ���������任����
				fish.setFish_X(fish.getFish_X()
						+ (float) (fishrunSpeed*Math.cos(Math.toRadians(fish.getCurrentRotate()))));
				fish.setFish_Y(fish.getFish_Y()
						+ (float) (fishrunSpeed*Math.sin((Math.toRadians(fish.getCurrentRotate())))*-1));
				moveShoal();
				// �Ƿ��߳�����Ļ
				if (isAtOut()&&!fishIsOut) {
					setFishAtOut();
					break;
				}
				// ����ƶ����ܳ��ȣ�ֹͣѭ��
				if (++sumLen == len) {
					break;
				}
				try {
					Thread.sleep(fish_speed);
				} catch (Exception e) {

				}
			}
			break;
		}
	}


	/**
	 * ���������Ӿ���X,Y�����ֵ����Сֵ
	 *
	 * @param rotateAngle
	 *            �����Ǹ��������������ת�ĽǶ����õ���Ӧ��X,Yֵ��
	 */
	private void setFishOutlintPoint(int rotateAngle) {
		fish.clearFishOutlinePoint();// �����һ�εķ�Χֵ
		int flagX = 0, flagY = 0;
		// �������Ͻ�����
		fish.getFishOutlinePoint()[0] = (int) getRotateX(0, 0,
				fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y(), rotateAngle)
				+ (int) fish.getFish_X();
		fish.getFishOutlinePoint()[1] = fish.getFishOutlinePoint()[0];
		fish.getFishOutlinePoint()[2] = (int) getRotateY(0, 0,
				fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y(), rotateAngle)
				+ (int) fish.getFish_Y();
		fish.getFishOutlinePoint()[3] = fish.getFishOutlinePoint()[2];
		// �������½�����
		flagX = (int) getRotateX(0, fish.getFish().getPicHeight(),
				fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y(), rotateAngle);
		flagY = (int) getRotateY(0, fish.getFish().getPicHeight(),
				fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y(), rotateAngle);
		putFishOutline((int) (flagX + fish.getFish_X()),
				(int) (flagY + fish.getFish_Y()));
		// �������Ͻ�����
		flagX = (int) getRotateX(fish.getFish().getPicWidth(), 0, fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y(), rotateAngle);
		flagY = (int) getRotateY(fish.getFish().getPicWidth(), 0, fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y(), rotateAngle);
		putFishOutline((int) (flagX + fish.getFish_X()),
				(int) (flagY + fish.getFish_Y()));
		// �������½�����
		flagX = (int) getRotateX(fish.getFish().getPicWidth(), fish.getFish().getPicHeight(),
				fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y(), rotateAngle);
		flagY = (int) getRotateY(fish.getFish().getPicWidth(), fish.getFish().getPicHeight(),
				fish.getFish().getFishRotatePoint_X(), fish.getFish().getFishRotatePoint_Y(), rotateAngle);
		putFishOutline((int) (flagX + fish.getFish_X()),
				(int) (flagY + fish.getFish_Y()));
	}

	// ֻ���ĸ���Χ�ڵ����ֵ����Сֵ����ȥ
	private void putFishOutline(int x, int y) {
		if (fish.getFishOutlinePoint()[1] < x) {
			fish.getFishOutlinePoint()[1] = x;
		} else if (fish.getFishOutlinePoint()[0] > x) {
			fish.getFishOutlinePoint()[0] = x;
		}
		if (fish.getFishOutlinePoint()[3] < y) {
			fish.getFishOutlinePoint()[3] = y;
		} else if (fish.getFishOutlinePoint()[2] > y) {
			fish.getFishOutlinePoint()[2] = y;
		}
	}

	/**
	 * ���ظ�����ת�Ƕ��Լ���ת�����Ҫ����ת������תǰX����
	 *
	 * @param x
	 * @param y
	 * @param rotatePoint_x
	 * @param rotatePoint_y
	 * @param angle
	 * @return
	 */
	private float getRotateX(float x, float y, float rotatePoint_x,
			float rotatePoint_y, float angle) {
		return (float) ((x - rotatePoint_x) * Math.cos(angle)
				+ (y - rotatePoint_y) * Math.sin(angle) + rotatePoint_x);
	}

	/**
	 * ���ظ�����ת�Ƕ��Լ���ת�����Ҫ����ת������תǰY����
	 *
	 * @param x
	 * @param y
	 * @param rotatePoint_x
	 * @param rotatePoint_y
	 * @param angle
	 * @return
	 */
	private float getRotateY(float x, float y, float rotatePoint_x,
			float rotatePoint_y, float angle) {
		return (float) ((y - rotatePoint_y) * Math.cos(angle)
				- (x - rotatePoint_x) * Math.sin(angle) + rotatePoint_y);
	}

	/**
	 * ������Ƿ��Ѿ�������Ļ��
	 *
	 * @return true:������Ļ��
	 */
	private boolean isAtOut() {
		// ��������Ӿ��ε��ϱ�Ե������Ļ�߶Ȼ����±�ԵС��0���϶��㴦����Ļ�»��ϱ�Ե��
		if (fish.getFishOutlinePoint()[2] > GamingInfo.getGamingInfo().getScreenHeight()
				|| fish.getFishOutlinePoint()[3] < 0) {
			return true;

			// �����ͷ�ǳ���x�ĸ����귽��
		} else if (fish.getCurrentRotate() >= 90
				&& fish.getCurrentRotate() <= 270
				|| fish.getCurrentRotate() <= -90
				&& fish.getCurrentRotate() >= -270) {
			// ����㴦����Ļ�����
			if (fish.getFishOutlinePoint()[1] < 0) {
				return true;
			}
		} else {
			// ����㴦����Ļ�ұ���
			if (fish.getFishOutlinePoint()[0] > GamingInfo.getGamingInfo().getScreenWidth()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ��������˱߽��Ĳ���
	 */
	private void setFishAtOut() {
			fishIsOut = true;
			new Thread(new Runnable(){
				public void run() {
					try{
						// TODO Auto-generated method stub
						//�����ͷ������Ⱥ
						for(Fish fishFlag:fish.getShoal()){
							while(GamingInfo.getGamingInfo().isGaming()){
								if(checkFishAtOut(fish,fishFlag)){
									GamingInfo.getGamingInfo().getFish().remove(fishFlag);
									GamingInfo.getGamingInfo().getSurface().removeDrawablePic(fishFlag.getFishInfo().getFishInLayer(), fishFlag);
									fishFlag.getPicActThread().stopPlay();//ֹͣ����
									break;
								}
								try{
									Thread.sleep(10);
								}catch(Exception e){
									e.printStackTrace();
								}
							}
						}
						//����Ⱥ�ƶ��߳�ͣ��
						setRun(false);
						//֪ͨ��Ⱥ���������������Ѿ��뿪��Ļ
						if(GamingInfo.getGamingInfo().isGaming()){
							GamingInfo.getGamingInfo().getShoalManager().notifyFishIsOutOfScreen();
						}
					}catch(Exception e){
						Log.doLogForException(e);
					}
				}

			}).start();
	}
	/**
	 * ��鵱ǰ���Ƿ��Ѿ�������Ļ
	 * @param fish
	 * @return		true:�Ѿ�������Ļ
	 */
	private boolean checkFishAtOut(HeadFish headFish,Fish fish){
		if(headFish.getFish_X()-fish.getDistanceHeadFishX()+fish.getPicWidth()<0||headFish.getFish_X()-fish.getDistanceHeadFishX()>GamingInfo.getGamingInfo().getScreenWidth()
				||headFish.getFish_Y()-fish.getDistanceHeadFishY()+fish.getPicHeight()<0||headFish.getFish_Y()-fish.getDistanceHeadFishY()>GamingInfo.getGamingInfo().getScreenHeight()){
			return true;
		}
		return false;
	}
	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
//		//������õ���false,��ʾ����ֹͣ���߳�
//		if(!isRun){
//			//���ö����߳�ֹͣ
//			this.fish.getPicActThread().stopPlay();
//		}
	}
}
