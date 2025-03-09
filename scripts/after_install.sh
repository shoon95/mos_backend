#!/bin/bash

echo "===== Phase 1: after_install ====="
# Docker Compose로 구성된 모든 컨테이너, 이미지, 볼륨 제거
cd /home/ubuntu/app
docker-compose down --rmi all --volumes || true