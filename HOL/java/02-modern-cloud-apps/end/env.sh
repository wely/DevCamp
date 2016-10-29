#!/bin/bash

export AZURE_STORAGE_ACCESS_KEY=yMH9b0E3u5Cf9sZTwxwoQXsM90ygX/VO5qyJmRStJ1NR5cr8xiLm788ELahxTtzL376BGJZG8SOdx5PnlZpkbA==
export AZURE_STORAGE_ACCOUNT=incidentblobstg32csxy6h3
export AZURE_STORAGE_BLOB_CONTAINER=images
export AZURE_STORAGE_QUEUE=thumbnails
export INCIDENT_API_URL=http://incidentapi32csxy6h3sbku.azurewebsites.net
export INCIDENT_RESOURCE_PATH=/incidents
export REDISCACHE_HOSTNAME=incidentcache32csxy6h3sbku.redis.cache.windows.net
export REDISCACHE_PORT=6379
export REDISCACHE_PRIMARY_KEY=Fw1IK6VXPILlqXNOiUBd9Kl8nyWLLBvR2QwosWZkRLY=
export REDISCACHE_SSLPORT=6380



gradle bootRun "-Dazure.storage.account.key=$AZURE_STORAGE_ACCESS_KEY" "-Dazure.storage.account.name=$AZURE_STORAGE_ACCOUNT" "-Dazure.storage.account.blobContainer=$AZURE_STORAGE_BLOB_CONTAINER" "-Dazure.storage.account.queue=$AZURE_STORAGE_QUEUE" "-Dapplication.incidentApiUrl=$INCIDENT_API_URL" "-Dapplication.incidentResourcePath=$INCIDENT_RESOURCE_PATH" "-Dapplication.redisHost=$REDISCACHE_HOSTNAME" "-Dapplication.redisPort=$REDISCACHE_PORT" "-Dapplication.primaryKey=$REDISCACHE_PRIMARY_KEY" "-Dapplication.redisSslPort=$REDISCACHE_SSLPORT"
