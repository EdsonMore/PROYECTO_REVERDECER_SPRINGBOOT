// Validación del formulario
const form = document.getElementById('contactForm');
const charCountSpan = document.getElementById('charCount');
const mensajeTextarea = document.getElementById('mensaje');

// Contador de caracteres
if (mensajeTextarea) {
    mensajeTextarea.addEventListener('input', function() {
        const count = this.value.length;
        charCountSpan.textContent = count;
        if (count > 500) {
            this.value = this.value.substring(0, 500);
            charCountSpan.textContent = 500;
        }
    });
}

// Validaciones
function validateNombre() {
    const nombre = document.getElementById('nombre');
    const error = document.getElementById('nombreError');
    const regex = /^[A-Za-záéíóúÁÉÍÓÚñÑ\s]+$/;
    if (!nombre.value.trim()) {
        error.textContent = 'El nombre es requerido';
        nombre.classList.add('contact-input-error');
        return false;
    } else if (!regex.test(nombre.value)) {
        error.textContent = 'Ingrese un nombre válido (solo letras)';
        nombre.classList.add('contact-input-error');
        return false;
    } else {
        error.textContent = '';
        nombre.classList.remove('contact-input-error');
        return true;
    }
}

function validateCorreo() {
    const correo = document.getElementById('correo');
    const error = document.getElementById('correoError');
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!correo.value.trim()) {
        error.textContent = 'El correo es requerido';
        correo.classList.add('contact-input-error');
        return false;
    } else if (!regex.test(correo.value)) {
        error.textContent = 'Ingrese un correo válido';
        correo.classList.add('contact-input-error');
        return false;
    } else {
        error.textContent = '';
        correo.classList.remove('contact-input-error');
        return true;
    }
}

function validateAsunto() {
    const asunto = document.getElementById('asunto');
    const error = document.getElementById('asuntoError');
    if (!asunto.value) {
        error.textContent = 'Seleccione un asunto';
        asunto.classList.add('contact-input-error');
        return false;
    } else {
        error.textContent = '';
        asunto.classList.remove('contact-input-error');
        return true;
    }
}

function validateMensaje() {
    const mensaje = document.getElementById('mensaje');
    const error = document.getElementById('mensajeError');
    if (!mensaje.value.trim()) {
        error.textContent = 'El mensaje es requerido';
        mensaje.classList.add('contact-input-error');
        return false;
    } else if (mensaje.value.trim().length < 10) {
        error.textContent = 'El mensaje debe tener al menos 10 caracteres';
        mensaje.classList.add('contact-input-error');
        return false;
    } else {
        error.textContent = '';
        mensaje.classList.remove('contact-input-error');
        return true;
    }
}

function validateTerminos() {
    const terminos = document.getElementById('terminos');
    const error = document.getElementById('terminosError');
    if (!terminos.checked) {
        error.textContent = 'Debe aceptar los términos';
        return false;
    } else {
        error.textContent = '';
        return true;
    }
}

// Eventos - Versión corregida (sin optional chaining)
const nombreInput = document.getElementById('nombre');
const correoInput = document.getElementById('correo');
const asuntoSelect = document.getElementById('asunto');
const mensajeText = document.getElementById('mensaje');
const terminosCheck = document.getElementById('terminos');

if (nombreInput) {
    nombreInput.addEventListener('input', validateNombre);
}
if (correoInput) {
    correoInput.addEventListener('input', validateCorreo);
}
if (asuntoSelect) {
    asuntoSelect.addEventListener('change', validateAsunto);
}
if (mensajeText) {
    mensajeText.addEventListener('input', validateMensaje);
}
if (terminosCheck) {
    terminosCheck.addEventListener('change', validateTerminos);
}

// Submit
if (form) {
    form.addEventListener('submit', function(e) {
        e.preventDefault();
        const isValid = validateNombre() && validateCorreo() && validateAsunto() && validateMensaje() && validateTerminos();

        if (isValid) {
            const successMessage = document.getElementById('formSuccessMessage');
            if (successMessage) {
                successMessage.style.display = 'block';
            }
            form.reset();
            if (charCountSpan) {
                charCountSpan.textContent = '0';
            }
            setTimeout(function() {
                if (successMessage) {
                    successMessage.style.display = 'none';
                }
            }, 5000);
        }
    });
}

// Reset
const resetBtn = document.getElementById('resetBtn');
if (resetBtn) {
    resetBtn.addEventListener('click', function() {
        setTimeout(function() {
            if (charCountSpan && mensajeTextarea) {
                charCountSpan.textContent = mensajeTextarea.value.length;
            }
            const errorInputs = document.querySelectorAll('.contact-input-error');
            for (let i = 0; i < errorInputs.length; i++) {
                errorInputs[i].classList.remove('contact-input-error');
            }
            const errorMessages = document.querySelectorAll('.contact-error-message');
            for (let i = 0; i < errorMessages.length; i++) {
                errorMessages[i].textContent = '';
            }
            const successMessage = document.getElementById('formSuccessMessage');
            if (successMessage) {
                successMessage.style.display = 'none';
            }
        }, 10);
    });
}

// Toggle FAQ
function toggleFaq(button) {
    const faqItem = button.parentElement;
    const isActive = faqItem.classList.contains('active');

    const allFaqItems = document.querySelectorAll('.contact-faq-item');
    for (let i = 0; i < allFaqItems.length; i++) {
        allFaqItems[i].classList.remove('active');
    }

    if (!isActive) {
        faqItem.classList.add('active');
    }
}