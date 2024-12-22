## ZZNote

#### Демо приложение - http://zznote.ru/

Java-Приложение для создания, хранения и отображения информации/заметок и создания простых вэб-страниц, блога

Позволяет создавать web-страницы добавляя разные типы заметок с текстом, html, фото, видео и др

Поддерживает работу других пользователей, которые могут получить свой поддомен типа t777.site.com

Заметки могут выполнять различные, более специфичные операции, типа отправки http-запроса, сообщения в кафку, etc

Приложение может работать с заметками как rest-server 

Типичные операции работы с заметками продублированы в тг бота

Для получения токена пользователям необходимо отправить '+' бот в ВК, или в ТГ

## Development & Run
Запускается как модуль проекта https://github.com/mostali/udav_project


### Configure Tg Bot

```
#Задайте настройки для тг-бота
tg.bt.enable=true
#owner chat id 
tg.bt.owner.id=123
#botid
tg.bt.id=botname
#bottoken
tg.bt.tk=bottoken
```

### Run Application

#### Run App as Idea Project

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

#Run application Or Skip It & Run in docker
./r.sh --p

#mv zznote/Dockerfile-Rocky .
#sudo docker build -t cr-image:1 -f Dockerfile-Rocky . && sudo docker run  cr-image:1
```

### Добавление кастомной страницы
```commandline
@PageRoute(sd3="subdomain" pagename = "zk_notes", role = ROLE.USER )
public class NotesPSP extends PageSP {...}
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

### Перемещение сообщений
```commandline
#mv up / note to other server from local (byApi put)
~mvu s/p/i zn:s/p/i
#mv down / note from other server to local (byApi get) 
~mvd s/p/i zn:s/p/i
```


### Добавление, получение данных заметки с помощью команд

```commandline
#Добавить заметку
!s/p/i
content

#Получить содержимое заметки
*s/p/i
<--content

```

## Note as Http Client

### Http Client Via Telegram Message
Http запрос формируется за счет тела сообщения
При использовании в тг к сообщению добавляется префикс ??
```commandline
#Выполнить http GET запрос
??GET http://url.com
```
```
#Делаем POST запрос
??Выполнить http POST http://url.com
--#Header & comment
--Auth:...
Body..
```


### Примеры http-запросов для получения и обновления данных заметок

```
#Обновить содержимое заметки
??GET http://q.com:8080/_api/!/note?auth..&v=note
??POST http://q.com:8080/_api/page/!/note?auth..&v=note-data
--

#Получить содержимое заметки
??GET http://q.com:8080/_api/*/note?auth..
??GET http://q.com:8080/_api/page/*/note?auth..

```