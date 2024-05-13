# Server Admin

This is a simple client server system with a central server application an multiple clients.

## Infrastructure

The application is based on

- Postgresql and
- Kafka

For development purposes the project includes 2 docker configurations:

### docker/client

The application can be developed within this container. Intellij supports running applications inside a container
instead of locally. Make sure to run the client with the same network as the kafka containers.

Attention: This image isn't meant for production use!

### docker/infra

This defines a postgresql server and a 3 node kafka cluster. Use the compose file included in the folder.

Kafdrop is also started for easier kafka insight.

## Applications

You can find screenshots for IntelliJ run configurations for client and server.

### sa-server

Simple shell applications that listens to kafka and allows to type commands into the local shell.

The included default config is meant to be run from within a docker container. It needs to be started in the same
network as postgres and kafka.

### sa-client

Client application that sends a regular heartbeat to the central sa-server application. It also supports command line
input to trigger commands.

The included default config is meant to be run from within a docker container as defined in `docker/client`. It needs to
be in the same network as kafka. The client application is using a local file based sqlite database instead of relying
on any server.

The client container needs to be built before it can be used. A script `build-debian.sh` is included for that purpose.