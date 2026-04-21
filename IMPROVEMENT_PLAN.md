# 🍏 Plan de Mejora y Optimización - FruiterMan Fit

Este documento detalla la hoja de ruta para profesionalizar la aplicación y optimizar el flujo de trabajo IA-Desarrollador para minimizar el consumo de tokens y maximizar la precisión.

---

## ⚡ 0. Optimización de Flujo (IA + Dev)
**Objetivo:** Reducir el ruido y el consumo de tokens.

- [x] **Estructura I18n**: Creados `values/strings.xml` y `values-es/strings.xml`.
- [x] **Context Sync**: Mantener un archivo `.context_sync.json` para evitar re-lecturas innecesarias de archivos grandes.
- [x] **Edición Quirúrgica**: Priorizar `replace_file_content` sobre `write_file` para archivos extensos.
- [x] **Comunicación Concisa**: Uso de comandos claros y objetivos específicos por turno.

## 🛠 1. Internacionalización y Textos (Español)
**Objetivo:** Eliminar el inglés de la interfaz y centralizar todos los textos.

- [x] **Migración a `strings.xml`**: Dashboard migrado.
- [x] **Traducción de Categorías**: Traducidas en Biblioteca y Detalle usando `translateCategory`.
- [x] **Localización de Auth**: Login y Signup migrados a `strings.xml`.

## 🧹 2. Limpieza de Datos Hardcodeados
**Objetivo:** Hacer que la app sea dinámica y personalizable.

- [x] **Sistema de Usuarios**: Eliminado el usuario "invitado". Implementada `UserEntity`.
- [x] **Infraestructura de Usuarios (DAO/Repo/ViewModel)**: Implementados.
- [x] **Flujo de Login Real**: Conectado con `UserRepository`.
- [x] **Gestión de Sesión**: Implementado autologin en `SplashScreen`.

## 🎨 3. UI/UX: Simplicidad y Amigabilidad
**Objetivo:** Menos es más. Hacer la app más intuitiva siguiendo Material 3.

- [ ] **Rediseño de Tarjetas**: Mejorar contraste, añadir bordes suaves y profundidad.
- [ ] **Interactividad**: Hacer que los items de actividad en el Dashboard sean accionables o informativos.

## 🚀 4. Nueva Feature: "Retos de Frutas"
**Objetivo:** Gamificación.

- [x] **Infraestructura de Retos**: DAO y Repository listos.
- [ ] **UI: Fruit Challenges**: Añadir botón de reclamo y feedback visual al completar.

---
## 📅 Backlog de Desarrollo (Tickets próximos)

- [ ] **Ticket #5: Rediseño Visual**: Aplicar elevación y bordes consistentes en `DashboardComponents`.
- [ ] **Ticket #6: Gamificación Retos**: Implementar botón "Reclamar" en `ChallengeItem`.
- [ ] **Ticket #7: Refactor ActivityItem**: Añadir navegación a registro de nutrición/movilidad.

---
## ⚡ Notas para IA (Optimización de Contexto)
*   **Archivos Base**: Mantener `AppDatabase.kt`, `UserRepository.kt` y `MigrationProvider.kt` en el "sync" si se modifican.
*   **Edición**: Priorizar `replace_file_content` en `ViewModel` y `Screen`.
*   **Limitar Lectura**: Leer solo archivos de la screen/viewmodel específica en cada turno.
*   **Workflow Git**: Al completar un conjunto de tickets (Feature), cerrar rama, hacer merge y crear nueva rama para el siguiente bloque.
