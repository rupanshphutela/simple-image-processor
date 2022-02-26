package com.simple.image.processor.service;

import java.awt.image.BufferedImage;

public interface grayscaleImageService {
BufferedImage grayscale(BufferedImage imageFile, String grayScale);
}