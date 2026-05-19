// ============================================
// MAPA INTERACTIVO - INICIALIZACIÓN Y LÓGICA
// ============================================

let map;
let markersLayer;
let selectedMarkers = [];
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

    // Event listeners
    map.on('click', handleMapClick);

    // Botones de control
    document.getElementById('btnGetLocation').addEventListener('click', getMyLocation);
    document.getElementById('btnClearMarkers').addEventListener('click', clearMarkers);

    console.log('✅ Mapa inicializado correctamente');
}

// ============================================
// MANEJAR CLICS EN EL MAPA
// ============================================

function handleMapClick(e) {
    const { lat, lng } = e.latlng;
    addMarker(lat, lng, `Ubicación seleccionada: ${lat.toFixed(4)}, ${lng.toFixed(4)}`);
    updateCoordinates(lat, lng);
}

// ============================================
// AGREGAR MARCADOR
// ============================================

function addMarker(lat, lng, popupText = '') {
    const marker = L.marker([lat, lng], {
        icon: createCustomMarkerIcon('#3b82f6')
    }).addTo(markersLayer);

    if (popupText) {
        marker.bindPopup(popupText).openPopup();
    }

    selectedMarkers.push({ lat, lng, marker });
    updateMarkersList();

    showToast('Marcador agregado', 'success');
}

// ============================================
// CREAR ICONO PERSONALIZADO
// ============================================

function createCustomMarkerIcon(color = 'blue') {
    return L.divIcon({
        html: `
            <div class="flex items-center justify-center w-8 h-8 rounded-full border-2 border-white shadow-lg" style="background-color: ${color};">
                <div class="w-3 h-3 bg-white rounded-full"></div>
            </div>
        `,
        iconSize: [32, 32],
        className: 'custom-marker'
    });
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

            // Agregar marcador de ubicación actual
            const marker = L.marker([latitude, longitude], {
                icon: createCustomMarkerIcon('#22c55e')
            }).addTo(markersLayer);

            marker.bindPopup('📍 Mi ubicación actual').openPopup();

            selectedMarkers.push({ lat: latitude, lng: longitude, marker });
            updateMarkersList();
            updateCoordinates(latitude, longitude);

            showToast('Ubicación obtenida', 'success');
        },
        (error) => {
            console.error('Error de geolocalización:', error);
            showToast('Error al obtener ubicación: ' + error.message, 'error');
        }
    );
}

// ============================================
// LIMPIAR MARCADORES
// ============================================

function clearMarkers() {
    markersLayer.clearLayers();
    selectedMarkers = [];
    updateMarkersList();
    updateCoordinates(null, null);
    showToast('Marcadores eliminados', 'info');
}

// ============================================
// ACTUALIZAR INFORMACIÓN DE COORDENADAS
// ============================================

function updateCoordinates(lat, lng) {
    const coordsInfo = document.getElementById('coordsInfo');
    if (lat !== null && lng !== null) {
        coordsInfo.innerHTML = `
            <p><strong>Latitud:</strong> ${lat.toFixed(6)}</p>
            <p><strong>Longitud:</strong> ${lng.toFixed(6)}</p>
            <p><strong>Marcadores:</strong> ${selectedMarkers.length}</p>
        `;
    } else {
        coordsInfo.innerHTML = '<p>Selecciona una ubicación en el mapa</p>';
    }
}

// ============================================
// ACTUALIZAR LISTA DE MARCADORES
// ============================================

function updateMarkersList() {
    const markersList = document.getElementById('markersList');

    if (selectedMarkers.length === 0) {
        markersList.innerHTML = '<p class="empty-message">No hay marcadores</p>';
        return;
    }

    markersList.innerHTML = selectedMarkers.map((item, index) => `
        <div class="marker-item" onclick="focusMarker(${index})">
            <div class="marker-item-nombre">📍 Marcador ${index + 1}</div>
            <div class="marker-item-coords">
                ${item.lat.toFixed(6)}, ${item.lng.toFixed(6)}
            </div>
            <button class="btn-remove" onclick="removeMarker(${index}, event)">
                Eliminar
            </button>
        </div>
    `).join('');
}

// ============================================
// ENFOCAR MARCADOR
// ============================================

function focusMarker(index) {
    if (selectedMarkers[index]) {
        const { lat, lng, marker } = selectedMarkers[index];
        map.setView([lat, lng], 15);
        marker.openPopup();
    }
}

// ============================================
// ELIMINAR MARCADOR
// ============================================

function removeMarker(index, event) {
    event.stopPropagation();

    if (selectedMarkers[index]) {
        const { marker } = selectedMarkers[index];
        markersLayer.removeLayer(marker);
        selectedMarkers.splice(index, 1);
        updateMarkersList();
        showToast('Marcador eliminado', 'info');
    }
}

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
    console.log('✅ Script mapa.js cargado');
});

// ============================================
// MANEJAR CAMBIO DE TAMAÑO DE VENTANA
// ============================================

window.addEventListener('resize', () => {
    if (map) {
        map.invalidateSize();
    }
});
