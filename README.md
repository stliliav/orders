# orders
## Запуск инфраструктуры

Для локального запуска базы данных PostgreSQL и брокера сообщений RabbitMQ используется Docker

Убедитесь, что у вас установлен Docker и Docker Compose, затем выполните в корне проекта команду:
`docker-compose up -d`

Доступные сервисы после запуска:
PostgreSQL: `localhost:5432` (БД: `orders_db`, юзер: `postgres`, пароль: `password`)
RabbitMQ: `localhost:5672`
RabbitMQ Management UI: `http://localhost:15672` (логин и пароль по умолчанию: `guest` / `guest`)
