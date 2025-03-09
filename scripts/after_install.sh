#!/bin/bash

# 이전 컨테이너 중지 및 제거 (컨테이너가 실행 중인 경우에만)
docker stop mos || true
docker rm mos || true


sudo chmod +x /home/ubuntu/deployment/scripts/*.sh

# 최신 Docker 이미지 풀링
docker image rm shoon95/mos:latest
docker pull shoon95/mos:latest