document.addEventListener('DOMContentLoaded', function() {
    const dropArea = document.getElementById('drop-area');
    const fileInput = document.getElementById('multimediaInput');
    const previewContainer = document.getElementById('preview-container');

    if (!dropArea || !fileInput || !previewContainer) {
        // Elementos necesarios no encontrados; no hacemos nada
        return;
    }

    // DataTransfer para manipular los archivos en memoria
    let dataTransfer = new DataTransfer();


    // --- EVENTOS DRAG & DROP ---

    // Prevenir comportamientos por defecto
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
        dropArea.addEventListener(eventName, preventDefaults, false);
    });

    function preventDefaults(e) {
        e.preventDefault();
        e.stopPropagation();
    }

    // Efectos visuales al arrastrar
    ['dragenter', 'dragover'].forEach(eventName => {
        dropArea.addEventListener(eventName, () => dropArea.classList.add('border-primary', 'bg-white'));
    });

    ['dragleave', 'drop'].forEach(eventName => {
        dropArea.addEventListener(eventName, () => dropArea.classList.remove('border-primary', 'bg-white'));
    });

    // Manejar la caída de archivos (DROP)
    dropArea.addEventListener('drop', (e) => {
        const dt = e.dataTransfer;
        const newFiles = Array.from(dt.files);
        handleFiles(newFiles);
    });

    // --- EVENTO SELECCIÓN MANUAL (CLICK) ---
    // Al hacer click en el label, el navegador dispara el click del input automáticamente.
    // Solo necesitamos escuchar el cambio en el input.
    fileInput.addEventListener('change', function() {
        const newFiles = Array.from(this.files);
        handleFiles(newFiles);
    });

    // --- LÓGICA CENTRAL ---

    function handleFiles(files) {
        files.forEach(file => {
            // Validar duplicados (opcional)
            const existe = Array.from(dataTransfer.files).some(f => f.name === file.name && f.size === file.size);
            if (!existe) {
                dataTransfer.items.add(file);
            }
        });
        updateView();
    }

    function updateView() {
        // 1. Actualizar el input real con los datos acumulados
        fileInput.files = dataTransfer.files;

        // 2. Limpiar y reconstruir la vista previa
        previewContainer.innerHTML = '';

        Array.from(dataTransfer.files).forEach((file, index) => {
            const item = document.createElement('div');
            item.className = 'list-group-item d-flex justify-content-between align-items-center mb-2 border shadow-sm';

            // Icono o Imagen
            let icon = '<span style="font-size: 1.5rem;">📄</span>';
            if (file.type.startsWith('image/')) {
                const imgUrl = URL.createObjectURL(file);
                icon = `<img src="${imgUrl}" style="width: 40px; height: 40px; object-fit: cover; border-radius: 4px;" alt="preview">`;
            }

            item.innerHTML = `
                    <div class="d-flex align-items-center overflow-hidden gap-3">
                        ${icon}
                        <div style="min-width: 0;">
                            <span class="fw-bold d-block text-truncate" title="${file.name}">${file.name}</span>
                            <small class="text-muted">${(file.size / 1024 / 1024).toFixed(2)} MB</small>
                        </div>
                    </div>
                    <button type="button" class="btn btn-outline-danger btn-sm remove-file" data-index="${index}" title="Eliminar">
                        <span aria-hidden="true">&times;</span>
                    </button>
                `;
            previewContainer.appendChild(item);
        });

        // Reconectar eventos de eliminar
        document.querySelectorAll('.remove-file').forEach(btn => {
            btn.addEventListener('click', function() {
                const index = parseInt(this.dataset.index);
                removeItem(index);
            });
        });
    }

    function removeItem(index) {
        const newDataTransfer = new DataTransfer();
        Array.from(dataTransfer.files).forEach((file, i) => {
            if (i !== index) newDataTransfer.items.add(file);
        });
        dataTransfer = newDataTransfer;
        updateView();
    }

    // Drag and drop listo
});