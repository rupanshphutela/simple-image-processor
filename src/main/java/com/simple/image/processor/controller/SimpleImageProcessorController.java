package com.simple.image.processor.controller;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;


import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;

import com.simple.image.processor.service.MultipartToBufferedImage;
import com.simple.image.processor.service.flipImage;
import com.simple.image.processor.service.grayscaleImageService;
import com.simple.image.processor.service.resizeImageService;
import com.simple.image.processor.service.rotateImageService;
import com.simple.image.processor.service.impl.fetchAttributes;
@Controller
@CrossOrigin("http://localhost:8080")
public class SimpleImageProcessorController {
	String message="";
    @Autowired
    private flipImage flipImage;
    @Autowired
    private grayscaleImageService grayscaleImageService;
    @Autowired
    private resizeImageService resizeImageService;
    @Autowired
    private rotateImageService rotateImageService;
    @Autowired
    private MultipartToBufferedImage MultipartToBufferedImage;
    
    @Value("${image.size}")
    private int imageSize;
    
    @Value("${image.width}")
    private int imageWidth;
    
    @Value("${image.height}")
    private int imageHeight;
    
    @Value("${image.max_resize}")
    private int imageMaxResize;
    
    static {
        SpringDocUtils.getConfig().addRestControllers(SimpleImageProcessorController.class);
    }
    
    @Bean
    public MultipartResolver multipartResolver() {
        org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
  //      multipartResolver.setMaxUploadSize(1024*1024*10); //10MB
        return multipartResolver;
    }
    
    @GetMapping("")
    public String uploadImage() {
        return "uploadImage";
    }

	@PostMapping(value ="/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Image Transformation Application", responses = {
		      @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
		      @ApiResponse(responseCode = "406", description = "Values not Acceptable", content = @Content),
		      @ApiResponse(responseCode = "412", description = "Pre-condition Failure", content = @Content(schema = @Schema(hidden = true))) })
    public ResponseEntity<byte[]> uploadImage(@RequestParam(value="image",required=true) MultipartFile imageFile, fetchAttributes operations) {
    
      //Variables from HTML Form/DTO
    	System.out.println("Form to Controller inputs via DTO");
        String flipHorizontal = operations.getFlipHorizontal();
        System.out.println("FlipHorizontal=====>  " + flipHorizontal);
        String flipVertical = operations.getFlipVertical();
        System.out.println("FlipVertical=====>  " + flipVertical);
        String rotateDegrees = operations.getRotateDegrees();
        System.out.println("RotateDegrees=====>  " + rotateDegrees);
        String grayScale = operations.getGrayScale();
        System.out.println("GrayScale=====>  " + grayScale);
        String imgheight = operations.getImgheight();
        System.out.println("imgHeight=====>  " + imgheight);
        String imgwidth = operations.getImgwidth();
        System.out.println("imgWidth=====>  " + imgwidth);
        String resize = operations.getResize();
        System.out.println("Resize=====>  " + resize);
        String thumbnail = operations.getThumbnail();
        System.out.println("Thumbnail=====>  " + thumbnail);
        String rotateRight = operations.getRotateRight();
        System.out.println("RotateRight=====>  " + rotateRight);
        String rotateLeft = operations.getRotateLeft();
        System.out.println("RotateLeft=====>  " + rotateLeft);
        
        //Pre-validation response header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Pre-Transformation Validations
        try {
        	if(imageFile.isEmpty()) {
        		message = "Please choose file to upload.";
        		headers.set("Error Message", "Pre Condition Failure - " + message);
        		throw new Exception();
        		}
        	
        	if (imageFile.getSize()>imageSize) {
        		message = "File too large. Please select file size less than 10MB";
        		headers.set("Error Message",  "Pre Condition Failure - " + message);
        		throw new Exception();
        	}        	
        
        if(((!(thumbnail==null)&&!thumbnail.isEmpty()))&&(((!(resize==null)&&!resize.isEmpty())))) {
	        	message = "Both thumbnail/resize with aspect ratio operations are not permitted at same time. ";
	        	headers.set("Error Message",  "Pre Condition Failure - " + message);
	        	throw new Exception();
	        	}
        
        if(((!(rotateRight==null)&&!rotateRight.isEmpty()))&&(((!(rotateLeft==null)&&!rotateLeft.isEmpty())))) {
	        	message = "Both Rotate Left/Right operations are not permitted at same time. ";
	        	headers.set("Error Message",  "Pre Condition Failure - " + message);
	        	throw new Exception();
	    		}
        
        if(((!(imgwidth==null)&&!imgwidth.isEmpty()))&&((imgheight==null)||imgheight.isEmpty())) {
	        	message = "Image Height null/empty/non-numerical. Please enter both height and width. ";
	        	headers.set("Error Message",  "Pre Condition Failure - " + message);
	        	throw new Exception();
	    		}
        
        if(((!(imgheight==null)&&!imgheight.isEmpty()))&&((imgwidth==null)||imgwidth.isEmpty())) {
	        	message = "Image Width null/empty/non-numerical. Please enter both height and width. ";
	        	headers.set("Error Message",  "Pre Condition Failure - " + message);
	        	throw new Exception();
	    		}
        
        if(((!(thumbnail==null)&&!thumbnail.isEmpty()))&&((((!(imgwidth==null)&&!imgwidth.isEmpty()))||(!(imgheight==null)&&!imgheight.isEmpty())))) {
	        	message = "Both thumbnail/resize with height/width operations are not permitted at same time. ";
	        	headers.set("Error Message",  "Pre Condition Failure - " + message);
	        	throw new Exception();
	        	}
        
        if(((!(resize==null)&&!resize.isEmpty()))&&((((!(imgwidth==null)&&!imgwidth.isEmpty()))||(!(imgheight==null)&&!imgheight.isEmpty())))) {
	        	message = "Both resize with aspect ratio/resize with height/width operations are not permitted at same time.  ";
	        	headers.set("Error Message",  "Pre Condition Failure - " + message);
	        	throw new Exception();
	        	}

        if(!(imageFile.isEmpty()) && ((flipHorizontal==null)||flipHorizontal.isBlank()) && ((flipVertical==null)||flipVertical.isBlank()) && ((rotateDegrees==null)||rotateDegrees.isBlank()) && ((resize==null)||resize.isBlank()) && ((imgwidth==null)||imgwidth.isBlank()) && ((imgheight==null)||imgheight.isBlank()) && ((grayScale==null)||grayScale.isBlank()) && ((thumbnail==null)||thumbnail.isBlank()) && ((rotateRight==null)||rotateRight.isBlank()) && ((rotateLeft==null)||rotateLeft.isBlank())) {
	        	message = "Image selected but no changes are made . Can't proceed. ";
	        	headers.set("Error Message",  "Pre Condition Failure - " + message);
	        	throw new Exception();
	        	}
        
        if(((!(imgwidth==null)&&!imgwidth.isEmpty()))&&(!(imgheight==null)||!(imgheight.isEmpty()))) {
        	try {
        		int userInpWidth = Integer.parseInt(imgwidth);
        		int userInpHeight = Integer.parseInt(imgheight);
        		if(userInpWidth>imageWidth || userInpHeight>imageHeight) {
            		throw new Exception();
            		}
        	}
        	catch(Exception e) {
        		message = "Passed height/width resize parameter is too large. Please pass integers <=5000 for each";
        		headers.set("Error Message",  "Pre Condition Failure - " + message);
        		throw new Exception();
        	}
        }
        
        if(((!(imgwidth==null)&&!imgwidth.isEmpty()))&&(!(imgheight==null)||!(imgheight.isEmpty()))) {
        	try {
        		int userInpWidth = Integer.parseInt(imgwidth);
        		int userInpHeight = Integer.parseInt(imgheight);
        		if(userInpWidth<0 || userInpHeight<0) {
            		throw new Exception();
            		}
        	}
        	catch(Exception e) {
        		message = "One or both of Passed height/width resize parameters are < 0 . Please pass integers between 1 to 5000 for each";
        		headers.set("Error Message",  "Pre Condition Failure - " + message);
        		throw new Exception();
        	}
        }
        
        if(!(resize==null)&&!resize.isEmpty()) {
        	try {
        		int userInpResize = Integer.parseInt(resize);
        		if(userInpResize>imageMaxResize) {
            		throw new Exception();
            		}
        	}
        	catch(Exception e) {
        		message = "Passed resize by aspect ratio parameter is too large. Please pass integers <=250%";
        		headers.set("Error Message",  "Pre Condition Failure - " + message);
        		throw new Exception();
        	}
        }
        
        if(!(resize==null)&&!resize.isEmpty()) {
        	try {
        		int userInpResize = Integer.parseInt(resize);
        		if(userInpResize<0) {
            		throw new Exception();
            		}
        	}
        	catch(Exception e) {
        		message = "Passed resize by aspect ratio parameter <=0. Please pass integers between 1 and 250%";
        		headers.set("Error Message",  "Pre Condition Failure - " + message);
        		throw new Exception();
        	}
        }
        
        }
        catch(Exception e) {
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(null, headers, HttpStatus.PRECONDITION_FAILED);
            return responseEntity;
            }
  
        int width          = 0;
        int height         = 0;
		//Operation output variables
        BufferedImage imageInput = null;
		BufferedImage imageFlipHor = null;
        BufferedImage imageFlipVert = null;
        BufferedImage imageRotNDeg = null;
        BufferedImage imageGrayscale = null;
        BufferedImage imageResize = null;
        BufferedImage imageResizeHW = null;
        BufferedImage imageThumbnail = null;
        BufferedImage imageRotRight = null;
        BufferedImage imageRotLeft = null;
		try {
	        //MultipartFile to BufferedImage conversion
			imageInput = MultipartToBufferedImage.convert(imageFile);
	        width          = imageInput.getWidth();
	        height         = imageInput.getHeight();
	        System.out.println("Image dimensions after MultipartFile to BufferedImage conversion :"+width+"x"+height);
		
        	//Flip Function
        	System.out.println("Running flip methods on Image");
        	imageFlipHor = flipImage.horizontal(imageInput, flipHorizontal);
        	imageFlipVert = flipImage.vertical(imageFlipHor, flipVertical);
        	width          = imageFlipVert.getWidth();
        	height         = imageFlipVert.getHeight();
        	System.out.println("Updated image dimensions after flip method :"+width+"x"+height);

        	//Rotate Function
        	System.out.println("Running rotate n degrees method on Image");
        	imageRotNDeg = rotateImageService.ndegrees(imageFlipVert, rotateDegrees);
        	width          = imageRotNDeg.getWidth();
        	height         = imageRotNDeg.getHeight();
        	System.out.println("Updated image dimensions after rotate n degrees :"+width+"x"+height);

        	//Grayscale Function
        	System.out.println("Running grayscale method on Image");
        	imageGrayscale = grayscaleImageService.grayscale(imageRotNDeg, grayScale);
        	width          = imageGrayscale.getWidth();
        	height         = imageGrayscale.getHeight();
        	System.out.println("Updated image dimensions after thumbnail :"+width+"x"+height);
      
        	//Resize Function using percentage
        	System.out.println("Running resize method on Image");
        	imageResize = resizeImageService.resize(imageGrayscale, resize);
        	width          = imageResize.getWidth();
        	height         = imageResize.getHeight();
        	System.out.println("Updated image dimensions after resize by aspect ratio :"+width+"x"+height);
   
        	//Resize Function using Image height and width
        	System.out.println("Running resize method on Image");
        	imageResizeHW = resizeImageService.resizehw(imageResize, imgwidth, imgheight);
        	width          = imageResizeHW.getWidth();
        	height         = imageResizeHW.getHeight();
        	System.out.println("Updated image dimensions after resize by height/width :"+width+"x"+height);

        	//Thumbnail 
        	System.out.println("Running thumbnail method on Image");
        	imageThumbnail = resizeImageService.thumbnail(imageResizeHW, thumbnail);
        	width          = imageThumbnail.getWidth();
        	height         = imageThumbnail.getHeight();
        	System.out.println("Updated image dimensions after thumbnail :"+width+"x"+height);
   
        	//Rotate Left/Right Function
        	System.out.println("Running rotate methods on Image");
        	imageRotRight = rotateImageService.rotateRight(imageThumbnail, rotateRight);
        	width          = imageRotRight.getWidth();
        	height         = imageRotRight.getHeight();
        	System.out.println("Updated image dimensions after rotate right :"+width+"x"+height);
        	imageRotLeft = rotateImageService.rotateLeft(imageRotRight, rotateLeft);
        	width          = imageRotLeft.getWidth();
        	height         = imageRotLeft.getHeight();
        	System.out.println("Updated image dimensions after rotate left :"+width+"x"+height);
        	
        	//Building Success Response
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());
            String extension = FilenameUtils.getExtension(imageFile.getOriginalFilename());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            if(extension.equals("jpeg")||extension.equals("jpg")) {
            	headers.setContentType(MediaType.IMAGE_JPEG);
        		ImageIO.write(imageRotLeft , "jpg", byteArrayOutputStream);
            }
            else if(extension.equals("gif")) {
            	headers.setContentType(MediaType.IMAGE_GIF);
        		ImageIO.write(imageRotLeft , "gif", byteArrayOutputStream);
            }
            else if(extension.equals("png")) {
            	headers.setContentType(MediaType.IMAGE_PNG);
        		ImageIO.write(imageRotLeft , "png", byteArrayOutputStream);
            }
    		System.out.println("File written to outputstream");
            message = "Transformed the file successfully - " + imageFile.getOriginalFilename();
            headers.set("Success Message - ",  message);
            byte[] media = byteArrayOutputStream.toByteArray();
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
            return responseEntity;
        }
        catch(Exception e) {
			e.printStackTrace();
			if(imageInput==null)
				message="Either File is not a JPEG image or image height/width > 5000 px. Unable to process. ";
        	else if(imageFlipHor==null)         	
        		message="Flip Operation accepts only \"on\", null  or \"\" as inputs";
        	else if(imageFlipVert==null) 
        		message="Flip Operation accepts only \"on\", null  or \"\" as inputs";
        	else if(imageRotNDeg==null) 
        		message="Rotate N degrees Operation accepts only integers as inputs";
        	else if(imageGrayscale==null) 
        		message="Grayscale Operation accepts only \"on\", null  or \"\" as inputs";
        	else if(imageResize==null) 
        		message="Resize by Aspect Ratio Operation accepts only integers as inputs";
        	else if(imageResizeHW==null) 
        		message="Resize by height/width Operation accepts only integers as inputs";
        	else if(imageThumbnail==null) 
        		message="Thumbnail Operation accepts only \"on\", null  or \"\" as inputs";
        	else if(imageRotRight==null) 
        		message="Rotate Left/Right Operation accepts only \"on\", null  or \"\" as inputs";
        	else if(imageRotLeft==null) 
        		message="Rotate Left/Right Operation accepts only \"on\", null  or \"\" as inputs";
            headers.set("Error Message", "Incorrect Data - " + message);
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(null, headers, HttpStatus.NOT_ACCEPTABLE);
            return responseEntity;
            }
    }
}
/*
 * 
 backup - 1 March 2022
 
 //    public ResponseEntity<byte[]> uploadImage(@RequestParam(value="image",required=true) MultipartFile imageFile, fetchAttributes operations, RedirectAttributes attributes) {

 //        MultipartFile imageFile = operations.getImage();        

     /*    
        //Base64 to Buffered Image
        System.out.println("Converting to image from base64");
        BufferedImage image = base64ToImageService.transform(base64Image);
        int width          = image.getWidth();
		int height         = image.getHeight();
        System.out.println("Updated image dimensions after read :"+width+"x"+height);
     


backup - old
 * 
 * Original imports and references
 * 
 * //import java.util.HashMap;
//import java.util.Map;
//import com.simple.image.processor.message.ResponseMessage;
//import com.simple.image.processor.service.FileUploadService;
//import com.simple.image.processor.service.base64ToImageService;
//private FileUploadService fileUploadService;
//private base64ToImageService base64ToImageService;

//@Value("${image.folder}")
//private String imageFolder;
 * 
 * 
 * original base 64
 * 
 *      String base64Image = operations.getBase64Image();
        System.out.println("Base64Image size=====>  " + base64Image.length());
        
 *
 * original string response
 *
      //Building response template fields
        Map<String, Object> result = new HashMap<String,Object>();
        result.put("FlipHorizontal",flipHorizontal);
        result.put("FlipVertical",flipVertical);
        result.put("RotateDegrees",rotateDegrees);
        result.put("GrayScale",grayScale);
        result.put("Resize",resize);
        result.put("imgWidth",imgwidth);
        result.put("imgHeight",imgheight);
        result.put("Thumbnail",thumbnail);
        result.put("RotateRight",rotateRight);
        result.put("RotateLeft",rotateLeft);
        
 *
 *Success response loop
 *               
 *               result.put("Http Status",HttpStatus.OK);
 *
 * 
 * 
 * 
 * Original exception details with comments
 * 
 *         catch(Exception e) {
        	logger.error(e.getMessage(), e);
        	message = "Incorrect Data - Rotate Left/Right Operation accepts only \"on\",null  or \"\" as inputs";
            result.put("message",message);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.add("Location", "redirect:/");
            headers.set("Error Message",  message);
//            attributes.addFlashAttribute("message", message);
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(null, headers, HttpStatus.OK);
            return responseEntity;//new ResponseEntity<byte []>(null,headers,HttpStatus.EXPECTATION_FAILED);//ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(result));
        }
 *
 *
 * Original save file function to path
 * 
 * /*        //Save edited File
        System.out.println("Running save method on Image");
        File file = fileUploadService.upload(imageRotLeft);
        if(file == null) {
        	message = "No File : " + imageFile.getOriginalFilename();
            result.put("message",message);
            return null;//ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(result));

        }
 *
 *
*/