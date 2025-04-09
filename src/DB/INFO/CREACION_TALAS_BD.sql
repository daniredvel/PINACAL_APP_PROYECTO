-- BASE DE DATOS DE PINACAL

-- La app recoge ofertas o demandas de trabajo publicadas por empresas asociadas
-- Los usuarios pueden publicar ofertas o demandas de trabajo y guardar las publicaciones que les interesen
-- Los usuarios pueden registrarse en la aplicación y acceder a ella con un nombre de usuario y contraseña

-- Las tablas de la base de datos de Pinacal son las siguientes:
-- TIPOS_USUARIOS: Tabla que almacena los tipos de usuarios, sus nombres y permisos
-- USUARIOS: Tabla que almacena los datos de los usuarios de la aplicación
-- PUBLICACIONES: Tabla que almacena los datos de las publicaciones de los usuarios
-- PUBLICACIONES_GUARDADAS: Tabla que almacena los datos de las publicaciones guardadas por los usuarios
-- MENSAJES: Tabla que almacena los mensajes enviados por los administradores a los usuarios

CREATE TABLE TIPOS_USUARIOS (
    id_tipo_usuario INT PRIMARY KEY, -- ID como clave primaria, permisos de admin = 2, usuario = 1
    nombre_tipo VARCHAR(50) NOT NULL UNIQUE, -- Nombre del tipo de usuario (Administrador, Empresa asociada, etc.)
    permisos VARCHAR(255) NOT NULL -- Permisos del tipo de usuario
);

CREATE TABLE USUARIOS (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT, -- ID como clave primaria
    nombre VARCHAR(50) NOT NULL UNIQUE, -- Nombre de usuario para inicio de sesión
    password VARCHAR(500) NOT NULL, -- Contraseña
    email VARCHAR(50) NOT NULL UNIQUE, -- Email para el registro
    direccion VARCHAR(250), -- Dirección del usuario
    telefono VARCHAR(15) NOT NULL UNIQUE, -- Teléfono del usuario
    id_tipo_usuario INT NOT NULL, -- ID del tipo de usuario
    FOREIGN KEY (id_tipo_usuario) REFERENCES TIPOS_USUARIOS(id_tipo_usuario) ON DELETE CASCADE -- Clave foránea de la tabla TIPOS_USUARIOS
);

CREATE TABLE PUBLICACIONES (
    id_publicacion INT PRIMARY KEY AUTO_INCREMENT, -- ID como clave primaria
    titulo VARCHAR(50) NOT NULL, -- Título de la publicación
    descripcion VARCHAR(500) NOT NULL, -- Descripción de la publicación
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Fecha de la publicación
    tipo VARCHAR(50) NOT NULL, -- Tipo de publicación (oferta o demanda) No afecta a la lógica de la aplicación
    id_usuario INT NOT NULL, -- ID del usuario que publica
    FOREIGN KEY (id_usuario) REFERENCES USUARIOS(id_usuario) ON DELETE CASCADE -- Clave foránea de la tabla USUARIOS
);

CREATE TABLE MENSAJES (
    id_mensajes INT PRIMARY KEY AUTO_INCREMENT ,
    id_usuario_de INT NOT NULL,  -- ID del usuario que envía el mensaje, administrador
    id_usuario_para INT NOT NULL,  -- ID del usuario que recibe el mensaje
    asunto VARCHAR(255) NOT NULL, -- Asunto del mensaje
    contenido TEXT NOT NULL, -- Contenido del mensaje o Justificación de la eliminación de la publicación
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Fecha y hora de envío del mensaje
    leido BOOLEAN DEFAULT FALSE, -- Indica si el mensaje ha sido leído o no
    FOREIGN KEY (id_usuario_de) REFERENCES USUARIOS(id_usuario) ON DELETE CASCADE, -- ID del usuario que envia el mensaje
    FOREIGN KEY (id_usuario_para) REFERENCES USUARIOS(id_usuario) ON DELETE CASCADE -- ID del usuario que recibe el mensaje
);

CREATE TABLE PUBLICACIONES_GUARDADAS (
    id_publicacion_guardada INT PRIMARY KEY AUTO_INCREMENT, -- ID como clave primaria
    fecha_guardado TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Fecha de guardado, para organizar las publicaciones guardadas
    id_publicacion INT NOT NULL, -- ID de la publicación guardada
    FOREIGN KEY (id_publicacion) REFERENCES PUBLICACIONES(id_publicacion) ON DELETE CASCADE, -- Clave foránea de la tabla PUBLICACIONES
    id_usuario INT NOT NULL, -- ID del usuario que guarda la publicación
    FOREIGN KEY (id_usuario) REFERENCES USUARIOS(id_usuario) ON DELETE CASCADE -- Clave foránea de la tabla USUARIOS
);


-- INSERTAMOS LOS TIPOS DE USUARIOS
INSERT INTO TIPOS_USUARIOS (id_tipo_usuario, nombre_tipo, permisos) VALUES (0, 'Administrador', 'eliminar_publicacion_ajena, eliminar_usuario');
INSERT INTO TIPOS_USUARIOS (id_tipo_usuario, nombre_tipo, permisos) VALUES (1, 'Usuario', 'guardar_publicacion, ver_publicaciones');
INSERT INTO TIPOS_USUARIOS (id_tipo_usuario, nombre_tipo, permisos) VALUES (2, 'Empresa_asociada', 'publicar, guardar_publicacion, ver_publicaciones, ver_publicaciones_guardadas, eliminar_publicacion_propia, ver_todas_las_publicaciones');
INSERT INTO TIPOS_USUARIOS (id_tipo_usuario, nombre_tipo, permisos) VALUES (3, 'Empresa_no_asociada', 'publicar, guardar_publicacion, ver_publicaciones, ver_publicaciones_guardadas, eliminar_publicacion_propia, ver_las_publicaciones_de_la_empresas_asociadas');

-- USUARIO ADMINISTRADOR
INSERT INTO USUARIOS (nombre, password, email, direccion, telefono, id_tipo_usuario)
VALUES ('adm', 'adm', 'adm@inser.com', 'ej direccion', '000000000', 0);
