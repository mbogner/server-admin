#!/usr/bin/env bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
cd "${DIR}" || exit 1

docker build -t server-client:latest -f Dockerfile-debian . || exit 1
docker run -ti --rm -p "127.0.0.1:2022:22" \
  --name client-debian --hostname client-debian \
  server-client:latest