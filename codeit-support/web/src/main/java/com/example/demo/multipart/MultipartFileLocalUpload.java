package com.example.demo.multipart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class MultipartFileLocalUpload extends MultipartFileAbstractUpload {
    private final String directory;

    @Override
    protected String generate(MultipartFile file) {
        String original = file.getOriginalFilename();
        String sanitized = MultipartFileUtils.sanitize(original);
        return UUID.randomUUID() + "_" + sanitized;
    }

    @Override
    protected String upload(MultipartFile file, String filename) throws IOException {
        Path uploadDirectory = Path.of(directory).toAbsolutePath(); // 업로드 디렉토리 경로를 절대 경로로 변환
        Files.createDirectories(uploadDirectory);  // 디렉토리가 없으면 생성 (이미 있으면 무시됨)

        Path destinationPath = uploadDirectory.resolve(filename);  // 디렉토리 경로 + 파일명으로 최종 저장 경로 생성
        File destinationFile = destinationPath.toFile();  // Path를 File 객체로 변환
        file.transferTo(destinationFile); // 업로드된 파일(MultipartFile)을 실제 파일 시스템에 저장
        return destinationFile.getAbsolutePath();  // 저장된 파일의 절대 경로 반환
    }
}
