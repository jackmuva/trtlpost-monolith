package com.jackmu.service;

import com.jackmu.model.editorjs.DownloadedImage;
import com.jackmu.model.editorjs.UploadedImage;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage(UploadedImage image);
    DownloadedImage downloadImage(String filename);
    void deleteImage(String filename);
    void deleteImagesInEntry(Long entryId);
    String generateFileName(MultipartFile multiPart);
}
