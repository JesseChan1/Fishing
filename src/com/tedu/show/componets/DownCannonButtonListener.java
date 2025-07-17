package com.fish.model.componets;

import com.fish.manager.CannonManager;
import com.fish.model.interfaces.OnClickListener;

/**
 * ���ʹ��������İ�ť�߼�
 * @author Xiloerfan
 *
 */
public class DownCannonButtonListener implements OnClickListener{

	@Override
	public void onClick() {
		// TODO Auto-generated method stub
		CannonManager.getCannonManager().downCannon();
	}

}
