// ============================================
// MAPA DE ÁRBOLES - INICIALIZACIÓN Y LÓGICA
// ============================================

let map;
let markersLayer;
let myLocationMarker = null;
let arboles = [];
let filteredArboles = [];
const defaultCenter = [-5.1946, -80.6307]; // Piura, Perú
const defaultZoom = 13;

// ============================================
// INICIALIZAR MAPA
// ============================================

function initializeMap() {
    // Crear mapa
    map = L.map('map', {
        preferCanvas: true,
        zoomControl: true,
        fadeAnimation: false,
        zoomAnimation: false
    }).setView(defaultCenter, defaultZoom);

    // Agregar capa de OpenStreetMap
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
        maxZoom: 19
    }).addTo(map);

    // Crear grupo de marcadores
    markersLayer = L.layerGroup().addTo(map);

    // Botones de control
    document.getElementById('btnGetLocation').addEventListener('click', getMyLocation);
    document.getElementById('btnCenterMap').addEventListener('click', centerMapOnArboles);
    document.getElementById('filterEspecie').addEventListener('input', filterAndUpdateMarkers);

    // Configurar modal
    const modal = document.getElementById('detalleArbolModal');
    const closeBtn = document.querySelector('.modal-close');
    if (closeBtn) {
        closeBtn.addEventListener('click', () => modal.classList.remove('active'));
    }
    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            modal.classList.remove('active');
        }
    });

    // Cargar árboles
    loadArboles();

    console.log('✅ Mapa de árboles inicializado correctamente');
}

// ============================================
// CARGAR ÁRBOLES DESDE EL SERVIDOR
// ============================================

async function loadArboles() {
    try {
        showToast('Cargando árboles...', 'info');

        console.log('📡 Llamando a endpoint: /arboles/api');
        const response = await fetch('/arboles/api', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        console.log('Response status:', response.status, 'ok:', response.ok);

        if (!response.ok) {
            const errorText = await response.text();
            console.error('Error response:', errorText);
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        console.log('✅ Datos recibidos:', data);

        // Verificar si la respuesta es un array o un objeto con datos
        arboles = Array.isArray(data) ? data : (data.arboles || data.content || []);

        if (arboles.length === 0) {
            console.warn('No se encontraron árboles');
            showToast('No hay árboles registrados', 'info');
            updateStats(0);
        } else {
            console.log(`✅ ${arboles.length} árboles cargados`);
            showToast(`${arboles.length} árboles cargados`, 'success');
            filteredArboles = [...arboles];
            displayMarkers();
            updateStats(arboles.length);
            centerMapOnArboles();
        }
    } catch (error) {
        console.error('Error cargando árboles:', error);
        showToast('Error al cargar árboles de la base de datos', 'error');
        updateStats(0);
    }
}

// ============================================
// MOSTRAR MARCADORES EN EL MAPA
// ============================================

function displayMarkers() {
    try {
        markersLayer.clearLayers();

        if (filteredArboles.length === 0) {
            console.warn('No hay árboles para mostrar');
            showToast('No hay árboles que mostrar', 'info');
            return;
        }

        filteredArboles.forEach((arbol, index) => {
            try {
                if (!arbol || !arbol.latitud || !arbol.longitud) {
                    console.warn('Árbol sin coordenadas:', arbol);
                    return;
                }

                const lat = parseFloat(arbol.latitud);
                const lng = parseFloat(arbol.longitud);

                if (isNaN(lat) || isNaN(lng)) {
                    console.warn(`Coordenadas inválidas para árbol ${arbol.nombre}`);
                    return;
                }

                // Crear marcador personalizado
                const marker = L.marker([lat, lng], {
                    icon: createArbolMarkerIcon(index)
                }).addTo(markersLayer);

                // Crear popup con información del árbol
                const nombre = arbol.nombre || 'Sin nombre';
                const especie = arbol.especie || 'Sin especie';
                const fecha = arbol.fecha_plantacion || arbol.fechaPlantacion || 'Sin fecha';
                const descripcion = arbol.descripcion || 'Sin descripción';

                const popupContent = `
                    <div class="arbol-popup">
                        <h5 class="mb-2">${nombre}</h5>
                        <p><strong>Especie:</strong> <em>${especie}</em></p>
                        <p><strong>Plantación:</strong> ${formatDate(fecha)}</p>
                        <p><strong>Descripción:</strong> ${descripcion}</p>
                        <p><strong>Coordenadas:</strong> ${lat.toFixed(4)}, ${lng.toFixed(4)}</p>
                    </div>
                `;

                marker.bindPopup(popupContent);
                marker.on('click', () => showArbolDetails(arbol));
            } catch (error) {
                console.error(`Error al procesar árbol:`, error);
            }
        });

        updateMarkersList();
    } catch (error) {
        console.error('Error en displayMarkers:', error);
        throw error;
    }
}

// ============================================
// CREAR ICONO PERSONALIZADO PARA ÁRBOL
// ============================================

function createArbolMarkerIcon(index) {
    return L.icon({
        iconUrl: '/img/logo1.webp',
        iconSize: [40, 40],
        iconAnchor: [20, 40],
        popupAnchor: [0, -40],
        shadowUrl: '',
        className: 'custom-arbol-marker'
    });
}

// ============================================
// FILTRAR ÁRBOLES POR ESPECIE
// ============================================

function filterAndUpdateMarkers() {
    const filterValue = document.getElementById('filterEspecie').value.toLowerCase();

    if (filterValue === '') {
        filteredArboles = [...arboles];
    } else {
        filteredArboles = arboles.filter(arbol =>
            arbol.especie.toLowerCase().includes(filterValue) ||
            arbol.nombre.toLowerCase().includes(filterValue)
        );
    }

    updateStats(filteredArboles.length);
    displayMarkers();

    if (filteredArboles.length === 0) {
        showToast('No se encontraron árboles con ese filtro', 'info');
    } else {
        showToast(`${filteredArboles.length} árbol(es) encontrado(s)`, 'success');
    }
}

// ============================================
// CENTRAR MAPA EN ÁRBOLES
// ============================================

function centerMapOnArboles() {
    if (filteredArboles.length === 0) {
        map.setView(defaultCenter, defaultZoom);
        return;
    }

    if (filteredArboles.length === 1) {
        const arbol = filteredArboles[0];
        map.setView([parseFloat(arbol.latitud), parseFloat(arbol.longitud)], 15);
        return;
    }

    // Calcular bounds de todos los árboles
    const lats = filteredArboles.map(a => parseFloat(a.latitud));
    const lngs = filteredArboles.map(a => parseFloat(a.longitud));

    const bounds = L.latLngBounds([
        [Math.min(...lats), Math.min(...lngs)],
        [Math.max(...lats), Math.max(...lngs)]
    ]);

    map.fitBounds(bounds, { padding: [50, 50], maxZoom: 15 });
    showToast('Mapa centrado en árboles', 'success');
}

// ============================================
// OBTENER MI UBICACIÓN
// ============================================

function getMyLocation() {
    if (!navigator.geolocation) {
        showToast('Geolocalización no disponible', 'error');
        return;
    }

    showToast('Obteniendo ubicación...', 'info');

    navigator.geolocation.getCurrentPosition(
        (position) => {
            const { latitude, longitude } = position.coords;
            map.setView([latitude, longitude], 15);

            // Remover marcador anterior si existe
            if (myLocationMarker) {
                map.removeLayer(myLocationMarker);
            }

            // Agregar marcador de ubicación actual (DIRECTO al mapa, NO a markersLayer)
            myLocationMarker = L.marker([latitude, longitude], {
                icon: L.divIcon({
                    html: `
                        <div class="location-marker">
                            <i class="fas fa-user-location" style="color: white; font-size: 20px;"></i>
                        </div>
                    `,
                    iconSize: [40, 40],
                    className: 'custom-location-marker'
                })
            }).addTo(map);

            myLocationMarker.bindPopup('📍 Mi ubicación actual').openPopup();
            showToast('Ubicación obtenida', 'success');
        },
        (error) => {
            console.error('Error de geolocalización:', error);
            showToast('Error al obtener ubicación: ' + error.message, 'error');
        }
    );
}

// ============================================
// MOSTRAR DETALLES DEL ÁRBOL EN MODAL
// ============================================

function showArbolDetails(arbol) {
    const modal = document.getElementById('detalleArbolModal');
    const modalTitle = document.getElementById('modalTitle');
    const modalBody = document.getElementById('modalBody');

    modalTitle.textContent = arbol.nombre;

    modalBody.innerHTML = `
        <div class="arbol-details">
            <div class="detail-row">
                <strong>Especie:</strong>
                <span><em>${arbol.especie}</em></span>
            </div>
            <div class="detail-row">
                <strong>Fecha de Plantación:</strong>
                <span>${formatDate(arbol.fechaPlantacion)}</span>
            </div>
            <div class="detail-row">
                <strong>Ubicación:</strong>
                <span>${parseFloat(arbol.latitud).toFixed(6)}, ${parseFloat(arbol.longitud).toFixed(6)}</span>
            </div>
            <div class="detail-row">
                <strong>Descripción:</strong>
                <p>${arbol.descripcion || 'Sin descripción'}</p>
            </div>
            ${arbol.fotoUrl ? `
                <div class=\"detail-row\">
                    <img src=\"${arbol.fotoUrl}\" alt=\"${arbol.nombre}\" style=\"width: 100%; max-height: 300px; border-radius: 6px;\">
                </div>
            ` : ''}
            <div class="detail-row" style="padding-top: 15px; border-top: 1px solid #eee; margin-top: 15px;">
                <a href="/arboles/detalles/${arbol.id}" style="display: inline-block; padding: 10px 20px; background: #28a745; color: white; text-decoration: none; border-radius: 6px; font-weight: 500; transition: all 0.3s;">
                    📖 Ver Detalles Completos
                </a>
            </div>
        </div>
    `;

    modal.classList.add('active');
}

// ============================================
// ACTUALIZAR LISTAS DE ÁRBOLES EN SIDEBAR
// ============================================

function updateMarkersList() {
    try {
        const misArboles = document.getElementById('misArboles');
        const otrosArboles = document.getElementById('otrosArboles');

        // Si no hay usuario logueado, solo mostrar todos los árboles
        if (!currentUserId) {
            console.log('Usuario no logueado, mostrando todos los árboles');
            if (otrosArboles) {
                if (filteredArboles.length === 0) {
                    otrosArboles.innerHTML = '<p class="empty-message">Sin árboles registrados</p>';
                } else {
                    otrosArboles.innerHTML = filteredArboles.map((arbol, index) => {
                        try {
                            const lat = parseFloat(arbol.latitud);
                            const lng = parseFloat(arbol.longitud);
                            const nombre = arbol.nombre || 'Sin nombre';
                            const especie = arbol.especie || 'Sin especie';
                            
                            return `
                                <div class="marker-item" onclick="focusArbol(${index})">
                                    <div class="marker-item-nombre">🌳 ${nombre}</div>
                                    <div class="marker-item-especie">${especie}</div>
                                    <div class="marker-item-coords">
                                        ${lat.toFixed(4)}, ${lng.toFixed(4)}
                                    </div>
                                </div>
                            `;
                        } catch (e) {
                            console.error('Error renderizando árbol:', e);
                            return '';
                        }
                    }).join('');
                }
            }
            return;
        }

        if (!misArboles || !otrosArboles) {
            console.warn('Contenedores de árboles no encontrados');
            return;
        }

        // Separar árboles por usuario
        let arbolesTos = [];
        let arbolesOtros = [];

        filteredArboles.forEach((arbol, index) => {
            try {
                if (arbol && arbol.usuario) {
                    if (currentUserId && arbol.usuario.id === currentUserId) {
                        arbolesTos.push({ arbol, index });
                    } else {
                        arbolesOtros.push({ arbol, index });
                    }
                } else {
                    arbolesOtros.push({ arbol, index });
                }
            } catch (e) {
                console.error('Error separando árboles:', e);
                arbolesOtros.push({ arbol, index });
            }
        });

        // Actualizar mi sección
        if (currentUserId) {
            if (arbolesTos.length === 0) {
                misArboles.innerHTML = '<p class="empty-message">Sin árboles propios</p>';
            } else {
                misArboles.innerHTML = arbolesTos.map(({ arbol, index }) => {
                    try {
                        const lat = parseFloat(arbol.latitud);
                        const lng = parseFloat(arbol.longitud);
                        const nombre = arbol.nombre || 'Sin nombre';
                        const especie = arbol.especie || 'Sin especie';
                        
                        return `
                            <div class="marker-item" onclick="focusArbol(${index})">
                                <div class="marker-item-nombre">🌳 ${nombre}</div>
                                <div class="marker-item-especie">${especie}</div>
                                <div class="marker-item-coords">
                                    ${lat.toFixed(4)}, ${lng.toFixed(4)}
                                </div>
                            </div>
                        `;
                    } catch (e) {
                        console.error('Error renderizando árbol propio:', e);
                        return '';
                    }
                }).join('');
            }
        }

        // Actualizar sección de otros
        if (arbolesOtros.length === 0) {
            otrosArboles.innerHTML = '<p class="empty-message">Sin árboles registrados</p>';
        } else {
            otrosArboles.innerHTML = arbolesOtros.map(({ arbol, index }) => {
                try {
                    const lat = parseFloat(arbol.latitud);
                    const lng = parseFloat(arbol.longitud);
                    const nombre = arbol.nombre || 'Sin nombre';
                    const especie = arbol.especie || 'Sin especie';
                    
                    return `
                        <div class="marker-item" onclick="focusArbol(${index})">
                            <div class="marker-item-nombre">🌳 ${nombre}</div>
                            <div class="marker-item-especie">${especie}</div>
                            <div class="marker-item-coords">
                                ${lat.toFixed(4)}, ${lng.toFixed(4)}
                            </div>
                        </div>
                    `;
                } catch (e) {
                    console.error('Error renderizando árbol ajeno:', e);
                    return '';
                }
            }).join('');
        }
    } catch (error) {
        console.error('Error en updateMarkersList:', error);
    }
}

// ============================================
// ENFOCAR ÁRBOL EN EL MAPA
// ============================================

function focusArbol(index) {
    if (filteredArboles[index]) {
        const arbol = filteredArboles[index];
        map.setView(
            [parseFloat(arbol.latitud), parseFloat(arbol.longitud)],
            16
        );
        showArbolDetails(arbol);
    }
}

// ============================================
// ACTUALIZAR ESTADÍSTICAS
// ============================================

function updateStats(count) {
    document.getElementById('totalArboles').textContent = count;
}

// ============================================
// UTILIDADES - FORMATO DE FECHA
// ============================================

function formatDate(dateString) {
    if (!dateString) return 'Desconocida';

    const date = new Date(dateString);
    return date.toLocaleDateString('es-PE', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

// ============================================
// MOSTRAR NOTIFICACIONES (TOAST)
// ============================================

function showToast(message, type = 'info') {
    const toastContainer = document.getElementById('toastContainer');

    const toastElement = document.createElement('div');
    const typeClass = {
        'success': 'toast-success',
        'error': 'toast-error',
        'info': 'toast-info'
    }[type] || 'toast-info';

    toastElement.className = `toast ${typeClass}`;
    toastElement.textContent = message;

    toastContainer.appendChild(toastElement);

    // Auto eliminar después de 3 segundos
    setTimeout(() => {
        toastElement.remove();
    }, 3000);
}

// ============================================
// INICIALIZAR CUANDO EL DOM ESTÉ LISTO
// ============================================

document.addEventListener('DOMContentLoaded', () => {
    initializeMap();
    console.log('✅ Script mapa-arboles.js cargado');
});

// ============================================
// MANEJAR CAMBIO DE TAMAÑO DE VENTANA
// ============================================

window.addEventListener('resize', () => {
    if (map) {
        map.invalidateSize();
    }
});

// ============================================
// ESTILOS ADICIONALES PARA POPUPS
// ============================================

const style = document.createElement('style');
style.textContent = `
    .arbol-popup {
        padding: 10px;
        font-size: 0.9rem;
    }

    .arbol-popup h5 {
        color: #22863a;
        margin-bottom: 0.5rem;
    }

    .arbol-popup p {
        margin: 0.25rem 0;
        font-size: 0.85rem;
    }

    .arbol-marker {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        border: 3px solid white;
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);
        cursor: pointer;
        transition: transform 0.2s ease;
    }

    .arbol-marker:hover {
        transform: scale(1.15);
    }

    .location-marker {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background-color: #ef4444;
        border: 3px solid white;
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 0 2px 6px rgba(239, 68, 68, 0.4);
    }

    .detail-row {
        margin-bottom: 1rem;
        padding-bottom: 0.75rem;
        border-bottom: 1px solid #e0e0e0;
    }

    .detail-row:last-child {
        border-bottom: none;
    }

    .detail-row strong {
        color: #22863a;
        display: block;
        margin-bottom: 0.25rem;
    }

    .detail-row span {
        color: #333;
    }

    .detail-row p {
        margin: 0;
        color: #666;
    }
`;

document.head.appendChild(style);
