package com.fish.manager;

import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;

import com.fish.canvas.Bitmap;
import com.fish.manager.ImageConfig.ActConfig;
import com.fish.model.FishInfo;
import com.fish.model.GamingInfo;
import com.fish.model.Fish;
import com.fish.tools.Log;

/**
 * ��Ĺ�����
 * @author Xiloerfan
 *
 */
public class FishManager {
	/**
	 * ����ģʽ
	 */
	private static FishManager fishManager;

	private FishManager(){
	}

	public static FishManager getFishMananger(){
		if(fishManager==null){
			fishManager = new FishManager();
		}
		return fishManager;
	}
	/**
	 * �������ֱ����������������Ϣ
	 */
	private HashMap<String,FishInfo> allFishConfig = new HashMap<String,FishInfo>();
	/**
	 * �������ֱ���������Ķ���������Ϣ
	 */
	private HashMap<String,ActConfig[]> allFishActConfigs = new HashMap<String,ActConfig[]>();
	/**
	 * �������ֱ���������Ĳ�����������Ϣ
	 */
	private HashMap<String,ActConfig[]> allFishCatchActConfigs = new HashMap<String,ActConfig[]>();
	/**
	 * �������ֻ������Ķ���ͼƬ
	 */
	private HashMap<String,Bitmap[]> allFishActs = new HashMap<String,Bitmap[]>();
	/**
	 * �������ֻ������Ĳ�����ͼƬ
	 */
	private HashMap<String,Bitmap[]> allFishCatchActs = new HashMap<String,Bitmap[]>();
	/**
	 * �������
	 */
	private ArrayList<String> allFish = new ArrayList<String>();
	/**
	 * ����XML�����ļ�����ʼ��������
	 * ����������ļ���û���壬ֻ��д�ڴ������ˣ��Ժ���Ըĳ�ͨ����ȡ�����ļ������ز�ͬ
	 * ��Դ����
	 * @param initXml
	 */

	/**
	 * �Ƿ���Դ����µ���
	 * ���ֵ�ĸı������»ᷢ��:
	 * ÿ������updateFish����ʱ���Ὣ���ֵ����Ϊfalse
	 * updateFish����ִ�����ʱ���Ὣ���ֵ�ڸı��true
	 */
	private boolean createable = false;
	/**
	 * ��ʼ��������
	 * ������ȡfish�ļ����µ�FishConfig.plist�ļ�����������������������Ϣ
	 */
	public void initFish(){
		try{
			HashMap<String,ActConfig> configs = new HashMap<String,ActConfig>();
			//����һ���㶯�������ļ�����
			String fishActConfiges[];
			//��Ļ�����Ϣ�����ļ���
			String fishInfoConfig;
			//���ع������������ļ�
			XmlPullParser xml = XmlManager.getXmlParser("fish/FishConfig", "UTF-8");
			//��ȡfishActConfig��Ϣ
			XmlManager.gotoTagByTagName(xml, "string");
			fishActConfiges = XmlManager.getValueByCurrentTag(xml).split(";");
			//��ȡfishInfoConfig��Ϣ
			XmlManager.gotoTagByTagName(xml, "string");
			fishInfoConfig = XmlManager.getValueByCurrentTag(xml);
			//���Xml�����ͷſռ�
			xml = null;
			//��ʼ����Ķ�����Ϣ
			this.initFishAct(configs, fishActConfiges);
			//��ʼ����Ļ�����Ϣ
			this.initFishInfo(fishInfoConfig);

			int fishIndex = 1;
			StringBuffer fishName = new StringBuffer("fish01");
			while(getFishByName(fishName.toString(),configs)){
				fishName.delete(0, fishName.length());
				fishIndex++;
				if(fishIndex<10){
					fishName.append("fish0"+fishIndex);
				}else{
					fishName.append("fish"+fishIndex);
				}
			}
			configs = null;
			fishName = null;
			System.gc();
		}catch(Exception e){
			e.printStackTrace();
			Log.doLogForException(e);
		}
	}
	/**
	 * ����������ֻ�ȡһ�����ʵ��
	 * @param fishName
	 * @return
	 */
	public Fish birthFishByFishName(String fishName){
		if(createable){
			Fish fish = new Fish(getFishActByFishName(fishName),getFishCatchActsByFishName(fishName),allFishConfig.get(fishName));
			/**
			 * ������Ӧ�û�Ҫ����һ��������ö���FishConfig
			 */
			return fish;
		}else{
			System.out.println("FishManager:���ܴ����㣬�Ƿ���ù�updateFish����?");
			return null;
		}

	}
	/**
	 * ���¼��ص���
	 * @param fish
	 */
	public void updateFish(String []fish){
		this.createable = false;
		HashMap<String,Bitmap[]> fishAct = new HashMap<String,Bitmap[]>();
		HashMap<String,Bitmap[]> fishCatchAct = new HashMap<String,Bitmap[]>();
		for(String fishName:fish){
			fishAct.put(fishName, getFishActByFishName(fishName));
			fishCatchAct.put(fishName, getFishCatchActsByFishName(fishName));
		}
		allFishActs = fishAct;
		allFishCatchActs = fishCatchAct;
		this.createable = true;
		//���������е����Ժ�����һ�»���
		ImageManager.getImageMnagaer().clearImageCache();
	}
	/**
	 * ��ȡ�����������
	 * @return
	 */
	public ArrayList<String> getAllFishName(){
		return allFish;
	}
	/**
	 * �����ͷ���Դ
	 */
	public static void destroy(){
		fishManager = null;
		System.gc();
	}

	/**
	 * ������Ķ������������㶯���ṹ��
	 * @param fishName
	 * @param fishActs
	 * @return true:���óɹ� false:����ʧ��
	 */
	private boolean getFishByName(String fishName,HashMap<String,ActConfig> configs){
		//���ͼȫ��(fish12_01.png)
		StringBuffer fishFullName = new StringBuffer();
		//��ı�����ͼȫ��(fish12_catch_01.png)
		StringBuffer fishCatchFullName = new StringBuffer();
		//��ǰ�������ֵ������ƴ����ļ���
		int fishNum = 1;
		//��ʱ��ŵ�ǰ������ͼƬ�ļ���
		ArrayList<ActConfig> allActs = new ArrayList<ActConfig>();
		//��ʱ��ŵ�ǰ�����б�����ͼƬ�ļ���
		ArrayList<ActConfig> allCatchActs = new ArrayList<ActConfig>();
		//��ȡ��ǰ������ж���
		while(GamingInfo.getGamingInfo().isGaming()){
			fishFullName.delete(0, fishFullName.length());
			//һ֡ͼƬ������
			ActConfig fishAct = null;
			if(fishNum<10){
				fishFullName.append(fishName+"_0"+fishNum+".png");
			}else{
				fishFullName.append(fishName+"_"+fishNum+".png");
			}
			fishNum++;
			if((fishAct = configs.get(fishFullName.toString()))!=null){
				allActs.add(fishAct);
			}else{
				break;
			}
		}
		fishFullName = null;
		System.gc();
		fishNum = 1;
		//��ȡ��ǰ������б�������
		while(GamingInfo.getGamingInfo().isGaming()){
			fishCatchFullName.delete(0, fishCatchFullName.length());
			//һ֡ͼƬ������
			ActConfig fishCatchAct = null;
			if(fishNum<10){
				fishCatchFullName.append(fishName+"_catch_0"+fishNum+".png");
			}else{
				fishCatchFullName.append(fishName+"_catch_"+fishNum+".png");
			}
			fishNum++;
			if((fishCatchAct = configs.get(fishCatchFullName.toString()))!=null){
				allCatchActs.add(fishCatchAct);
			}else{
				break;
			}
		}
		fishCatchFullName = null;
		System.gc();
		//���û�н�������Ķ���
		if(allActs.size()==0){
			//����null����ʾû��������
			return false;
		}else{
			//���ݵ�ǰ�������ַ����Ӧ����������ж���
			ActConfig[] fishActArray = new ActConfig[allActs.size()];
			ActConfig[] fishCatchActsArray = new ActConfig[allCatchActs.size()];
			for(int i =0;i<allActs.size();i++){
				fishActArray[i] = allActs.get(i);
			}
			for(int i =0;i<allCatchActs.size();i++){
				fishCatchActsArray[i] = allCatchActs.get(i);
			}
			allActs = null;
			allCatchActs = null;
			allFishActConfigs.put(fishName,fishActArray);
			allFishCatchActConfigs.put(fishName,fishCatchActsArray);
			allFish.add(fishName);
			System.gc();
			return true;
		}
	}

	/**
	 * ��ȡ����ζ�ͼƬ��
	 * @param fishName
	 * @return
	 */
	private Bitmap[] getFishActByFishName(String fishName){
		if(allFishActs.get(fishName)==null){
			Bitmap []acts = ImageManager.getImageMnagaer().getImagesByActConfigs(allFishActConfigs.get(fishName),ImageManager.getImageMnagaer().fishScaleNum);
			//�����е�ͼ����ת180�ȣ���Ϊ��������ǻ���ͷ���ҵ�
			for(int i =0;i<acts.length;i++){
				acts[i] = ImageManager.getImageMnagaer().rotateImage(180, acts[i]);
			}
			return acts;

		}else{
			return allFishActs.get(fishName);
		}

	}
	/**
	 * ��ȡ��ı�����ͼƬ��
	 * @param fishName
	 * @return
	 */
	private Bitmap[] getFishCatchActsByFishName(String fishName){
		if(allFishCatchActs.get(fishName)==null){
			Bitmap []acts = ImageManager.getImageMnagaer().getImagesByActConfigs(allFishCatchActConfigs.get(fishName),ImageManager.getImageMnagaer().fishScaleNum);
			//�����е�ͼ����ת180�ȣ���Ϊ��������ǻ���ͷ���ҵ�
			for(int i =0;i<acts.length;i++){
				acts[i] = ImageManager.getImageMnagaer().rotateImage(180, acts[i]);
			}
			return acts;
		}else{
			return allFishCatchActs.get(fishName);
		}

	}
	/**
	 * ��ʼ�����������Ϣ
	 * @param config
	 */
	private void initFishInfo(String config){
		try{
			//���������Ϣû���ҵ����׳��쳣
			if(config==null){
				throw new Exception("FishManager:��ȡ�����ļ�����û���ҵ�fishInfoConfig��Ϣ");
			}
			//������Ļ�����Ϣ�����ļ�
			XmlPullParser xml = XmlManager.getXmlParser(config, "UTF-8");
			//�������е���Ļ�����Ϣ
			while(GamingInfo.getGamingInfo().isGaming()&&XmlManager.gotoTagByTagName(xml, "key")){
				XmlManager.gotoTagByTagName(xml, "string");
				String fishName = XmlManager.getValueByCurrentTag(xml);
				FishInfo fishInfo = new FishInfo();
				//���������ת�Ƕ�
				XmlManager.gotoTagByTagName(xml, "integer");
				fishInfo.setMaxRotate(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
				//�����ƶ��ٶ�
				XmlManager.gotoTagByTagName(xml, "integer");
				fishInfo.setFishRunSpeed(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
				//���ö����ٶ�
				XmlManager.gotoTagByTagName(xml, "integer");
				fishInfo.setActSpeed(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
				//������Ⱥ�������
				XmlManager.gotoTagByTagName(xml, "integer");
				fishInfo.setFishShoalMax(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
				//�������ͼ��ID
				XmlManager.gotoTagByTagName(xml, "integer");
				fishInfo.setFishInLayer(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
				//������ļ�ֵ
				XmlManager.gotoTagByTagName(xml, "integer");
				fishInfo.setWorth(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
				//������Ĳ�׽����
				XmlManager.gotoTagByTagName(xml, "integer");
				fishInfo.setCatchProbability(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
				allFishConfig.put(fishName, fishInfo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * ��ʼ����Ķ�����Ϣ
	 * @param configs			������������ÿ�������ļ��������Map��
	 * @param fishActConfiges	���е������ļ�����
	 */
	private void initFishAct(HashMap<String,ActConfig> configs,String fishActConfiges[]){
		try{
			//���������Ϣû���ҵ����׳��쳣
			if(fishActConfiges==null){
				throw new Exception("FishManager:��ȡ�����ļ�����û���ҵ�fishActConfig��Ϣ");
			}
			for(String actConfig : fishActConfiges){
				configs.putAll(ImageManager.getImageMnagaer().createImageConfigByPlist(actConfig).getAllActs());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
