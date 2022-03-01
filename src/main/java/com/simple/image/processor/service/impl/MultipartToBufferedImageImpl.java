package com.simple.image.processor.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.simple.image.processor.service.MultipartToBufferedImage;
@Service
public class MultipartToBufferedImageImpl implements MultipartToBufferedImage{
    @Value("${image.width}")
    private int imageWidth;
    
    @Value("${image.height}")
    private int imageHeight;
	private Logger logger = LoggerFactory.getLogger(MultipartToBufferedImageImpl.class);
	@Override
	public BufferedImage convert(MultipartFile imageFile) {
		BufferedImage image = null;
		try{
			MultipartFile multipartFile = Objects.requireNonNull(imageFile);
	        File tempFile;
			tempFile = Files.createTempFile("image_upload_", ".tmp").toFile();
			multipartFile.transferTo(tempFile);
			image = ImageIO.read(tempFile);
			try {
				if(image.getWidth()<=imageWidth||image.getHeight()<=imageHeight)
					return image;
				else
					System.out.println("Fata here");
					throw new Exception();
			}
			catch(Exception e){
	        	logger.error(e.getMessage(), e);
				return null;
			}
        }
        catch (Exception e){
        	logger.error(e.getMessage(), e);
        	return null;
        }
	}

}
