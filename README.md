# 🏥 Clínica - Módulo de Persistencia y Testing

Repositorio con la implementación y suite de pruebas del módulo de persistencia para un sistema de gestión clínica, enfocado en el acceso a datos y validaciones arquitectónicas.

## 📋 Arquitectura

* **AsociadoDTO:** Encapsula los datos garantizando reglas de integridad estructural.


* **DAO:** Ejecuta las operaciones CRUD parametrizadas para prevenir inyección SQL.


* **BaseDeDatos:** Gestiona la conexión a MySQL mediante el patrón Singleton y autoconfigura el esquema de datos inicial.



## 🧪 Estrategia de Pruebas

1. **Testing Unitario:** Valida de forma aislada la asignación y reglas de integridad de los setters en `AsociadoDTO` (ej. campos no nulos, números positivos).


2. **Testing de Integración:** Verifica la comunicación real con MySQL (módulos DTO, DAO y BaseDeDatos). Evalúa el Singleton, la creación automática de tablas, los datos semilla y las operaciones CRUD atrapando excepciones SQL.


3. **Testing de Concurrencia y Mocks:** Evalúa el flujo de la clase `Ambulancia` y la rutina `RetornoAutomarico`, utilizando dobles de prueba (interfaz `State`) para validar sincronización e hilos independientes.



## ⚙️ Stack y Ejecución

* **Tecnologías:** Java, JUnit.
* **Base de Datos:** Entorno local con Apache y MySQL activo (ej. XAMPP). *Nota: La configuración de la BD (tablas y datos de prueba) se autogenera al iniciar mediante la rutina `iniciarBD` con credenciales internas*.


* **Pasos:**
1. Abrir el proyecto en **Eclipse**.
2. Hacer clic derecho en las clases de test.
3. Seleccionar `Run As` > `JUnit Test`.
