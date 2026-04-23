const mensaje = document.getElementById("mensaje");
const contador = document.getElementById("contador");

mensaje.addEventListener("input", function () {
  contador.textContent = this.value.length;
});

const form = document.getElementById("contactoForm");

form.addEventListener("submit", function (e) {
  e.preventDefault();
  let valido = true;

  const nombre = document.getElementById("nombre");
  const correo = document.getElementById("correo");
  const asunto = document.getElementById("asunto");
  const mensaje = document.getElementById("mensaje");
  const terminos = document.getElementById("terminos");

  if (nombre.value.trim() === "") {
    document.getElementById("errorNombre").textContent =
      "El nombre es obligatorio";
    nombre.classList.add("contacto-input-error");
    valido = false;
  } else {
    document.getElementById("errorNombre").textContent = "";
    nombre.classList.remove("contacto-input-error");
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(correo.value)) {
    document.getElementById("errorCorreo").textContent =
      "Correo electrónico inválido";
    correo.classList.add("contacto-input-error");
    valido = false;
  } else {
    document.getElementById("errorCorreo").textContent = "";
    correo.classList.remove("contacto-input-error");
  }

  if (asunto.value === "") {
    document.getElementById("errorAsunto").textContent = "Seleccione un asunto";
    asunto.classList.add("contacto-input-error");
    valido = false;
  } else {
    document.getElementById("errorAsunto").textContent = "";
    asunto.classList.remove("contacto-input-error");
  }

  if (mensaje.value.trim().length < 10) {
    document.getElementById("errorMensaje").textContent =
      "El mensaje debe tener al menos 10 caracteres";
    mensaje.classList.add("contacto-input-error");
    valido = false;
  } else {
    document.getElementById("errorMensaje").textContent = "";
    mensaje.classList.remove("contacto-input-error");
  }

  if (!terminos.checked) {
    document.getElementById("errorTerminos").textContent =
      "Debes aceptar los términos";
    valido = false;
  } else {
    document.getElementById("errorTerminos").textContent = "";
  }

  if (valido) {
    document.getElementById("mensajeExito").style.display = "block";
    form.reset();
    contador.textContent = "0";
    setTimeout(() => {
      document.getElementById("mensajeExito").style.display = "none";
    }, 3000);
  }
});

document.getElementById("limpiarBtn").addEventListener("click", function () {
  form.reset();
  contador.textContent = "0";
  document.querySelectorAll(".contacto-input-error").forEach((el) => {
    el.classList.remove("contacto-input-error");
  });
  document.querySelectorAll(".contacto-error").forEach((el) => {
    el.textContent = "";
  });
});

function toggleFaq(btn) {
  const item = btn.closest(".contacto-faq-item");
  item.classList.toggle("active");
}
