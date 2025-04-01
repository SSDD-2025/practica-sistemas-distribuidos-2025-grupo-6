package es.dlj.onlinestore.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.dlj.onlinestore.dto.ImageDTO;
import es.dlj.onlinestore.dto.UserDTO;
import es.dlj.onlinestore.service.ImageService;
import es.dlj.onlinestore.service.UserService;

@RestController
@RequestMapping("/api/image")
public class ImageRestController {
    
    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getImage(@PathVariable Long id){
        return imageService.loadImage(id);
    }

    

    @GetMapping("/user")
    public ResponseEntity<Object> getUserImage(){
        UserDTO user = userService.getLoggedUserDTO();
        if (user == null) return imageService.loadDefaultImage();
        ImageDTO image = user.profilePhoto();
        if (image == null) return imageService.loadDefaultImage();
        return imageService.loadImage(image.id());
    }
}
