# 🍏 Plan de Mejora y Optimización - FruiterMan Fit

Este documento detalla la hoja de ruta para profesionalizar la aplicación y optimizar el flujo de trabajo IA-Desarrollador para minimizar el consumo de tokens y maximizar la precisión.

---

## ⚡ 0. Optimización de Flujo (IA + Dev)
**Objetivo:** Reducir el ruido y el consumo de tokens.

- [x] **Estructura I18n**: Creados `values/strings.xml` y `values-es/strings.xml`.
- [ ] **Context Sync**: Mantener un archivo `.context_sync.json` para evitar re-lecturas innecesarias de archivos grandes.
- [ ] **Edición Quirúrgica**: Priorizar `replace_file_content` sobre `write_file` para archivos extensos.
- [ ] **Comunicación Concisa**: Uso de comandos claros y objetivos específicos por turno.

## 🛠 1. Internacionalización y Textos (Español)
**Objetivo:** Eliminar el inglés de la interfaz y centralizar todos los textos.

- [x] **Migración a `strings.xml`**: Dashboard migrado.
- [ ] **Traducción de Categorías**: Traducir "Chest", "Waist", "Back", etc., en la lógica de filtrado y visualización.

## 🧹 2. Limpieza de Datos Hardcodeados
**Objetivo:** Hacer que la app sea dinámica y personalizable.

- [x] **Sistema de Usuarios**: Eliminar el usuario "invitado" hardcodeado en los ViewModels. Implementar una entidad `UserEntity` en Room.
- [ ] **Flujo de Login Real**: Conectar las pantallas de Login/Signup con el repositorio de usuarios.

## 🎨 3. UI/UX: Simplicidad y Amigabilidad
**Objetivo:** Menos es más. Hacer la app más intuitiva siguiendo Material 3.

- [ ] **Rediseño de Tarjetas**: Mejorar contraste y legibilidad.
- [ ] **Botones Fantasma**: Eliminar o implementar acciones para botones sin funcionalidad.

## 🚀 4. Nueva Feature: "Retos de Frutas"
**Objetivo:** Gamificación.

- [ ] **Feature: Fruit Challenges**: Implementar retos diarios con recompensas visuales.

---
## 📅 Backlog de Desarrollo (Tickets próximos)

- [ ] **Ticket #1: Crear UserViewModel**: Implementar lógica para registrar/loguear usuarios.
- [ ] **Ticket #2: Integrar LoginScreen**: Conectar campos de texto a ViewModel y persistencia.
- [ ] **Ticket #3: Integrar SignupScreen**: Validar inputs y llamar a `UserRepository.registerUser`.
- [ ] **Ticket #4: Gestión de Sesión**: Implementar lógica de autologin/redirigir a Dashboard si el usuario ya existe.

---
## ⚡ Notas para IA (Optimización de Contexto)
*   **Archivos Base**: Mantener `AppDatabase.kt`, `UserRepository.kt` y `MigrationProvider.kt` en el "sync" si se modifican.
*   **Edición**: Priorizar `replace_file_content` en `ViewModel` y `Screen`.
*   **Limitar Lectura**: Leer solo archivos de la screen/viewmodel específica en cada turno.
*   **Workflow Git**: Al completar un conjunto de tickets (Feature), cerrar rama, hacer merge y crear nueva rama para el siguiente bloque.
