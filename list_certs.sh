#!/bin/sh
#
# Print the inclusion list for the EUDIW Reference Wallet
for file in resources-logic/src/main/res/raw/*.pem; do
    f="$(basename "$file")"
    echo "R.raw.${f%.pem},"
done
