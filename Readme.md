## Guide to install multiple apk
adb install-multiple app/build/outputs/apk/debug/app-debug.apk favorite/build/outputs/apk/debug/favorite-debug.apk

## Guide environemtn variable :
open local.properties, create if not exists

```shell
touch local.properties
vim local.properties

```
- variable
  Please make sure these three variables exist
```shell
    ACCESS_TOKEN=[your-token]
    BASE_URL=https://api.themoviedb.org/3/
    BASE_URL_IMAGE=https://image.tmdb.org/t/p/w500

```

## build guide

```shell
   cd ~/project
   gradlew clean
   gradlew build
```

## Circle CI

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/jeryllaboratory/App16_MovieAppCapstone/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/jeryllaboratory/App16_MovieAppCapstone/tree/main)

