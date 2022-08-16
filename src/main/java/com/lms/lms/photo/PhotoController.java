package com.lms.lms.photo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/book/photo")
public class PhotoController {
    private PhotoService photoService;
    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }


    @GetMapping(path = "/get/bybookid/")
    public Photo getPhotoByBookId(@RequestParam Long bookId){
        return photoService.getPhotoByBookId(bookId);
    }

    @GetMapping(path="get/{photoId}")
    public Photo getPhotoById(@PathVariable("photoId") Long photoId){
        return photoService.getPhotoById(photoId);
    }

    @PostMapping(path = "/add/{bookId}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public Photo addPhoto(@PathVariable("bookId") Long bookId, @RequestBody Photo photo){
        return photoService.addPhoto(photo, bookId);
    }

    @DeleteMapping(path = "/delete/{photoId}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity deletePhoto( @PathVariable("photoId") Long id ){
        photoService.deletePhoto(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
