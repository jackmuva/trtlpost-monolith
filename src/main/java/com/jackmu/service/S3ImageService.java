package com.jackmu.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jackmu.model.editorjs.DownloadedImage;
import com.jackmu.model.editorjs.ImageLookup;
import com.jackmu.model.editorjs.UploadedImage;
import com.jackmu.repository.ImageLookupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

@Service
@Profile("!local-profile")
public class S3ImageService implements ImageService{
    @Autowired
    AmazonS3 amazonS3;

    @Autowired
    ImageLookupRepository imageLookupRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(S3ImageService.class);
    private static final String S3_BUCKET_NAME = "trtlmail-images";
    private static final String S3_DOWNLOAD_URL = "https://trtlmail-images.s3.amazonaws.com/";

    private File convertMultiPartToFile(MultipartFile file) {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return convFile;
    }

    @Override
    public String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    @Override
    public String uploadImage(UploadedImage image) {
        File imageFile = convertMultiPartToFile(image.getImage());
        String filename = generateFileName(image.getImage());
        try{
            PutObjectRequest putObjectRequest = new PutObjectRequest(S3_BUCKET_NAME, filename, imageFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest);

            mapImage(filename, image.getEntryId());
            imageFile.delete();
            return filename;
        } catch(Exception e){
            LOGGER.info(e.getMessage());
            return null;
        }
    }

    public void mapImage(String filename, Long entryId){
        ImageLookup image = new ImageLookup(filename, entryId);
        imageLookupRepository.save(image);
    }

    @Override
    public DownloadedImage downloadImage(String filename) {
        DownloadedImage downloadedImage = new DownloadedImage(1, Collections.singletonMap("url",
                S3_DOWNLOAD_URL + filename));
        return downloadedImage;
    }

    @Override
    public void deleteImage(String filename) {
        try {
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(S3_BUCKET_NAME, filename);
            amazonS3.deleteObject(deleteObjectRequest);
            imageLookupRepository.deleteByImageFilename(filename);
        } catch(Exception e){
            LOGGER.info(e.getMessage());
        }
    }

    @Override
    public void deleteImagesInEntry(Long entryId) {
        for(ImageLookup imageLookup : imageLookupRepository.findAllByEntryId(entryId)){
            try {
                String filename = imageLookup.getImageFilename();
                deleteImage(filename);
            } catch(Exception e){
                LOGGER.error(e.getMessage());
            }
        }
    }
}
