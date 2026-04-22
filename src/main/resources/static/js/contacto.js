// contacto.js - Script NATIVO para formulario de contacto

document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("contactForm");
  const textarea = document.getElementById("mensaje");
  const charCount = document.getElementById("charCount");
  const MAX_CHARS = 500;

  if (!form) return;

  // Contador de caracteres
  if (textarea && charCount) {
    textarea.addEventListener("input", function () {
      const count = this.value.length;
      charCount.textContent = count + "/" + MAX_CHARS;

      if (count > MAX_CHARS) {
        this.value = this.value.substring(0, MAX_CHARS);
        charCount.textContent = MAX_CHARS + "/" + MAX_CHARS;
      }

      // Cambiar color según el nivel
      if (count > 450) {
        charCount.style.color = "#dc3545"; // Rojo
      } else if (count > 400) {
        charCount.style.color = "#fd7e14"; // Naranja
      } else {
        charCount.style.color = "#6c757d"; // Gris
      }
    });
  }

  // Validación del formulario
  form.addEventListener("submit", function (event) {
    if (!validarFormularioContacto(this)) {
      event.preventDefault();
      event.stopPropagation();
    }
  });

  // Validación en tiempo real
  const campos = form.querySelectorAll("input[required], textarea[required]");
  campos.forEach((campo) => {
    campo.addEventListener("blur", function () {
      validarCampoContacto(this);
    });
  });
});

/**
 * Valida un campo del formulario de contacto
 */
function validarCampoContacto(campo) {
  const valor = campo.value.trim();
  const tipo = campo.type;
  const nombre = campo.name;

  if (!valor && campo.required) {
    mostrarErrorContacto(campo, "Este campo es requerido");
    return false;
  }

  if (tipo === "email" && valor) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!regex.test(valor)) {
      mostrarErrorContacto(campo, "Ingresa un email válido");
      return false;
    }
  }

  if ((tipo === "tel" || nombre === "telefono") && valor) {
    const soloNumeros = valor.replace(/\D/g, "");
    if (soloNumeros.length < 7) {
      mostrarErrorContacto(campo, "El teléfono debe tener al menos 7 dígitos");
      return false;
    }
  }

  limpiarErrorContacto(campo);
  return true;
}

/**
 * Valida todo el formulario de contacto
 */
function validarFormularioContacto(form) {
  let valido = true;

  const campos = form.querySelectorAll("input[required], textarea[required]");
  campos.forEach((campo) => {
    if (!validarCampoContacto(campo)) {
      valido = false;
    }
  });

  if (valido) {
    mostrarMensajeExitoContacto();
  }

  return valido;
}

/**
 * Muestra error
 */
function mostrarErrorContacto(campo, mensaje) {
  campo.classList.add("is-invalid");

  let feedback = campo.nextElementSibling;
  if (!feedback || !feedback.classList.contains("invalid-feedback")) {
    feedback = document.createElement("div");
    feedback.classList.add("invalid-feedback");
    feedback.style.display = "block";
    campo.parentNode.insertBefore(feedback, campo.nextSibling);
  }
  feedback.textContent = mensaje;
}

/**
 * Limpia errores
 */
function limpiarErrorContacto(campo) {
  campo.classList.remove("is-invalid");
  const feedback = campo.nextElementSibling;
  if (feedback && feedback.classList.contains("invalid-feedback")) {
    feedback.textContent = "";
    feedback.style.display = "none";
  }
}

/**
 * Muestra mensaje de éxito
 */
function mostrarMensajeExitoContacto() {
  const alerta = document.createElement("div");
  alerta.className =
    "alert alert-success alert-dismissible fade show position-fixed top-0 end-0 m-3";
  alerta.style.zIndex = "9999";
  alerta.style.width = "300px";
  alerta.innerHTML = `
        <strong>¡Éxito!</strong> Tu mensaje ha sido enviado correctamente.
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

  document.body.appendChild(alerta);

  // Remover automáticamente después de 5 segundos
  setTimeout(() => {
    alerta.remove();
  }, 5000);
}
