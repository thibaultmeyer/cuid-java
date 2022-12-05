#!/usr/bin/env bash

NEW_RELEASE_VERSION=$1
REGEXP='^([0-9]+){1}\.([0-9]+){1}\.([0-9]+){1}$'

if [[ ${NEW_RELEASE_VERSION} =~ ${REGEXP} ]]; then
 echo "${NEW_RELEASE_VERSION} is a valid version number"
else
 echo "${NEW_RELEASE_VERSION} is an invalid version number"
 exit 1
fi
