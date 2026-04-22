// perfil.js - Script NATIVO para el perfil de usuario

document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form') || document.getElementById('perfilForm');

    if (!form) return;

    // Validación al enviar
    form.addEventListener('submit', function (e) {
        if (!validarFormularioPerfil(this)) {
            e.preventDefault();
            e.stopPropagation();
        }
    });

    // Validación en tiempo real para todos los campos requeridos
    const campos = form.querySelectorAll('input[required], select[required]');
    campos.forEach(campo => {
        campo.addEventListener('blur', function () {
            validarCampoPerfil(this);
        });

        // Para el campo de número, filtrar solo dígitos
        if (campo.name === 'numero') {
            campo.addEventListener('input', function () {
                this.value = this.value.replace(/\D/g, '');
                if (this.value.length > 9) {
                    this.value = this.value.substring(0, 9);
                }
            });
        }
    });
});

/**
 * Valida un campo del perfil
 */
function validarCampoPerfil(campo) {
    const valor = campo.value.trim();
    const nombre = campo.name;
    const tipo = campo.type;

    if (!valor && campo.required) {
        mostrarErrorPerfil(campo, 'Este campo es requerido');
        return false;
    }

    if (tipo === 'email' && valor) {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!regex.test(valor)) {
            mostrarErrorPerfil(campo, 'Email inválido');
            return false;
        }
    }

    if (nombre === 'numero' && valor) {
        const soloNumeros = valor.replace(/\D/g, '');
        if (soloNumeros.length !== 9) {
            mostrarErrorPerfil(campo, 'El teléfono debe tener 9 dígitos');
            return false;
        }
    }

    limpiarErrorPerfil(campo);
    return true;
}

/**
 * Valida todo el formulario de perfil
 */
function validarFormularioPerfil(form) {
    let valido = true;
    
    const campos = form.querySelectorAll('input[required], select[required]');
    campos.forEach(campo => {
        if (!validarCampoPerfil(campo)) {
            valido = false;
        }
    });

    return valido;
}

/**
 * Muestra error
 */
function mostrarErrorPerfil(campo, mensaje) {
    campo.classList.add('is-invalid');
    
    let feedback = campo.nextElementSibling;
    if (!feedback || !feedback.classList.contains('invalid-feedback')) {
        feedback = document.createElement('div');
        feedback.classList.add('invalid-feedback');
        feedback.style.display = 'block';
        campo.parentNode.insertBefore(feedback, campo.nextSibling);
    }
    feedback.textContent = mensaje;
}

/**
 * Limpia errores
 */
function limpiarErrorPerfil(campo) {
    campo.classList.remove('is-invalid');
    const feedback = campo.nextElementSibling;
    if (feedback && feedback.classList.contains('invalid-feedback')) {
        feedback.textContent = '';
        feedback.style.display = 'none';
    }
}
