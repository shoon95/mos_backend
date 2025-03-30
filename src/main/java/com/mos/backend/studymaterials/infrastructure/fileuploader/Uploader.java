package com.mos.backend.studymaterials.infrastructure.fileuploader;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.studymaterials.application.fileuploader.FileUploader;
import com.mos.backend.studymaterials.entity.FileUploadErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
public abstract class Uploader implements FileUploader {

    private static final String TEMP_FILE_PATH_PREFIX = "upload-async-";
    private static final String TEMP_FILE_PATH_SUFFIX_PREFIX = "-";

    @Override
    public Path createTempFile(MultipartFile file) {
        Path tempFilePath = null;
        try {
            tempFilePath = Files.createTempFile(TEMP_FILE_PATH_PREFIX, TEMP_FILE_PATH_SUFFIX_PREFIX + file.getOriginalFilename());
            Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            if (tempFilePath != null) {
                deleteTemporaryFile(tempFilePath);
            }
            throw new MosException(FileUploadErrorCode.FILE_UPLOAD_EXCEPTION);
        }
        return tempFilePath;
    }

    public void deleteTemporaryFile(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch(IOException e) {
            log.error("임시 파일 삭제 실패: {}", path, e);
        }
    }

    @Override
    public String generateUUIDFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID() + extension;
    }

}
