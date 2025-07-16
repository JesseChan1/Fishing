package com.fish.tools;


public class Tool {
	private static final double ARG = 180/Math.PI;
	/**
	 * 	���Ŀ����Դ֮��ĽǶ�
	 * @param targetX		Ŀ���X����
	 * @param targetY		Ŀ���Y����
	 * @param sourceX		Դ��X����
	 * @param sourceY		Դ��Y����
	 * @return				Ŀ����Դ֮��ĽǶ�
	 */
	public static float getAngle(float targetX,float targetY,float sourceX,float sourceY){
		return -(float)(ARG*Math.atan2(targetY-sourceY,targetX-sourceX));

	}
}
