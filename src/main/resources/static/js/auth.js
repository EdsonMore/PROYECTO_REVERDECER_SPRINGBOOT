/**
 * AUTH.JS - VALIDACIONES JAVASCRIPT NATIVAS
 * ==========================================
 *
 * Este archivo contiene validaciones del lado del cliente
 * para los formularios de Login y Registro.
 *
 * IMPORTANTE: Las validaciones en JavaScript son para experiencia del usuario.
 * Las validaciones REALES deben ocurrir en el Backend (Service) por seguridad.
 *
 * Validaciones implementadas (nativas, sin librerías):
 * 1. Campos no vacíos
 * 2. Email válido (formato correcto)
 * 3. Password mínimo de caracteres
 * 4. Manipulación del DOM para mostrar/ocultar errores
 * 5. Eventos: onchange, onclick, onsubmit
 *
 * Patrón usado: Expresiones Regulares (Regex) nativas de JavaScript
 */

/**
 * FUNCIÓN: Validar formato de email
 *
 * Usa una expresión regular para verificar que el email
 * tenga un formato válido: usuario@dominio.com
 *
 * Componentes del regex:
 * - ^ : Inicio de la cadena
 * - [^@]+ : Uno o más caracteres que no sean @
 * - @ : El símbolo @
 * - [^@]+ : Uno o más caracteres que no sean @
 * - \. : Un punto literal
 * - [^@]+ : Uno o más caracteres que no sean @
 * - $ : Fin de la cadena
 *
 * @param {string} email Email a validar
 * @returns {boolean} true si email es válido, false si no
 */
function validarFormatoEmail(email) {
  // Expresión regular para validar email
  const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return regexEmail.test(email);
}

/**
 * FUNCIÓN: Validar email en tiempo real
 *
 * Se ejecuta cuando el usuario sale del campo email (onchange)
 * Muestra/oculta mensaje de error dinámicamente
 *
 * @param {HTMLElement} input Elemento input que dispara el evento
 */
function validarEmail(input) {
  const email = input.value.trim();
  const errorElement = document.getElementById("errorEmail");

  // Si campo está vacío, limpiar error
  if (!email) {
    errorElement.textContent = "";
    return;
  }

  // Validar formato
  if (!validarFormatoEmail(email)) {
    errorElement.textContent =
      "❌ Email inválido. Ejemplo: usuario@dominio.com";
    input.classList.add("is-invalid");
  } else {
    errorElement.textContent = "✅ Email válido";
    input.classList.remove("is-invalid");
    input.classList.add("is-valid");
  }
}

/**
 * FUNCIÓN: Validar contraseña en tiempo real
 *
 * Se ejecuta cuando el usuario sale del campo password (onchange)
 * Verifica requisitos mínimos y muestra feedback
 *
 * Requisitos:
 * - Mínimo 8 caracteres
 * - Idealmente variedad (mayúsculas, números, símbolos)
 *
 * @param {HTMLElement} input Elemento input que dispara el evento
 */
function validarPassword(input) {
  const password = input.value.trim();
  const errorElement = document.getElementById("errorPassword");

  // Si campo está vacío, limpiar error
  if (!password) {
    errorElement.textContent = "";
    return;
  }

  // Validar longitud mínima
  if (password.length < 8) {
    errorElement.textContent = "❌ Mínimo 8 caracteres";
    input.classList.add("is-invalid");
    return;
  }

  // Validaciones adicionales (fortaleza)
  let fortaleza = 0;

  // Verificar si tiene minúsculas
  if (/[a-z]/.test(password)) fortaleza++;

  // Verificar si tiene mayúsculas
  if (/[A-Z]/.test(password)) fortaleza++;

  // Verificar si tiene números
  if (/[0-9]/.test(password)) fortaleza++;

  // Verificar si tiene caracteres especiales
  if (/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password)) fortaleza++;

  // Mostrar feedback según fortaleza
  if (fortaleza < 2) {
    errorElement.textContent =
      "⚠️ Contraseña débil. Mezcla letras, números y símbolos";
  } else if (fortaleza < 3) {
    errorElement.textContent = "✅ Contraseña moderada";
  } else {
    errorElement.textContent = "✅ Contraseña fuerte";
  }

  input.classList.remove("is-invalid");
  input.classList.add("is-valid");
}

/**
 * FUNCIÓN: Mostrar mensaje de error en el DOM
 *
 * Utilidad para mostrar mensajes de error de forma consistente
 *
 * @param {string} elementId ID del elemento donde mostrar el error
 * @param {string} mensaje Texto del error a mostrar
 */
function mostrarError(elementId, mensaje) {
  const elemento = document.getElementById(elementId);
  if (elemento) {
    elemento.textContent = "❌ " + mensaje;
    elemento.style.color = "#dc3545";
    elemento.style.fontSize = "0.875rem";
    elemento.style.marginTop = "0.25rem";
  }
}

/**
 * FUNCIÓN: Limpiar errores de un formulario
 *
 * Elimina todos los mensajes de error y clases de validación
 *
 * @param {string} formId ID del formulario a limpiar
 */
function limpiarErrores(formId) {
  const form = document.getElementById(formId);
  if (form) {
    // Encontrar todos los elementos con clase de error
    const errores = form.querySelectorAll(".error-message");
    errores.forEach((error) => {
      error.textContent = "";
    });

    // Limpiar clases de validación
    const inputs = form.querySelectorAll("input, select, textarea");
    inputs.forEach((input) => {
      input.classList.remove("is-invalid", "is-valid");
    });
  }
}

/**
 * FUNCIÓN: Validación completa de formulario LOGIN
 *
 * Se ejecuta al hacer submit del formulario de login
 * Valida todos los campos antes de enviar al servidor
 *
 * Validaciones:
 * 1. Email no vacío
 * 2. Email formato válido
 * 3. Password no vacío
 * 4. Password mínimo 8 caracteres
 *
 * @returns {boolean} true si todas las validaciones pasan, false si falla
 */
function validarLogin() {
  // Limpiar errores previos
  document.querySelectorAll(".error-message").forEach((el) => {
    el.textContent = "";
  });

  // Obtener referencias a los inputs
  const emailInput = document.getElementById("correo");
  const passwordInput = document.getElementById("password");

  // Obtener valores (trim elimina espacios al inicio/final)
  const email = emailInput ? emailInput.value.trim() : "";
  const password = passwordInput ? passwordInput.value.trim() : "";

  let esValido = true;

  // VALIDACIÓN 1: Email no vacío
  if (!email) {
    mostrarError("errorCorreo", "El email es requerido");
    esValido = false;
  }
  // VALIDACIÓN 2: Email formato válido
  else if (!validarFormatoEmail(email)) {
    mostrarError("errorCorreo", "Email inválido. Ejemplo: usuario@dominio.com");
    esValido = false;
  }

  // VALIDACIÓN 3: Password no vacío
  if (!password) {
    mostrarError("errorPassword", "La contraseña es requerida");
    esValido = false;
  }
  // VALIDACIÓN 4: Password longitud mínima
  else if (password.length < 8) {
    mostrarError(
      "errorPassword",
      "La contraseña debe tener mínimo 8 caracteres",
    );
    esValido = false;
  }

  // Si hay errores, no enviar el formulario
  if (!esValido) {
    console.log("[VALIDACIÓN LOGIN] Errores encontrados");
    return false;
  }

  console.log("[VALIDACIÓN LOGIN] ✅ Todas las validaciones pasaron");
  return true; // Enviar formulario al servidor
}

/**
 * FUNCIÓN: Validación completa de formulario REGISTRO
 *
 * Se ejecuta al hacer submit del formulario de registro
 * Valida todos los campos antes de enviar al servidor
 *
 * Validaciones:
 * 1. Nombres no vacíos
 * 2. Apellidos no vacíos
 * 3. Email válido
 * 4. Password válido (mínimo 8)
 * 5. DNI no vacío
 * 6. Género seleccionado
 *
 * @returns {boolean} true si todas las validaciones pasan
 */
function validarRegistro() {
  // Limpiar errores previos
  document.querySelectorAll('[id^="error"]').forEach((el) => {
    el.textContent = "";
  });

  // Obtener referencias a los inputs
  const nombres = document.getElementById("nombres");
  const apellidoPaterno = document.getElementById("apellidoPaterno");
  const correo = document.getElementById("correo");
  const password = document.getElementById("password");
  const dni = document.getElementById("dni");
  const genero = document.getElementById("genero");

  let esValido = true;

  // VALIDACIÓN: Nombres no vacío
  if (!nombres || !nombres.value.trim()) {
    mostrarError("errorNombres", "Los nombres son requeridos");
    esValido = false;
  }

  // VALIDACIÓN: Apellido paterno no vacío
  if (!apellidoPaterno || !apellidoPaterno.value.trim()) {
    mostrarError("errorApellidoPaterno", "El apellido paterno es requerido");
    esValido = false;
  }

  // VALIDACIÓN: Email válido
  if (!correo || !validarFormatoEmail(correo.value.trim())) {
    mostrarError("errorCorreo", "Email inválido. Ejemplo: usuario@dominio.com");
    esValido = false;
  }

  // VALIDACIÓN: Password válido
  if (!password || password.value.trim().length < 8) {
    mostrarError(
      "errorPassword",
      "La contraseña debe tener mínimo 8 caracteres",
    );
    esValido = false;
  }

  // VALIDACIÓN: DNI no vacío
  if (!dni || !dni.value.trim()) {
    mostrarError("errorDni", "El DNI es requerido");
    esValido = false;
  }

  // VALIDACIÓN: Género seleccionado
  if (!genero || !genero.value) {
    mostrarError("errorGenero", "Debes seleccionar un género");
    esValido = false;
  }

  if (!esValido) {
    console.log("[VALIDACIÓN REGISTRO] Errores encontrados");
    return false;
  }

  console.log("[VALIDACIÓN REGISTRO] ✅ Todas las validaciones pasaron");
  return true; // Enviar formulario al servidor
}

/**
 * EVENTO: DOMContentLoaded
 *
 * Se ejecuta cuando el HTML está completamente cargado
 * Inicializa event listeners para validación en tiempo real
 */
document.addEventListener("DOMContentLoaded", function () {
  console.log("[AUTH.JS] Script de validación cargado");

  // Agregar validación en tiempo real al campo de email (si existe)
  const emailInput = document.getElementById("correo");
  if (emailInput) {
    emailInput.addEventListener("change", function () {
      validarEmail(this);
    });
  }

  // Agregar validación en tiempo real al campo de password (si existe)
  const passwordInput = document.getElementById("password");
  if (passwordInput) {
    passwordInput.addEventListener("change", function () {
      validarPassword(this);
    });
  }

  console.log("[AUTH.JS] Event listeners configurados");
});

/**
 * FLUJO DE DATOS - RESUMEN
 * ==========================
 *
 * 1. USUARIO COMPLETA FORMULARIO
 *    ↓
 * 2. HACE SUBMIT DEL FORMULARIO
 *    ↓
 * 3. JavaScript valida (onsubmit)
 *    ├─ Si falla: muestra errores, NO envía
 *    └─ Si pasa: envía al servidor
 *    ↓
 * 4. BACKEND valida (Service)
 *    ├─ Si falla: retorna a formulario con error
 *    └─ Si pasa: procesa datos
 *    ↓
 * 5. RESPUESTA AL USUARIO
 *    ├─ Login exitoso: sesión activa + redirección a inicio
 *    └─ Registro exitoso: redirección a login
 */
