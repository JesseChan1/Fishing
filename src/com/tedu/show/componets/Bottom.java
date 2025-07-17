package com.fish.model.componets;

import com.fish.canvas.Bitmap;
import com.fish.tools.Log;
import com.fish.manager.ImageManager;
import com.fish.model.GamingInfo;
/**
 * ���ڵ���
 * @author Xiloerfan
 *
 */
public class Bottom extends Componet{
	private Bitmap pic;
	public Bottom(){
		try{
		pic = ImageManager.getImageMnagaer().getscaleImageByScreenFromAssets("componet/bottom.png");
		this.setLayoutX(GamingInfo.getGamingInfo().getScreenWidth()/2-getPicWidth()/2);
		this.setLayoutY(750-getPicHeight());
		this.getPicMatrix().setTranslate(this.getLayoutX(),this.getLayoutY());
		}catch(Exception e){
			Log.e("Bottom", e.toString());
		}
	}

	public Bitmap getCurrentPic() {
		return pic;
	}

	public int getPicWidth() {
		return pic.getWidth();
	}

	public int getPicHeight() {
		return pic.getHeight();
	}

}
