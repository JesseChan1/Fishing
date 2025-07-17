package com.fish.model.componets;

import com.fish.canvas.Bitmap;
import com.fish.tools.Log;
import com.fish.model.DrawableAdapter;
import com.fish.model.GamingInfo;

/**
 * �������д��ڵ�ģ����
 * @author Xiloerfan
 *
 */
public class Cannon extends DrawableAdapter{
	private Bitmap[] cannonImage;
	private int currentId;
	//���ڵ���ת������
	private int gun_rotate_point_x;
	private int gun_rotate_point_y;
	//���ڵ�λ��
	private float x;
	private float y;

	public Cannon(Bitmap[] cannonImage){
		this.cannonImage = cannonImage;
	}
	public void init(){
		gun_rotate_point_x = this.getPicWidth()/2;
		gun_rotate_point_y = this.getPicHeight()/2;
		x = GamingInfo.getGamingInfo().getCannonLayoutX()-gun_rotate_point_x;
		y = GamingInfo.getGamingInfo().getCannonLayoutY()-gun_rotate_point_y;
		this.getPicMatrix().setTranslate(x, y);
	}

	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public int getGun_rotate_point_x() {
		return gun_rotate_point_x;
	}

	public int getGun_rotate_point_y() {
		return gun_rotate_point_y;
	}
	/**
	 * ���ŷ����ڵ��Ķ���
	 */
	public void shot(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// ��ΪĬ�Ͼ��ǵ�һ��ͼ�����Դӵڶ���ͼ��ʼ����
				for(int i =1;i<cannonImage.length;i++){
					try {
						currentId = i;
						Thread.sleep(300);
					} catch (Exception e) {
						Log.e("Cannon", e.toString());
					}
				}
				//���Ҫ�ָ���һ��ͼ������
				currentId = 0;
			}
		}).start();
	}


	@Override
	public Bitmap getCurrentPic() {
		// TODO Auto-generated method stub
		return cannonImage[currentId];
	}
	@Override
	public int getPicWidth() {
		// TODO Auto-generated method stub
		return cannonImage[currentId].getWidth();
	}
	@Override
	public int getPicHeight() {
		// TODO Auto-generated method stub
		return cannonImage[currentId].getHeight();
	}
}
