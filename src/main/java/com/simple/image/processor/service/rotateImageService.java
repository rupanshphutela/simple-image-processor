package com.simple.image.processor.service;

import java.awt.image.BufferedImage;

public interface rotateImageService {
	BufferedImage ndegrees(BufferedImage imageFile, String rotateDegrees);
	BufferedImage rotateRight(BufferedImage imageFile, String rotateRight);
	BufferedImage rotateLeft(BufferedImage imageFile, String rotateLeft);
	}
