# spring-cloud-stream-rabbitmq-cloudevents
Multi binding project with Spring Cloud Stream Functions reading messages from RabbitMQ with CloudEvents format

__Table of content__

- [Technical Stack](#technical-stack)
- [Configuration](#configuration)
  - [Actuator](#actuator)    
- [Run RabbitMQ](#run-rabbitmq)
- [Run it](#run-it)



## Technical Stack:

- Java 11+
- Maven 3.6+
- [Spring Boot (2.3.4.RELEASE)](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/reference/html/)
    - Spring Boot Actuator for exposing management endpoints
    - WebFlux
- [Spring Cloud (Hoxton.SR8)](https://cloud.spring.io/spring-cloud-static/Hoxton.SR8/reference/html/spring-cloud.html)
    - [Sleuth](https://spring.io/projects/spring-cloud-sleuth)
    - [Spring Cloud Stream (3.0.4.RELEASE)](https://cloud.spring.io/spring-cloud-static/spring-cloud-stream/3.0.4.RELEASE/reference/html/)
    - [Function](https://spring.io/projects/spring-cloud-function)
- [Cloud Events v1.0](https://cloudevents.io/)
- [Lombok abstraction](https://projectlombok.org/)


## Architecture Model

This application relies on spring-cloud-stream and binder dependencies.
It implements functional requirements based on the incoming message using interface simple [java.util.function.Function](https://cloud.spring.io/spring-cloud-static/spring-cloud-stream/3.0.4.RELEASE/reference/html/).
Multiple Functions defined in `application.yaml`so each one consumes from its own queue [More info](https://cloud.spring.io/spring-cloud-stream/reference/html/spring-cloud-stream.html#_multiple_functions_in_a_single_application).
These consumers bind to a more descriptive name ``cat`` and ``dog`` with following properties:

- spring.cloud.stream.bindings.cat.destination: mammal.tx (rabbit exchange where the queue is bounded)
- spring.cloud.stream.bindings.cat.group: cat-queue (rabbit queue)

- spring.cloud.stream.bindings.dog.destination: mammal.tx (rabbit exchange where the queue is bounded)
- spring.cloud.stream.bindings.cat.group: dog-queue (rabbit queue)

[More functional binding info](https://cloud.spring.io/spring-cloud-static/spring-cloud-stream/3.0.4.RELEASE/reference/html/spring-cloud-stream.html#_functional_binding_names).

Through the above java.util.function.Function implementation and configuration, Spring Cloud Stream starter automatically detects the binder found on the classpath and uses this configuration.

Spring Cloud Stream provides a Binder abstraction.
The yukon-analytics implements [RabbitMQ binder](https://cloud.spring.io/spring-cloud-static/spring-cloud-stream-binder-rabbit/3.0.4.RELEASE/reference/html/spring-cloud-stream-binder-rabbit.html#_using_existing_queuesexchanges) and uses following properties with existing values already defined:

[More info about Rabbit binder properties](https://cloud.spring.io/spring-cloud-static/spring-cloud-stream-binder-rabbit/3.0.4.RELEASE/reference/html/spring-cloud-stream-binder-rabbit.html#_using_existing_queuesexchanges)

## Configuration

### Actuator

This application uses [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.3.3.RELEASE/reference/htmlsingle/#production-ready), which exposes different management endpoints.
As it also uses [Spring Cloud Stream](https://cloud.spring.io/spring-cloud-static/spring-cloud-stream/3.0.3.RELEASE/reference/html/spring-cloud-stream.html#binding_visualization_control), 
then it contributes with a couple of endpoints when we run the following request:

```bash
:~$ curl http://localhost:8079/actuator
```

## Run RabbitMQ

This application use RabbitMQ, run it with Docker before start the application.

```bash
docker run --rm -it --hostname my-rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management-alpine
```

### Run it
```bash
mvn spring-boot:run
```