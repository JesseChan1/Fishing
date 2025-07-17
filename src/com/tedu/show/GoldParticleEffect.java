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
 * �������Ч��
 * @author Xiloer
 *
 */
public class GoldParticleEffect extends DrawableAdapter{
	private static byte ADD = 1;
	private static byte REMOVE = 2;
	private static byte UPDATE = 3;
	//����ͼ
	private Bitmap effectImg;
	private ArrayList<Particle> effects = new ArrayList<Particle>();
	private ArrayList<Particle> news =  new ArrayList<Particle>();
	private ArrayList<Particle> removes =  new ArrayList<Particle>();
	private int indexByDraw;//���ֵ���ڻ��Ʒ���ѭ��ʹ��
	private Particle particle;//���ֵ���ڻ��Ʒ���ѭ��ʹ��
	private boolean isPlay;//�Ƿ񲥷�����Ч��
	private float targetOffsetX,targetOffsetY;//���뵱ǰ�����ƫ����,������ֵ����currentX,currentY���õ����ӳ�ʼλ��
	private float currentX,currentY;
	public GoldParticleEffect(Bitmap effectImg){
		this.effectImg = effectImg;
	}

	/**
	 * ����һ������Ч��
	 * @param x				���ӵ�����λ��X
	 * @param y				���ӵ�����λ��Y
	 * @param offX			����ƫ����X ������ֵ����������ʱ���ж�·�ߣ����Ӧ�ú͸����������ƫ�����෴
	 * @param offY			����ƫ����Y
	 */
	public void playEffect(float targetOffsetX,float targetOffsetY,float x,float y,float offX,float offY){
		try {
			isPlay = true;
			this.targetOffsetX = targetOffsetX;
			this.targetOffsetY = targetOffsetY;
			startCreateEffectThread(x,y,offX,offY);
			GamingInfo.getGamingInfo().getSurface().putDrawablePic(Constant.PARTICLE_EFFECT_LAYER, this);
		}catch(Exception e){
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
	/**
	 * �����������ӵ��߳�
	 */
	private void startCreateEffectThread(final float x,final float y,final float offX,final float offY){
		this.currentX = x;
		this.currentY = y;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					while(GamingInfo.getGamingInfo().isGaming()){
						while(!GamingInfo.getGamingInfo().isPause()&&isPlay){
							updateEffect(ADD,new Particle(currentX,currentY,offX+(float)(Math.random()*5),offY+(float)(Math.random()*5),0.5f,effectImg));
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
	public void setEffectMatrix(float currentX,float currentY){
		this.currentX = currentX+targetOffsetX;
		this.currentY = currentY+targetOffsetY;
		Particle particle;
		for(int i =0;i<effects.size();i++){
			particle = effects.get(i);
			particle.offX -=particle.offX*0.1f;
			particle.offY -=particle.offY*0.1f;
			particle.scale -=particle.scale*0.1f;
			particle.currentX = particle.currentX-particle.offX;
			particle.currentY = particle.currentY-particle.offY;
			particle.matrix.setTranslate(particle.currentX, particle.currentY);
			particle.matrix.preScale(particle.scale, particle.scale);
			if(particle.scale<0.1){
				updateEffect(REMOVE,particle);
			}
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
		 *
		 * @param currentX
		 * @param currentY
		 * @param offX
		 * @param offY
		 */
		public Particle(float currentX,float currentY,float offX,float offY,float scale,Bitmap effect){
			this.offX = offX;
			this.offY = offY;
			this.scale = scale;
			this.currentX = currentX-effect.getWidth()/2*scale+targetOffsetX;
			this.currentY = currentY-effect.getHeight()/2*scale+targetOffsetY;
			this.matrix.setTranslate(this.currentX, this.currentY);
			this.matrix.preScale(scale, scale);
			this.effect = effect;
		}
	}
}
