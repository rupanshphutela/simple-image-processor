package com.simple.image.processor.service.impl;

import java.awt.image.BufferedImage;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.simple.image.processor.service.resizeImageService;
@Service
public class resizeImageServiceImpl implements resizeImageService{
    private Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

	public BufferedImage resize(BufferedImage imageFile, String resize) {
		BufferedImage image = imageFile;
		int width          = imageFile.getWidth();
		int height         = imageFile.getHeight();
		int scaled_width = 0;
		int scaled_height = 0;
        System.out.println("Image dimensions for resize :"+width+"x"+height);
		try{
        	if(resize==null || resize.isEmpty()) {
        		return imageFile;
        	}
        	else {
        		int resize_num = Integer.parseInt(resize);
				scaled_width = ((width*resize_num)/100);
				scaled_height = ((height*resize_num)/100);
				image = Scalr.resize(imageFile, Scalr.Mode.AUTOMATIC, scaled_width, scaled_height);
				imageFile.flush();
				return image;
        	}
        }
        catch (Exception e){
        	logger.error(e.getMessage(), e);
        	return null;
        }
	}
	
	public BufferedImage resizehw(BufferedImage imageFile, String imgwidth, String imgheight) {
		BufferedImage image = imageFile;
		try{
        	if(imgwidth==null || imgheight==null || imgwidth.isEmpty() || imgheight.isEmpty()) {
        		return imageFile;
        	}
        	else {
        		int width = Integer.parseInt(imgwidth);
        		int height = Integer.parseInt(imgheight);
				image = Scalr.resize(imageFile, Scalr.Mode.FIT_EXACT , width, height);
				imageFile.flush();
				return image;
        	}
        }
        catch (Exception e){
        	logger.error(e.getMessage(), e);
        	return null;
        }
	}
	
	public BufferedImage thumbnail(BufferedImage imageFile, String thumbnail) {
		try{
			if(thumbnail==null || thumbnail.isEmpty()) {
        		return imageFile;
			}
			else {
//				if(imageFile.getHeight()>200) {
	        		System.out.println("Inside thumbnail on loop with > 200 height ");
	        		Scalr.Mode mode = Scalr.Mode.FIT_TO_HEIGHT;
	        		BufferedImage image = Scalr.resize(imageFile, Scalr.Method.QUALITY, mode, 200);
	        		imageFile.flush();
					return image;
//				}
//				else {
//	        		System.out.println("Inside thumbnail on loop with < 200 height ");
//	        		Scalr.Mode mode = Scalr.Mode.FIT_TO_HEIGHT;
//	        		BufferedImage image = Scalr.resize(imageFile, Scalr.Method.QUALITY, mode, imageFile.getHeight());
//	        		imageFile.flush();
//					return image;
//				}
			}
        }
		catch (Exception e){
	        	logger.error(e.getMessage(), e);
	        	return null;
        }
	}
}
