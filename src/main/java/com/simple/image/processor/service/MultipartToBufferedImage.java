package com.simple.image.processor.service;

import java.awt.image.BufferedImage;

import org.springframework.web.multipart.MultipartFile;

public interface MultipartToBufferedImage {
	BufferedImage convert(MultipartFile imageFile);
}
