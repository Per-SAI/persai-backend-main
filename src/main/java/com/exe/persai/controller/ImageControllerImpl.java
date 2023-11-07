package com.exe.persai.controller;

import com.exe.persai.controller.api.ImageController;
import com.exe.persai.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageControllerImpl implements ImageController {

    private final ImageService imageService;

    @Override
    public ResponseEntity<byte[]> viewImageByImageName(String feImageName) {
        byte[] imageData = imageService.getImageByFeImageName(feImageName);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(MediaType.IMAGE_PNG_VALUE))
                .cacheControl(CacheControl.maxAge(2, TimeUnit.HOURS))
                .body(imageData);
    }
}
