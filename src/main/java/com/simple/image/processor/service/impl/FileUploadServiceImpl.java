package com.simple.image.processor.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.simple.image.processor.service.FileUploadService;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${image.folder}")
    private String imageFolder;

    private Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    @Override
    public File upload(BufferedImage imageFile) {
        try {
        	
        	File outputfile = new File(imageFolder+"image.jpg");
        	System.out.println(imageFolder+"image.jpg");
        	ImageIO.write(imageFile, "jpg", outputfile);
/*            Path path = Paths.get(imageFolder, imageFile.getOriginalFilename());
            Files.write(path, imageFile.getBytes()); 
            return outputfile.toFile();*/
            return outputfile;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
