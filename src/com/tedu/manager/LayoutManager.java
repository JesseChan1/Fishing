package com.fish.manager;

import java.util.ArrayList;

import com.fish.src.Constant;
import com.fish.model.GamingInfo;
import com.fish.model.componets.Bottom;
import com.fish.model.componets.BottomGold;
import com.fish.model.componets.BottomTime;
import com.fish.model.componets.BottomLevel;
import com.fish.model.componets.BottomTarget;
import com.fish.model.componets.ButtonAdapter;
import com.fish.model.componets.Cannon;
import com.fish.model.componets.DownCannonButtonListener;
import com.fish.model.componets.UpCannonButtonListener;
import com.fish.model.interfaces.Button;
import com.fish.tools.Log;

/**
 * ���ֹ�����
 * @author Xiloerfan
 *
 */
public class LayoutManager {
	private static LayoutManager manager;
	/**
	 *	���а�ť
	 */
	private ArrayList<com.fish.manager.LayoutInfo> allButton = new ArrayList<com.fish.manager.LayoutInfo>();
	/**
	 * �Ʒְ壬����Ǹ�ScoreManagerʹ�õ�
	 */
	private BottomGold bottomGold;
	/**
	 * ��ʱ�壬����Ǹ����ӽ��ʹ�õ�
	 */
	private BottomTime bottomTime;
	/**
	 * ��ؿ��壬����ǹؿ�����ʾ
	 */
	private BottomLevel bottomLevel;
	/**
	 * ��ؿ��Ŀ��ּ��壬����ǹؿ��Ŀ���ʾ
	 */
	private BottomTarget bottomTarget;
	/**
	 * ��ǰʹ�õĴ���
	 */
	private Cannon cannon;
	public void initCannon(Cannon cannon){
		try{
			this.cannon = cannon;
			if(GamingInfo.getGamingInfo().getSurface()!=null){
				GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.GUN_LAYER, cannon);
			}
		}catch(Exception e){
			Log.doLogForException(e);
		}
	}
	public void updateCannon(final Cannon newCannon){
		new Thread(new Runnable() {
			public void run() {
				try {
					float scale;
					float pos;
					for(int i=5;i>=1;i--){
						scale = i*0.2f;
						pos = (5-i)*0.2f;
						cannon.getPicMatrix().setTranslate(cannon.getX()+cannon.getPicWidth()/2*pos, cannon.getY()+cannon.getPicHeight()/2*pos);
						cannon.getPicMatrix().preScale(scale, scale);
						Thread.sleep(60);
					}
					GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Constant.GUN_LAYER, cannon);
					cannon = newCannon;
					GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.GUN_LAYER, cannon);
					for(int i=1;i<=5;i++){
						scale = i*0.2f;
						pos = (5-i)*0.2f;
						cannon.getPicMatrix().setTranslate(cannon.getX()+cannon.getPicWidth()/2*pos, cannon.getY()+cannon.getPicHeight()/2*pos);
						cannon.getPicMatrix().preScale(scale, scale);
						Thread.sleep(60);
					}
				} catch (Exception e) {
					Log.doLogForException(e);
				}
			}
		}).start();

	}
	/**
	 * ��ȡ�Ʒְ�
	 * @return
	 */
	public BottomGold getBottomGold() {
		return bottomGold;
	}
	/**
	 * ��ȡ��ʱ��
	 * @return
	 */
	public BottomTime getBottomTime() {
		return bottomTime;
	}
	/**
	 * ����һ����ť
	 * @param button
	 */
	public void addButton(ButtonAdapter button,float x,float y){
		button.getPicMatrix().setTranslate(x, y);
		button.setLayoutX(x);
		button.setLayoutY(y);
		allButton.add(new com.fish.manager.LayoutInfo(button,x,y));

	}
	/**
	 *  ��Ӧ�����¼�
	 * @param x	������x��
	 * @param y ������y��
	 * @return
	 */
	public boolean onClick(float x,float y){
		for(com.fish.manager.LayoutInfo button:allButton){
			if(x>=button.getLayout_x()&&x<=button.getLayout_x()+button.getDrawable().getPicWidth()
					&&y>=button.getLayout_y()&&y<=button.getLayout_y()+button.getDrawable().getPicHeight()){
				/**
				 * ��������ǰ�ť��͸��λ�ã�����ԡ�����Ϊ����˰�ť
				 */
				if(button.getDrawable().getCurrentPic().getPixel((int)(x-button.getLayout_x()), (int)(y-button.getLayout_y()))!=0x00000000){
					((Button)button.getDrawable()).onClick();
					return true;
				}
			}
		}
		return false;
	}

	private LayoutManager(){}
	/**
	 * ��ȡһ�����ֹ�����,����
	 * @return
	 */
	public static LayoutManager getLayoutManager(){
		if(manager == null){
			manager = new LayoutManager();
		}
		return manager;
	}
	/**
	 * ��ʼ������
	 * ������ʼ���ı���ڵİ�ť���Ƿְ壬��ͣ��ť��
	 */
	public void init(){
		try {
			//��ʼ�����ڵ���
			Bottom bottom = new Bottom();
			GamingInfo.getGamingInfo().setCannonLayoutX(bottom.getLayoutX()+bottom.getPicWidth()/2);
			GamingInfo.getGamingInfo().setCannonLayoutY(bottom.getLayoutY()+bottom.getPicHeight()/2);
			GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, bottom);
			//��ʼ����ߴ��������İ�ť
			ButtonAdapter upCannon = new ButtonAdapter(com.fish.manager.ImageManager.getImageMnagaer().getscaleImageByScreenFromAssets("cannon/add.png"),new UpCannonButtonListener());
			addButton(upCannon,bottom.getLayoutX()+bottom.getPicWidth(),750-upCannon.getPicHeight());
			GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, upCannon);
			//��ʼ�����ʹ��������İ�ť
			ButtonAdapter downCannon = new ButtonAdapter(com.fish.manager.ImageManager.getImageMnagaer().getscaleImageByScreenFromAssets("cannon/sub.png"),new DownCannonButtonListener());
			addButton(downCannon,bottom.getLayoutX()-downCannon.getPicWidth(),750-downCannon.getPicHeight());
			GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, downCannon);
			//��ʼ���Ʒְ�
			//�Ʒְ�����������������ť�ұ���Ļ����1/30,1/3��ť�߶ȵ�λ��
			bottomGold = new BottomGold((int)(upCannon.getLayoutX()+upCannon.getPicWidth()+GamingInfo.getGamingInfo().getScreenWidth()/30),(int)upCannon.getLayoutY()+upCannon.getPicHeight()/3);
			GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, bottomGold);
			//��ʼ���Ʒְ�
			//�Ʒְ��ڽ��ʹ���������ť��߱���Ļ����1/30���������,1/3��ť�߶ȵ�λ��
			bottomTime = new BottomTime();
			bottomTime.setPosition((int)(downCannon.getLayoutX()-GamingInfo.getGamingInfo().getScreenWidth()/30-bottomTime.getPicWidth()),(int)downCannon.getLayoutY()+downCannon.getPicHeight()/3);
			GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, bottomTime);
			
			bottomLevel = new BottomLevel();
			bottomLevel.setPosition((int)(bottomTime.getLayoutX()-GamingInfo.getGamingInfo().getScreenWidth()/30-bottomLevel.getPicWidth()),(int)bottomTime.getLayoutY());
			bottomLevel.updateNumIndex();
			GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, bottomLevel);
			
			bottomTarget = new BottomTarget();
			bottomTarget.setPosition((int)(bottomGold.getLayoutX()+bottomGold.getPicWidth()+GamingInfo.getGamingInfo().getScreenWidth()/30),(int)bottomGold.getLayoutY());
			bottomTarget.updateNumIndex();
			GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.COMPONENTS_LAYER, bottomTarget);
		} catch (Exception e) {
			Log.doLogForException(e);
		}
	}
	
	/**
	 * 更新关卡和目标分数显示
	 */
	public void updateLevelAndTarget() {
		try {
			if (bottomLevel != null) {
				bottomLevel.updateNumIndex();
			}
			if (bottomTarget != null) {
				bottomTarget.updateNumIndex();
			}
		} catch (Exception e) {
			Log.doLogForException(e);
		}
	}
}
