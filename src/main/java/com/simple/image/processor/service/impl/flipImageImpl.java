package com.simple.image.processor.service.impl;

import java.awt.image.BufferedImage;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.simple.image.processor.service.flipImage;
@Service
public class flipImageImpl implements flipImage{
    private Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);
    @Override
	public BufferedImage horizontal(BufferedImage imageFile, String flipHorizontal) {
		try {
			if(flipHorizontal==null || flipHorizontal.isEmpty()){
				return imageFile;
			}
			else {
				Scalr.Rotation rotation=Scalr.Rotation.FLIP_HORZ;
				BufferedImage image = Scalr.rotate(imageFile, rotation);
				imageFile.flush();
				return image;
				}
		}
		catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
            }
		}
    @Override
	public BufferedImage vertical(BufferedImage imageFile, String flipVertical) {
		try {
			if(flipVertical==null || flipVertical.isEmpty()){
				return imageFile;
			}
			else {
				Scalr.Rotation rotation=Scalr.Rotation.FLIP_VERT;
				BufferedImage image = Scalr.rotate(imageFile, rotation);
				imageFile.flush();
				return image;
				}
		}
		catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
		}
	}
}