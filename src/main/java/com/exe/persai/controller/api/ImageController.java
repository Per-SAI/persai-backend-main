package com.exe.persai.controller.api;

import com.exe.persai.constants.SwaggerApiTag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/image")
public interface ImageController {

    @Tag(name = SwaggerApiTag.IMAGE)
    @Operation(
            summary = "View public image"
    )
    @GetMapping("/{fe_image_name}")
    ResponseEntity<byte[]> viewImageByImageName(@PathVariable("fe_image_name") String feImageName);
}
