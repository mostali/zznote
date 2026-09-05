## ZZNote

#### Демо приложение - http://xnode.space
#### Демо приложение (OLD) - http://zznote.ru/

#### Manual - [MANUAL](http://xnode.space/_manual)

Приложение позволяет:

- создавать сайты, как набор простых заметок

- хранить и просматривать файлы как заметки в виде текста, html, фото, видео и др.

- писать различные скрипты на Phyton, Bash, Groovy, Java, Sql, запускать программы по расписанию

- интегрироваться с различными системами посредством REST API и Kafka

- интегрироваться с OpenAI (experimental note)

- использоваться как REST server, файловый сервер, key/value хранилище

- использование в тексте заметки плэйсхолдера типа @{{/page/note}} - позволяет читать заметки из других заметок

- заметка как прокси-сервис с возможностью управления через REST. URL query позволяет задать кастомный контекст выполнения, используя в заметке плэйсхолдеры типа ${{queryKey}} 

- приложение может использоваться как среда для создания сложных модулей и прототипов, авто- и интеграционных тестов ( mini NiFi )

- создание кастомных страниц и других сервисов или приложений на XNode ZKoss Framework

## Встроенные страницы - сервисы

- Jira Task View - 
- Simple HTML View - http://view.zznote.ru/html
- XSD Validator View - http://view.zznote.ru/xsd
- JSON path analyze View - http://view.zznote.ru/json
- Charts View 
- Log Analyzer View http://zznote.ru/@@logs
- Spring Actuator View http://zznote.ru/@@actuator
- Docker API View  - http://zznote.ru/@@docker

## Development & Run

Запускается как модуль проекта https://github.com/mostali/udav_project

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

#copy app.jar to any app-dir located in home, e.g. ~/appdir (home location is required)
mv zznote/target/app.jar .

#Unpack application.properties & run-script r.sh to app-dir
java -jar app.jar --init

chmod +x ./r.sh

#Choice way to run application
#1. Directly (via script r.sh or as java -jar app.jar )
#2. via Docker . Additionally, Docker container will be run as Desktop App if comment line which run application (see Dockerfile)

#1
# This script run application on port 80 
#./r.sh --p
# or 
# java -jar app.jar

#2
# Run in utl_docker
mv zznote/Rocky.Dockerfile .
#sudo utl_docker build -t cr-image:1 -f Rocky.Dockerfile . && sudo utl_docker run -it -v /home/dav/.data/zzn:/home/lu/.data/zzn -e VNC_PW=password -p 8081:8081 cr-image:1
sudo utl_docker build -t cr-image:1 -f Rocky.Dockerfile . && sudo utl_docker run -it -e VNC_PW=password -p 8081:8081 cr-image:1
```

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

### Аутентификация в приложении

- пользователь отправляет боту '+' и получает токен приложения от тг-бота. Пользователь получает свой домен типа *
  *t123.domain.com**
- аутентификация через ModHeader и ключ super.header.auth 
- техническая аутентификация как **админа** по ключу (Deprecated)
- также доступен режим аутентификации по логину и паролю (необходимо активировать mode in application.properties)
- первый запуск приложения создает пользователя-owner. Логин/пароль сохраняется в logs/init.log

## Note as HttpClient, Kafka, Sql, QuartzTask

### Http Client

```shell
#This note do http call.
POST http://domain.com
--#header comment
--Authorization:Bearer
{"some":"body"}
```

### Kafka Client

```shell
#This note put data to kafka
KPUT http://localhost:9092
--#header comment
--topic:topic
--key:msgkey
msgvalue
```

### Execute Sql

```shell
jdbc:postgresql://localhost:5432/dbname?currentSchema=cur_schema&searchpath=cur_schema
--login:username
--password:userpassword
SELECT * FROM pg_catalog.pg_tables;
```

### Execute Qurtz Task

```shell
#send telegram message with next options:
#job start date is 20250108155500 - 3day
#job end date is 20250108155500 + 3day
#send message every 12 hour
qzjob:zkbea_nett.TgSendMsgJob
20250108155500 -msg myMsg -b 3d -a 3d -e 12h
```

## Работа встроенного rest-server

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
## Java XNode Framefwork (ZKoss)

### Создание кастомных страниц

```commandline
//This page will by available at http://subdomain.domain.com/zk_notes
@PageRoute(sd3="subdomain" pagename = "zk_notes", role = ROLE.USER )
public class NotesPSP extends PageSP {...}
```


## Experimental - Run Note via Tg

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