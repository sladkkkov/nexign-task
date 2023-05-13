# nexign-task

Проект Nexign Bootcamp, выполнил Данила Сладков. Telegram @sladkkov.

Использованные технологии: 
Spring Boot, Spring Data Jpa, PostgreSQL, Kafka, OpenApi, MapStruct, Liquibase. 

Для запуска приложения необходимо запустить: docker-compose.yaml находящийся в корневой папке проекта. При ошибке проверить доступность всех портов. 

1. http://localhost:7653/api/v1/auth/registration Путь для регистрации. По умолчанию зарегистрировавшийся абонент получает права Абонента. Post

```
{
      "username": "user",
      "password": "1234",
      "firstName": "user",
      "lastName": "user"
}
```
2. http://localhost:7653/api/v1/auth/login Путь для получения токена.  Post
```
{
      "username": "user",
      "password": "1234"
}
```
В ответ получаем: 
```
{
      "id": 2,
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE2ODI5OTAxNzMsImV4cCI6MTY4Mjk5Mzc3M30.SwzOmapU6Kd0FB1PcU8NR2y0hW81ImFz7fsSv1Vn7jQ",
      "username": "user",
      "roles": [
            {
                  "id": 2,
                  "name": "USER"
            }
      ]
}
```
Копируем полученный токен и вставляем в header Authorization
![img.png](img.png)
Теперь мы имеем доступ к роутам Abonent.

Данные по умолчанию:
1. abonent: username: user, password: 1234
2. admin: username: admin, password: 1234 
С ними можно начать работу с шага 2.

http://localhost:7653/api/v1/abonent/pay Необходимо ввести актуальный телефон из БД. Запрос имеет уровень доступа USER
```
{
"abonentPhone": "+7 936 843-04-58",
"money": "120"
}
```
http://localhost:7653/api/v1/manager/changeTariff Необходимо ввести актуальный телефон из БД. Запрос имеет уровень доступа ADMIN. Patch 
```
{
      "abonentPhone": "+7 990 998-35-03",
      "tariffId": "11"
}
```
```http://localhost:7653/api/v1/abonent/report/+7 989 272-21-47``` Необходимо ввести актуальный телефон из БД. Запрос имеет уровень доступа USER. Patch

http://localhost:7653/api/v1/manager/save Запрос имеет уровень доступа ADMIN. Post 
```
{
      "abonentPhone": "+79909983413",
      "tariffDto": {
            "tariffId": "11",
            "name": "Безлимит"
      },
      "money": 120
}
```




Swagger doc: http://localhost:7653/swagger-ui/index.html#/ 
KafDrop: http://localhost:9000/ (ui для кафки)


Опишу проект. В модуле cdr, создаются абоненты и записываются в БД. По номерам этиъ абонентов создаются CDR, и каждая CDR отправляется в топик Кафки brt-topic. 
В brt происходит проверка баланса, и если всё проходит успешно, cdr наполняется данными о тарифе и отправляется в hrs. Там происходит расчёт стоимости звонка и возвращается в brt.

Разработка и проектирования тарифа в БД мне показалось не лучшим рещением, т.к. любой новый тариф ломал бы всё и пришлось бы переписывать много кода. 
Поэтому принял решение в БД хранить только Id и Имя тарифа, а всю логику реализовывать с помощью паттерна стратегия.

Ещё момент о котором хочу сказать, сделал запись cdr в файлы txt и парсинг на всех этапах программы, но в итоге ушел от этого подхода, т.к. мне хотелось, 
чтобы cdr обрабатывались в реальном времени. Для этого нужно было делать нотификацию микросервисов, для обработки, через брокер сообщений. 
В общем в ходе своих рассуждений, я остановился на 1 варианте. 

Все общие Сервисы, Репозитории, Модели и Дто вынесены в модуль common.


