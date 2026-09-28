# orden-driver-api

Desarrollo de una API REST para la gestión de órdenes de transporte de una empresa de
movilidad.

## Crear imagenes docker y lanzar los contenedores

```shell
docker compose up -d
```


## Crear imagenes docker y lanzar los contenedores y mantenerlos en segundo plano

```shell
docker compose up -d
```

## URLs

- Documentacion swagger: http://localhost:8080/swagger-ui/index.html

## Detener los contenedores y eliminarlas

```shell
docker compose down
```

## Detener los contenedores, eliminarlas y eliminar la data de la base de datos

```shell
docker compose down --volumes
```
