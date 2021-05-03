# Proyecto comic-service

Esta aplicación tiene la responsabilidad de traer datos de la API de Marvel y persistirlos en una base de datos local, únicamente para los personajes `Iron Man (ironman)` y `Captain America (capamerica)` La información que se sincroniza es:
- Listado de cómics en los que aparece cada personaje.
- Listado de cada uno de los colaboradores creativos, y su rol, que participa en cada uno de los cómics del punto anterior.
- Listado de personajes que co-aparecen con los personajes principales.

## Características

La aplicación esta construida con Quarkus, dada su simplicidad y naturaleza cloud, lo cual lo hace muy ligero.
El componente sirve peticiones REST y utiliza las siguientes extensiones:
- Swagger, para exponer UI de Swagger.
- Health, para exponer UI de salud del componente.
- Rest (server y client), mediante Jackson.
- Hibernate ORM Panache, para capa de persistencia.
- Conectividad con BD MySql.
- Scheduler, para la programación de llamadas a la API cada 24 hrs.
- Caffeine, para implementación de caché para peticiones rest.

## Extras

La aplicación cuenta con dos endpoints adicionales a la funcionalidad básica requerida:

- Swagger. UI de Swagger que documenta los servicios expuestos, además de endpoints utilitarios
```shell script
http://localhost/q/swagger-ui/
```

- Health. UI que muestra el estado de salud de la aplicación.
```shell script
http://localhost/q/health-ui/
```

## Inicializando aplicación y base de datos

### Prerrequisitos

Para poder orquestar los componentes de la aplicación, jar y base de datos, es necesario contar con Docker.

### Compilando y construyendo aplicación

Aprovechando la naturaleza contenerizable de Quarkus, ésta debe compilarse y empaquetarse utilizando Docker. Para ello, es necesario ejecutar los siguientes comandos:

### Modo JVM (también llamado FAT JAR)

```shell script
chmod +x mvnw
./mvnw clean package -Dquarkus.package.type=uber-jar
```
A continuación, construir la imagen de docker
```shell script
docker build -f src/main/docker/Dockerfile.jvm-uber-jar -t albo/comic-service-jvm-uber .
```
Por último, levantar Docker compose
```shell script
docker-compose up -f src/main/docker/docker-compose-native-jvm-uber.yml -p comic_library -d
```

Contenedores iniciados
![image](https://user-images.githubusercontent.com/4373067/116845816-8809cb80-abac-11eb-8d00-325cabfefa78.png)

Log ejecución
![image](https://user-images.githubusercontent.com/4373067/116845844-9821ab00-abac-11eb-9a5b-60d6a09e2649.png)

Solicitud colaborator
![image](https://user-images.githubusercontent.com/4373067/116845993-fa7aab80-abac-11eb-81d9-562e98582950.png)

Solicitud character
![image](https://user-images.githubusercontent.com/4373067/116845989-f77fbb00-abac-11eb-97d8-7b0555875bdf.png)


### Levantando con docker-compose

Para realizar el levantamiento del entorno de la apliación, el cual incluye una base de datos MySql

- BD MySql, versión 8.0.24. Al momento de inicialización de docker-compose, se inyecta script de creación de esquema, así como datos de acceso.
- Contenedor de la aplicación Quarkus, el cual se crea al momento de levantamiento.


## Otros modos
Se incluyen otras versiones de docker file, para realizar compilación con graalvm. Lamentablemente, he encontrado problemas de conectividad con la BD en este tipo de imágenes, y `no son recomendables hasta nuevo aviso`.

#### Modo JVM

```shell script
./mvnw clean package
```
A continuación, construir la imagen de docker
```shell script
docker build -f src/main/docker/Dockerfile.jvm -t albo/comic-service-jvm .
```
Por último, levantar Docker compose
```shell script
docker-compose up -f src/main/docker/docker-compose-jvm.yml -p comic_library -d
```

#### Modo Nativo

```shell script
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
```
A continuación, construir la imagen de docker
```shell script
docker build -f src/main/docker/Dockerfile.native -t albo/comic-service-native .
```
Por último, levantar Docker compose
```shell script
docker-compose up -f src/main/docker/docker-compose-native.yml -p comic_library -d
```
