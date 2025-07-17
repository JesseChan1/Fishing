package com.fish.model;

import java.util.ArrayList;

import com.fish.canvas.Bitmap;
import com.fish.canvas.Canvas;
import com.fish.canvas.Matrix;
import com.fish.canvas.Paint;
import com.fish.canvas.JMatrix;
import com.fish.src.Constant;
import com.fish.tools.Log;
/**
 * ��������Ч��
 * @author Xiloer
 *
 */
public class NetParticleEffect extends DrawableAdapter{
	private static byte ADD = 1;
	private static byte REMOVE = 2;
	private static byte UPDATE = 3;
	//���Ӳ�ɫͼ
	private Bitmap effectImgs[];
	private ArrayList<Particle> effects = new ArrayList<Particle>();
	private ArrayList<Particle> news =  new ArrayList<Particle>();
	private ArrayList<Particle> removes =  new ArrayList<Particle>();
	private int indexByDraw;//���ֵ���ڻ��Ʒ���ѭ��ʹ��
	private Particle particle;//���ֵ���ڻ��Ʒ���ѭ��ʹ��
	private boolean isPlay;//�Ƿ񲥷�����Ч��
	public NetParticleEffect(Bitmap effectImgs[]){
		this.effectImgs = effectImgs;
	}

	/**
	 * ����һ������Ч��
	 * @param x				���ӵ�����λ��X
	 * @param y				���ӵ�����λ��Y
	 * @param level			���ӵȼ�
	 */
	public void playEffect(float x,float y,int level){
		try {
			isPlay = true;
			startCreateEffectThread(x,y,level);
			startSetEffectThread();
			GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.PARTICLE_EFFECT_LAYER, this);
		} catch (Exception e) {
			Log.doLogForException(e);
		}
	}
	private void updateEffect(byte mode,Particle p){
		if(mode==ADD){
			news.add(p);
		}else if(mode==REMOVE){
			removes.add(p);
		}else if(mode == UPDATE){
			if(news.size()>0){
				effects.addAll(news);
				news.clear();
			}
			if(removes.size()>0){
				effects.removeAll(removes);
				removes.clear();
			}
		}
	}
	private void startSetEffectThread(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					while(GamingInfo.getGamingInfo().isGaming()){
						while(!GamingInfo.getGamingInfo().isPause()&&isPlay){
							setEffectMatrix();
							Thread.sleep(50);
						}
						break;
					}
				}catch(Exception e){
					Log.doLogForException(e);
				}
			}
		}).start();
	}
	/**
	 * �����������ӵ��߳�
	 */
	private void startCreateEffectThread(final float x,final float y,final int level){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					byte sum = 0;
					float scale;
					while(GamingInfo.getGamingInfo().isGaming()&&isPlay){
						while(!GamingInfo.getGamingInfo().isPause()&&isPlay){
							scale = (float)((Math.random()*level+1)/10);
							switch(sum){
								case 0:
									updateEffect(ADD,new Particle(x,y,-(float)(Math.random()*6+1),(float)(Math.random()*6+1),scale,effectImgs[(int)(Math.random()*effectImgs.length)]));
									break;
								case 1:
									updateEffect(ADD,new Particle(x,y,(float)(Math.random()*6+1),-(float)(Math.random()*6+1),scale,effectImgs[(int)(Math.random()*effectImgs.length)]));
									break;
								case 2:
									updateEffect(ADD,new Particle(x,y,(float)(Math.random()*6+1),(float)(Math.random()*6+1),scale,effectImgs[(int)(Math.random()*effectImgs.length)]));
									break;
								case 3:
									updateEffect(ADD,new Particle(x,y,-(float)(Math.random()*6+1),-(float)(Math.random()*6+1),scale,effectImgs[(int)(Math.random()*effectImgs.length)]));
									break;
							}
							sum++;
							if(sum>3){
								sum = 0;
							}
							Thread.sleep((long)(Math.random()*51));
						}
						break;
					}
				}catch(Exception e){
					Log.doLogForException(e);
				}
			}
		}).start();
	}
	@Override
	public void onDraw(Canvas canvas, Paint paint) {
		updateEffect(UPDATE,null);
		indexByDraw = 0;
		while(GamingInfo.getGamingInfo().isGaming()){
			while(!GamingInfo.getGamingInfo().isPause()&&isPlay&&indexByDraw<effects.size()){
				particle = effects.get(indexByDraw);
				canvas.drawBitmap(particle.effect, particle.matrix, paint);
				indexByDraw++;
			}
			break;
		}
	}
	/**
	 * ֹͣ��������
	 */
	public void stopEffect(){
		this.isPlay = false;
		GamingInfo.getGamingInfo().getSurface().removeDrawablePic(Constant.PARTICLE_EFFECT_LAYER, this);
	}
	/**
	 * ��������λ��
	 */
	public void setEffectMatrix(){
		Particle particle;
		for(int i =0;i<effects.size();i++){
			particle = effects.get(i);
			if(particle.currentLen>=particle.maxLen){
				updateEffect(REMOVE,particle);
			}
//			particle.offX -=particle.offX*0.05f;
//			particle.offY -=particle.offY*0.05f;
//			particle.scale -=particle.scale*0.05f;
			particle.currentX = particle.currentX+particle.offX;
			particle.currentY = particle.currentY+particle.offY;
			particle.matrix.setTranslate(particle.currentX, particle.currentY);
			particle.matrix.preScale(particle.scale, particle.scale);
			particle.currentLen++;
		}
	}

	@Override
	public Bitmap getCurrentPic() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPicWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPicHeight() {
		// TODO Auto-generated method stub
		return 0;
	}
	/**
	 * ���Ӷ���
	 * @author Xiloer
	 *
	 */
	private class Particle{
		private Bitmap effect;
		/**
		 * ��ǰ������������X
		 */
		public float currentX;
		/**
		 * ��ǰ������������Y
		 */
		public float currentY;
		/**
		 * ƫ����X
		 */
		public float offX;
		/**
		 * ƫ����Y
		 */
		public float offY;
		/**
		 * ����
		 */
		public float scale;//���Ż���
		/**
		 * ���Ӿ���
		 */
		public Matrix matrix = new JMatrix();
		/**
		 * ������ߴ���
		 */
		public int maxLen = (int)(Math.random()*20);
		/**
		 * ��ǰ���ߴ���
		 */
		public int currentLen;
		/**
		 *
		 * @param currentX
		 * @param currentY
		 * @param offX
		 * @param offY
		 */
		public Particle(float currentX,float currentY,float offX,float offY,float scale,Bitmap effect){
			this.offX = offX*scale;
			this.offY = offY*scale;
			this.scale = scale;
			this.currentX = currentX-effect.getWidth()/2*scale;
			this.currentY = currentY-effect.getHeight()/2*scale;
			this.matrix.setTranslate(this.currentX, this.currentY);
			this.matrix.preScale(scale, scale);
			this.effect = effect;
		}
	}
}
