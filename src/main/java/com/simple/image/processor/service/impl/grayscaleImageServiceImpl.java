package com.simple.image.processor.service.impl;

import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.simple.image.processor.service.grayscaleImageService;
@Service
public class grayscaleImageServiceImpl implements grayscaleImageService{
    private Logger logger = LoggerFactory.getLogger(grayscaleImageServiceImpl.class);

	public BufferedImage grayscale(BufferedImage imageFile, String grayScale) {
		try {
			if(grayScale==null || grayScale.isEmpty()){
				return imageFile;
			}
			else if (grayScale.equals("on")){
				ColorConvertOp color = Scalr.OP_GRAYSCALE;
				BufferedImage image = Scalr.apply(imageFile, color);
				imageFile.flush();
				return image;
				}
			else {
				throw new Exception();
			}
		}
		catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
            }
		}
}