# ProKati — Сервис аренды автомобилей

Веб-приложение для управления арендой автомобилей, написанное на чистых Jakarta Servlets без использования фреймворков.

## Стек технологий

- **Java 17**, Maven, WAR
- **Jakarta Servlet 6.0** — контроллеры (сервлеты)
- **JSP + JSTL 3.0** — шаблоны представлений
- **JDBC + PostgreSQL** — работа с базой данных
- **jBCrypt** — хеширование паролей
- **Apache Tomcat 10.1+** — сервер приложений
- **Bootstrap 5** — UI-фреймворк

## Архитектура

Трёхслойная архитектура без Spring и ORM:

```
Repository (JDBC) → Service (бизнес-логика) → Servlet (контроллер) → JSP (представление)
```

- **11 сущностей:** User, Role, Client, Car, CarCategory, CarColor, CarStatus, Rental, RentalStatus, Violation, ViolationType
- **Фильтры:** AuthFilter (проверка сессии), AdminFilter (проверка роли), EncodingFilter (UTF-8)
- **Паттерн PRG:** POST-запросы завершаются redirect для предотвращения повторной отправки форм

## Функциональность

### Для пользователей
- Регистрация и авторизация (HttpSession)
- Просмотр каталога автомобилей с фильтрацией по категории, цвету и статусу
- Заполнение профиля клиента (ФИО, телефон, email, водительское удостоверение)
- Создание аренды с автоматическим расчётом стоимости
- Просмотр своих аренд и нарушений

### Для администраторов
- Панель управления (dashboard) со сводной статистикой
- CRUD автомобилей, типов нарушений
- Управление клиентами (просмотр, редактирование)
- Управление арендами (просмотр, редактирование статуса)
- Управление нарушениями (создание, редактирование, привязка к аренде)
- Статистика аренд за произвольный период

## Требования

- **Java 17+**
- **Apache Maven 3.8+**
- **PostgreSQL 14+**
- **Apache Tomcat 10.1+**

## Установка и запуск

### 1. Создание базы данных

```sql
CREATE DATABASE car_rental;
```

### 2. Настройка подключения

Есть два способа сконфигурировать подключение к БД (env-переменные имеют приоритет):

**Способ A — через `db.properties` (для локальной разработки):**

Создайте файл `src/main/resources/db.properties` (он в `.gitignore`):

```properties
db.url=jdbc:postgresql://localhost:5432/car_rental
db.username=postgres
db.password=postgres
db.driver=org.postgresql.Driver
db.pool.size=10
```

**Способ B — через переменные окружения:**

Скопируйте `.env.example` и задайте свои значения:

```
DB_URL=jdbc:postgresql://localhost:5432/car_rental
DB_USERNAME=postgres
DB_PASSWORD=postgres
```

### 3. Сборка проекта

```bash
mvn clean package
```

### 4. Деплой на Tomcat

Скопируйте `target/car-rental.war` в директорию `webapps/` вашего Tomcat, либо запустите через IDE (см. ниже).

### 5. Запуск через IntelliJ IDEA (Shift+F10)

1. **Установите Tomcat** (скачайте [Apache Tomcat 10.1](https://tomcat.apache.org/) и распакуйте в любую папку)
2. **Edit Configurations** (вверху справа, рядом с кнопкой Run) → **Add New** → **Tomcat Server → Local**
3. Во вкладке **Server**:
   - **Application server** → Configure → укажите путь к папке Tomcat
   - **URL:** `http://localhost:8080/car-rental/`
4. Во вкладке **Deployment**:
   - Нажмите **+** → **Artifact** → выберите `car-rental:war exploded`
   - **Application context:** `/car-rental`
5. *(Опционально)* Для переменных окружения: вкладка **Startup/Connection** → **Environment variables** → добавьте `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
6. Нажмите **OK** → **Shift+F10** для запуска

### 6. Открыть в браузере

```
http://localhost:8080/car-rental/
```

Таблицы базы данных и начальные данные (роли, статусы, категории, цвета) создаются автоматически при первом запуске приложения через `AppContextListener`.

## Структура проекта

```
src/main/
├── java/com/degaltseva/carrental/
│   ├── model/          # POJO-сущности (11 классов)
│   ├── repository/     # Слой данных — JDBC, PreparedStatement
│   ├── service/        # Бизнес-логика, валидация
│   ├── servlet/        # Контроллеры
│   │   ├── auth/       #   Логин, регистрация, выход
│   │   ├── catalog/    #   Каталог, детали авто
│   │   ├── account/    #   Профиль, аренды, нарушения пользователя
│   │   └── admin/      #   Админ-панель, CRUD, статистика
│   ├── filter/         # AuthFilter, AdminFilter, EncodingFilter
│   ├── util/           # ConnectionPool, PasswordUtil, PathUtil
│   └── listener/       # AppContextListener
├── resources/
│   ├── db.properties   # Конфигурация БД
│   └── sql/init.sql    # DDL — создание таблиц и начальных данных
└── webapp/
    ├── WEB-INF/jsp/    # JSP-шаблоны
    └── static/css/     # Стили
```

## Схема базы данных

| Таблица | Описание |
|---------|----------|
| `roles` | Роли пользователей (user, admin) |
| `users` | Учётные записи (username, password_hash, role_id) |
| `clients` | Профили клиентов (ФИО, телефон, email, ВУ) |
| `car_categories` | Категории авто (эконом, комфорт, бизнес...) |
| `car_colors` | Цвета автомобилей |
| `car_statuses` | Статусы авто (доступен, в аренде, на ТО) |
| `cars` | Автомобили (марка, модель, год, стоимость/день) |
| `rental_statuses` | Статусы аренды (активна, завершена, отменена) |
| `rentals` | Записи об аренде (клиент, авто, даты, сумма) |
| `violation_types` | Типы нарушений (штраф по умолчанию) |
| `violations` | Нарушения (аренда, тип, сумма штрафа, оплата) |
