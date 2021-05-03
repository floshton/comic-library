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
- Health. UI que muestra el estado de salud de la aplicación.

## Inicializando aplicación y base de datos

### Prerrequisitos

Para poder orquestar los componentes de la aplicación, jar y base de datos, es necesario contar con Docker.

### Compilando y construyendo aplicación

Aprovechando la naturaleza contenerizable de Quarkus, ésta debe compilarse y empaquetarse utilizando Docker. Para ello, es necesario ejecutar el siguiente comando:

```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true

### Levantando con docker-compose

Para realizar el levantamiento del entorno de la apliación, el cual incluye una base de datos MySql, es necesario ejecutar el siguiente comando de Docker:
```shell script
docker-compose up -f src/main/docker/docker-compose.yml -p comic_library -d
```
el cual incluye:
- BD MySql, versión 8.0.24. Al momento de inicialización de docker-compose, se inyecta script de creación de esquema, así como datos de acceso.
- Contenedor de la aplicación Quarkus, el cual se crea al momento de levantamiento.