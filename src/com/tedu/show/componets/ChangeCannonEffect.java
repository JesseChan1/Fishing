package com.fish.model.componets;

import com.fish.canvas.Bitmap;
import com.fish.tools.Log;
import com.fish.src.Constant;
import com.fish.model.DrawableAdapter;
import com.fish.model.GamingInfo;

/**
 * ��������ʱ�ı任Ч��
 * @author Xiloerfan
 *
 */
public class ChangeCannonEffect extends DrawableAdapter{
	private Bitmap[] effect;
	private int currentId;
	public ChangeCannonEffect(Bitmap[] effect){
		this.effect = effect;
	}
	/**
	 * ����Ч��
	 */
	public void playEffect(){
		this.getPicMatrix().setTranslate(GamingInfo.getGamingInfo().getCannonLayoutX()-this.getPicWidth()/2, GamingInfo.getGamingInfo().getCannonLayoutY()-this.getPicHeight()/2);
		GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.CHANGE_CANNON_EFFECT_LAYER, this);
		for(int i =0;i<effect.length;i++){
			currentId = i;
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("ChangeCannonEffect", e.toString());
			}
		}
		GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Constant.CHANGE_CANNON_EFFECT_LAYER, this);
	}

	@Override
	public Bitmap getCurrentPic() {
		// TODO Auto-generated method stub
		return effect[currentId];
	}
	@Override
	public int getPicWidth() {
		// TODO Auto-generated method stub
		return effect[currentId].getWidth();
	}
	@Override
	public int getPicHeight() {
		// TODO Auto-generated method stub
		return effect[currentId].getHeight();
	}

}
