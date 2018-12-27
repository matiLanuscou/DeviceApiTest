**Instalación**

```
clonar repositorio
ejecutar mvn clean install
ejecutar mvn exec:java
```
De esta forma se levanta una instancia el servidor enbebido (jetty)

También se puede utilizar instalando el war en un servidor, por ejemplo Tomcat

**Utilización**
- Guardar registro
```
POST
URI: http://localhost:8080/DeviceApi/api/device/save
BODY: {
	      "mac" : "FF:FF:FF:FF:FF:FF",
	      "time" : "31/01/2018"
      }
```

- Buscar registro por mac
```
GET
URI: http://localhost:8080/DeviceApi/api/device/getDevice?mac=FF:FF:FF:FF:FF:FF
```

- Buscar registro por id
```
GET
URI: http://localhost:8080/DeviceApi/api/device/getDevice?id=1
```

- Buscar todos los registros
```
GET
URI: http://localhost:8080/DeviceApi/api/device/getDevices
```

**Aclaración**

-Este proyecto es solo un comienzo, se necesita implementar el modelo, por ahora solo es un objeto en memoria, como también implementar la inyección de dependencias. Como así también el manejo más profundo de errores. Vuelvo a aclarar, es solo una base que forma parte de un ejercicio.
