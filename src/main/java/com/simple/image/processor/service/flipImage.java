package com.simple.image.processor.service;

import java.awt.image.BufferedImage;

public interface flipImage {
	BufferedImage horizontal(BufferedImage imageFile, String flipHorizontal);
	
	BufferedImage vertical(BufferedImage imageFile, String flipVertical);

}
