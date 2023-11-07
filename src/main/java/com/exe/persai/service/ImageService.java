package com.exe.persai.service;

import com.exe.persai.model.entity.Image;
import com.exe.persai.model.exception.ResourceNotFoundException;
import com.exe.persai.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ImageService {
    private final ImageRepository imageRepository;
    private final FileService fileService;
    public Image createImageFromGooglePicture(String googlePicture) {
        Image image = Image.builder()
                .feImageName(googlePicture)
                .s3ImageName(null)
                .ggImageLink(googlePicture)
                .build();
        return imageRepository.save(image);
    }

    public byte[] getImageByFeImageName(String feImageName) {
        Image image = imageRepository.findById(feImageName)
                .orElseThrow(() -> new ResourceNotFoundException("Fe Image name not found"));
        return fileService.downloadFile(image.getS3ImageName());
    }
}
