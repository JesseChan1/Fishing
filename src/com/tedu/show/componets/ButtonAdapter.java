package com.fish.model.componets;

import com.fish.canvas.Bitmap;
import com.fish.model.interfaces.Button;
import com.fish.model.interfaces.OnClickListener;

public class ButtonAdapter extends Componet implements Button{
	private boolean enable = true;
	private OnClickListener onClickListener;
	private Bitmap btn_img;
	/**
	 *  ����һ����ť
	 * @param btn_img			û�е��ʱ�İ�ť��ť
	 * @throws Exception		��btn_imgΪ��ʱ�׳�һ������
	 */
	public ButtonAdapter(Bitmap btn_img,OnClickListener onClickListener)throws Exception{
		if(btn_img==null){
			throw new Exception("ButtonAdapter:ͼƬ��ť����Ϊ��");
		}
		this.onClickListener = onClickListener;
		this.btn_img = btn_img;
	}
	@Override
	public Bitmap getCurrentPic() {
		// TODO Auto-generated method stub
		return btn_img;
	}

	@Override
	public int getPicWidth() {
		// TODO Auto-generated method stub
		return btn_img.getWidth();
	}

	@Override
	public int getPicHeight() {
		// TODO Auto-generated method stub
		return btn_img.getHeight();
	}

	@Override
	public void onClick() {
		// TODO Auto-generated method stub
		onClickListener.onClick();
	}
	@Override
	public boolean isEnable() {
		// TODO Auto-generated method stub
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

}
