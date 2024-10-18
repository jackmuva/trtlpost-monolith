#!/bin/sh

echo "Pre-Build Steps:"
echo "authenticating with AWS ECR"
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 514832027284.dkr.ecr.us-east-1.amazonaws.com

echo "Maven clean and package:"
mvn clean package

echo "Build Steps:"
echo "building image..."
docker build -t 514832027284.dkr.ecr.us-east-1.amazonaws.com/trtlpost-monolith:latest .

echo "Syncing env file in S3"
aws s3 --region 'us-east-1' sync ./env 's3://trtlmail-env/'

echo "updating AWS ECS service..."
aws ecs update-service --cluster trtlpost-cluster --service trtlpost-monolith-service --force-new-deployment

echo "Post-Build steps:"
echo "pushing image to AWS ECR"
docker push 514832027284.dkr.ecr.us-east-1.amazonaws.com/trtlpost-monolith:latest
