package com.simple.image.processor.service.impl;

import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.simple.image.processor.service.base64ToImageService;

@Service
public class base64ToImageServiceImpl implements base64ToImageService{

    private Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);
    public BufferedImage transform(String base64Image) {
        try {
        	String b64Image = base64Image.split(",")[1];
            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(b64Image);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            int width          = image.getWidth();
    		int height         = image.getHeight();
            System.out.println("Decoded image dimensions"+width+" "+height);
    		return image; 
    		} 
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;        
            }
	}

}
