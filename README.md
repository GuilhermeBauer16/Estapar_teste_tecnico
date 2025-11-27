# Estapar Teste Tecnico
## Necessário para rodar o projeto

## Simulador

Inicie o simulador:

```bash
docker run -d --network="host" cfontes0estapar/garage-sim:1.0.0
```

* Para rodar o projeto, você precisa de um arquivo .env na sua máquina com a seguinte configuração:

```dotenv
DB_NAME=garage
DB_USERNAME=root
DB_PASSWORD=123456
DB_PORT=3306
DB_EXPOSE_PORT=3307
APPLICATION_PORT=3003
CONTAINER_PORT=8080
```

## Requisições

## Webhook controller

### Exemplo: 

#### ENTRY
 ```ENTRY
{ "sector":"B",
  "license_plate": "INIT_PLACA_SIM_12",
  "entry_time": "2025-11-27T08:03:00.000-03:00",
  "event_type": "ENTRY"
}
```

#### PARKED
 ```PARKED
{
  "license_plate": "INIT_PLACA_SIM_121",
  "lat": -23.561684,
  "lng": -46.655981,
  "event_type": "PARKED"
}
```

#### EXIT
 ```EXIT
{
  "license_plate": "INIT_PLACA_SIM_12",
  "exit_time": "2025-11-27T20:03:00.000-03:00",
  "event_type": "EXIT"
}
```

## Revenue Controller

* O campo currency é opcional, pois possui um valor padrão.

### Exemplo

 ```revenue
    http://localhost:3003/revenue?date=2025-11-27&sector=B

```


