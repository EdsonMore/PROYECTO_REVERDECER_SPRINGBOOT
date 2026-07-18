-- Script SQL: Datos de Árboles Reales de Catacaos
-- Región: Piura, Perú
-- Coordenadas base: -5.2652777777778, -80.675 (Catacaos)
-- Altitud: ~28 m s.n.m.
-- Año: 2026

-- ===============================================
-- USUARIO 1: EDSON DUBERLY (edson@gmail.com)
-- DNI: 49070853
-- ===============================================

INSERT INTO arboles (especie, ubicacion, fecha_plantacion, estado, descripcion, fecha_registro, latitud, longitud, nombre, foto_url, usuario_id) 
VALUES 
('Mangifera indica', 'Jr. Comercio 234, Catacaos', '2023-06-15', 'Saludable', 'Árbol de mangó criollo con excelente producción de frutos. Altura aproximada 8 metros. Especie bien adaptada al clima tropical seco de Catacaos. Riego por goteo.', '2026-05-01', -5.2632, -80.6745, 'Mangó Criollo #1', '/img/arboles/mango-criollo-1.jpg', 1);

INSERT INTO arboles (especie, ubicacion, fecha_plantacion, estado, descripcion, fecha_registro, latitud, longitud, nombre, foto_url, usuario_id) 
VALUES 
('Prosopis spp', 'Av. Principal 156, Catacaos', '2021-03-22', 'Saludable', 'Algarrobo nativo bien establecido. Altura 12 metros. Frondoso y proporcionador de sombra. Muy resistente al clima seco. Vainas amarillas productivas.', '2026-05-02', -5.2672, -80.6750, 'Algarrobo #2', '/img/arboles/algarrobo-2.jpg', 1);

INSERT INTO arboles (especie, ubicacion, fecha_plantacion, estado, descripcion, fecha_registro, latitud, longitud, nombre, foto_url, usuario_id) 
VALUES 
('Carica papaya', 'Zona Agrícola 89, Catacaos', '2024-02-10', 'Saludable', 'Papayo de 3 años con producción constante. Altura 4.5 metros. Frutos de buena calidad. Especie adaptada a la zona. Riego regular por goteo.', '2026-05-03', -5.2615, -80.6760, 'Papayo #3', '/img/arboles/papayo-3.jpg', 1);

-- ===============================================
-- USUARIO 3: BRANDON (brandon@gmail.com)
-- DNI: 44444444
-- ===============================================

INSERT INTO arboles (especie, ubicacion, fecha_plantacion, estado, descripcion, fecha_registro, latitud, longitud, nombre, foto_url, usuario_id) 
VALUES 
('Citrus limetta', 'Jr. Piura 512, Catacaos', '2022-08-18', 'Saludable', 'Árbol de limón persa de buena producción. Altura 6 metros. Frondoso con excelente exposición solar. Produce frutas todo el año en Catacaos. Riego establecido.', '2026-05-04', -5.2668, -80.6735, 'Limón Persa #4', '/img/arboles/limon-persa-4.jpg', 3);

INSERT INTO arboles (especie, ubicacion, fecha_plantacion, estado, descripcion, fecha_registro, latitud, longitud, nombre, foto_url, usuario_id) 
VALUES 
('Cocos nucifera', 'Av. Los Cocoteros 198, Catacaos', '2020-11-05', 'Saludable', 'Cocotero de 6 años bien establecido. Altura 8 metros. Frondas largas características. Produce cocos regularmente. Muy adaptado al clima tropical de Piura. Palmera productiva.', '2026-05-05', -5.2695, -80.6760, 'Cocotero #5', '/img/arboles/cocotero-5.jpg', 3);

INSERT INTO arboles (especie, ubicacion, fecha_plantacion, estado, descripcion, fecha_registro, latitud, longitud, nombre, foto_url, usuario_id) 
VALUES 
('Tamarindus indica', 'Zona Parques 345, Catacaos', '2023-05-14', 'Saludable', 'Tamarindo en buen estado vegetativo. Altura 7 metros. Copudo y sombrante. Excelente para agroforestería. Produce vainas con pulpa aromática. Clima ideal en Catacaos.', '2026-05-06', -5.2640, -80.6728, 'Tamarindo #6', '/img/arboles/tamarindo-6.jpg', 3);

-- ===============================================
-- USUARIO 6: JOHANA (joa@gmail.com)
-- DNI: 44444443
-- ===============================================

INSERT INTO arboles (especie, ubicacion, fecha_plantacion, estado, descripcion, fecha_registro, latitud, longitud, nombre, foto_url, usuario_id) 
VALUES 
('Annona cherimola', 'Camino Viejo 267, Catacaos', '2023-09-22', 'Saludable', 'Chirimoyo híbrido de 3 años con floración abundante. Altura 5 metros. Bien ramificado. Frutos de buena calidad en zona de Catacaos. Polinización natural exitosa.', '2026-05-07', -5.2655, -80.6752, 'Chirimoyo #7', '/img/arboles/chirimoyo-7.jpg', 6);

INSERT INTO arboles (especie, ubicacion, fecha_plantacion, estado, descripcion, fecha_registro, latitud, longitud, nombre, foto_url, usuario_id) 
VALUES 
('Enterolobium cyclocarpum', 'Sector Rural 421, Catacaos', '2021-07-08', 'Saludable', 'Guanacaste majestuoso de 5 años. Altura 11 metros. Amplia copa proporciona sombra excelente. Maderable de buena calidad. Muy importante para conservación. Especie emblemática de Piura.', '2026-05-08', -5.2710, -80.6710, 'Guanacaste #8', '/img/arboles/guanacaste-8.jpg', 6);

INSERT INTO arboles (especie, ubicacion, fecha_plantacion, estado, descripcion, fecha_registro, latitud, longitud, nombre, foto_url, usuario_id) 
VALUES 
('Musa sapientum', 'Huerta Familiar 156, Catacaos', '2024-01-20', 'Saludable', 'Platanero de 2 años en producción inicial. Altura 4 metros. Follaje verde intenso. Especie de rápido crecimiento. Adaptación excelente al clima tropical-seco de Catacaos.', '2026-05-09', -5.2620, -80.6740, 'Platanero #9', '/img/arboles/platanero-9.jpg', 6);

-- ===============================================
-- Resumen de inserción:
-- Total de árboles insertados: 9
-- Usuario Edson (ID 1): 3 árboles (Mangó, Algarrobo, Papaya)
-- Usuario Brandon (ID 3): 3 árboles (Limón, Cocotero, Tamarindo)
-- Usuario Johana (ID 6): 3 árboles (Chirimoyo, Guanacaste, Platanero)
-- ===============================================
