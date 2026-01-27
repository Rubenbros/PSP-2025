# Ejercicio Clase 3: Migrar Comercios a JPA

**Puntuación:** 0.5 puntos
**Tiempo:** 20-25 minutos

---

## Objetivo

Convertir la API de comercios de la clase anterior para usar Spring Data JPA con base de datos H2.

---

## Pasos

### 1. Añadir dependencias (2 min)

En `pom.xml`, añade:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 2. Configurar application.properties (2 min)

```properties
spring.datasource.url=jdbc:h2:mem:springfielddb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.h2.console.enabled=true
```

### 3. Convertir Comercio a Entidad JPA (5 min)

```java
@Entity
public class Comercio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String propietario;
    private String tipo;
    private boolean abierto;

    // Constructores, getters, setters
}
```

### 4. Convertir Repository a Interface (3 min)

```java
@Repository
public interface ComercioRepository extends JpaRepository<Comercio, Long> {
    // ¡Los métodos CRUD vienen gratis!

    // Añade este Query Method:
    List<Comercio> findByTipo(String tipo);
}
```

### 5. Cargar datos iniciales (5 min)

```java
@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initData(ComercioRepository repository) {
        return args -> {
            repository.save(new Comercio(null, "Kwik-E-Mart", "Apu", "supermercado", true));
            repository.save(new Comercio(null, "Taberna de Moe", "Moe", "bar", true));
            repository.save(new Comercio(null, "Krusty Burger", "Krusty", "restaurante", true));
            System.out.println("Comercios cargados: " + repository.count());
        };
    }
}
```

### 6. Actualizar Servicio y Controlador (5 min)

- Usa `repository.existsById()` para verificar existencia
- Añade endpoint: `GET /api/comercios/tipo/{tipo}`
- Añade `@Valid` en POST y PUT para activar validaciones

---

## Entrega

Demuestra al profesor:

1. **Consola H2 funciona:**
   - [ ] Accede a `http://localhost:8080/h2-console`
   - [ ] JDBC URL: `jdbc:h2:mem:springfielddb`
   - [ ] Ejecuta: `SELECT * FROM COMERCIO`

2. **Endpoints funcionan:**
   - [ ] `GET /api/comercios` → Lista desde BD
   - [ ] `POST /api/comercios` → Crea y persiste (verificar en H2)
   - [ ] `GET /api/comercios/tipo/bar` → Filtra por tipo
   - [ ] `POST /api/comercios` con nombre vacío → Error de validación
