package com.hcycom.sso.utils;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


public class TwoDimensionCodeUtil {

	 /**
     * 生成二维码图片
     * @param content 二维码内容
     * @param width 宽
     * @param height 高
     * @param margin 二维码外边距，0到4
     * @param imgPath 二维码图片存放路径
     */
    public void createQRCode(String content,int width,int height,int margin,String imgPath,File imageFile) throws WriterException, IOException {
        /**二维码参数设定*/
        Hashtable<EncodeHintType,Object> hintTypes = new Hashtable<EncodeHintType, Object>();
        hintTypes.put(EncodeHintType.CHARACTER_SET,CharacterSetECI.UTF8);
        hintTypes.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);//容错级别，共4级，详细问度娘
        hintTypes.put(EncodeHintType.MARGIN,margin);//二维码外边距

        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,width,height,hintTypes);//创建二维码，本质为二位数组

/**将二维码转为图片*/
        BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bufferedImage.setRGB(i,j,bitMatrix.get(i,j)? Color.BLACK.getRGB():Color.WHITE.getRGB());
            }
        }

        if (!imageFile.getParentFile().exists()){
        	imageFile.getParentFile().mkdirs();
        }
        ImageIO.write(bufferedImage,"png",imageFile);
    }
}
