package com.gomezrondon.springawssqs.service;

import java.io.File;
import java.io.IOException;

public interface AwsService {

    void downloadS3Object(String s3Name,String fileName) throws IOException;

    void uploadFileToS3(String s3Name,String fileName) throws IOException;

}
