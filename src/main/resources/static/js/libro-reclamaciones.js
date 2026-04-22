// libro-reclamaciones.js - Script NATIVO para el libro de reclamaciones

document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("reclamacionForm");
  const textarea = document.getElementById("detalleSolicitud");
  const charCount = document.getElementById("caracterCount");
  const fechaInput = document.getElementById("fechaIncidente");
  const MAX_CHARS = 1000;

  if (!form) return;

  // Configurar fecha máxima (hoy)
  if (fechaInput) {
    const hoy = new Date().toISOString().split("T")[0];
    fechaInput.max = hoy;
    fechaInput.value = hoy;
  }

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
      if (count > 900) {
        charCount.style.color = "#dc3545"; // Rojo
      } else if (count > 800) {
        charCount.style.color = "#fd7e14"; // Naranja
      } else {
        charCount.style.color = "#6c757d"; // Gris
      }
    });
  }

  // Validación al enviar
  form.addEventListener("submit", function (e) {
    if (!validarFormularioReclamacion(this)) {
      e.preventDefault();
      e.stopPropagation();
    }
  });

  // Validación en tiempo real
  const campos = form.querySelectorAll(
    "input[required], select[required], textarea[required]",
  );
  campos.forEach((campo) => {
    campo.addEventListener("blur", function () {
      validarCampoReclamacion(this);
    });
  });
});

/**
 * Valida un campo de reclamación
 */
function validarCampoReclamacion(campo) {
  const valor = campo.value.trim();
  const nombre = campo.name;
  const tipo = campo.type;

  if (!valor && campo.required) {
    mostrarErrorReclamacion(campo, "Este campo es requerido");
    return false;
  }

  if (tipo === "email" && valor) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!regex.test(valor)) {
      mostrarErrorReclamacion(campo, "Email inválido");
      return false;
    }
  }

  if (nombre === "numeroDocumento" && valor) {
    const soloNumeros = valor.replace(/\D/g, "");
    if (soloNumeros.length < 8) {
      mostrarErrorReclamacion(
        campo,
        "El número de documento debe tener al menos 8 dígitos",
      );
      return false;
    }
  }

  if (nombre === "detalleSolicitud" && valor) {
    if (valor.length < 20) {
      mostrarErrorReclamacion(
        campo,
        "La descripción debe tener al menos 20 caracteres",
      );
      return false;
    }
  }

  if (nombre === "fechaIncidente" && valor) {
    const fecha = new Date(valor);
    const hoy = new Date();
    if (fecha > hoy) {
      mostrarErrorReclamacion(campo, "La fecha no puede ser futura");
      return false;
    }
  }

  limpiarErrorReclamacion(campo);
  return true;
}

/**
 * Valida todo el formulario
 */
function validarFormularioReclamacion(form) {
  let valido = true;

  const campos = form.querySelectorAll(
    "input[required], select[required], textarea[required]",
  );
  campos.forEach((campo) => {
    if (!validarCampoReclamacion(campo)) {
      valido = false;
    }
  });

  if (valido) {
    mostrarMensajeExitoReclamacion();
  }

  return valido;
}

/**
 * Muestra error en campo
 */
function mostrarErrorReclamacion(campo, mensaje) {
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
function limpiarErrorReclamacion(campo) {
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
function mostrarMensajeExitoReclamacion() {
  const alerta = document.createElement("div");
  alerta.className =
    "alert alert-success alert-dismissible fade show position-fixed top-0 end-0 m-3";
  alerta.style.zIndex = "9999";
  alerta.style.width = "300px";
  alerta.innerHTML = `
        <strong>¡Éxito!</strong> Tu reclamación ha sido registrada correctamente.
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

  document.body.appendChild(alerta);

  // Remover automáticamente después de 5 segundos
  setTimeout(() => {
    alerta.remove();
  }, 5000);
}
