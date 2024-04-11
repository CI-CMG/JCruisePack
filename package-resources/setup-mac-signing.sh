#!/usr/bin/env bash

set -ex

#decode certificate
echo "$APP_CERT_BASE64" > app_sign.p12.txt
echo "$INSTALL_CERT_BASE64" > install_sign.p12.txt
base64 -d -i app_sign.p12.txt -o app_sign.p12
base64 -d -i install_sign.p12.txt -o install_sign.p12

# default again user login keychain
security list-keychains -d user -s login.keychain

# Create temp keychain
security create-keychain -p "$MY_KEYCHAIN_PASSWORD" "$MY_KEYCHAIN"

# Append temp keychain to the user domain
security list-keychains -d user -s "$MY_KEYCHAIN" $(security list-keychains -d user | sed s/\"//g)

# Remove relock timeout
security set-keychain-settings "$MY_KEYCHAIN"

# Unlock keychain
security unlock-keychain -p "$MY_KEYCHAIN_PASSWORD" "$MY_KEYCHAIN"

# Add certificate to keychain
security import app_sign.p12 -k "$MY_KEYCHAIN" -P "$CERT_PASSWORD" -A -T "/usr/bin/codesign" -T "/usr/bin/productsign"
security import install_sign.p12 -k "$MY_KEYCHAIN" -P "$CERT_PASSWORD" -A -T "/usr/bin/codesign" -T "/usr/bin/productsign"

# Enable codesigning from a non user interactive shell
security set-key-partition-list -S apple-tool:,apple:, -s -k $MY_KEYCHAIN_PASSWORD -D "${IDENTITY_CERTIFICATE}" -t private $MY_KEYCHAIN