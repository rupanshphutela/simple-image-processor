package com.simple.image.processor.controller;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.simple.image.processor.message.ResponseMessage;
import com.simple.image.processor.service.FileUploadService;
import com.simple.image.processor.service.base64ToImageService;
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
    private base64ToImageService base64ToImageService;
    @Autowired
    private flipImage flipImage;
    @Autowired
    private grayscaleImageService grayscaleImageService;
    @Autowired
    private resizeImageService resizeImageService;
    @Autowired
    private rotateImageService rotateImageService;
    @Autowired
    private FileUploadService fileUploadService;

    @Value("${image.folder}")
    private String imageFolder;

    @GetMapping("")
    public String uploadImage() {
        return "uploadImage";
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<ResponseMessage> uploadImage(@RequestParam("image") MultipartFile imageFile,fetchAttributes operations, RedirectAttributes redirectAttributes) throws IOException {
        
      //Variables from HTML Form
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
        System.out.println("Resize=====>  " + imgheight);
        String imgwidth = operations.getImgwidth();
        System.out.println("Resize=====>  " + imgwidth);
        String resize = operations.getResize();
        System.out.println("Resize=====>  " + resize);
        String thumbnail = operations.getThumbnail();
        System.out.println("Thumbnail=====>  " + thumbnail);
        String rotateRight = operations.getRotateRight();
        System.out.println("RotateRight=====>  " + rotateRight);
        String rotateLeft = operations.getRotateLeft();
        System.out.println("RotateLeft=====>  " + rotateLeft);
        String base64Image = operations.getBase64Image();
        System.out.println("Base64Image size=====>  " + base64Image.length());
        
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
            redirectAttributes.addFlashAttribute("errorMessage", message);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(result));
        }
        
        if(!(imageFile.isEmpty()) && flipHorizontal==null && flipVertical==null && rotateDegrees=="0" && resize=="100" && grayscale==null && thumbnail==null && rotateRight==null && rotateLeft==null) {
        	message = "Both rotate operations are not permitted at same time. ";
            result.put("message",message);
            redirectAttributes.addFlashAttribute("errorMessage", message);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(result));
        }
        
        if(((!(thumbnail==null)&&!thumbnail.isEmpty()))&&(((!(resize==null)&&!resize.isEmpty()))&&(((!(imgwidth==null)&&!imgwidth.isEmpty()))||((!(imgheight==null)&&!imgheight.isEmpty()))))) {
        	message = "Both resize operations are not permitted at same time. ";
            result.put("message",message);
            redirectAttributes.addFlashAttribute("errorMessage", message);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(result));
        }
        
        if(!(imageFile.isEmpty()) && flipHorizontal==null && flipVertical==null && rotateDegrees=="" && resize=="" && imgwidth=="" && imgheight=="" && grayscale==null && thumbnail==null && rotateRight==null && rotateLeft==null) {
        	message = "No changes made to the image. Can't proceed. ";
            result.put("message",message);
            redirectAttributes.addFlashAttribute("errorMessage", message);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(result));
        }
        
        //Base64 to Buffered Image
        System.out.println("Converting to image from base64");
        BufferedImage image = base64ToImageService.transform(base64Image);
        int width          = image.getWidth();
		int height         = image.getHeight();
        System.out.println("Updated image dimensions after read :"+width+"x"+height);
/* Using above instead - Base64(generated by Front-end) to BufferImage conversion    
        //MultipartFile to BufferedImage conversion
        MultipartFile mulpartFile = Objects.requireNonNull(imageFile);
        File tempFile = Files.createTempFile("image_upload_", ".tmp").toFile();
        mulpartFile.transferTo(tempFile);
        BufferedImage image = ImageIO.read(tempFile);
        int width          = image.getWidth();
		int height         = image.getHeight();
        System.out.println("Updated image dimensions after read :"+width+"x"+height);
*/
        //Flip function
        System.out.println("Running flip methods on Image");
        BufferedImage imageFlipHor = flipImage.horizontal(image, flipHorizontal);
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
                
        //Save edited File
        System.out.println("Running save method on Image");
        File file = fileUploadService.upload(imageRotLeft);
        if(file == null) {
        	message = "No File : " + imageFile.getOriginalFilename();
            result.put("message",message);
            redirectAttributes.addFlashAttribute("errorMessage", message);   
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(result));

        }
        //Building Success Response
        message = "Uploaded the file successfully - " + imageFile.getOriginalFilename();
        result.put("message",message);
        result.put("Http Status",HttpStatus.OK);
    /*
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(imageRotLeft , "jpg", byteArrayOutputStream);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageInByte = baos.toByteArray();
*/
        redirectAttributes.addFlashAttribute("successMessage", message);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(result));

    }

}