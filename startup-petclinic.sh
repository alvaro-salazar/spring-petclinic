#!/bin/bash
cd /home/ec2-user
aws s3 cp s3://my-bucket-uniandes.edu.co/spring-petclinic-3.3.0-SNAPSHOT.jar .
java -jar spring-petclinic-3.3.0-SNAPSHOT.jar