package com.gomezrondon.springawssqs.controller;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.gomezrondon.springawssqs.service.AwsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    private final QueueMessagingTemplate queueMessagingTemplate;

    @Value("${cloud.aws.end-point.uri}")
    private String sqsEndPoint;

    private final AwsService awsService;

    public SQSController(AmazonSQSAsync amazonSQSAsync, AwsService awsService) {
        this.queueMessagingTemplate = new QueueMessagingTemplate(amazonSQSAsync);
        this.awsService = awsService;
    }

    @GetMapping
    public String sendMessage(){
        String time = LocalDateTime.now().toString();

        queueMessagingTemplate.send(sqsEndPoint, MessageBuilder.withPayload("Hola, son las: "+time).build());
        return "Message sent At "+time;
    }

    @SqsListener("spring-boot-sqs")
    public void listen(String message) {
        Log.info("Message from SQS is: "+ message);
    }

    @GetMapping("/s3/{s3Name}/{fileName}")
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


}
