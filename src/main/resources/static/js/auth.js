function validarLogin() {
  // Limpiar errores anteriores
  limpiarErrores();

  const correo = document.getElementById("correo");
  const password = document.getElementById("password");
  let esValido = true;

  // Validación de correo (estructura if)
  if (!correo.value.trim()) {
    mostrarError(correo, "El correo es obligatorio");
    esValido = false;
  } else if (!validarFormatoCorreo(correo.value)) {
    mostrarError(correo, "Ingrese un correo válido (ejemplo@dominio.com)");
    esValido = false;
  }

  // Validación de contraseña
  if (!password.value) {
    mostrarError(password, "La contraseña es obligatoria");
    esValido = false;
  } else if (password.value.length < 6) {
    mostrarError(password, "La contraseña debe tener al menos 6 caracteres");
    esValido = false;
  }

  return esValido;
}

// ============ VALIDACIÓN DE REGISTRO ============
function validarRegistro() {
  limpiarErrores();

  // Obtener todos los campos
  const campos = {
    nombres: document.getElementById("nombres"),
    apellidoPaterno: document.getElementById("apellidoPaterno"),
    apellidoMaterno: document.getElementById("apellidoMaterno"),
    correo: document.getElementById("correo"),
    password: document.getElementById("password"),
    tipoDoc: document.getElementById("tipoDoc"),
    dni: document.getElementById("dni"),
    fechaNacimiento: document.getElementById("fechaNacimiento"),
    genero: document.getElementById("genero"),
    numero: document.getElementById("numero"),
    direccion1: document.getElementById("direccion1"),
  };

  let esValido = true;

  // Validación de nombres (solo letras)
  if (!campos.nombres.value.trim()) {
    mostrarError(campos.nombres, "Los nombres son obligatorios");
    esValido = false;
  } else if (!validarSoloLetras(campos.nombres.value)) {
    mostrarError(campos.nombres, "Los nombres solo deben contener letras");
    esValido = false;
  }

  // Validación de apellidos
  if (!campos.apellidoPaterno.value.trim()) {
    mostrarError(campos.apellidoPaterno, "El apellido paterno es obligatorio");
    esValido = false;
  } else if (!validarSoloLetras(campos.apellidoPaterno.value)) {
    mostrarError(
      campos.apellidoPaterno,
      "El apellido solo debe contener letras",
    );
    esValido = false;
  }

  if (!campos.apellidoMaterno.value.trim()) {
    mostrarError(campos.apellidoMaterno, "El apellido materno es obligatorio");
    esValido = false;
  } else if (!validarSoloLetras(campos.apellidoMaterno.value)) {
    mostrarError(
      campos.apellidoMaterno,
      "El apellido solo debe contener letras",
    );
    esValido = false;
  }

  // Validación de correo
  if (!campos.correo.value.trim()) {
    mostrarError(campos.correo, "El correo es obligatorio");
    esValido = false;
  } else if (!validarFormatoCorreo(campos.correo.value)) {
    mostrarError(campos.correo, "Ingrese un correo válido");
    esValido = false;
  }

  // Validación de contraseña
  if (!campos.password.value) {
    mostrarError(campos.password, "La contraseña es obligatoria");
    esValido = false;
  } else if (campos.password.value.length < 6) {
    mostrarError(
      campos.password,
      "La contraseña debe tener al menos 6 caracteres",
    );
    esValido = false;
  } else if (!validarPasswordFuerte(campos.password.value)) {
    mostrarError(
      campos.password,
      "La contraseña debe tener al menos una mayúscula y un número",
    );
    esValido = false;
  }

  // Validación de tipo de documento
  if (!campos.tipoDoc.value) {
    mostrarError(campos.tipoDoc, "Seleccione un tipo de documento");
    esValido = false;
  }

  // Validación de DNI/RUC según tipo (estructura switch en JS)
  if (!campos.dni.value.trim()) {
    mostrarError(campos.dni, "El número de documento es obligatorio");
    esValido = false;
  } else {
    const tipoDoc = campos.tipoDoc.value;
    const numeroDoc = campos.dni.value.trim();

    switch (tipoDoc) {
      case "DNI":
        if (!validarDNI(numeroDoc)) {
          mostrarError(campos.dni, "DNI debe tener 8 dígitos");
          esValido = false;
        }
        break;
      case "RUC":
        if (!validarRUC(numeroDoc)) {
          mostrarError(campos.dni, "RUC debe tener 11 dígitos");
          esValido = false;
        }
        break;
      case "PASAPORTE":
        if (!validarPasaporte(numeroDoc)) {
          mostrarError(
            campos.dni,
            "Pasaporte debe tener 6-12 caracteres alfanuméricos",
          );
          esValido = false;
        }
        break;
    }
  }

  // Validación de fecha de nacimiento (edad mínima 18 años)
  if (!campos.fechaNacimiento.value) {
    mostrarError(
      campos.fechaNacimiento,
      "La fecha de nacimiento es obligatoria",
    );
    esValido = false;
  } else if (!validarEdad(campos.fechaNacimiento.value)) {
    mostrarError(campos.fechaNacimiento, "Debes ser mayor de 18 años");
    esValido = false;
  }

  // Validación de género
  if (!campos.genero.value) {
    mostrarError(campos.genero, "Seleccione un género");
    esValido = false;
  }

  // Validación de teléfono
  if (!campos.numero.value) {
    mostrarError(campos.numero, "El teléfono es obligatorio");
    esValido = false;
  } else if (!validarTelefono(campos.numero.value)) {
    mostrarError(campos.numero, "Teléfono debe tener 9 dígitos");
    esValido = false;
  }

  // Validación de dirección
  if (!campos.direccion1.value.trim()) {
    mostrarError(campos.direccion1, "La dirección es obligatoria");
    esValido = false;
  }

  return esValido;
}

// ============ FUNCIONES DE VALIDACIÓN (DOM Manipulation) ============

function mostrarError(input, mensaje) {
  // Manipulación del DOM - agregar clases y mensajes de error
  const formGroup = input.closest(".auth-group");
  input.classList.add("input-error");

  // Verificar si ya existe mensaje de error
  let errorMsg = formGroup.querySelector(".auth-error-msg");
  if (!errorMsg) {
    errorMsg = document.createElement("span");
    errorMsg.className = "auth-error-msg";
    formGroup.appendChild(errorMsg);
  }
  errorMsg.textContent = mensaje;
}

function limpiarErrores() {
  // Limpiar todos los mensajes de error y clases de error
  const errores = document.querySelectorAll(".auth-error-msg");
  errores.forEach((error) => error.remove());

  const inputsError = document.querySelectorAll(".input-error");
  inputsError.forEach((input) => input.classList.remove("input-error"));
}

// ============ FUNCIONES DE VALIDACIÓN ESPECÍFICAS ============

function validarFormatoCorreo(correo) {
  const regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
  return regex.test(correo);
}

function validarSoloLetras(texto) {
  const regex = /^[a-zA-ZáéíóúñÁÉÍÓÚÑ\s]+$/;
  return regex.test(texto);
}

function validarPasswordFuerte(password) {
  // Al menos una mayúscula y un número
  const tieneMayuscula = /[A-Z]/.test(password);
  const tieneNumero = /[0-9]/.test(password);
  return tieneMayuscula && tieneNumero;
}

function validarDNI(dni) {
  return /^\d{8}$/.test(dni);
}

function validarRUC(ruc) {
  return /^\d{11}$/.test(ruc);
}

function validarPasaporte(pasaporte) {
  return /^[A-Z0-9]{6,12}$/i.test(pasaporte);
}

function validarTelefono(telefono) {
  return /^\d{9}$/.test(telefono);
}

function validarEdad(fechaNacimiento) {
  const hoy = new Date();
  const fechaNac = new Date(fechaNacimiento);
  let edad = hoy.getFullYear() - fechaNac.getFullYear();
  const mes = hoy.getMonth() - fechaNac.getMonth();

  if (mes < 0 || (mes === 0 && hoy.getDate() < fechaNac.getDate())) {
    edad--;
  }

  return edad >= 18;
}

// ============ EVENTOS EN TIEMPO REAL (onchange, oninput) ============

// Inicializar eventos cuando el DOM esté listo
document.addEventListener("DOMContentLoaded", function () {
  // Validación en tiempo real para el formulario de registro
  const registroForm = document.getElementById("registroForm");
  if (registroForm) {
    // Agregar eventos a todos los inputs del formulario de registro
    const inputs = registroForm.querySelectorAll("input, select");
    inputs.forEach((input) => {
      // Evento oninput para validación en tiempo real
      input.addEventListener("input", function () {
        // Limpiar error específico de este campo
        const errorMsg =
          this.closest(".auth-group")?.querySelector(".auth-error-msg");
        if (errorMsg) errorMsg.remove();
        this.classList.remove("input-error");
      });

      // Evento onchange para validación al salir del campo
      input.addEventListener("change", function () {
        if (registroForm.onsubmit) {
          // Trigger de validación suave
          validarRegistro();
        }
      });
    });

    // Validación específica para DNI según tipo de documento
    const tipoDoc = document.getElementById("tipoDoc");
    const dniInput = document.getElementById("dni");

    if (tipoDoc && dniInput) {
      tipoDoc.addEventListener("change", function () {
        const tipo = this.value;
        let placeholder = "";
        let maxLength = 0;

        switch (tipo) {
          case "DNI":
            placeholder = "8 dígitos";
            maxLength = 8;
            break;
          case "RUC":
            placeholder = "11 dígitos";
            maxLength = 11;
            break;
          case "PASAPORTE":
            placeholder = "6-12 caracteres";
            maxLength = 12;
            break;
        }

        dniInput.placeholder = placeholder;
        dniInput.maxLength = maxLength;
        dniInput.value = ""; // Limpiar al cambiar tipo
      });
    }

    // Validación de contraseña en tiempo real
    const passwordInput = document.getElementById("password");
    if (passwordInput) {
      passwordInput.addEventListener("input", function () {
        const valor = this.value;
        const formGroup = this.closest(".auth-group");
        let existingHint = formGroup.querySelector(".password-hint");

        if (!existingHint && valor.length > 0) {
          existingHint = document.createElement("small");
          existingHint.className = "password-hint";
          existingHint.style.display = "block";
          existingHint.style.fontSize = "0.75rem";
          existingHint.style.marginTop = "0.25rem";
          existingHint.style.color = "#666";
          formGroup.appendChild(existingHint);
        }

        if (existingHint) {
          if (valor.length >= 6 && /[A-Z]/.test(valor) && /[0-9]/.test(valor)) {
            existingHint.innerHTML = "✅ Contraseña válida";
            existingHint.style.color = "#2e7d32";
          } else if (valor.length > 0) {
            existingHint.innerHTML =
              "⚠️ La contraseña debe tener: mínimo 6 caracteres, 1 mayúscula y 1 número";
            existingHint.style.color = "#f57c00";
          } else {
            existingHint.remove();
          }
        }
      });
    }
  }

  // Validación en tiempo real para login
  const loginForm = document.getElementById("loginForm");
  if (loginForm) {
    const inputs = loginForm.querySelectorAll("input");
    inputs.forEach((input) => {
      input.addEventListener("input", function () {
        const errorMsg =
          this.closest(".auth-group")?.querySelector(".auth-error-msg");
        if (errorMsg) errorMsg.remove();
        this.classList.remove("input-error");
      });
    });
  }
});
