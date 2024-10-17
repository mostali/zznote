## ZZNote

#### Демо приложение - http://zznote.ru/

Java-Приложение для создания, хранения и отображения информации/заметок и создания простых вэб-страниц

Для создания заметок требуется аутентификация

Используйте бота в ВК, или в ТГ для получения токена

Для аутентификации отправь '+' боту. Далее, полученый токен используется для атворизации на страницах сайта

Приложение имеет встроенный rest-server для получения данных via http

Запускается как модуль проекта https://github.com/mostali/udav_project

## Development & Run

### Configure Tg Bot

```
#Задайте настройки для тг-бота
#owner chat id 
tg.bt.owner.id=123
#botid
tg.bt.id=botname
#bottoken
tg.bt.tk=bottoken
```

### Run Application

#### Run App from Idea Project

```commandline
#GetRepo + import project in Idea
git clone git@github.com:mostali/udav_project.git

...
//Run From Idea Project
ZznApplication.Run_LOCAL.main()
```

#### Run App as java instance (+Docker Way)

```bash
cd ~
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

### Аутентификация в приложении

- пользователь получает токен приложения от тг-бота. Пользователь получает свой домен типа **t123.site.com**
- системная аутентификация как **админа** по ключу в урле?ska=zz ( задается как проперти **ska** в *
  *application.peroperties)**

### Работа встроенного rest-server

```commandline
#put note (GET,POST)
http://s.q.com:8080/_api/page/!/note?v=123
#get note data (GET)
http://s.q.com:8080/_api/page/*/note
```

## Команды 

- s/p/i = subdomain/page/item


```commandline
#mv up / note to other server from local (byApi put)
~mvu s/p/i zn:s/p/i
#mv down / note from other server to local (byApi get) 
~mvd s/p/i zn:s/p/i
```

## Команды в чат боте тг

```commandline
#Добавить заметку
!s/p/i
content

#Получить содержимое заметки
*s/p/i

```