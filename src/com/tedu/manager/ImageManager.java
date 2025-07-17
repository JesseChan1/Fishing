package com.fish.manager;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.xmlpull.v1.XmlPullParser;

import com.fish.canvas.Bitmap;
import com.fish.manager.ImageConfig.ActConfig;
import com.fish.model.GamingInfo;
import com.fish.tools.Log;

/**
 * ͼƬ������,����ģʽ
 *
 * @author Xiloerfan
 */
public class ImageManager {
private static ImageManager manager;

private ImageManager() {

}

public static ImageManager getImageMnagaer() {
    if (manager == null) {
        manager = new ImageManager();
    }
    return manager;
}

//���ű���
public float scaleNum = 1;
//�����ű���
public float fishScaleNum = 1;
//ͼƬ���棬���ڲ�ͼʱ��ͼ�Ľ��ͼ��ش���
private Bitmap baseImageCache;
//����ͼƬ������
private String baseImageString;

/**
 * ������������棬�ͷſռ�
 */
public void clearImageCache() {
    baseImageCache = null;
    baseImageString = null;
}

//	/**
//	 * ��ʼ��������
//	 */
public void initManager() {
    int len = GamingInfo.getGamingInfo().getScreenHeight();
    if (len <= 500) {
        scaleNum = 0.75f;
        fishScaleNum = 0.5f;
    }
}

/**
 * ���ݸ����������ļ���������ص�������Ϣ��
 *
 * @param configFileName ���assets�Ĵ�·�����ļ� �� fish/fish2(fish2.plist)
 * @return ����һ��ImageConfig����
 */
public ImageConfig createImageConfigByPlist(
        String configFileName) {
    ImageConfig config = new ImageConfig();
    try {
        XmlPullParser xml = XmlManager.getXmlParser(configFileName, "UTF-8");
        if (xml == null) {
            throw new Exception("ImageManager:������xml�ļ�Ϊnull!");
        }
        config.setSrcImageFileName(configFileName);
        while (GamingInfo.getGamingInfo().isGaming()) {
            /**
             * ��ǩΪkey��
             */
            XmlManager.gotoTagByTagName(xml, "key");
            String value = XmlManager.getValueByCurrentTag(xml);
            if (value != null) {
                /**
                 * ��������Դͼ��Ϣ
                 */
                if (value.equals("texture")) {
                    setScaleInfo(xml, config);
                    /**
                     * ���ý�ȡÿ֡������Ϣ
                     */
                } else if (value.equals("frames")) {
                    XmlManager.gotoTagByTagName(xml, "dict");
                    getCutImageInfo(xml, config);
                    break;
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return config;
}


/**
 * ����Դͼ������Ϣ
 *
 * @param xml
 * @param config
 */
private void setScaleInfo(XmlPullParser xml, ImageConfig config) {
    XmlManager.gotoTagByTagName(xml, "key");
    String mode = XmlManager.getValueByCurrentTag(xml);
    XmlManager.gotoTagByTagName(xml, "integer");
    if (mode.equals("width")) {
        config.setSrcImageWidth(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
    } else if (mode.equals("height")) {
        config.setSrcImageHeight(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
    }
    XmlManager.gotoTagByTagName(xml, "key");
    mode = XmlManager.getValueByCurrentTag(xml);
    XmlManager.gotoTagByTagName(xml, "integer");
    if (mode.equals("width")) {
        config.setSrcImageWidth(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
    } else if (mode.equals("height")) {
        config.setSrcImageHeight(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
    }
}

/**
 * �����ز�ͼ
 *
 * @param config ͼƬ��������Ϣ
 * @return �������ź��ͼƬ�����ͼƬû�ҵ�����null
 */
private synchronized Bitmap scaledSrcBitmap(ImageConfig config) {
    try {
        if (this.baseImageString != null && this.baseImageString.equals(config.getSrcImageFileName())) {
            return this.baseImageCache;
        } else {
            if (this.baseImageCache != null) {
//					this.baseImageCache.recycle();
                this.baseImageCache = null;
                System.gc();
            }
            this.baseImageCache = getBitmapByAssets(config.getSrcImageFileName() + ".png");
            this.baseImageString = config.getSrcImageFileName();
            if (config.getSrcImageWidth() != this.baseImageCache.getWidth() || config.getSrcImageHeight() != this.baseImageCache.getHeight()) {
                float proportion = config.getSrcImageWidth() * 1f / this.baseImageCache.getWidth();
                this.baseImageCache = scaleImageByProportion(this.baseImageCache, proportion);
            }
            return this.baseImageCache;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

/**
 * ��ȡ��ͼ��Ϣ
 *
 * @param xml         ��Ӧ�������ļ�xml�ļ�
 * @param ImageConfig �����������ݱ�����������ö�����
 */
private void getCutImageInfo(XmlPullParser xml,
                             ImageConfig config) {
    String imageName = null;
    while (GamingInfo.getGamingInfo().isGaming() && XmlManager.gotoTagByTagName(xml, "key")) {
        imageName = XmlManager.getValueByCurrentTag(xml);
        if (XmlManager.gotoTagByTagName(xml, "dict")) {
            ActConfig actConfig = getActConfig(xml);
            config.getAllActs().put(imageName, actConfig);
            actConfig.setImageName(imageName);
            actConfig.setConfig(config);
//				getFishActImage(xml, src);
        }
    }
}

/**
 * ����ͼƬ������Ϣ
 * �����ϸ���˳�����������������˳�����⣬����xml�Ǳ����õ�˳��Ҫ�Ƚ��ϸ�
 *
 * @param xml
 * @return
 */
private ActConfig getActConfig(XmlPullParser xml) {
    ActConfig imageConfig = new ActConfig();
    //�ҵ���Ӧ������Ϣ
    XmlManager.gotoTagByTagName(xml, "integer");
    imageConfig.setImageX(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
    XmlManager.gotoTagByTagName(xml, "integer");
    imageConfig.setImageY(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
    XmlManager.gotoTagByTagName(xml, "integer");
    imageConfig.setImageWidth(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
    XmlManager.gotoTagByTagName(xml, "integer");
    imageConfig.setImageHeight(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
    XmlManager.gotoTagByTagName(xml, "real");
    imageConfig.setOffsetX(Float.parseFloat(XmlManager.getValueByCurrentTag(xml)));
    XmlManager.gotoTagByTagName(xml, "real");
    imageConfig.setOffsetY(Float.parseFloat(XmlManager.getValueByCurrentTag(xml)));
    XmlManager.gotoTagByTagName(xml, "integer");
    imageConfig.setOriginalWidth(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
    XmlManager.gotoTagByTagName(xml, "integer");
    imageConfig.setOriginalHeight(Integer.parseInt(XmlManager.getValueByCurrentTag(xml)));
    return imageConfig;
}

/**
 * ����ͼƬ��������Ϣ��ȡͼƬ
 *
 * @param config ͼƬ�������ļ�
 * @param src    Դͼ
 * @return �ó�����ͼ
 */
private Bitmap getImage(ActConfig config, Bitmap src, float proportion) {
    // ����һ��ͼƬ
    BufferedImage newImage = new BufferedImage(config.getOriginalWidth(), config.getOriginalHeight(), BufferedImage.TYPE_4BYTE_ABGR);
    Graphics g = newImage.getGraphics();
    // �ó�����ͼƬ
    g.drawImage(
            src.getImage(),
            (int) (config.getOriginalWidth() / 2 + config.getOffsetX() - config.getImageWidth() / 2),
            (int) (config.getOriginalHeight() / 2 - config.getOffsetY() - config.getImageHeight() / 2),
            (int) (config.getOriginalWidth() / 2 + config.getOffsetX() - config.getImageWidth() / 2) + config.getImageWidth(),
            (int) (config.getOriginalHeight() / 2 - config.getOffsetY() - config.getImageHeight() / 2) + config.getImageHeight(),
            config.getImageX(),
            config.getImageY(),
            config.getImageX() + config.getImageWidth(),
            config.getImageY() + config.getImageHeight(),
            null
    );
    return new Bitmap(newImage);
}

/**
 * ���ظ���������Ϣ��һ��ͼƬ
 *
 * @param configs
 * @return
 */
public Bitmap[] getImagesByActConfigs(ActConfig[] configs, float proportion) {
    Bitmap[] imgs = new Bitmap[configs.length];
    Bitmap src = null;
    String srcFileName = null;
    for (int i = 0; i < configs.length; i++) {
        if (srcFileName == null || !srcFileName.equals(configs[i].getConfig().getSrcImageFileName())) {
            srcFileName = configs[i].getConfig().getSrcImageFileName();
            src = scaledSrcBitmap(configs[i].getConfig());
        }
        imgs[i] = getImage(configs[i], src, proportion);
    }
    src = null;
    System.gc();
    return imgs;
}

/**
 * ����ͼƬ���ö�����Ϣ��ȡ��Ӧ��һ��ͼƬ��HashMap����
 *
 * @param config ��Ӧ��ͼƬ���ö���
 * @return һ��HashMap���� key:ͼƬ���� value:��Ӧ��ͼƬ
 */
public HashMap<String, Bitmap> getImagesMapByImageConfig(ImageConfig config, float proportion) {
    HashMap<String, Bitmap> allAct = new HashMap<String, Bitmap>();
    try {
        Bitmap src = scaledSrcBitmap(config);
        //������������Ϣ�е�ͼƬ���õ�������
        for (ActConfig act : config.getAllActs().values()) {
            allAct.put(act.getImageName(), getImage(act, src, proportion));
        }
    } catch (Exception e) {
        Log.doLogForException(e);
    }
    return allAct;
}

/**
 * ��תͼƬ
 *
 * @param angle    �����Ƕ�
 * @param newImage ��ת��ͼƬ
 * @return
 */
public Bitmap rotateImage(int angle, Bitmap newImage) {
    AffineTransform trans = new AffineTransform();
    trans.rotate(Math.toRadians(angle), newImage.getWidth() / 2, newImage.getHeight() / 2);
    BufferedImage img = new BufferedImage(newImage.getWidth(), newImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D g = (Graphics2D) img.getGraphics();
    RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    qualityHints.put(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
    g.setRenderingHints(qualityHints);
    g.drawImage(newImage.getImage(), trans, null);
    return new Bitmap(img);
}

/**
 * ������Ļ�ߴ�����ͼƬ
 * ������Ҫע���һ���ǣ�Ҫ��ʼ��GamingInfo�����Ļ�ߴ磬��Ϊ���������������ͼƬ��
 *
 * @param src ��Ҫ���ŵ�ͼƬ
 * @return ���ź��ͼƬ
 */
public Bitmap scaleImageByScreen(Bitmap src) {
    AffineTransform trans = new AffineTransform();
    trans.scale(scaleNum, scaleNum);
    BufferedImage img = new BufferedImage((int) (src.getWidth() * scaleNum), (int) (src.getHeight() * scaleNum), BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D g = (Graphics2D) img.getGraphics();
    RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    qualityHints.put(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
    g.setRenderingHints(qualityHints);
    g.drawImage(src.getImage(), trans, null);
    return new Bitmap(img);
}

/**
 * ���ݱ�������ͼƬ
 *
 * @param src
 * @param proportion
 * @return
 */
public Bitmap scaleImageByProportion(Bitmap src, float proportion) {
    AffineTransform trans = new AffineTransform();
    trans.scale(proportion, proportion);
    BufferedImage img = new BufferedImage((int) (src.getWidth() * proportion), (int) (src.getHeight() * proportion), BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D g = (Graphics2D) img.getGraphics();
    RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    qualityHints.put(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
    g.setRenderingHints(qualityHints);
    g.drawImage(src.getImage(), trans, null);
    return new Bitmap(img);
}

/**
 * ���ݸ����ߴ�����ͼƬ
 *
 * @param src
 * @param width
 * @param height
 * @return
 */
public Bitmap sacleImageByWidthAndHeight(Bitmap src, int width, int height) {
    AffineTransform trans = new AffineTransform();
    trans.scale(width * 1f / src.getWidth(), height * 1f / src.getHeight());
    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D g = (Graphics2D) img.getGraphics();
    RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    qualityHints.put(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
    g.setRenderingHints(qualityHints);
    g.drawImage(src.getImage(), trans, null);
    return new Bitmap(img);
}

/**
 * ���ʲ��л�ȡͼƬ
 *
 * @param imageName ����ʲ�assets��Ŀ¼�µĴ�·����ͼƬ����
 * @throws Exception
 * @return ����ָ����ͼƬ
 */
public Bitmap getBitmapByAssets(String imageName) throws Exception {
    try {
        return new Bitmap(ImageIO.read(new File(imageName)));
    } catch (IOException e) {
        Log.doLogForException(e);
        throw e;
    }
}

/**
 * ���ʲ��л�ȡ������Ļ�ߴ����ź��ͼƬ
 *
 * @param imageName
 * @return
 * @throws Exception
 */
public Bitmap getscaleImageByScreenFromAssets(String imageName) throws Exception {
    try {
        return scaleImageByScreen(getBitmapByAssets(imageName));
    } catch (IOException e) {
        throw e;
    }
}

}

