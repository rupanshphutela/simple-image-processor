package com.simple.image.processor.service.impl;

import java.awt.image.BufferedImage;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.simple.image.processor.service.rotateImageService;

@Service
public class rotateImageServiceImpl implements rotateImageService {

    private Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);
/*    
    public BufferedImage ndegrees(BufferedImage imageFile, int rotateDegrees) {
	rotateDegrees = rotateDegrees%360;
	try {
		
	if(rotateDegrees == 0) {
	     return imageFile ;
	}
	else {
		javaxt.io.Image image2 = new javaxt.io.Image(imageFile);
		image2.rotate(rotateDegrees);
		java.awt.image.BufferedImage image = image2.getBufferedImage();
		System.out.println("Dimensions of javaxt rotated image - Height: " + image.getHeight() + ", Width: "+ image.getWidth());
		return image;
	}
}
	catch (Exception e) {
		logger.error(e.getMessage(), e);
		return null;
	}
}
*/   		
    public BufferedImage ndegrees(BufferedImage imageFile, String rotateDegrees) {
    	try {
    	Scalr.Rotation rotation = null;
    	if(rotateDegrees == null || rotateDegrees.isEmpty()) {
    		return imageFile;
    	}
    	else {
    		int rotateDegrees_num = Integer.parseInt(rotateDegrees);
    		rotateDegrees_num = rotateDegrees_num%360;
    		switch(rotateDegrees_num) {
    			case 90:
            		rotation = Scalr.Rotation.CW_90;
            		break;
    			case 180:
            		rotation = Scalr.Rotation.CW_180;
            		break;
    			case 270:
            		rotation = Scalr.Rotation.CW_270;
            		break;
    			default:
    				return imageFile;
    		}
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
    public BufferedImage rotateLeft(BufferedImage imageFile, String rotateLeft) {
		try {
			if(rotateLeft==null || rotateLeft.isEmpty()) {
				return imageFile;
            }
			else {
				Scalr.Rotation rotation = Scalr.Rotation.CW_270;
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
    
    public BufferedImage rotateRight(BufferedImage imageFile, String rotateRight) {
		try {
			if(rotateRight==null || rotateRight.isEmpty()) {
				return imageFile;
            }
			else {
				Scalr.Rotation rotation = Scalr.Rotation.CW_90;
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