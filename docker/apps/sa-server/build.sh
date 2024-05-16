#!/bin/sh

set -ex


# copy .jar package from the original build location into this directory
# if the .jar package does not exist, bailout
JARPATH="../../../apps/sa-server/build/libs/sa-server.jar"
if [[ ! -e "${JARPATH}" ]]
then 
	echo "${0} the ${JARPATH} is not found, bailing out..."
	exit 1
fi
cp "${JARPATH}" .

# build the image
docker build --tag sa-server:0.0.1 .

# upload image to Minikube local registry
minikube image load sa-server:0.0.1
