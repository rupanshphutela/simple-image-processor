package com.simple.image.processor.service.impl;

import java.io.Serializable;

//DTO Object
public class fetchAttributes implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8686834337329208533L;
	public String flipHorizontal;
    public String flipVertical;
    public String rotateDegrees;
    public String grayScale;
    public String resize;
    public String imgheight;
    public String imgwidth;
    public String thumbnail;
    public String rotateRight;
    public String rotateLeft;
//    public String base64Image;
	
    public String getFlipHorizontal() {
		return flipHorizontal;
	}
	public void setFlipHorizontal(String flipHorizontal) {
		this.flipHorizontal = flipHorizontal;
	}
	public String getFlipVertical() {
		return flipVertical;
	}
	public void setFlipVertical(String flipVertical) {
		this.flipVertical = flipVertical;
	}
	public String getRotateDegrees() {
		return rotateDegrees;
	}
	public void setRotateDegrees(String rotateDegrees) {
		this.rotateDegrees = rotateDegrees;
	}
	public String getGrayScale() {
		return grayScale;
	}
	public void setGrayScale(String grayScale) {
		this.grayScale = grayScale;
	}
	public String getResize() {
		return resize;
	}
	public void setResize(String resize) {
		this.resize = resize;
	}
	public String getImgheight() {
		return imgheight;
	}
	public void setImgheight(String imgheight) {
		this.imgheight = imgheight;
	}
	public String getImgwidth() {
		return imgwidth;
	}
	public void setImgwidth(String imgwidth) {
		this.imgwidth = imgwidth;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getRotateRight() {
		return rotateRight;
	}
	public void setRotateRight(String rotateRight) {
		this.rotateRight = rotateRight;
	}
	public String getRotateLeft() {
		return rotateLeft;
	}
	public void setRotateLeft(String rotateLeft) {
		this.rotateLeft = rotateLeft;
	}
	
/*	public String getBase64Image() {
		return base64Image;
	}
	public void setBase64Image(String base64Image) {
		this.base64Image = base64Image;
	}
*/
    @Override
    public String toString() {
        return "fetchAttributes [flipHorizontal=" + flipHorizontal + ", flipVertical=" + flipVertical + ", rotateDegrees=" + rotateDegrees + ", grayScale=" + grayScale + ", resize=" + resize + ", thumbnail=" + thumbnail + ", rotateRight=" + rotateRight + ", rotateLeft=" + rotateLeft + "]";
//        return "fetchAttributes [flipHorizontal=" + flipHorizontal + ", flipVertical=" + flipVertical + ", rotateDegrees=" + rotateDegrees + ", grayScale=" + grayScale + ", resize=" + resize + ", thumbnail=" + thumbnail + ", rotateRight=" + rotateRight + ", rotateLeft=" + rotateLeft + ", base64Image=" + base64Image + "]";
    }
}

