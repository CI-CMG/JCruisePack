#!/usr/bin/env bash

set -ex

# Delete temporary keychain
security delete-keychain "$MY_KEYCHAIN"

# default again user login keychain
security list-keychains -d user -s login.keychain
