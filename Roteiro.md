# Roteiro

## Kafka Server

### Iniciando Kafka

Para baixar a versão mais recente do Kafka, faça o download em: https://kafka.apache.org/downloads

Eu normalmente prefiro instalar qualquer software em `C:\opt\`.

Até o momento o Kafka possui uma dependência com o Zookeeper. Então para isso inicaremos os dois.

> :warning: validar se o diretórios de dados está vazio. `C:\tmp`   

```cmd
cd c:\opt\kafka
.\bin\windows\zookeeper-server-start.bat config\zookeeper.properties
```

```cmd
cd c:\opt\kafka
.\bin\windows\kafka-server-start.bat config\server.properties
```

### Criando um Tópico

```cmd
cd c:\opt\kafka
.\bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --create --topic my-dummy-topic --partitions 3 --replication-factor 3
```

### Executando o Console Consumer

```cmd
.\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic cccc --from-beginning
```

### Executando o Console Producer

```cmd
.\bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic cccc
```

## Exemplos

### Bookstore API

```cmd
cd personal\kafka-introdução\source\bookstore
mvn clean quarkus:dev
```

### Cart Service

```cmd
cd personal\kafka-introdução\source\cart
mvn clean compile exec:java
```

### Stock Service

```cmd
cd personal\kafka-introdução\source\stock
mvn clean compile exec:java
```
