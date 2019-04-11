package com.gomezrondon.springawssqs.service;

import java.io.IOException;

public interface AwsService {

    void downloadS3Object(String s3Url) throws IOException;

}
