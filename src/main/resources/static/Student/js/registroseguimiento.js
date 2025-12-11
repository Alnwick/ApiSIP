// Funciones para actualizar los nombres de los archivos seleccionados y mostrar el icono de borrado
document.getElementById('cartaInput').addEventListener('change', function(e) {
    const fileName = e.target.files.length ? e.target.files[0].name : 'No se ha seleccionado ningún archivo';
    document.getElementById('cartaName').textContent = fileName;
    document.getElementById('cartaDelete').style.display = e.target.files.length ? 'block' : 'none';
});

document.getElementById('reporteInput').addEventListener('change', function(e) {
    const fileName = e.target.files.length ? e.target.files[0].name : 'No se ha seleccionado ningún archivo';
    document.getElementById('reporteName').textContent = fileName;
    document.getElementById('reporteDelete').style.display = e.target.files.length ? 'block' : 'none';
});

document.getElementById('cartaTerminoInput').addEventListener('change', function(e) {
    const fileName = e.target.files.length ? e.target.files[0].name : 'No se ha seleccionado ningún archivo';
    document.getElementById('cartaTerminoName').textContent = fileName;
    document.getElementById('cartaTerminoDelete').style.display = e.target.files.length ? 'block' : 'none';
});

document.getElementById('informeInput').addEventListener('change', function(e) {
    const fileName = e.target.files.length ? e.target.files[0].name : 'No se ha seleccionado ningún archivo';
    document.getElementById('informeName').textContent = fileName;
    document.getElementById('informeDelete').style.display = e.target.files.length ? 'block' : 'none';
});

// Funciones para eliminar archivos
document.getElementById('cartaDelete').addEventListener('click', function() {
    document.getElementById('cartaInput').value = '';
    document.getElementById('cartaName').textContent = 'No se ha seleccionado ningún archivo';
    this.style.display = 'none';
});

document.getElementById('reporteDelete').addEventListener('click', function() {
    document.getElementById('reporteInput').value = '';
    document.getElementById('reporteName').textContent = 'No se ha seleccionado ningún archivo';
    this.style.display = 'none';
});

document.getElementById('cartaTerminoDelete').addEventListener('click', function() {
    document.getElementById('cartaTerminoInput').value = '';
    document.getElementById('cartaTerminoName').textContent = 'No se ha seleccionado ningún archivo';
    this.style.display = 'none';
});

document.getElementById('informeDelete').addEventListener('click', function() {
    document.getElementById('informeInput').value = '';
    document.getElementById('informeName').textContent = 'No se ha seleccionado ningún archivo';
    this.style.display = 'none';
});

// Simular envío de formulario
document.querySelector('.btn-guardar').addEventListener('click', function() {
    alert('Documentos guardados correctamente. Serán revisados por el operativo.');
});