package com.jackmu.controller;

import com.jackmu.model.editorjs.DownloadedImage;
import com.jackmu.model.editorjs.UploadedImage;
import com.jackmu.model.editorjs.UrlLink;
import com.jackmu.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("api")
public class EditorJsController {
    @Autowired
    ImageService imageService;
    private static final Logger LOGGER = LoggerFactory.getLogger(EditorJsController.class);

    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/image/save", consumes = { "multipart/form-data" })
    public ResponseEntity<DownloadedImage> postImage(@ModelAttribute UploadedImage image) {
        String filename = imageService.uploadImage(image);
        DownloadedImage downloadedImage = imageService.downloadImage(filename);
        return new ResponseEntity<>(downloadedImage, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/fetchUrl")
    public ResponseEntity<UrlLink> getUrl(@RequestParam String url){
        UrlLink urlLink = new UrlLink(1, Collections.singletonMap("url", url));
        return new ResponseEntity<>(urlLink, HttpStatus.OK);
    }

    //TODO: scheduled service that deletes unused images
}
