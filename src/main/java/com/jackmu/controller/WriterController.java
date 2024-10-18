package com.jackmu.controller;

import com.jackmu.model.Writer;
import com.jackmu.service.WriterService;
import com.jackmu.util.GenericHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/writer")
public class WriterController {
    @Autowired
    private WriterService writerService;

    private static final Logger LOGGER = Logger.getLogger(WriterController.class.getName());

    @PostMapping("/new")
    public GenericHttpResponse postWriter(@RequestBody Writer writer){
        Boolean writerCreated = writerService.createWriter(writer);
        if(writerCreated){
            return new GenericHttpResponse(HttpStatus.OK.value(), "Writer successfully created");
        } else{
            return new GenericHttpResponse(HttpStatus.NOT_ACCEPTABLE.value(), "PenName already taken");
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteWriter(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id){
        if(writerService.fetchWriterByWriterId(id).isEmpty()){
            return new ResponseEntity<>("No writer with that id", HttpStatus.BAD_REQUEST);
        }
        else if(!userDetails.getUsername().equals(writerService.fetchWriterByWriterId(id).get(0).getEmail())){
            return new ResponseEntity<>("Do not have permission to that id", HttpStatus.BAD_REQUEST);
        }
        writerService.deleteWriter(id);
        return new ResponseEntity<>("Writer deleted", HttpStatus.OK);
    }

    @GetMapping("/get/{penName}")
    public Writer getWriter(@PathVariable String penName){
        return writerService.fetchWriterByPenName(penName);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getWriter")
    public List<Writer> getAuthenticatedWriter(@AuthenticationPrincipal UserDetails userDetails){
        return writerService.fetchWriterByEmail(userDetails.getUsername());
    }
}
