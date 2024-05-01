## Powerlifting Project 2

## Dockercommands

docker build --platform linux/amd64 -t lukasweibel99/powerlifting:latest . --push

docker run -d -p 8080:8080 --name lukasweibel99/powerlifting powerlifting:latest

## Deploy on Azure

az container create --resource-group mdm-weibelu1-project --name mdm-powerlifting --image lukasweibel99/powerlifting:latest --dns-name-label mdm-powerlifting --ports 8080

http://mdm-powerlifting.westeurope.azurecontainer.io:8080
