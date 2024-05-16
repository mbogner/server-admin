#!/bin/sh

set -ex


# copy .jar package from the original build location into this directory
# if the .jar package does not exist, bailout
JARPATH="../../../apps/sa-client/build/libs/sa-client.jar"
cp "${JARPATH}" .

# build the image
docker build --tag sa-client:0.0.1 .

# upload image to Minikube local registry
minikube image load sa-client:0.0.1
