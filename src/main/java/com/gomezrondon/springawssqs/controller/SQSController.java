package com.gomezrondon.springawssqs.controller;

import com.gomezrondon.springawssqs.service.AwsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/aws")
public class SQSController {

    private static Logger Log = LoggerFactory.getLogger((SQSController.class));
    private static final String QUEUE_NAME = "spring-sqs-test";

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;



    private final AwsService awsService;

    public SQSController(AwsService awsService) {
        this.awsService = awsService;
    }

    @GetMapping("/sqs/{message}")
    public String sendMessage(@PathVariable("message") final String message){
        String time = LocalDateTime.now().toString();
        queueMessagingTemplate.send(QUEUE_NAME, MessageBuilder.withPayload(message+": "+time).build());
        return "Message "+message+" sent At "+time;
    }

    @SqsListener(QUEUE_NAME)
    public void listen(String message) {
        Log.info("Message from SQS is: "+ message);
    }

    @GetMapping("/s3/down/{s3Name}/{fileName}")
    public String donwloadS3Object(@PathVariable("s3Name") final String s3Name,
                                 @PathVariable("fileName") final String fileName){
        try {
            awsService.downloadS3Object(s3Name,fileName );
            Log.info("S3 Objedct: "+ fileName +" was downloaded");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "S3 Object: "+ fileName +" was downloaded";
    }

    @GetMapping("/s3/up/{s3Name}/{fileName}")
    public String uploadS3Object(@PathVariable("s3Name") final String s3Name,
                                   @PathVariable("fileName") final String fileName){
        try {
            awsService.uploadFileToS3(s3Name,fileName );
            Log.info("S3 Objedct: "+ fileName +" was Uploaded");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "S3 Object: "+ fileName +" was Uploaded";
    }

}
