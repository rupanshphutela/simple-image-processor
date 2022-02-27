package com.simple.image.processor.controller;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;

import com.simple.image.processor.service.flipImage;
import com.simple.image.processor.service.grayscaleImageService;
import com.simple.image.processor.service.resizeImageService;
import com.simple.image.processor.service.rotateImageService;
import com.simple.image.processor.service.impl.fetchAttributes;
import com.simple.image.processor.service.impl.resizeImageServiceImpl;
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
    
    private Logger logger = LoggerFactory.getLogger(resizeImageServiceImpl.class);

    static {
        SpringDocUtils.getConfig().addRestControllers(SimpleImageProcessorController.class);
    }
    
    @Bean
    public MultipartResolver multipartResolver() {
        org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
        //multipartResolver.setMaxUploadSize(1000000);
        return multipartResolver;
    }
    
    @GetMapping("")
    public String uploadImage() {
        return "uploadImage";
    }

	@PostMapping(value ="/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
//    public ResponseEntity<byte[]> uploadImage(@RequestParam(value="image",required=true) MultipartFile imageFile, fetchAttributes operations, RedirectAttributes attributes) {
    public ResponseEntity<byte[]> uploadImage(@RequestParam(value="image",required=true) MultipartFile imageFile, fetchAttributes operations) {
    
      //Variables from HTML Form
//        MultipartFile imageFile = operations.getImage();        
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

        //Pre-Transformation Validations
        try {
        	if(imageFile.isEmpty()) {
        	message = "Please choose file to upload.";
        	throw new Exception();
        	}
        
        if(((!(thumbnail==null)&&!thumbnail.isEmpty()))&&(((!(resize==null)&&!resize.isEmpty())))) {
        	message = "Both thumbnail/resize with aspect ratio operations are not permitted at same time. ";
        	throw new Exception();
        	}
        
        if(((!(rotateRight==null)&&!rotateRight.isEmpty()))&&(((!(rotateLeft==null)&&!rotateLeft.isEmpty())))) {
        	message = "Both Rotate Left/Right operations are not permitted at same time. ";
        	throw new Exception();
    		}
        
        if(((!(imgwidth==null)&&!imgwidth.isEmpty()))&&((((imgheight==null)||imgheight.isEmpty())))) {
        	message = "Image Height null/empty/non-numerical. Please enter both height and width. ";
        	throw new Exception();
    		}
        
        if(((!(imgheight==null)&&!imgheight.isEmpty()))&&((((imgwidth==null)||imgwidth.isEmpty())))) {
        	message = "Image Width null/empty/non-numerical. Please enter both height and width. ";
        	throw new Exception();
    		}
        
        if(((!(thumbnail==null)&&!thumbnail.isEmpty()))&&((((!(imgwidth==null)&&!imgwidth.isEmpty()))||(!(imgheight==null)&&!imgheight.isEmpty())))) {
        	message = "Both thumbnail/resize with height/width operations are not permitted at same time. ";
        	throw new Exception();
        	}
        
        if(((!(resize==null)&&!resize.isEmpty()))&&((((!(imgwidth==null)&&!imgwidth.isEmpty()))||(!(imgheight==null)&&!imgheight.isEmpty())))) {
        	message = "Both resize with aspect ratio/resize with height/width operations are not permitted at same time.  ";
        	throw new Exception();
        	}

        if(!(imageFile.isEmpty()) && ((flipHorizontal==null)||flipHorizontal.isBlank()) && ((flipVertical==null)||flipVertical.isBlank()) && ((rotateDegrees==null)||rotateDegrees.isBlank()) && ((resize==null)||resize.isBlank()) && ((imgwidth==null)||imgwidth.isBlank()) && ((imgheight==null)||imgheight.isBlank()) && ((grayScale==null)||grayScale.isBlank()) && ((thumbnail==null)||thumbnail.isBlank()) && ((rotateRight==null)||rotateRight.isBlank()) && ((rotateLeft==null)||rotateLeft.isBlank())) {
        	message = "Image selected but no changes are made . Can't proceed. ";
        	throw new Exception();
        	}
        }
        catch(Exception e) {
        	HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Error Message",  message);
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(null, headers, HttpStatus.EXPECTATION_FAILED);
            return responseEntity;
            }
    /*    
        //Base64 to Buffered Image
        System.out.println("Converting to image from base64");
        BufferedImage image = base64ToImageService.transform(base64Image);
        int width          = image.getWidth();
		int height         = image.getHeight();
        System.out.println("Updated image dimensions after read :"+width+"x"+height);
   */     
        //MultipartFile to BufferedImage conversion
        BufferedImage InputImage = null;
        int width          = 0;
        int height         = 0;
		try {
			MultipartFile multipartFile = Objects.requireNonNull(imageFile);
	        File tempFile;
			tempFile = Files.createTempFile("image_upload_", ".tmp").toFile();
			multipartFile.transferTo(tempFile);
	        InputImage = ImageIO.read(tempFile);
	        width          = InputImage.getWidth();
	        height         = InputImage.getHeight();
	        System.out.println("Updated image dimensions after read :"+width+"x"+height);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
        	message = "Unable to convert MultipartFile to BufferedImage";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Error Message",  message);
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(null, headers, HttpStatus.EXPECTATION_FAILED);
            return responseEntity;
            }
		
        //Flip function
        BufferedImage imageFlipHor = null;
        BufferedImage imageFlipVert = null;
        try {
        	System.out.println("Running flip methods on Image");
        	imageFlipHor = flipImage.horizontal(InputImage, flipHorizontal);
        	imageFlipVert = flipImage.vertical(imageFlipHor, flipVertical);
        	width          = imageFlipVert.getWidth();
        	height         = imageFlipVert.getHeight();
        	System.out.println("Updated image dimensions after rotate n degrees :"+width+"x"+height);
        }
        //flipHorizontal/flipVertical restricted to null, empty and "on" values
        catch(Exception e) {
        	message = "Incorrect Data - Flip Operation accepts only \"on\",null  or \"\" as inputs";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Error Message",  message);
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(null, headers, HttpStatus.EXPECTATION_FAILED);
            return responseEntity;
            }
        
        //Rotate Function
        BufferedImage imageRotNDeg = null;
        try {
        	System.out.println("Running rotate n degrees method on Image");
        	imageRotNDeg = rotateImageService.ndegrees(imageFlipVert, rotateDegrees);
        	width          = imageRotNDeg.getWidth();
        	height         = imageRotNDeg.getHeight();
        	System.out.println("Updated image dimensions after rotate n degrees :"+width+"x"+height);
        }
        catch(Exception e){
        	message = "Incorrect Data - Rotate N degrees Operation accepts only integers as inputs";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Error Message",  message);
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(null, headers, HttpStatus.EXPECTATION_FAILED);
            return responseEntity;
            }

        //Grayscale Function
        BufferedImage imageGrayscale = null;
        try {
        	System.out.println("Running grayscale method on Image");
        	imageGrayscale = grayscaleImageService.grayscale(imageRotNDeg, grayScale);
        	width          = imageGrayscale.getWidth();
        	height         = imageGrayscale.getHeight();
        	System.out.println("Updated image dimensions after thumbnail :"+width+"x"+height);
        }
        //grayscale restricted to null, empty and "on" values
        catch(Exception e) {
        	message = "Incorrect Data - Grayscale Operation accepts only \"on\",null  or \"\" as inputs";
        	HttpHeaders headers = new HttpHeaders();
        	headers.setContentType(MediaType.APPLICATION_JSON);
        	headers.set("Error Message",  message);
        	ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(null, headers, HttpStatus.EXPECTATION_FAILED);
            return responseEntity;
            }
        
    	//Resize Function using percentage
        BufferedImage imageResize = null;
        try {
        	System.out.println("Running resize method on Image");
        	imageResize = resizeImageService.resize(imageGrayscale, resize);
        	width          = imageResize.getWidth();
        	height         = imageResize.getHeight();
        	System.out.println("Updated image dimensions after resize :"+width+"x"+height);
        }
        //Resize by Aspect Ratio restricted to only integers
        catch(Exception e) {
        	message = "Incorrect Data - Resize by Aspect Ratio Operation accepts only integers as inputs";
        	HttpHeaders headers = new HttpHeaders();
        	headers.setContentType(MediaType.APPLICATION_JSON);
        	headers.set("Error Message",  message);
        	ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(null, headers, HttpStatus.EXPECTATION_FAILED);
            return responseEntity;
            }
        
    	//Resize Function using Image height and width
        BufferedImage imageResizeHW = null;
        try {
        	System.out.println("Running resize method on Image");
        	imageResizeHW = resizeImageService.resizehw(imageResize, imgwidth, imgheight);
        	width          = imageResizeHW.getWidth();
        	height         = imageResizeHW.getHeight();
        	System.out.println("Updated image dimensions after resize :"+width+"x"+height);
        }
        //Resize by height/width restricted to only integers 
        catch(Exception e) {
        	message = "Incorrect Data - Resize by height/width Operation accepts only integers as inputs";
        	HttpHeaders headers = new HttpHeaders();
        	headers.setContentType(MediaType.APPLICATION_JSON);
        	headers.set("Error Message",  message);
        	ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(null, headers, HttpStatus.EXPECTATION_FAILED);
            return responseEntity;
        	}
        
        //Thumbnail 
        BufferedImage imageThumbnail = null;
        try {
        	System.out.println("Running thumbnail method on Image");
        	imageThumbnail = resizeImageService.thumbnail(imageResizeHW, thumbnail);
        	width          = imageThumbnail.getWidth();
        	height         = imageThumbnail.getHeight();
        	System.out.println("Updated image dimensions after thumbnail :"+width+"x"+height);
        }
        //thumbnail restricted to null, empty and "on" values
        catch(Exception e) {
        	message = "Incorrect Data - Thumbnail Operation accepts only \"on\",null  or \"\" as inputs";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(null, headers, HttpStatus.EXPECTATION_FAILED);
            return responseEntity;
        }
        
    	//Rotate Left/Right Function
        BufferedImage imageRotRight = null;
        BufferedImage imageRotLeft = null;
        try {
        	System.out.println("Running rotate methods on Image");
        	imageRotRight = rotateImageService.rotateRight(imageThumbnail, rotateRight);
        	width          = imageRotRight.getWidth();
        	height         = imageRotRight.getHeight();
        	System.out.println("Updated image dimensions after rotate right :"+width+"x"+height);
        	imageRotLeft = rotateImageService.rotateLeft(imageRotRight, rotateLeft);
        	width          = imageRotLeft.getWidth();
        	height         = imageRotLeft.getHeight();
        	System.out.println("Updated image dimensions after rotate left :"+width+"x"+height);
        }
        //rotateRight/rotateLeft restricted to null, empty and "on" values
        catch(Exception e) {
        	message = "Incorrect Data - Rotate Left/Right Operation accepts only \"on\",null  or \"\" as inputs";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Error Message",  message);
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(null, headers, HttpStatus.EXPECTATION_FAILED);
            return responseEntity;
        }
                
      //Building Success Response
        try {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.IMAGE_JPEG);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(imageRotLeft , "jpg", byteArrayOutputStream);
		System.out.println("File written to outputstream");
        message = "Transformed the file successfully - " + imageFile.getOriginalFilename();
        headers.set("Success Message",  message);
        byte[] media = byteArrayOutputStream.toByteArray();
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
        return responseEntity;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
}

/*backup
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