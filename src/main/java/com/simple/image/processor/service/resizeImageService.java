package com.simple.image.processor.service;

import java.awt.image.BufferedImage;

public interface resizeImageService {
	BufferedImage resize(BufferedImage imageFile, String resize);
	BufferedImage resizehw(BufferedImage imageFile, String imgwidth, String imgheight);
	BufferedImage thumbnail(BufferedImage imageFile, String thumbnail);	
	}
