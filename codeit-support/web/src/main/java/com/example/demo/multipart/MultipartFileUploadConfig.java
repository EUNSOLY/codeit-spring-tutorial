package com.example.demo.multipart;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * MultipartFileUploadConfig
 * - application.yml(properties)의 file.storage-type 설정값에 따라
 * 실제 사용할 MultipartFileUpload 구현체(LOCAL/S3/DUMMY 등)를 스프링 빈으로 등록
 * - storage-type을 바꾸는 것만으로 업로드 방식을 교체할 수 있게 하기 위한 설정 클래스
 * (LOCAL만 구현되어 있고, S3/DUMMY는 예시로만 적용 주석 처리)
 */
@Configuration
public class MultipartFileUploadConfig {

    @Value("${file.upload-directory:${user.dir}/uploads}")
    private String uploadDirectory;

    @Value("${file.storage-type:LOCAL}")
    private MultipartFileStorageType storageType;

    @Bean
    public MultipartFileUpload multipartFileUpload() {
        return switch (storageType) {
            case LOCAL -> new MultipartFileLocalUpload(uploadDirectory);
//          case S3    -> new MultipartFileS3Upload(uploadDirectory);
//          case DUMMY -> new MultipartFileDummyUpload();
            default -> throw new IllegalStateException("Not implemented: " + storageType);
        };
    }

}
