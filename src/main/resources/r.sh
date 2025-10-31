#!/bin/bash
function die() { echo $2 && exit $1; }
#####################################################
######################-GO-###########################
#####################################################

APP_NAME=DEF
DEBUG_PORT=5006
APP_JAR_NAME=app.jar
APP_DIR=~/$APP_NAME

for arg0 in "$@"; do

  echo "Processing arg [$arg0]"

  case $arg0 in
  --v) ########### DEBUG
    java -jar $APP_JAR_NAME --v || die 1 "error run [$arg0]"
    die 0
    ;;
  --init) ###########
    cd ~/$RADIR
    java -jar $APP_JAR_NAME -crtrl /r.sh -crtrl /logback.xml -crtrl /application.properties -crtrl /application-prod.properties --exit || die 1 "error run [$arg0]"
    die 0
    ;;
  --p) ########### PROM
    JAVA_OPTIONS="-Dspring.profiles.active=prod -Dlogback.configurationFile=./logback.xml "
    java $JAVA_OPTIONS -Dserver.port=80 -jar $APP_JAR_NAME
    ;;
  --init-docker) ########### PROM
      JAVA_OPTIONS=""
      java $JAVA_OPTIONS -Dserver.port=80 -jar $APP_JAR_NAME
      ;;
  --P) ########### PROM
    echo kill-ses
    tmux kill-session -t $APP_NAME || echo "tmux session $APP_NAME  not found"
    echo new-ses
    tmux new-session -d -s $APP_NAME ./r.sh --p
    #echo run-app
    #tmux send-keys -t $APP_NAME "cd ~/$APP_DIR && ./r.sh --p" ENTER
    ;;

  --r) ########### PROM
      $0 --k || die 1 kill
      $0 --p || die 1 run-prom
      ;;
  --R) ########### PROM
        echo "cd $APP_DIR && ./r.sh --P" > ~/arunner/task.sh
        ;;
  --b) ########### DEBUG
    JAVA_OPTIONS="-Dserver.port=80 -Dspring.profiles.active=prod  -Dlogback.configurationFile=./logback.xml -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:$DEBUG_PORT"
    java $JAVA_OPTIONS -jar $APP_JAR_NAME || die 1 "error run [$arg0]"
    ;;
  --k) ########### KILL
    if [ -f APP.PID ]; then APP_PID=$(cat APP.PID) && kill -9 $APP_PID && rm APP.PID; else echo "APP.PID not found"; fi
    ;;
  --test)
    tmux new-session -d -s $APP_NAME
    tmux send-keys 'appjt -runia 2000' C-m
    tmux detach -s $APP_NAME
    ;;
  --tm)
    cd $APP_DIR

    ;;
  *)
    echo "Unknown arg [$arg0]" && exit 1
    ;;
  esac

done

exit 0
