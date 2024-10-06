## ZZNote

Демо - http://zznote.ru/

Сайт предназначен для создания, хранения и отображения информации/заметок и создания простых вэб-страниц

Для входа на сайт требуется аутентификация

Используйте бота в ВК, или в ТГ для получения токена

Для аутентификации отправь '+' боту. Далее, полученый токен используется для атворизации на страницах сайта

Приложение имеет встроенный rest-server для получения данных via http


## RUN APPLICATION

```commandline
#GetRepo + import project in Idea
git clone git@github.com:mostali/udav_project.git

...
//Run From Idea Project
ZznApplication.Run_LOCAL.main()
```
OR
```bash
rm -rf udav_project
#GetRepo
git clone git@github.com:mostali/udav_project.git

cd udav_project

#Create App Jar
mvnd -f mp/pom.xml clean package -Pzznote
ls zznote/target
sleep 5
#Unpack run script r.sh
java -jar zznote/target/app.jar --init

#mv app.jar
mv zznote/target/app.jar .

#Run application
./r.sh --p
```

