// ========== VARIABLES GLOBALES ==========
const API_BASE_URL = window.location.origin;
let emailRegistro = '';
let datosEstudianteTemporal = null;
let countdownTimer = null;

// Variables para recuperación de contraseña
let recoveryTimer = null;
let recoveryUserEmail = '';
let recoveryCountdown = 60;
let passwordResetToken = '';

// ========== FUNCIONES DE NAVEGACIÓN (VISTAS) ==========
function mostrarLogin() {
    document.getElementById('login-form').style.display = 'block';
    document.getElementById('registro-form').style.display = 'none';
    document.getElementById('verificacion-form').style.display = 'none';

    document.getElementById('btn-login').classList.add('active');
    document.getElementById('btn-login').classList.remove('inactive');
    document.getElementById('btn-registro').classList.remove('active');
    document.getElementById('btn-registro').classList.add('inactive');

    if (countdownTimer) clearInterval(countdownTimer);
}

function mostrarRegistro() {
    document.getElementById('login-form').style.display = 'none';
    document.getElementById('registro-form').style.display = 'block';
    document.getElementById('verificacion-form').style.display = 'none';

    document.getElementById('btn-login').classList.remove('active');
    document.getElementById('btn-login').classList.add('inactive');
    document.getElementById('btn-registro').classList.add('active');
    document.getElementById('btn-registro').classList.remove('inactive');

    // Cargar catálogos si están vacíos
    if (document.getElementById('escuela').options.length <= 1) {
        cargarEscuelas();
        cargarSemestres();
    }

    if (countdownTimer) clearInterval(countdownTimer);
}

function mostrarVerificacion() {
    document.getElementById('login-form').style.display = 'none';
    document.getElementById('registro-form').style.display = 'none';
    document.getElementById('verificacion-form').style.display = 'block';

    document.getElementById('email-destino').textContent = emailRegistro;
    iniciarCountdown();
}

function volverAlRegistro() {
    mostrarRegistro();
}

// ========== FUNCIONES DE RECUPERACIÓN DE CONTRASEÑA ==========

function mostrarRecuperacionModal() {
    const modal = document.getElementById('recuperar-contrasena-modal');
    const emailInput = document.getElementById('recovery-email');
    const errorMsg = document.getElementById('recovery-email-error');

    if (modal && emailInput) {
        modal.style.display = 'flex';
        emailInput.value = '';
        emailInput.classList.remove('input-error');
        errorMsg.style.display = 'none';

        // Ocultar mensaje de éxito si existe
        const successMsg = document.getElementById('recovery-success');
        if (successMsg) successMsg.style.display = 'none';
    }
}

function cerrarRecuperacionModal() {
    const modal = document.getElementById('recuperar-contrasena-modal');
    if (modal) {
        modal.style.display = 'none';
    }
}

async function enviarCodigoRecuperacion() {
    const emailInput = document.getElementById('recovery-email');
    const errorMsg = document.getElementById('recovery-email-error');
    const sendBtn = document.getElementById('send-recovery');
    const successMsg = document.getElementById('recovery-success');

    if (!emailInput || !errorMsg || !sendBtn) return;

    const email = emailInput.value.trim();

    // Validar email
    if (!isValidEmail(email)) {
        emailInput.classList.add('input-error');
        errorMsg.textContent = 'Por favor ingresa un correo electrónico válido';
        errorMsg.style.display = 'block';
        return;
    }

    // Limpiar errores
    emailInput.classList.remove('input-error');
    errorMsg.style.display = 'none';

    // Deshabilitar botón temporalmente
    const originalText = sendBtn.textContent;
    sendBtn.textContent = 'Enviando...';
    sendBtn.disabled = true;

    try {
        // Usar tu endpoint real para recuperación
        const response = await fetch(`${API_BASE_URL}/api/forgot-password`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                email: email
            })
        });

        if (response.ok) {
            // Guardar email para uso posterior
            recoveryUserEmail = email;

            // Mostrar mensaje de éxito
            if (successMsg) {
                successMsg.textContent = `Se ha enviado un código de verificación a ${email}`;
                successMsg.style.display = 'block';
            }

            // IMPORTANTE: Tu backend usa tokens por URL, no códigos por formulario
            // Muestra mensaje indicando que revise el correo
            mostrarMensaje('Código enviado', 'Revisa tu correo electrónico para encontrar el enlace de recuperación de contraseña.');

            // Cerrar modal después de 2 segundos
            setTimeout(() => {
                cerrarRecuperacionModal();
            }, 2000);

        } else {
            throw new Error('No se pudo enviar el código de recuperación');
        }

    } catch (error) {
        console.error('Error enviando código:', error);
        errorMsg.textContent = 'Error al enviar el código de recuperación';
        errorMsg.style.display = 'block';
        emailInput.classList.add('input-error');
    } finally {
        // Restaurar botón
        sendBtn.textContent = originalText;
        sendBtn.disabled = false;
    }
}

function mostrarMensaje(titulo, mensaje) {
    const messageModal = document.getElementById('recovery-message');
    const messageTitle = document.getElementById('message-title');
    const messageText = document.getElementById('message-text');

    if (messageModal && messageTitle && messageText) {
        messageTitle.textContent = titulo;
        messageText.textContent = mensaje;
        messageModal.style.display = 'flex';
    }
}

function cerrarMensaje() {
    const messageModal = document.getElementById('recovery-message');
    if (messageModal) {
        messageModal.style.display = 'none';
    }
}

// NOTA: Tu backend usa un sistema diferente:
// 1. /api/forgot-password envía un correo con un token
// 2. El usuario hace clic en el enlace del correo
// 3. El enlace lo lleva a una página con el token en la URL
// 4. /api/reset-password/validate valida el token
// 5. /api/reset-password cambia la contraseña

// Por lo tanto, NO necesitas el formulario de código de 6 dígitos
// Ya que tu sistema usa tokens en URLs

// ========== FUNCIONES AUXILIARES ==========

function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function generateVerificationCode() {
    return Math.floor(100000 + Math.random() * 900000).toString();
}

function manejarCodigoInputs() {
    const codeInputs = document.querySelectorAll('.code-input');

    if (!codeInputs) return;

    codeInputs.forEach((input, index) => {
        input.addEventListener('input', function() {
            // Validar que solo sean números
            this.value = this.value.replace(/\D/g, '');

            // Si se ingresó un dígito, pasar al siguiente input
            if (this.value.length === 1 && index < codeInputs.length - 1) {
                codeInputs[index + 1].focus();
            }
        });

        input.addEventListener('keydown', function(e) {
            // Navegación con flechas
            if (e.key === 'ArrowLeft' && index > 0) {
                codeInputs[index - 1].focus();
            } else if (e.key === 'ArrowRight' && index < codeInputs.length - 1) {
                codeInputs[index + 1].focus();
            } else if (e.key === 'Backspace' && !this.value && index > 0) {
                codeInputs[index - 1].focus();
            }
        });
    });
}

// ========== FUNCIONES EXISTENTES (mantenerlas) ==========

async function cargarEscuelas() {
    try {
        const response = await fetch(`${API_BASE_URL}/catalogs/schools`);
        if (response.ok) {
            const schools = await response.json();
            const select = document.getElementById('escuela');

            select.innerHTML = '<option value="">Selecciona una escuela</option>';

            schools.forEach(school => {
                const option = document.createElement('option');
                option.value = school.acronym;
                option.dataset.name = school.name;
                option.dataset.acronym = school.acronym;
                option.textContent = school.acronym;
                select.appendChild(option);
            });
        }
    } catch (error) {
        console.error("Error cargando escuelas:", error);
    }
}

async function cargarCarreras(escuelaNombre) {
    const select = document.getElementById('carrera');
    const planSelect = document.getElementById('plan-estudios');

    select.innerHTML = '<option value="">Selecciona una carrera</option>';
    select.disabled = true;
    planSelect.innerHTML = '<option value="">Primero selecciona escuela</option>';
    planSelect.disabled = true;

    if (!escuelaNombre) return;

    try {
        const response = await fetch(`${API_BASE_URL}/catalogs/careers?SchoolName=${encodeURIComponent(escuelaNombre)}`);
        if (response.ok) {
            const carreras = await response.json();
            carreras.forEach(carrera => {
                const option = document.createElement('option');
                option.value = carrera.acronym;
                option.dataset.name = carrera.name;
                option.dataset.acronym = carrera.acronym;
                option.textContent = carrera.acronym;
                select.appendChild(option);
            });
            select.disabled = false;
        }
    } catch (error) {
        console.error("Error cargando carreras:", error);
    }
}

async function cargarPlanes(escuelaAcronym, carreraAcronym) {
    const select = document.getElementById('plan-estudios');
    select.innerHTML = '<option value="">Selecciona un plan</option>';
    select.disabled = true;

    if (!carreraAcronym || !escuelaAcronym) return;

    try {
        const response = await fetch(`${API_BASE_URL}/catalogs/syllabus?schoolAcronym=${encodeURIComponent(escuelaAcronym)}&careerAcronym=${encodeURIComponent(carreraAcronym)}`);
        if (response.ok) {
            const planes = await response.json();
            planes.forEach(plan => {
                const option = document.createElement('option');
                option.value = plan.code;
                option.textContent = plan.code;
                select.appendChild(option);
            });
            select.disabled = false;
        }
    } catch (error) {
        console.error("Error cargando planes:", error);
    }
}

async function cargarSemestres() {
    try {
        const response = await fetch(`${API_BASE_URL}/catalogs/semesters`);
        if (response.ok) {
            const semestres = await response.json();
            const select = document.getElementById('semestre');
            select.innerHTML = '<option value="">Selecciona un semestre</option>';

            semestres.sort((a, b) => parseInt(a.description) - parseInt(b.description));

            semestres.forEach(sem => {
                const option = document.createElement('option');
                option.value = sem.description;
                option.textContent = "Semestre " + sem.description;
                select.appendChild(option);
            });
        }
    } catch (error) {
        console.error("Error cargando semestres:", error);
    }
}

// ========== INICIO DE SESIÓN (LOGIN) ==========
async function iniciarSesion() {
    const emailInput = document.getElementById('login-email');
    const passwordInput = document.getElementById('login-password');
    const loginBtn = document.getElementById('login-button');
    const originalText = loginBtn.textContent;

    if (!emailInput.value || !passwordInput.value) {
        alert("Por favor ingresa correo y contraseña");
        return;
    }

    loginBtn.disabled = true;
    loginBtn.textContent = "Verificando...";

    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                email: emailInput.value,
                password: passwordInput.value
            })
        });

        if (!response.ok) {
            throw new Error('Credenciales incorrectas o error en el servidor');
        }

        const data = await response.json();

        if (data.flag) {
            console.log("usuario: " + data.userType);
            switch (data.userType) {
                case 'ALUMNO':
                    window.location.href = 'Student/home.html';
                    break;
                case 'ADMINISTRADOR':
                    window.location.href = 'Administrator/home.html';
                    break;
                case 'OPERADOR':
                    window.location.href = 'Operative/home.html';
                    break;
                default:
                    alert("Rol desconocido: " + data.userType);
            }
        } else {
            alert(data.message || "Error al iniciar sesión");
        }

    } catch (error) {
        console.error("Login error:", error);
        alert("Error: " + error.message);
    } finally {
        loginBtn.disabled = false;
        loginBtn.textContent = originalText;
    }
}

// ========== REGISTRO DE ESTUDIANTE ==========
async function iniciarRegistro() {
    const esValido = validarFormularioRegistro();

    if (esValido) {
        const boton = document.getElementById('register-button');
        const textoOriginal = boton.textContent;
        boton.textContent = 'Procesando...';
        boton.disabled = true;

        try {
            emailRegistro = document.getElementById('email').value;

            const isEgresado = document.getElementById('egresado-check').checked;

            const semestreVal = isEgresado ? "1" : document.getElementById('semestre').value;

            datosEstudianteTemporal = {
                email: emailRegistro,
                fLastName: document.getElementById('apellido-paterno').value,
                mLastName: document.getElementById('apellido-materno').value,
                name: document.getElementById('nombre').value,
                password: document.getElementById('password').value,
                confirmPassword: document.getElementById('confirm-password').value,
                enrollment: document.getElementById('boleta').value,
                phone: document.getElementById('phone').value,
                semester: semestreVal,
                graduated: isEgresado,
                schoolName: document.getElementById('escuela').value,
                acronymCareer: document.getElementById('carrera').value,
                syllabusCode: document.getElementById('plan-estudios').value
            };

            console.log('Enviando registro...');

            const response = await fetch(`${API_BASE_URL}/student/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(datosEstudianteTemporal)
            });

            if (!response.ok) {
                const errorText = await response.text();
                try {
                    const errorJson = JSON.parse(errorText);
                    throw new Error(errorJson.message || errorText);
                } catch (e) {
                    throw new Error(errorText || 'Error desconocido');
                }
            }

            mostrarVerificacion();

        } catch (error) {
            console.error('Error registro:', error);
            alert('Error: ' + error.message);
        } finally {
            boton.textContent = textoOriginal;
            boton.disabled = false;
        }
    }
}

// ========== VERIFICACIÓN DE CÓDIGO ==========
async function verificarCodigo() {
    const codigo = document.getElementById('codigo-verificacion').value;
    const boton = document.getElementById('verify-button');

    if (!codigo || codigo.length !== 6) {
        alert("Ingresa un código de 6 dígitos");
        return;
    }

    boton.disabled = true;
    boton.textContent = "Verificando...";

    try {
        const payload = {
            email: emailRegistro,
            code: codigo
        };

        const response = await fetch(`${API_BASE_URL}/auth/confirm-email`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error('Código incorrecto o expirado.');
        }

        alert('¡Cuenta verificada! Inicia sesión.');
        mostrarLogin();

    } catch (error) {
        alert(error.message);
    } finally {
        boton.disabled = false;
        boton.textContent = "Verificar y Completar Registro";
    }
}

// ========== UTILIDADES (Timer, Validaciones) ==========
function iniciarCountdown() {
    let tiempoRestante = 60;
    const countdownElement = document.getElementById('countdown');
    const resendButton = document.getElementById('resend-button');
    const timerText = document.getElementById('timer-text');

    countdownElement.textContent = tiempoRestante;
    resendButton.disabled = true;
    timerText.style.display = 'block';

    countdownTimer = setInterval(() => {
        tiempoRestante--;
        countdownElement.textContent = tiempoRestante;
        if (tiempoRestante <= 0) {
            clearInterval(countdownTimer);
            resendButton.disabled = false;
            timerText.style.display = 'none';
        }
    }, 1000);
}

async function reenviarCodigo() {
    const boton = document.getElementById('resend-button');
    boton.textContent = 'Enviando...';
    boton.disabled = true;
    await new Promise(resolve => setTimeout(resolve, 1000));
    alert('¡Código reenviado! Revisa tu correo.');
    boton.textContent = 'Reenviar código';
    iniciarCountdown();
}

function validarFormularioRegistro() {
    const nombre = document.getElementById('nombre');
    const paterno = document.getElementById('apellido-paterno');
    const materno = document.getElementById('apellido-materno');
    const boleta = document.getElementById('boleta');
    const escuela = document.getElementById('escuela');
    const carrera = document.getElementById('carrera');
    const plan = document.getElementById('plan-estudios');
    const email = document.getElementById('email');
    const pass = document.getElementById('password');
    const confirmPass = document.getElementById('confirm-password');
    const telefono = document.getElementById('phone');

    let esValido = true;

    const setError = (id, msg) => {
        const el = document.getElementById(id);
        el.classList.add('input-error');
        const err = document.getElementById(id + '-error');
        if(err) { err.textContent = msg; err.style.display = 'block'; }
        esValido = false;
    };
    const clearError = (id) => {
        const el = document.getElementById(id);
        el.classList.remove('input-error');
        const err = document.getElementById(id + '-error');
        if(err) err.style.display = 'none';
    };

    if(!nombre.value.trim()) setError('nombre', 'Requerido'); else clearError('nombre');
    if(!paterno.value.trim()) setError('apellido-paterno', 'Requerido'); else clearError('apellido-paterno');
    if(!materno.value.trim()) setError('apellido-materno', 'Requerido'); else clearError('apellido-materno');
    if(!/^\d{10}$/.test(boleta.value)) setError('boleta', 'Debe tener 10 dígitos'); else clearError('boleta');

    if(!escuela.value) { escuela.classList.add('input-error'); esValido=false; } else escuela.classList.remove('input-error');
    if(!carrera.value) setError('carrera', 'Selecciona carrera'); else clearError('carrera');
    if(!plan.value) setError('plan-estudios', 'Selecciona plan'); else clearError('plan-estudios');

    const isEgresado = document.getElementById('egresado-check').checked;
    if(!isEgresado) {
        const sem = document.getElementById('semestre');
        if(!sem.value) setError('semestre', 'Selecciona semestre'); else clearError('semestre');
    }

    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if(!emailRegex.test(email.value)) setError('email', 'Correo inválido'); else clearError('email');

    if(pass.value.length < 6) setError('password', 'Mínimo 6 caracteres'); else clearError('password');
    if(pass.value !== confirmPass.value) setError('confirm-password', 'No coinciden'); else clearError('confirm-password');
    if(!/^\d{10}$/.test(telefono.value)) setError('phone', 'Debe tener 10 dígitos'); else clearError('phone');

    return esValido;
}

// ========== LISTENERS AL CARGAR ==========
document.addEventListener('DOMContentLoaded', function() {
    mostrarLogin();

    // Botones Navegación
    document.getElementById('btn-login').addEventListener('click', mostrarLogin);
    document.getElementById('btn-registro').addEventListener('click', mostrarRegistro);

    // Checkbox Egresado
    const egresadoCheck = document.getElementById('egresado-check');
    const semesterGroup = document.getElementById('semester-group');
    const semesterSelect = document.getElementById('semestre');

    if (egresadoCheck) {
        egresadoCheck.addEventListener('change', function() {
            if (this.checked) {
                semesterGroup.classList.add('hidden');
                semesterSelect.value = '';
            } else {
                semesterGroup.classList.remove('hidden');
            }
        });
    }

    // Combos en Cascada
    const escuelaSelect = document.getElementById('escuela');
    if(escuelaSelect) {
        escuelaSelect.addEventListener('change', function() {
            const selectedOption = this.options[this.selectedIndex];
            if(selectedOption.value) {
                const nombre = selectedOption.dataset.name;
                cargarCarreras(nombre);
            }
        });
    }

    const carreraSelect = document.getElementById('carrera');
    if(carreraSelect) {
        carreraSelect.addEventListener('change', function() {
            const selectedOption = this.options[this.selectedIndex];
            if(selectedOption.value) {
                const carreraAcronym = selectedOption.dataset.acronym;
                const escuelaSelectEl = document.getElementById('escuela');
                const escuelaSelectedOption = escuelaSelectEl.options[escuelaSelectEl.selectedIndex];
                const escuelaAcronym = escuelaSelectedOption.dataset.acronym;
                cargarPlanes(escuelaAcronym, carreraAcronym);
            }
        });
    }

    // Botones Acción
    document.getElementById('login-button').addEventListener('click', (e) => {
        e.preventDefault();
        iniciarSesion();
    });

    document.getElementById('register-button').addEventListener('click', (e) => {
        e.preventDefault();
        iniciarRegistro();
    });

    document.getElementById('verify-button').addEventListener('click', (e) => {
        e.preventDefault();
        verificarCodigo();
    });

    // ========== LISTENERS PARA RECUPERACIÓN DE CONTRASEÑA ==========

    // Enlace "¿Olvidaste tu contraseña?"
    const olvidoContrasenaLink = document.getElementById('olvido-contrasena-link');
    if (olvidoContrasenaLink) {
        olvidoContrasenaLink.addEventListener('click', function(e) {
            e.preventDefault();
            mostrarRecuperacionModal();
        });
    }

    // Botones del modal de recuperación
    const closeModalBtn = document.getElementById('close-modal');
    const cancelRecoveryBtn = document.getElementById('cancel-recovery');
    if (closeModalBtn) {
        closeModalBtn.addEventListener('click', cerrarRecuperacionModal);
    }
    if (cancelRecoveryBtn) {
        cancelRecoveryBtn.addEventListener('click', cerrarRecuperacionModal);
    }

    // Botón enviar código de recuperación
    const sendRecoveryBtn = document.getElementById('send-recovery');
    if (sendRecoveryBtn) {
        sendRecoveryBtn.addEventListener('click', enviarCodigoRecuperacion);
    }

    // Configurar inputs de código
    manejarCodigoInputs();

    // Botones de formulario de verificación
    const closeVerificationBtn = document.getElementById('close-verification');
    const backToRecoveryBtn = document.getElementById('back-to-recovery');
    const verifyRecoveryBtn = document.getElementById('verify-recovery-button');
    const recoveryResendBtn = document.getElementById('recovery-resend-button');

    if (closeVerificationBtn) {
        closeVerificationBtn.addEventListener('click', cerrarVerificacionRecuperacion);
    }
    if (backToRecoveryBtn) {
        backToRecoveryBtn.addEventListener('click', function() {
            cerrarVerificacionRecuperacion();
            mostrarRecuperacionModal();
        });
    }
    if (verifyRecoveryBtn) {
        verifyRecoveryBtn.addEventListener('click', verificarCodigoRecuperacion);
    }
    if (recoveryResendBtn) {
        recoveryResendBtn.addEventListener('click', reenviarCodigoRecuperacion);
    }

    // Botones de formulario de nueva contraseña
    const closeNewPasswordBtn = document.getElementById('close-new-password');
    const backToVerificationBtn = document.getElementById('back-to-verification');
    const saveNewPasswordBtn = document.getElementById('save-new-password');

    if (closeNewPasswordBtn) {
        closeNewPasswordBtn.addEventListener('click', cerrarNuevaContrasenaForm);
    }
    if (backToVerificationBtn) {
        backToVerificationBtn.addEventListener('click', function() {
            cerrarNuevaContrasenaForm();
            mostrarVerificacionRecuperacion();
        });
    }
    if (saveNewPasswordBtn) {
        saveNewPasswordBtn.addEventListener('click', guardarNuevaContrasena);
    }

    // Botones del modal de mensajes
    const closeMessageBtn = document.getElementById('close-message');
    const acceptMessageBtn = document.getElementById('accept-message');

    if (closeMessageBtn) {
        closeMessageBtn.addEventListener('click', cerrarMensaje);
    }
    if (acceptMessageBtn) {
        acceptMessageBtn.addEventListener('click', cerrarMensaje);
    }

    // Cerrar modales al hacer clic fuera
    window.addEventListener('click', function(e) {
        const recuperacionModal = document.getElementById('recuperar-contrasena-modal');
        const verificacionModal = document.getElementById('verificacion-recuperacion-form');
        const nuevaContrasenaModal = document.getElementById('nueva-contrasena-form');
        const messageModal = document.getElementById('recovery-message');

        if (recuperacionModal && e.target === recuperacionModal) {
            cerrarRecuperacionModal();
        }
        if (verificacionModal && e.target === verificacionModal) {
            cerrarVerificacionRecuperacion();
        }
        if (nuevaContrasenaModal && e.target === nuevaContrasenaModal) {
            cerrarNuevaContrasenaForm();
        }
        if (messageModal && e.target === messageModal) {
            cerrarMensaje();
        }
    });
});