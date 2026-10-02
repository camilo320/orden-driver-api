# orden-driver-api

Desarrollo de una API REST para la gestión de órdenes de transporte de una empresa de
movilidad.

## Volver a crear imagenes docker 

```shell
docker compose up --build -d
```


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
## Credenciales para el usuario de prueba

```json
{
  "username": "usuario.uno@ait.pase.com",
  "password": "password"
}
```
## Eliminar logs

```shell
rm -r logs
```

## Detener los contenedores y eliminarlas

```shell
docker compose down
```

## Detener los contenedores, eliminarlas y eliminar la data de la base de datos

```shell
docker compose down --volumes
```
