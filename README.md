## ZZNote

Демо - http://zznote.ru/

Java-Приложение для создания, хранения и отображения информации/заметок и создания простых вэб-страниц

Для входа на сайт требуется аутентификация

Используйте бота в ВК, или в ТГ для получения токена

Для аутентификации отправь '+' боту. Далее, полученый токен используется для атворизации на страницах сайта

Приложение имеет встроенный rest-server для получения данных via http

Запускается как модуль проекта https://github.com/mostali/udav_project

## RUN APPLICATION

```commandline
#GetRepo + import project in Idea
git clone git@github.com:mostali/udav_project.git

...
//Run From Idea Project
ZznApplication.Run_LOCAL.main()
```
OR (+See Commented Docker Way)
```bash
rm -rf udav_project
#GetRepo
git clone git@github.com:mostali/udav_project.git

cd udav_project

#Create App Jar
mvnd -f mp/pom.xml clean package -Pzznote

#Unpack run script r.sh
java -jar zznote/target/app.jar --init

#mv app.jar
mv zznote/target/app.jar .

#Run application on our Java Or Skip It and Run in docker
./r.sh --p

#mv zznote/Dockerfile-Rocky .
#sudo docker build -t cr-image:1 -f Dockerfile-Rocky . && sudo docker run  cr-image:1
```

