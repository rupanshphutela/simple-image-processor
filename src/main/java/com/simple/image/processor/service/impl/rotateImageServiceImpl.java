package com.simple.image.processor.service.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.simple.image.processor.service.rotateImageService;

@Service
public class rotateImageServiceImpl implements rotateImageService {

    private Logger logger = LoggerFactory.getLogger(rotateImageServiceImpl.class);
	
    @SuppressWarnings("removal")
	public BufferedImage ndegrees(BufferedImage imageFile, String rotateDegrees) {
    	try {
    	Scalr.Rotation rotation = null;
    	if(rotateDegrees == null || rotateDegrees.isEmpty()) {
    		return imageFile;
    	}
    	else {
    		int rotateDegrees_num = Integer.parseInt(rotateDegrees);
    		rotateDegrees_num = rotateDegrees_num%360;
    		BufferedImage image = null;
    		
    		switch(rotateDegrees_num) {
    			case 90:
            		rotation = Scalr.Rotation.CW_90;
            		image = Scalr.rotate(imageFile, rotation);
                    imageFile.flush();
                    return image;
    			case 180:
            		rotation = Scalr.Rotation.CW_180;
            		image = Scalr.rotate(imageFile, rotation);
                    imageFile.flush();
                    return image;
    			case -180:
            		rotation = Scalr.Rotation.CW_180;
            		image = Scalr.rotate(imageFile, rotation);
                    imageFile.flush();
                    return image;
    			case 270:
            		rotation = Scalr.Rotation.CW_270;
            		image = Scalr.rotate(imageFile, rotation);
                    imageFile.flush();
                    return image;
    			case -90:
            		rotation = Scalr.Rotation.CW_270;
            		image = Scalr.rotate(imageFile, rotation);
                    imageFile.flush();
                    return image;
    			default:
    				double radians = Math.toRadians(rotateDegrees_num);
    			    int w = (new Double(Math.abs(imageFile.getWidth() * Math.cos(radians)) + Math.abs(imageFile.getHeight() * Math.sin(radians)))).intValue();
    			    int h = (new Double(Math.abs(imageFile.getWidth() * Math.sin(radians)) + Math.abs(imageFile.getHeight() * Math.cos(radians)))).intValue();
    			    image = new BufferedImage(w, h, imageFile.getType());
    			    Graphics2D graphics = image.createGraphics();
    			    graphics.setColor(Color.WHITE);
    			    graphics.fillRect(0, 0, w, h);
    			    int x = -1 * (imageFile.getWidth() - w) / 2;
    			    int y = -1 * (imageFile.getHeight() - h) / 2;
    			    graphics.rotate(Math.toRadians(rotateDegrees_num), (w / 2), (h / 2));
    			    graphics.drawImage(imageFile, null, x, y);
    			    return image;
    		}
    	}
    }
    	catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
    }
    public BufferedImage rotateLeft(BufferedImage imageFile, String rotateLeft) {
		try {
			if(rotateLeft==null || rotateLeft.isEmpty()) {
				return imageFile;
            }
			else if(rotateLeft.equals("on")){
				Scalr.Rotation rotation = Scalr.Rotation.CW_270;
	            BufferedImage image = Scalr.rotate(imageFile, rotation);
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
    
    public BufferedImage rotateRight(BufferedImage imageFile, String rotateRight) {
		try {
			if(rotateRight==null || rotateRight.isEmpty()) {
				return imageFile;
            }
			else if(rotateRight.equals("on")){
				Scalr.Rotation rotation = Scalr.Rotation.CW_90;
	            BufferedImage image = Scalr.rotate(imageFile, rotation);
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