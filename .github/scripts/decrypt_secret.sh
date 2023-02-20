#!/bin/sh

# Decrypt the file
# --batch to prevent interactive command
# --yes to assume "yes" for questions
gpg --quiet --batch --yes --decrypt --passphrase="$RELEASE_PASSPHRASE" \
--output ./signing.properties encrypted/signing.properties.gpg
gpg --quiet --batch --yes --decrypt --passphrase="$RELEASE_PASSPHRASE" \
--output ./com.pax.innov.apk.sign encrypted/com.pax.innov.apk.sign.gpg
