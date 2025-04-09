# Aplicación para PINACAL

PINACAL, Piedra Natural de Castilla y León es una sociedad de empresas mineras de Castilla y León, esta aplicación tiene como proposito
conceder una plataforma a las empresas asociadas para poder publicar ofertas y demandas de trabajo.

Las empresas, tanto asociadas como no asociadas pueden publicar sus orfetas. Sin embargo los usuarios y empresas no asociadas solo veran
las publicaciones de las empresas asociadas mientras que estas veran todas las publicaciones.

## Proyecto de Gestión de Publicaciones y Mensajes

Este proyecto es una aplicación de escritorio desarrollada en **Java** que permite la gestión de publicaciones y mensajes entre usuarios. Incluye funcionalidades como la administración de publicaciones, visualización de mensajes y manejo de usuarios, con conexión a una base de datos **SQL**.

## 📌 Características Principales

### 📝 Gestión de Publicaciones

- **Aprobar o denegar publicaciones**: Los administradores pueden gestionar publicaciones con opciones de aprobación o denegación.
- **Eliminación de publicaciones**: Se incluye un botón para eliminar publicaciones con confirmación mediante una ventana emergente.
- **Validación de justificaciones**: Al denegar una publicación, se requiere una justificación.

### 💬 Gestión de Mensajes

- **Visualización de mensajes**: Los mensajes se muestran en una lista con un diseño personalizado utilizando la clase `Mensajes_Vista`.
- **Marcar mensajes como leídos**: Al abrir un mensaje, se actualiza su estado en la base de datos.
- **Interfaz gráfica**: Los mensajes y su lista comparten un diseño visual uniforme.

### 🗄️ Conexión a Base de Datos

- **Gestión de datos**: Se utiliza una base de datos SQL para almacenar usuarios, publicaciones y mensajes.
- **Conexión centralizada**: La conexión a la base de datos se gestiona mediante la clase `GestorConexion`.
- **Cierre seguro**: Se implementa un `Shutdown Hook` para cerrar la conexión al finalizar la aplicación.

## 🗂️ Estructura del Proyecto

src/
├── DB/UTIL/      # Clases para la conexión y gestión de la base de datos
├── MODEL/        # Clases de modelo (Mensajes, Publicaciones, Usuarios, etc.)
├── VIEW/         # Interfaces gráficas de usuario
├── MAIN/         # Clase principal para iniciar la aplicación
├── INFO/         # Archivos de información y tareas pendientes

## ⚙️ Requisitos del Sistema

- **Java**: JDK 8 o superior
- **Base de datos**: MySQL o cualquier base de datos SQL compatible
- **IDE recomendado**: IntelliJ IDEA 2024.3.5

## 🚀 Instalación y Configuración

1. **Clonar el repositorio**:
   bash
   git clone <URL_DEL_REPOSITORIO>
   cd <NOMBRE_DEL_PROYECTO>

2. **Configurar la base de datos**:
    - Crear una base de datos SQL con las tablas necesarias (`MENSAJES`, `PUBLICACIONES`, `USUARIOS`, etc.).
    - Configurar las credenciales de conexión en la clase `GestorConexion`.

3. **Ejecutar la aplicación**:
    - Abrir el proyecto en IntelliJ IDEA.
    - Ejecutar la clase `MAIN/Main.java`.

## 🧑‍💻 Uso

### 🔐 Inicio de Sesión

Los usuarios deben iniciar sesión para acceder a las funcionalidades de la aplicación.

## ⏳ Tareas Pendientes

- 🔧 Interfaz gráfica: Revisar y mejorar el diseño de las vistas.
- 🔐 Recuperación de contraseñas: Implementar validación por correo electrónico o notificación.
- 🧹 Depuración: Eliminar métodos y sentencias de prueba o depuración.

## 📄 Licencia

Este proyecto está bajo la licencia **MIT**.

## 📬 Contacto

Para cualquier consulta o soporte, contacta al desarrollador principal a través de este repositorio.