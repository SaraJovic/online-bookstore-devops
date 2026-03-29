# Online Bookstore — Mikroservisna aplikacija

Projekat razvijen u okviru predmeta Integracija razvoja i operative u informacionim tehnologijama.  
Aplikacija je zasnovana na mikroservisnoj arhitekturi sa Spring Boot frameworkom.

Arhitektura
API Gateway (port 8080)
  User Service        (port 8081) — registracija, login, JWT
  Book Service        (port 8082) — katalog knjiga
  Order Service       (port 8083) — narudžbine, RabbitMQ publisher
  Notification Service(port 8084) — RabbitMQ consumer, obaveštenja

## Tehnologije

|Sloj|Tehnologija|
|-|-|
|Backend|Java 17, Spring Boot 3.x|
|API Gateway|Spring Cloud Gateway|
|Baza podataka|PostgreSQL|
|Message broker|RabbitMQ|
|Reaktivna komunikacija|Spring WebFlux / Project Reactor|
|RPC|gRPC (User ↔ Order)|
|Kontejnerizacija|Docker, Docker Compose|
|CI/CD|GitHub Actions|
|Deployment|Railway.app|
|Monitoring|Prometheus + Grafana|
|Tracing|Zipkin|
|Logovi|Logback + Spring Actuator|
|Statička analiza|SonarCloud|

## Komunikacija između servisa

* **REST** — Gateway rutira sve zahteve ka servisima
* **RabbitMQ** — Order Service objavljuje poruke, Notification Service konzumira
* **WebFlux/Reactor** — Book Service koristi reaktivni stack
* **gRPC** — User Service i Order Service (validacija tokena)

## Pokretanje lokalno

### Preduslovi

* Java 17+
* Docker i Docker Compose
* Maven 3.8+

### Pokretanje

bash
Kloniranje repozitorijuma
git clone https://github.com/SaraJovic/online-bookstore.git
cd online-bookstore

Pokretanje svih servisa
docker compose up --build

### Portovi

|Servis|Port|
|-|-|
|API Gateway|8080|
|User Service|8081|
|Book Service|8082|
|Order Service|8083|
|Notification Service|8084|
|RabbitMQ Management UI|15672|
|Prometheus|9090|
|Grafana|3000|
|Zipkin|9411|

## Git Workflow

* main — uvek stabilan, production-ready kod
* develop — integraciona grana
* feature/<naziv>— svaka nova funkcionalnost
* Svaka promena ide kroz **Pull Request**
* CI pipeline se pokreće automatski na svaki push i PR



