// ============================================
// SCRIPT DE GEOLOCALIZACIÓN - REGISTRO DE ÁRBOLES
// ============================================

document.addEventListener('DOMContentLoaded', function() {
    const btnObtenerUbicacion = document.getElementById('btnObtenerUbicacion');
    const latitudInput = document.getElementById('latitud');
    const longitudInput = document.getElementById('longitud');

    // Agregar evento al botón "Mi Ubicación"
    if (btnObtenerUbicacion) {
        btnObtenerUbicacion.addEventListener('click', function(e) {
            e.preventDefault();
            obtenerMiUbicacion();
        });
    }

    console.log('✅ Script de registro.js cargado');
});

// ============================================
// OBTENER UBICACIÓN ACTUAL
// ============================================

function obtenerMiUbicacion() {
    // Verificar si el navegador soporta geolocalización
    if (!navigator.geolocation) {
        alert('❌ Tu navegador no soporta geolocalización');
        return;
    }

    // Cambiar el botón a estado "cargando"
    const btn = document.getElementById('btnObtenerUbicacion');
    const textOriginal = btn.textContent;
    btn.textContent = '⏳ Obteniendo ubicación...';
    btn.disabled = true;

    // Usar geolocalización
    navigator.geolocation.getCurrentPosition(
        function(position) {
            // Éxito - obtuvimos las coordenadas
            const latitud = position.coords.latitude;
            const longitud = position.coords.longitude;

            // Llenar los campos
            document.getElementById('latitud').value = latitud.toFixed(6);
            document.getElementById('longitud').value = longitud.toFixed(6);

            // Mostrar mensaje de éxito
            mostrarMensaje(`✅ Ubicación obtenida: ${latitud.toFixed(4)}, ${longitud.toFixed(4)}`, 'success');

            // Restaurar botón
            btn.textContent = textOriginal;
            btn.disabled = false;
        },
        function(error) {
            // Error al obtener la ubicación
            console.error('Error de geolocalización:', error);

            let mensajeError = '❌ No se pudo obtener tu ubicación';
            
            if (error.code === error.PERMISSION_DENIED) {
                mensajeError = '❌ Permiso denegado. Habilita la geolocalización en tu navegador.';
            } else if (error.code === error.POSITION_UNAVAILABLE) {
                mensajeError = '❌ Ubicación no disponible en este momento.';
            } else if (error.code === error.TIMEOUT) {
                mensajeError = '❌ Tiempo de espera agotado.';
            }

            mostrarMensaje(mensajeError, 'error');

            // Restaurar botón
            btn.textContent = textOriginal;
            btn.disabled = false;
        },
        {
            // Opciones de geolocalización
            enableHighAccuracy: true,  // Usar GPS de alta precisión
            timeout: 10000,            // Esperar máximo 10 segundos
            maximumAge: 0              // No usar caché
        }
    );
}

// ============================================
// MOSTRAR MENSAJE AL USUARIO
// ============================================

function mostrarMensaje(mensaje, tipo = 'info') {
    // Crear elemento de mensaje
    const div = document.createElement('div');
    div.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        border-radius: 6px;
        font-weight: 500;
        z-index: 1000;
        animation: slideInRight 0.3s ease;
    `;

    // Aplicar estilos según el tipo
    if (tipo === 'success') {
        div.style.background = 'linear-gradient(135deg, #28a745 0%, #218838 100%)';
        div.style.color = 'white';
        div.style.borderLeft = '4px solid #1e7e34';
    } else if (tipo === 'error') {
        div.style.background = 'linear-gradient(135deg, #dc3545 0%, #c82333 100%)';
        div.style.color = 'white';
        div.style.borderLeft = '4px solid #a71d2a';
    } else {
        div.style.background = 'linear-gradient(135deg, #17a2b8 0%, #0c5460 100%)';
        div.style.color = 'white';
        div.style.borderLeft = '4px solid #0a3f45';
    }

    div.textContent = mensaje;
    document.body.appendChild(div);

    // Auto-remover después de 4 segundos
    setTimeout(() => {
        div.remove();
    }, 4000);
}

// ============================================
// ESTILOS DE ANIMACIÓN
// ============================================

const style = document.createElement('style');
style.textContent = `
    @keyframes slideInRight {
        from {
            transform: translateX(400px);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }

    .registro-btn-secondary:disabled {
        opacity: 0.6;
        cursor: not-allowed;
    }
`;
document.head.appendChild(style);
