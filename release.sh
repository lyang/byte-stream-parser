#!/bin/bash

SOURCE="${SOURCE:-$(git rev-parse --abbrev-ref HEAD)}"
TIMESTAMP="$(git log -1 --pretty=%cd --date=format:'%Y%m%d%H%M%S')"
REVISION="$(git rev-parse --short HEAD)"
RELEASE="${RELEASE:-0.$SOURCE.$TIMESTAMP.$REVISION}"

sed --in-place "s/^RELEASE = \"0.0.0\"$/RELEASE = \"$RELEASE\"/" BUILD.bazel

bazel run //:byte-stream-parser.publish 1>&2

echo "RELEASE=$RELEASE"