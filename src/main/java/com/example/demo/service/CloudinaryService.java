package com.example.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
@Service
@Log4j2
public class CloudinaryService {
    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }

    public void deleteFile(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    public String uploadString(String content) throws IOException {
        // Tạo tên tệp tạm thời
        String tempFileName = "temp.txt";
        Map<String, Object> uploadResult = null;
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(tempFileName), StandardCharsets.UTF_8))) {
            // Ghi nội dung chuỗi vào tệp
            writer.write(content);
            // Tải lên tệp tạm lên Cloudinary
            File tempFile = new File(tempFileName);
            uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
// Xóa tệp tạm sau khi tải lên
        File tempFile = new File(tempFileName);
        tempFile.delete();
// Trả về URL của tệp đã tải lên
        String uploadedFileUrl = uploadResult.get("url").toString();
        return uploadedFileUrl;

    }
}

