package com.fish.model.componets;

import java.util.HashMap;

import com.fish.canvas.Bitmap;
import com.fish.canvas.Canvas;
import com.fish.canvas.Paint;
import com.fish.manager.ImageManager;
import com.fish.manager.GamePartManager;
import com.fish.model.GamingInfo;
import com.fish.tools.Log;

public class BottomTarget extends Componet{
	private int[] num_index = new int[1];
	private Bitmap pic;
	private Bitmap[] num;
	private int numShowX,numShowY;
	private int numPicWidth;	 
	
	public BottomTarget(){
		try {
			initNum();
			pic = ImageManager.getImageMnagaer().getscaleImageByScreenFromAssets("componet/bottom_time.png");
			numPicWidth = num[0].getWidth();
		} catch (Exception e) {
			Log.doLogForException(e);
		}
	}
	
	public void setPosition(int layoutX,int layoutY){
		this.setLayoutX(layoutX);
		this.setLayoutY(layoutY);
		numShowX = layoutX+pic.getWidth()/3;
		numShowY = layoutY+pic.getHeight()/4;
		this.getPicMatrix().setTranslate(this.getLayoutX(),this.getLayoutY());
	}
	
	private void initNum(){
		HashMap<String,Bitmap> allNum = ImageManager.getImageMnagaer().getImagesMapByImageConfig(ImageManager.getImageMnagaer().createImageConfigByPlist("componet/num_gold"),ImageManager.getImageMnagaer().scaleNum);
		StringBuffer numFullName = new StringBuffer();
		String numName = "num_";
		num = new Bitmap[10];
		for(int num = 0;num<10&&GamingInfo.getGamingInfo().isGaming();num++){
			numFullName.delete(0, numFullName.length());
			numFullName.append(numName+num+".png");
			this.num[num] =  allNum.get(numFullName.toString());
		}
	}
	
	@Override
	public void onDraw(Canvas canvas, Paint paint) {
		super.onDraw(canvas, paint);
		for(int i=0;i<num_index.length;i++){
			canvas.drawBitmap(num[num_index[i]], numShowX+(i*numPicWidth), numShowY, paint);
		}
	}
	
	public void updateNumIndex(){
		int targetScore = GamePartManager.getManager().getCurrentPart().getTargetScore();
		String num = targetScore+"";
		num_index = new int[num.length()];
		int index = 0;
		for(char n:num.toCharArray()){
			num_index[index] = n-48;
			index++;
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