# orders
## Архитектура:


<img width="667" height="361" alt="изображение" src="https://github.com/user-attachments/assets/d1f89c21-3aae-425d-9486-f95d3af4624c" />


Стек: Java 21, Spring Boot 3.4, Spring Data JPA, Spring AMQP (RabbitMQ), Flyway, PostgreSQL, Springdoc OpenAPI

## Запуск инфраструктуры

## 1. Убедиться, что Docker Desktop запущен, затем выполнить в терминале:

`docker-compose up -d`

Поднимаются два сервиса:

PostgreSQL с адресом: localhost:5432 (БД: orders_db, user: postgres, pass: password)

RabbitMQ с адресом: localhost:5672RabbitMQ UIhttp://localhost:15672 (guest / guest)

## 2. Запустить приложение

`./mvnw spring-boot:run`

Или через IntelliJ IDEA/любую другую IDE: кнопка Run в файле MainApplication

## 3. Открыть Swagger UI

`http://localhost:8080/swagger-ui/index.html`

REST API

`POST /api/orders` - создать заказ

`curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Иван Иванов",
    "items": [
      {
        "productName": "Ноутбук",
        "quantity": 1,
        "price": 75000
      },
      {
        "productName": "Мышь",
        "quantity": 2,
        "price": 1500
      }
    ]
  }'`

Ответ: 201 Created:

`json{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "customerName": "Иван Иванов",
  "orderDate": "2026-06-15T11:37:44.667474",
  "status": "CREATED",
  "items": [
    { "id": 1, "productName": "Ноутбук", "quantity": 1, "price": 75000 },
    { "id": 2, "productName": "Мышь",    "quantity": 2, "price": 1500  }
  ]
}`

После создания заказ автоматически уходит в очередь RabbitMQ и его статус меняется на PROCESSING


`GET /api/orders` - список заказов с фильтрацией и пагинацией

# Все заказы (первые 10):
`curl http://localhost:8080/api/orders`

# Фильтр по статусу
`curl "http://localhost:8080/api/orders?status=CREATED"`

# Пагинация и сортировка
`curl "http://localhost:8080/api/orders?page=0&size=5&sort=orderDate,desc"`

# Комбинация
`curl "http://localhost:8080/api/orders?status=PROCESSING&page=0&size=10&sort=orderDate,desc"`


`GET /api/orders/{id}` - получить заказ по ID

`curl http://localhost:8080/api/orders/550e8400-e29b-41d4-a716-446655440000`

Ответ: 200 OK или 404 Not Found если заказ не найден


`PUT /api/orders/{id}/status` - обновить статус заказа

`curl -X PUT http://localhost:8080/api/orders/550e8400-e29b-41d4-a716-446655440000/status \
  -H "Content-Type: application/json" \
  -d '"COMPLETED"'`

Допустимые статусы: CREATED, PROCESSING, COMPLETED, CANCELED


`GET /api/orders/total?customerName=...` - сумма заказов клиента

`curl "http://localhost:8080/api/orders/total?customerName=Иван%20Иванов"`

Возвращает общую сумму всех заказов клиента (SUM(price * quantity))


## Сообщения HTTP

Ситуация           Код   

Заказ создан       201 Created

Запрос выполнен    200 OK

Неверные данные    400 Bad Request

Заказ не найден    404 Not Found


## Тестирование

Только unit-тесты:
`./mvnw test`

Все тесты, включая интеграционные:
`./mvnw verify`
