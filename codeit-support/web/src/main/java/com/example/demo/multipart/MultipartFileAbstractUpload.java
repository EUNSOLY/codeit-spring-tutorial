package com.example.demo.multipart;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public abstract class MultipartFileAbstractUpload implements MultipartFileUpload {

    @Override
    public final String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("파일이 비어있습니다. 업로드를 위한 파일을 보내주셔야합니다.");
        }

        String filename = generate(file);
        try {
            return upload(file, filename);
        } catch (IOException e) {
            log.error("파일 저장시 에러 발생했습니다 : {}", filename, e);
            return null;
        }
    }

    // 파일명 생성 로직을 다르게 쓰고 싶다면 구현체에서 오버라이드
    protected String sanitize(String original) {
        // 빈 값, Null 방어
        if (Objects.isNull(original) || original.isBlank()) {
            original = "unknown";
        }
        // Windows 및 Unix 경로 구분자를 기준으로 마지막 파일명만 추출
        int lastWinSep = original.lastIndexOf('\\');
        int lastUnixSep = original.lastIndexOf('/');
        int lastIndex = Math.max(lastWinSep, lastUnixSep);

        // 파일명이 빈 값이라면 unknown 파일명으로 변환하여 반환
        String sanitized = (lastIndex != -1)
                ? original.substring(lastIndex + 1)
                : original;
        return sanitized.isBlank() ? "unknown" : sanitized;
    }

    abstract protected String generate(MultipartFile file);

    abstract protected String upload(MultipartFile file, String filename) throws IOException;
}
