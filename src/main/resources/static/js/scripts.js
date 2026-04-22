// scripts.js - Funcionalidades NATIVAS globales para todas las páginas

document.addEventListener("DOMContentLoaded", function () {
  // Inicializar funcionalidades globales
  inicializarFuncionesGlobales();
});

/**
 * Inicializa todas las funciones globales
 */
function inicializarFuncionesGlobales() {
  // Botón volver arriba
  inicializarBotonVolver();

  // Animaciones simples para tarjetas
  animarTarjetas();

  // Cerrar alerta automáticamente
  cerrarAlertasAutomaticamente();
}

/**
 * Inicializa botón volver al inicio
 */
function inicializarBotonVolver() {
  const btn = document.getElementById("backToTopBtn");

  if (!btn) return;

  window.addEventListener("scroll", function () {
    if (window.scrollY > 300) {
      btn.style.display = "block";
    } else {
      btn.style.display = "none";
    }
  });

  btn.addEventListener("click", function (e) {
    e.preventDefault();
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  });
}

/**
 * Anima las tarjetas al cargar
 */
function animarTarjetas() {
  const tarjetas = document.querySelectorAll(".card");
  tarjetas.forEach((tarjeta, indice) => {
    tarjeta.style.opacity = "0";
    tarjeta.style.transform = "translateY(20px)";
    tarjeta.style.transition = "opacity 0.5s ease, transform 0.5s ease";

    setTimeout(() => {
      tarjeta.style.opacity = "1";
      tarjeta.style.transform = "translateY(0)";
    }, 100 * indice);
  });
}

/**
 * Cierra alertas automáticamente después de 5 segundos
 */
function cerrarAlertasAutomaticamente() {
  const alertas = document.querySelectorAll(".alert");
  alertas.forEach((alerta) => {
    if (!alerta.classList.contains("position-fixed")) {
      setTimeout(() => {
        alerta.style.transition = "opacity 0.5s ease";
        alerta.style.opacity = "0";
        setTimeout(() => {
          alerta.remove();
        }, 500);
      }, 5000);
    }
  });
}

/**
 * Utilidad para mostrar confirmación antes de eliminar
 */
function confirmarAccion(mensaje = "¿Estás seguro?") {
  return confirm(mensaje);
}

/**
 * Utilidad para desabilitar un botón temporalmente
 */
function deshabilitarBotonTemporalmente(boton, tiempoMs = 2000) {
  const textOriginal = boton.textContent;
  boton.disabled = true;

  setTimeout(() => {
    boton.disabled = false;
    boton.textContent = textOriginal;
  }, tiempoMs);
}

// Función para actualizar contador del carrito (global)
function actualizarContadorCarrito() {
  fetch("/carrito/info")
    .then((response) => response.json())
    .then((data) => {
      const contador = document.getElementById("carrito-count");
      if (contador) {
        contador.textContent = data.cantidadTotal || 0;
      }
    })
    .catch((error) => {
      console.error("Error al actualizar contador:", error);
      // En caso de error, mostrar 0
      const contador = document.getElementById("carrito-count");
      if (contador) {
        contador.textContent = "0";
      }
    });
}

// Función para validar carrito antes de navegar (global)
function validarCarritoClick() {
  const contadorCarrito = document.getElementById("carrito-count");
  const cantidadTotal = parseInt(contadorCarrito.textContent) || 0;

  if (cantidadTotal === 0) {
    mostrarToast("Debe agregar productos al carrito primero", "error");
    return;
  }

  // Si tiene productos, redirigir al carrito
  window.location.href = "/carrito";
}

// Función global para mostrar toasts/notificaciones
function mostrarToast(mensaje, tipo = "info") {
  // Crear elemento toast si no existe
  let toastContainer = document.getElementById("toast-container");
  if (!toastContainer) {
    toastContainer = document.createElement("div");
    toastContainer.id = "toast-container";
    toastContainer.className = "position-fixed top-0 end-0 p-3";
    toastContainer.style.zIndex = "1055";
    document.body.appendChild(toastContainer);
  }

  const toastId = "toast-" + Date.now();
  const bgColor = getBgColorForToast(tipo);

  const toastHTML = `
        <div id="${toastId}" class="toast align-items-center text-white ${bgColor} border-0 show" role="alert">
            <div class="d-flex">
                <div class="toast-body">
                    <i class="bi ${getIconForToast(tipo)} me-2"></i>
                    ${mensaje}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        </div>
    `;

  toastContainer.insertAdjacentHTML("beforeend", toastHTML);

  // Auto-eliminar después de 5 segundos
  setTimeout(() => {
    const toastElement = document.getElementById(toastId);
    if (toastElement) {
      toastElement.remove();
    }
  }, 5000);
}

// Funciones auxiliares para toasts
function getBgColorForToast(tipo) {
  switch (tipo) {
    case "success":
      return "bg-success";
    case "error":
      return "bg-danger";
    case "warning":
      return "bg-warning";
    default:
      return "bg-primary";
  }
}

function getIconForToast(tipo) {
  switch (tipo) {
    case "success":
      return "bi-check-circle";
    case "error":
      return "bi-x-circle";
    case "warning":
      return "bi-exclamation-triangle";
    default:
      return "bi-info-circle";
  }
}

// Efectos hover globales para tarjetas con clase específica
document.addEventListener("DOMContentLoaded", function () {
  const hoverCards = document.querySelectorAll(
    ".hover-card, .team-card, .producto-card",
  );
  hoverCards.forEach((card) => {
    card.addEventListener("mouseenter", function () {
      this.style.transform = "translateY(-2px)";
      this.style.transition = "transform 0.3s ease";
    });
    card.addEventListener("mouseleave", function () {
      this.style.transform = "translateY(0)";
    });
  });
});

// Función para formatear precios (útil globalmente)
function formatearPrecio(precio) {
  return `S/. ${parseFloat(precio).toFixed(2)}`;
}

// Función para validar formularios con Bootstrap
function validarFormulario(form) {
  if (!form.checkValidity()) {
    form.classList.add("was-validated");
    return false;
  }
  return true;
}
