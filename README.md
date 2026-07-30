# Proyecto Unidad 2

Aplicación REST desarrollada con Spring Boot para administrar un catálogo de videojuegos/items con reglas de negocio verificables, pruebas por capas, CI/CD y evidencia de TDD.

## Funcionalidades

- Crear items.
- Consultar items por id.
- Listar items.
- Filtrar items por plataforma.
- Actualizar items.
- Validar reglas de negocio:
  - `titulo` obligatorio.
  - `plataforma` obligatoria.
  - `precio` mayor a 0.
- Obtener el precio promedio desde el repositorio con consulta personalizada.

## Tecnologías

- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Validation
- JUnit 5
- Mockito
- H2 para pruebas
- Testcontainers para integración
- Maven
- GitHub Actions
- Docker
- k6 para carga

## Ejecución local

```bash
git clone <url-del-repo>
cd unidad2
mvn clean install
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

## Endpoints principales

- `GET /items`
- `GET /items/{id}`
- `GET /items?plataforma=Switch`
- `POST /items`
- `PUT /items/{id}`

Ejemplo de creación:

```json
{
  "titulo": "Zelda",
  "plataforma": "Switch",
  "precio": 59.99
}
```

## Pruebas

### Ejecutar todas las pruebas

```bash
mvn test
```

### Ejecutar verificación completa

```bash
mvn -B verify
```

### Tipos de prueba incluidos

- Pruebas unitarias de servicio con Mockito y AAA.
- Pruebas de repositorio con `@DataJpaTest`.
- Pruebas de controlador con `@WebMvcTest`.
- Pruebas de integración con `@SpringBootTest` + `MockMvc`.

## TDD aplicado

Una funcionalidad desarrollada con TDD fue la de crear items:

1. `red:` primero se escribió una prueba que esperaba `201 Created` y validación de campos.
2. `green:` se implementó lo mínimo en el servicio y el controlador para guardar el item.
3. `refactor:` se separó la lógica en `ItemService`, se movieron validaciones de negocio y se centralizó el manejo de errores.

## CI/CD

El workflow está en [`.github/workflows/ci.yml`](./.github/workflows/ci.yml).

Se ejecuta en cada `push` y `pull_request` sobre `main` o `develop`, corre:

```bash
mvn -B verify
```

Luego construye la imagen Docker y, si existen credenciales de registry, la publica.

## Docker

```bash
docker build -t unidad2:latest .
docker run -p 8080:8080 unidad2:latest
```

## Prueba de carga

El archivo [`loadtest-k6.js`](./loadtest-k6.js) sube progresivamente hasta 500 usuarios virtuales para observar saturación de hilos y aumento de latencia.

## Resultados esperados

- Más de 8 métodos de prueba.
- Pruebas verdes en CI.
- Cobertura generada por JaCoCo en `target/site/jacoco`.

