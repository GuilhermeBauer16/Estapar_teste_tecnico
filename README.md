# Estapar Teste Tecnico
## Necesário para rodar o projeto

## Simulador

Inicie o simulador:

```bash
docker run -d --network="host" cfontes0estapar/garage-sim:1.0.0
```

* To run the project, you need a .env file on your machine with the following configuration:

```dotenv
DB_NAME=garage
DB_USERNAME=root
DB_PASSWORD=123456
DB_PORT=3306
DB_EXPOSE_PORT=3307
APPLICATION_PORT=3003
CONTAINER_PORT=8080
```
