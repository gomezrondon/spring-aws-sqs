package com.gomezrondon.springawssqs.controller;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/sqs")
public class SQSController {

    private static Logger Log = LoggerFactory.getLogger((SQSController.class));

    private final QueueMessagingTemplate queueMessagingTemplate;

    @Value("${cloud.aws.end-point.uri}")
    private String sqsEndPoint;

    public SQSController(AmazonSQSAsync amazonSQSAsync) {
        this.queueMessagingTemplate = new QueueMessagingTemplate(amazonSQSAsync);
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

}
