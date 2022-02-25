package com.simple.image.processor.controller;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.simple.image.processor.SimpleImageProcessorApplication;
//import com.simple.image.processor.message.ResponseMessage;
//import com.simple.image.processor.service.FileUploadService;
//import com.simple.image.processor.service.base64ToImageService;
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
//  private FileUploadService fileUploadService;
//  private base64ToImageService base64ToImageService;

//    @Value("${image.folder}")
//    private String imageFolder;
    
    static {
        SpringDocUtils.getConfig().addRestControllers(SimpleImageProcessorController.class);
    }
    
    @Bean
    public MultipartResolver multipartResolver() {
        org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1000000);
        return multipartResolver;
    }
    
    @GetMapping("")
    public String uploadImage() {
        return "uploadImage";
    }

	@PostMapping(value ="/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> uploadImage(@RequestParam("image") MultipartFile imageFile, fetchAttributes operations, RedirectAttributes attributes) {
        
      //Variables from HTML Form
//        MultipartFile imageFile = operations.getImage();        
    	System.out.println("Form to Controller inputs via DTO");
        String flipHorizontal = operations.getFlipHorizontal();
        System.out.println("FlipHorizontal=====>  " + flipHorizontal);
        String flipVertical = operations.getFlipVertical();
        System.out.println("FlipVertical=====>  " + flipVertical);
        String rotateDegrees = operations.getRotateDegrees();
        System.out.println("RotateDegrees=====>  " + rotateDegrees);
        String grayscale = operations.getGrayScale();
        System.out.println("GrayScale=====>  " + grayscale);
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

   /*     String base64Image = operations.getBase64Image();
        System.out.println("Base64Image size=====>  " + base64Image.length());
     */   
      //Building response template fields
        Map<String, Object> result = new HashMap<String,Object>();
        result.put("FlipHorizontal",flipHorizontal);
        result.put("FlipVertical",flipVertical);
        result.put("RotateDegrees",rotateDegrees);
        result.put("GrayScale",grayscale);
        result.put("Resize",resize);
        result.put("imgWidth",imgwidth);
        result.put("imgHeight",imgheight);
        result.put("Thumbnail",thumbnail);
        result.put("RotateRight",rotateRight);
        result.put("RotateLeft",rotateLeft);
        
        //Validations
        if(imageFile.isEmpty()) {
        	message = "Please choose file to upload.";
            result.put("message",message);
            HttpHeaders headers = new HttpHeaders();
//            headers.add("Location", "redirect:/");
            attributes.addFlashAttribute("message", message);
            return new ResponseEntity<byte []>(null,headers,HttpStatus.EXPECTATION_FAILED);//ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(result));
        }
        
        if(!(imageFile.isEmpty()) && flipHorizontal==null && flipVertical==null && rotateDegrees=="0" && resize=="100" && grayscale==null && thumbnail==null && rotateRight==null && rotateLeft==null) {
        	message = "Both rotate operations are not permitted at same time. ";
            result.put("message",message);
            HttpHeaders headers = new HttpHeaders();
//            headers.add("Location", "redirect:/");
            attributes.addFlashAttribute("message", message);
            return new ResponseEntity<byte []>(null,headers,HttpStatus.EXPECTATION_FAILED);
            }
        
        if(((!(thumbnail==null)&&!thumbnail.isEmpty()))&&(((!(resize==null)&&!resize.isEmpty())))) {
        	message = "Both resize operations are not permitted at same time. ";
            result.put("message",message);
            HttpHeaders headers = new HttpHeaders();
//            headers.add("Location", "redirect:/");
            attributes.addFlashAttribute("message", message);
            return new ResponseEntity<byte []>(null,headers,HttpStatus.EXPECTATION_FAILED);
            }
        
        if(((!(imgwidth==null)&&!imgwidth.isEmpty()))&&((((imgheight==null)||imgheight.isEmpty())))) {
        	message = "Please enter both height and width. ";
            result.put("message",message);
            HttpHeaders headers = new HttpHeaders();
//            headers.add("Location", "redirect:/");
            attributes.addFlashAttribute("message", message);
            return new ResponseEntity<byte []>(null,headers,HttpStatus.EXPECTATION_FAILED);
            }
        
        if(((!(imgheight==null)&&!imgheight.isEmpty()))&&((((imgwidth==null)||imgwidth.isEmpty())))) {
        	message = "Please enter both height and width. ";
            result.put("message",message);
            HttpHeaders headers = new HttpHeaders();
//            headers.add("Location", "redirect:/");
            attributes.addFlashAttribute("message", message);
            return new ResponseEntity<byte []>(null,headers,HttpStatus.EXPECTATION_FAILED);
            }
        
        if(((!(thumbnail==null)&&!thumbnail.isEmpty()))&&((((!(imgwidth==null)&&!imgwidth.isEmpty()))||(!(imgheight==null)&&!imgheight.isEmpty())))) {
        	message = "Both resize operations are not permitted at same time. ";
            result.put("message",message);
            HttpHeaders headers = new HttpHeaders();
//            headers.add("Location", "redirect:/");
            attributes.addFlashAttribute("message", message);
            return new ResponseEntity<byte []>(null,headers,HttpStatus.EXPECTATION_FAILED);
            }
        
        if(((!(resize==null)&&!resize.isEmpty()))&&((((!(imgwidth==null)&&!imgwidth.isEmpty()))||(!(imgheight==null)&&!imgheight.isEmpty())))) {
        	message = "Both resize operations are not permitted at same time. ";
            result.put("message",message);
            HttpHeaders headers = new HttpHeaders();
//            headers.add("Location", "redirect:/");
            attributes.addFlashAttribute("message", message);
            return new ResponseEntity<byte []>(null,headers,HttpStatus.EXPECTATION_FAILED);
            }
        
        if(!(imageFile.isEmpty()) && flipHorizontal==null && flipVertical==null && ((rotateDegrees==null)||rotateDegrees.isBlank()) && ((resize==null)||resize.isBlank()) && ((imgwidth==null)||imgwidth.isBlank()) && ((imgheight==null)||imgheight.isBlank()) && grayscale==null && thumbnail==null && rotateRight==null && rotateLeft==null) {
        	message = "No changes made to the image. Can't proceed. ";
            result.put("message",message);
            HttpHeaders headers = new HttpHeaders();
//            headers.add("Location", "redirect:/");
            attributes.addFlashAttribute("message", message);
            return new ResponseEntity<byte []>(null,headers,HttpStatus.EXPECTATION_FAILED);}
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //Flip function
        System.out.println("Running flip methods on Image");
        BufferedImage imageFlipHor = flipImage.horizontal(InputImage, flipHorizontal);
        BufferedImage imageFlipVert = flipImage.vertical(imageFlipHor, flipVertical);
        
        //Rotate Function
        System.out.println("Running rotate n degrees method on Image");
        BufferedImage imageRotNDeg = rotateImageService.ndegrees(imageFlipVert, rotateDegrees);
        width          = imageRotNDeg.getWidth();
		height         = imageRotNDeg.getHeight();
        System.out.println("Updated image dimensions after rotate n degrees :"+width+"x"+height);
         
        //Grayscale Function
        System.out.println("Running grayscale method on Image");
        BufferedImage imageGrayscale = grayscaleImageService.grayscale(imageRotNDeg, grayscale);

        //Resize Function
        //Using percentage
        System.out.println("Running resize method on Image");
        BufferedImage imageResize = resizeImageService.resize(imageGrayscale, resize);
        width          = imageResize.getWidth();
		height         = imageResize.getHeight();
        System.out.println("Updated image dimensions after resize :"+width+"x"+height);
        
        //Using Image height and width
        System.out.println("Running resize method on Image");
        BufferedImage imageResizeHW = resizeImageService.resizehw(imageResize, imgwidth, imgheight);
        width          = imageResizeHW.getWidth();
		height         = imageResizeHW.getHeight();
        System.out.println("Updated image dimensions after resize :"+width+"x"+height);
        
        //Thumbnail 
        System.out.println("Running thumbnail method on Image");
        BufferedImage imageThumbnail = resizeImageService.thumbnail(imageResizeHW, thumbnail);
        width          = imageThumbnail.getWidth();
		height         = imageThumbnail.getHeight();
        System.out.println("Updated image dimensions after thumbnail :"+width+"x"+height);
        
        //Rotate Function
        System.out.println("Running rotate methods on Image");
        BufferedImage imageRotRight = rotateImageService.rotateRight(imageThumbnail, rotateRight);
        width          = imageRotRight.getWidth();
		height         = imageRotRight.getHeight();
        System.out.println("Updated image dimensions after rotate right :"+width+"x"+height);
        BufferedImage imageRotLeft = rotateImageService.rotateLeft(imageRotRight, rotateLeft);
        width          = imageRotLeft.getWidth();
		height         = imageRotLeft.getHeight();
        System.out.println("Updated image dimensions after rotate left :"+width+"x"+height);
                
/*        //Save edited File
        System.out.println("Running save method on Image");
        File file = fileUploadService.upload(imageRotLeft);
        if(file == null) {
        	message = "No File : " + imageFile.getOriginalFilename();
            result.put("message",message);
            return null;//ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(result));

        }
*/      //Building Success Response
        message = "Transformed the file successfully - " + imageFile.getOriginalFilename();
        result.put("message",message);
        result.put("Http Status",HttpStatus.OK);
         
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.IMAGE_JPEG);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
			ImageIO.write(imageRotLeft , "jpg", byteArrayOutputStream);
			System.out.println("File written to outputstream");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        byte[] media = byteArrayOutputStream.toByteArray();
        
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
        return responseEntity;

    }

}