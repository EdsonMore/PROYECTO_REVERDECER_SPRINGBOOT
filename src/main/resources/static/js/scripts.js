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

  // Efectos hover para tarjetas
  inicializarHoverTarjetas();
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

/**
 * Inicializa efectos hover para tarjetas
 */
function inicializarHoverTarjetas() {
  const hoverCards = document.querySelectorAll(
    ".hover-card, .team-card, .tree-card, .module-card",
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
}

// Función para validar formularios con Bootstrap
function validarFormulario(form) {
  if (!form.checkValidity()) {
    form.classList.add("was-validated");
    return false;
  }
  return true;
}
