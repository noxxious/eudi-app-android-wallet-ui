#!/bin/sh

set -euxo pipefail
IFS=$'\n\t'

BUILD_ID="20250227"

gcloud storage cp \
    app/build/outputs/apk/demo/debug/app-demo-debug.apk \
    gs://eudiw-lt-wallet-downloads/eudiw-lt-wallet-${BUILD_ID}.apk

sha256sum app/build/outputs/apk/demo/debug/app-demo-debug.apk \
    > eudiw-lt-wallet-${BUILD_ID}.sha256sum

gcloud storage cp \
    eudiw-lt-wallet-${BUILD_ID}.sha256sum \
    gs://eudiw-lt-wallet-downloads

rm -rf eudiw-lt-wallet-${BUILD_ID}.sha256sum


