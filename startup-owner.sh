#!/bin/bash
cd /home/ec2-user
aws s3 cp s3://my-bucket-uniandes.edu.co/owner-service-0.0.1-SNAPSHOT.jar .
java -jar owner-service-0.0.1-SNAPSHOT.jar