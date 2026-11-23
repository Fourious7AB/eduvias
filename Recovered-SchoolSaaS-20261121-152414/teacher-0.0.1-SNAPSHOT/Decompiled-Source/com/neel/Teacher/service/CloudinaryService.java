/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.cloudinary.Cloudinary
 *  com.cloudinary.utils.ObjectUtils
 *  com.neel.Teacher.service.CloudinaryService
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.stereotype.Service
 *  org.springframework.web.multipart.MultipartFile
 */
package com.neel.Teacher.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(@Value(value="${cloudinary.cloud_name}") String cloudName, @Value(value="${cloudinary.api_key}") String apiKey, @Value(value="${cloudinary.api_secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap((Object[])new Object[]{"cloud_name", cloudName, "api_key", apiKey, "api_secret", apiSecret}));
    }

    public Map uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        File uploadedFile = this.convertMultiPartToFile(file);
        Map uploadResult = this.cloudinary.uploader().upload((Object)uploadedFile, ObjectUtils.emptyMap());
        uploadedFile.delete();
        return uploadResult;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}

