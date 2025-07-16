package com.fish.threads;

import java.util.ArrayList;

import com.fish.canvas.Matrix;
import com.fish.src.Constant;
import com.fish.manager.CatchFishManager;
import com.fish.manager.ParticleEffectManager;
import com.fish.model.Ammo;
import com.fish.model.AmmoParticleEffect;
import com.fish.model.GamingInfo;
import com.fish.model.Fish;
import com.fish.tools.Log;
import com.fish.tools.Tool;
/**
 * �ӵ������߳�
 * */
public class ShotThread extends Thread {
	private float targetX;
	private float targetY;
	private float currentX;
	private float currentY;
	private float ammoRotateX;
	private float ammoRotateY;
	private float speed_x; 														// ȡһ������ֵ������ÿ֡�ƶ���������
	private float speed_y;
	private int ammo_speed = 1000 / Constant.ON_DRAW_SLEEP; 					// �ӵ������ٶ�,�������Ļˢ���ٶ�һ��
	private Ammo ammo;															//�ӵ�
	private boolean ammoActIsRun;												//�ӵ������Ƿ񲥷�

	public ShotThread(float targetX, float targetY, Ammo ammo,float fromX,float fromY) {
		this.ammo = ammo;
		currentX = fromX;
		currentY = fromY;
		ammoRotateX = ammo.getPicWidth()/2;
		ammoRotateY = ammo.getPicHeight()/2;
		this.targetX = targetX;
		this.targetY = targetY;
		float x = Math.abs(this.targetX - fromX); 				// ��ȡĿ������ӵ�ʼ����X���곤��
		float y = Math.abs(this.targetY - fromY); 				// ��ȡĿ������ӵ�ʼ����Y���곤��
		float len = (float) Math.sqrt(x * x + y * y); 							// Ŀ���ʼ����֮��ľ���
		float time = len / (Constant.AMMO_SPEED / Constant.ON_DRAW_SLEEP); 		// ����Ŀ����ʼ��֮���ӵ���Ҫ���ߵ�֡��
		speed_x = x / time; 													// �����ӵ���X���н�������
		speed_y = y / time; 													// �����ӵ���Y���н�������
		if (targetX < fromX) {
			speed_x = -speed_x;
		}
		if (targetY < fromY) {
			speed_y = -speed_y;
		}
	}

	public void run() {
		try{
			//����ӵ�֡������1���Ͳ����ӵ�����
			if(ammo.getAmmoPicLenght()>1){
				new Thread(this.playAmmoAct()).start();
			}
			// �����ӵ���Ҫ����ת�Ƕ�
			float angle = Tool.getAngle(targetX, targetY, currentX, currentY);
			AmmoParticleEffect effect = ParticleEffectManager.getParticleEffectManager().getAmmoEffect();
			int ammoRedius = ammo.getPicHeight()/2;//����뾶�����������ڼ����ӵ�β�ʹ���������ʹ��
			effect.playEffect((float)(ammoRedius*Math.cos(Math.toRadians(angle+180)))+ammoRotateX,-(float)(ammoRedius*Math.sin(Math.toRadians(angle+180)))+ammoRotateY,currentX, currentY, speed_x, speed_y);
			// �����ӵ�����ת��ԭ�������һ����
			if (angle >= 90) {
				angle = -(angle - 90);
			} else {
				angle = 90 - angle;
			}
			// �����任����
			Matrix matrix = ammo.getPicMatrix();
			matrix.setTranslate(currentX, currentY);
			matrix.preRotate(angle,ammoRotateX,ammoRotateY);
			GamingInfo.getGamingInfo().getSurface()
			.putDrawablePic(Constant.AMMO_LAYER, ammo); 				// ���ӵ�����ͼ�㣬�ȴ�������
			// ���������ƶ��ӵ�
			while (GamingInfo.getGamingInfo().isGaming()) {
				while(!GamingInfo.getGamingInfo().isPause()){
					matrix.reset();
					matrix.setTranslate(currentX, currentY);
					matrix.preRotate(angle,ammoRotateX,ammoRotateY);
					currentX += speed_x;
					currentY += speed_y;
					effect.setEffectMatrix(currentX,currentY);
					if (checkHit()) {
						effect.stopEffect();
						// ���к�ɾ������ӵ�
						GamingInfo.getGamingInfo().getSurface()
								.removeDrawablePic(Constant.AMMO_LAYER, ammo);
						CatchFishManager.getCatchFishManager().catchFishByAmmo(currentX, currentY, ammo);
						// ���������Ļ����ͼ����ɾ���ӵ�
						GamingInfo.getGamingInfo().getSurface()
								.removeDrawablePic(Constant.AMMO_LAYER, ammo);
						this.ammoActIsRun = false;//ֹͣ�ӵ�����
						break;
					} else if (currentX - 100 >= GamingInfo.getGamingInfo().getScreenWidth()
							|| currentX + 100 <= 0 || currentY + 100 <= 0) {
						// ���������Ļ����ͼ����ɾ���ӵ�
						effect.stopEffect();
						GamingInfo.getGamingInfo().getSurface()
								.removeDrawablePic(Constant.AMMO_LAYER, ammo);
						this.ammoActIsRun = false;//ֹͣ�ӵ�����
						break;
					}
					try {
						Thread.sleep(ammo_speed);
					} catch (Exception e) {

					}
				}
				break;
			}
		}catch(Exception e){
			Log.doLogForException(e);
		}
	}

	private Runnable playAmmoAct(){
		Runnable runnable = new Runnable(){
			@Override
			public void run() {
				ammoActIsRun = true;
				int picIndex = 0;
				try {
					while(GamingInfo.getGamingInfo().isGaming()){
						while(!GamingInfo.getGamingInfo().isPause()&&ammoActIsRun){
							ammo.setCurrentId(picIndex);
							picIndex++;
							if(picIndex==ammo.getAmmoPicLenght()){
								picIndex=0;
							}
							Thread.sleep(200);
						}
						break;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		};
		return runnable;
	}

	private boolean checkHit() {
		try{
			ArrayList<Fish> allFish = (ArrayList<Fish>)GamingInfo.getGamingInfo().getFish().clone();
			for (Fish fish : allFish) {
				if (currentX > fish.getFishOutlinePoint()[0]
						&& currentX < fish.getFishOutlinePoint()[1]
						&& currentY > fish.getFishOutlinePoint()[2]
						&& currentY < fish.getFishOutlinePoint()[3]) {
					return true;
				}
			}
		}catch(Exception e){
			Log.doLogForException(e);
		}
		return false;
	}
}
