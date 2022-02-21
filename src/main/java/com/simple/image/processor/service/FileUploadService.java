package com.simple.image.processor.service;

import java.awt.image.BufferedImage;
import java.io.File;

public interface FileUploadService {
	
	File upload(BufferedImage image);

}



