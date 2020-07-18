# spring-aws-sqs
an AWS S3/SQS hello world implemented with Spring boot Cloud

1) You need to configure your AWS CLI credentials so the application will pick them up from it.
1.1) set the region
```
cloud:
  aws:
    stack:
      auto: false
    region:
      static: <the bucket reagion>
      auto: false
```
1.2) or create an user and grant access to the sqs (you need to get the credentials) 
```
cloud:
  aws:
    stack:
      auto: false
    region:
      static: <reagion>
      auto: false
    end-point:
      uri: <url of the queue>
    credentials:
      access-key: <access-key>
      secret-key: <secret-key>
```
1.3) you would need to set the name of the queue is you want to use the SQSListener
```
private static final String QUEUE_NAME = "********";
```
2) You need to configure a S3 policy and assign it to the user you are using to execute the application.

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": [
                "s3:PutObject",
                "s3:GetObject",
                "s3:ListBucketVersions",
                "s3:GetObjectTagging",
                "s3:DeleteObject",
                "s3:GetBucketLocation",
                "s3:GetObjectVersion"
            ],
            "Resource": [
                "arn:aws:s3:::<your s3 bucket>/*",
                "arn:aws:s3:::<your s3 bucket>"
            ]
        }
    ]
}
```

To run the application simply do:

```bash
./gradlew bootRun
```

to quick test the SQS

```
http://localhost:9001/aws/sqs/{message}
```

to download a file from a bucket:

```
http://localhost:9001/aws/s3/down/<bucket name>/<file name>
```

to upload a file to a bucket

```
http://localhost:9001/aws/s3/up/<bucket name>/<file name>
```

## To execute as a docker container
1) compile the application
```
./gradlew clean build -x Test unzip
```
2) create/run the container with a docker compose file:
```
docker-compose up -d
```

to stop the running container
```
docker-compose down
```