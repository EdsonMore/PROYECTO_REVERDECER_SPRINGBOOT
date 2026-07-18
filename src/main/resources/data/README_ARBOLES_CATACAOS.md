# 📋 Script SQL: Árboles de Catacaos - Instrucciones de Ejecución

## 📍 Información General
**Archivo:** `arboles-catacaos.sql`  
**Ubicación:** `src/main/resources/data/`  
**Región:** Catacaos, Piura, Perú  
**Coordenadas:** -5.2652777777778, -80.675  
**Altitud:** ~28 m s.n.m.  
**Año de datos:** 2026

---

## 🌳 Contenido del Script

### Árboles Insertados: 9 registros total

| Usuario | ID | Árboles | Especies |
|---------|----|---------|-|
| **Edson Duberly** | 1 | 3 | Mangó Criollo, Algarrobo, Papaya |
| **Brandon** | 3 | 3 | Limón Persa, Cocotero, Tamarindo |
| **Johana Ramos** | 6 | 3 | Chirimoyo, Guanacaste, Platanero |

---

## 🚀 Cómo Ejecutar el Script

### Opción 1: Desde MySQL/MariaDB CLI
```bash
mysql -u tu_usuario -p tu_base_de_datos < src/main/resources/data/arboles-catacaos.sql
```

### Opción 2: Usando SQL Workbench
1. Abre MySQL Workbench
2. Conecta a tu base de datos
3. Ve a `File > Open SQL Script`
4. Selecciona `arboles-catacaos.sql`
5. Presiona `Execute All` (Ctrl+Shift+Enter)

### Opción 3: Desde pgAdmin (PostgreSQL)
1. Click derecho en la base de datos
2. Selecciona `Query Tool`
3. Abre el archivo `arboles-catacaos.sql`
4. Ejecuta (F5 o botón ejecutar)

### Opción 4: Usando Spring Boot (application-dev.properties)
Si deseas que se ejecute automáticamente al iniciar, agrega a `application.properties`:
```properties
spring.sql.init.data-locations=classpath:data/arboles-catacaos.sql
spring.jpa.hibernate.ddl-auto=validate
```

---

## 📊 Datos Coherentes del Script

### Características de los Árboles:
✅ **Especies Reales:** Todas son plantas que crecen naturalmente en Catacaos  
✅ **Coordenadas Precisas:** Variadas dentro del área de Catacaos (-5.26 a -5.27, -80.67 a -80.68)  
✅ **Fechas Coherentes:** Años 2020-2024 de plantación, registros en 2026  
✅ **Estados Realistas:** Todos en "Saludable" con descripciones agrícolas auténticas  
✅ **URLs de Fotos:** Rutas relativas coherentes en `/img/arboles/`  

### Especies Seleccionadas para Catacaos:
1. **Mangifera indica** (Mangó) - Clima tropical seco ideal
2. **Prosopis spp** (Algarrobo) - Nativo de la región
3. **Carica papaya** (Papaya) - Bien adaptada
4. **Citrus limetta** (Limón Persa) - Producción todo el año
5. **Cocos nucifera** (Cocotero) - Palmera tropical
6. **Tamarindus indica** (Tamarindo) - Agroforestería
7. **Annona cherimola** (Chirimoyo) - Frutos de calidad
8. **Enterolobium cyclocarpum** (Guanacaste) - Emblemático de Piura
9. **Musa sapientum** (Platanero) - Rápido crecimiento

---

## ⚠️ Recomendaciones Importantes

1. **Hacer backup primero:**
   ```bash
   mysqldump -u usuario -p base_de_datos > backup_antes_arboles.sql
   ```

2. **Verificar conexiones de usuarios:**
   - Antes de ejecutar, asegúrate que los usuarios con ID 1, 3 y 6 existen en la tabla `usuarios`
   - El script referencia estos IDs en `usuario_id`

3. **Validar después de insertar:**
   ```sql
   SELECT COUNT(*) as total_arboles FROM arboles WHERE usuario_id IN (1, 3, 6);
   -- Debería retornar: 9
   ```

4. **No duplicar datos:**
   - El script está diseñado para insertar nuevos registros
   - Si lo ejecutas dos veces, habrá duplicados
   - Usa `DELETE` si necesitas limpiar:
     ```sql
     DELETE FROM arboles WHERE usuario_id IN (1, 3, 6) AND nombre LIKE '%#%';
     ```

---

## 🔍 Verificación de Datos

Después de ejecutar, verifica los datos con estos comandos:

```sql
-- Ver todos los árboles insertados
SELECT * FROM arboles WHERE usuario_id IN (1, 3, 6) ORDER BY usuario_id, nombre;

-- Contar por usuario
SELECT usuario_id, COUNT(*) as cantidad FROM arboles WHERE usuario_id IN (1, 3, 6) GROUP BY usuario_id;

-- Ver coordenadas (para verificar están dentro de Catacaos)
SELECT nombre, latitud, longitud FROM arboles WHERE usuario_id IN (1, 3, 6) ORDER BY nombre;
```

---

## 📝 Notas Técnicas

- **Tabla:** `arboles` (ya debe existir en tu BD)
- **Motor:** Compatible con MySQL 5.7+, MariaDB, PostgreSQL
- **Constraints:** Respeta las FK de `usuario_id`
- **Campos Nullable:** `latitud`, `longitud`, `nombre`, `foto_url` pueden ser NULL (están completos aquí)
- **Encoding:** UTF-8 (para caracteres especiales españoles)

---

## 💬 Soporte

Si hay algún error al ejecutar:
1. Verifica que la tabla `arboles` existe: `DESCRIBE arboles;`
2. Verifica usuarios existen: `SELECT id FROM usuarios WHERE id IN (1, 3, 6);`
3. Revisa los IDs de secuencia de `arboles_id_seq` si es necesario resetearla

¡Listo para verdear Catacaos! 🌱
