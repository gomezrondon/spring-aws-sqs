package com.gomezrondon.springawssqs.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Service
public class AwsServiceDefaultImpl implements AwsService{


    private final ResourceLoader resourceLoader;

    public AwsServiceDefaultImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @Override
    public void downloadS3Object(String s3Name,String fileName) throws IOException {
        String s3Url="https://s3-us-west-2.amazonaws.com/"+s3Name+"/"+fileName;

        Resource resource = resourceLoader.getResource(s3Url);
        File downloadedS3Object = new File(resource.getFilename());

        try (InputStream inputStream = resource.getInputStream()) {
            Files.copy(inputStream, downloadedS3Object.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void uploadFileToS3(File file, String s3Url) throws IOException {
        WritableResource resource = (WritableResource) resourceLoader.getResource(s3Url);
        try (OutputStream outputStream = resource.getOutputStream()) {
            Files.copy(file.toPath(), outputStream);
        }
    }


}
