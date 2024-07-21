# LunaLink 6X-1 Lander Simulator

## Usage

Set environment variables in a `.env` file:

```
LANDER_SECRET="secret"
LANDER_ADMIN_PASS="pass"
LANDER_FLAG="flag"
```

Set `/etc/hosts`:

```
127.0.0.1 lander
```

Bring up simulator:

```
docker compose up --build
```

Visit:

```
curl http://lander:8080/
```
