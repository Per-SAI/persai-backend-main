package com.exe.persai.repository;

import com.exe.persai.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    boolean existsByS3ImageName(String s3ImageName);
}
