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
    private Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

	public BufferedImage grayscale(BufferedImage imageFile, String grayscale) {
		try {
			if(grayscale==null || grayscale.isEmpty()){
				return imageFile;
			}
			else {
				ColorConvertOp color = Scalr.OP_GRAYSCALE;
				BufferedImage image = Scalr.apply(imageFile, color);
				return image;
				}
		}
		catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
            }
		}
}