#!/bin/sh

SCRIPT_DIR=$(cd `dirname $0` && pwd)
DOCKER_IMG="572787244808.dkr.ecr.us-east-1.amazonaws.com/passport-tree:latest"

# check connection to docker daemon
docker version
if [ $? -ne 0 ]; then exit; fi

cd $SCRIPT_DIR/..

echo "Building for Production deployment"
boot build
if [ $? -ne 0 ]; then exit; fi

echo "Copying build artifacts"
BUILD_DIR=$SCRIPT_DIR/build
rm -rf $BUILD_DIR
mkdir $BUILD_DIR
cp -r src $BUILD_DIR/src
cp -r target $BUILD_DIR/target
cp passport-tree.*.db $BUILD_DIR

cd $SCRIPT_DIR

echo "Building Docker image"
docker build --tag $DOCKER_IMG .

echo "Docker image: $DOCKER_IMG"
