// ===== LOGIN Y REGISTRO - JAVASCRIPT NATIVO PURO =====

document.addEventListener("DOMContentLoaded", function () {
  // Validar formulario de login
  const loginForm = document.querySelector('form[action="/login"]');
  if (loginForm) {
    loginForm.addEventListener("submit", function (e) {
      if (!validarFormularioLogin(this)) {
        e.preventDefault();
        e.stopPropagation();
      }
    });

    // Validación en tiempo real
    const emailInput = loginForm.querySelector('input[name="correo"]');
    const passwordInput = loginForm.querySelector('input[name="password"]');

    if (emailInput) {
      emailInput.addEventListener("blur", function () {
        if (!validarEmail(this.value)) {
          mostrarError(this, "Ingresa un correo válido");
        } else {
          limpiarError(this);
        }
      });
    }

    if (passwordInput) {
      passwordInput.addEventListener("blur", function () {
        if (this.value.trim().length === 0) {
          mostrarError(this, "La contraseña es requerida");
        } else {
          limpiarError(this);
        }
      });
    }
  }

  // Validar formulario de registro
  const registroForm = document.getElementById("registroForm");
  if (registroForm) {
    registroForm.addEventListener("submit", function (e) {
      if (!validarFormularioRegistro(this)) {
        e.preventDefault();
        e.stopPropagation();
      }
    });

    // Validación en tiempo real para campos principales
    const campos = registroForm.querySelectorAll(
      "input[required], select[required]",
    );
    campos.forEach((campo) => {
      campo.addEventListener("blur", function () {
        validarCampoRegistro(this);
      });
    });
  }
});

// ===== FUNCIONES DE VALIDACIÓN =====

/**
 * Valida formato de email
 */
function validarEmail(email) {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return regex.test(email);
}

/**
 * Valida contraseña (mínimo 8 caracteres)
 */
function validarPassword(password) {
  return password.length >= 8;
}

/**
 * Calcula edad a partir de fecha de nacimiento
 */
function calcularEdad(fecha) {
  const nacimiento = new Date(fecha);
  const hoy = new Date();
  let edad = hoy.getFullYear() - nacimiento.getFullYear();
  const mesActual = hoy.getMonth();
  const mesNacimiento = nacimiento.getMonth();

  if (
    mesActual < mesNacimiento ||
    (mesActual === mesNacimiento && hoy.getDate() < nacimiento.getDate())
  ) {
    edad--;
  }
  return edad;
}

/**
 * Valida DNI / Documento según tipo
 */
function validarDocumento(tipo, valor) {
  if (tipo === "DNI") return /^\d{8}$/.test(valor);
  if (tipo === "RUC") return /^\d{11}$/.test(valor);
  if (tipo === "PASAPORTE") return /^[A-Z0-9]{6,9}$/i.test(valor);
  if (tipo === "CARNET_EXTRANJERIA") return /^[A-Z0-9]{9}$/i.test(valor);
  return false;
}

/**
 * Valida teléfono (9 dígitos)
 */
function validarTelefono(numero) {
  return /^\d{9}$/.test(numero);
}

/**
 * Valida un campo de registro
 */
function validarCampoRegistro(campo) {
  const nombre = campo.name;
  const valor = campo.value.trim();

  if (!valor && campo.required) {
    mostrarError(campo, "Este campo es requerido");
    return false;
  }

  // Validaciones específicas
  if (nombre === "correo" && valor) {
    if (!validarEmail(valor)) {
      mostrarError(campo, "Ingresa un correo válido");
      return false;
    }
  }

  if (nombre === "password" && valor) {
    if (!validarPassword(valor)) {
      mostrarError(campo, "Mínimo 8 caracteres");
      return false;
    }
  }

  if (nombre === "dni" && valor) {
    const tipoDocSelect = campo
      .closest("form")
      .querySelector('select[name="tipoDoc"]');
    const tipoDoc = tipoDocSelect ? tipoDocSelect.value : "DNI";
    if (!validarDocumento(tipoDoc, valor)) {
      mostrarError(campo, `Número de ${tipoDoc} inválido`);
      return false;
    }
  }

  if (nombre === "numero" && valor) {
    if (!validarTelefono(valor)) {
      mostrarError(campo, "Debe ser un número de 9 dígitos");
      return false;
    }
  }

  if (nombre === "fechaNacimiento" && valor) {
    const edad = calcularEdad(valor);
    if (edad < 18) {
      mostrarError(campo, "Debes tener al menos 18 años");
      return false;
    }
  }

  limpiarError(campo);
  return true;
}

/**
 * Valida todo el formulario de login
 */
function validarFormularioLogin(form) {
  let valido = true;

  const correo = form.querySelector('input[name="correo"]');
  const password = form.querySelector('input[name="password"]');

  if (correo && (!correo.value.trim() || !validarEmail(correo.value))) {
    mostrarError(correo, "Ingresa un correo válido");
    valido = false;
  } else {
    limpiarError(correo);
  }

  if (
    password &&
    (!password.value.trim() || !validarPassword(password.value))
  ) {
    mostrarError(password, "Contraseña requerida (mínimo 8 caracteres)");
    valido = false;
  } else {
    limpiarError(password);
  }

  return valido;
}

/**
 * Valida todo el formulario de registro
 */
function validarFormularioRegistro(form) {
  let valido = true;

  const campos = form.querySelectorAll("input[required], select[required]");
  campos.forEach((campo) => {
    if (!validarCampoRegistro(campo)) {
      valido = false;
    }
  });

  return valido;
}

/**
 * Muestra mensaje de error en un campo
 */
function mostrarError(campo, mensaje) {
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
 * Limpia el mensaje de error de un campo
 */
function limpiarError(campo) {
  campo.classList.remove("is-invalid");
  const feedback = campo.nextElementSibling;
  if (feedback && feedback.classList.contains("invalid-feedback")) {
    feedback.textContent = "";
    feedback.style.display = "none";
  }
}
