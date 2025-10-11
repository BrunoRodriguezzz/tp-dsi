package ar.edu.utn.frba.dds.client.controllers;

import ar.edu.utn.frba.dds.client.dtos.EstadisticaCategoriaDTO;
import ar.edu.utn.frba.dds.client.dtos.EstadisticaSolicitudesDTO;
import ar.edu.utn.frba.dds.client.services.EstadisticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/estadisticas")
public class EstadisticaController {
    @Autowired
    private EstadisticaService estadisticaService;

    @GetMapping
    public String getEstadisticasGenerales(Model model){
        model.addAttribute("titulo", "Estadisticas y Análisis");

        EstadisticaCategoriaDTO categoriaTop = this.estadisticaService.getCategoriaTop();
        model.addAttribute("categoriaTop", categoriaTop.getCategoriasConHechos().entrySet().iterator().next().getKey());
        EstadisticaSolicitudesDTO cantSpam = this.estadisticaService.getCantSolicitudesSpam();
        model.addAttribute("cantSpam", cantSpam.getSolicitudes_spam());
        // TODO: faltan estos valores del servicio de estadísticas
        model.addAttribute("totalHechos",1456);
        model.addAttribute("totalColecciones",89);
        return "estadisticasAnalisis/estadisticas";
    }
}

/*
<!DOCTYPE html>
<html lang="en">


<body>
    <div class="container-fluid px-4 py-4">
        <div class="mb-4">
            <h1>Estadísticas y Análisis</h1>
            <p>Dashboards interactivos para analizar patrones y tendencias en los datos de MetaMapa</p>
        </div>

        <!-- Tabs -->
        <div class="stats-tabs-wrapper">
            <ul class="nav nav-tabs stats-tabs" id="statisticsTabs" role="tablist">
                <li class="nav-item flex-fill" role="presentation">
                    <button class="nav-link active w-100" type="button" data-bs-toggle="tab"
                        data-bs-target="#coleccion-provincia" role="tab" aria-controls="coleccion-provincia"
                        aria-selected="true">
                        <i class="bi bi-geo-alt me-2"></i>
                        <span class="d-none d-sm-inline">Colección por Provincia</span>
                        <span class="d-sm-none">Col/Prov</span>
                    </button>
                </li>
                <li class="nav-item flex-fill" role="presentation">
                    <button class="nav-link w-100" type="button" data-bs-toggle="tab"
                        data-bs-target="#categoria-general" role="tab" aria-controls="categoria-general"
                        aria-selected="false">
                        <i class="bi bi-bullseye me-2"></i>
                        <span class="d-none d-sm-inline">Categorías</span>
                        <span class="d-sm-none">Cat</span>
                    </button>
                </li>
                <li class="nav-item flex-fill" role="presentation">
                    <button class="nav-link w-100" type="button" data-bs-toggle="tab"
                        data-bs-target="#categoria-provincia" role="tab" aria-controls="categoria-provincia"
                        aria-selected="false">
                        <i class="bi bi-geo-alt me-2"></i>
                        <span class="d-none d-sm-inline">Cat por Provincia</span>
                        <span class="d-sm-none">Cat/Prov</span>
                    </button>
                </li>
                <li class="nav-item flex-fill" role="presentation">
                    <button class="nav-link w-100" type="button" data-bs-toggle="tab" data-bs-target="#categoria-hora"
                        role="tab" aria-controls="categoria-hora" aria-selected="false">
                        <i class="bi bi-clock me-2"></i>
                        <span class="d-none d-sm-inline">Cat por Hora</span>
                        <span class="d-sm-none">Cat/Hora</span>
                    </button>
                </li>
                <li class="nav-item flex-fill" role="presentation">
                    <button class="nav-link w-100" type="button" data-bs-toggle="tab" data-bs-target="#solicitudes-spam"
                        role="tab" aria-controls="solicitudes-spam" aria-selected="false">
                        <i class="bi bi-exclamation-triangle me-2"></i>
                        <span class="d-none d-sm-inline">Solicitudes</span>
                        <span class="d-sm-none">Spam</span>
                    </button>
                </li>
            </ul>
        </div>
        <!-- Tab Content -->
        <div class="tab-content mt-4" id="statisticsTabContent">

            <!-- Tab 1: Colección por Provincia -->
            <div class="tab-pane fade show active" id="coleccion-provincia" role="tabpanel">
                <div class="card">
                    <div
                        class="card-header d-flex flex-column flex-sm-row justify-content-between align-items-start align-items-sm-center">
                        <h5 class="card-title mb-3 mb-sm-0 d-flex align-items-center">
                            <i class="bi bi-geo-alt me-2"></i>
                            Distribución de Hechos por Provincia
                        </h5>
                        <!-- DROPDOWN -->
                        <div class="dropdown">
                            <button class="btn btn-outline-secondary dropdown-toggle" type="button"
                                id="coleccionDropdown" data-bs-toggle="dropdown" aria-expanded="false"
                                aria-haspopup="true">
                                <!-- Acá va el valor del elemento seleccionado por JS -->
                                <span id="selectedColeccion"></span>
                            </button>
                            <!-- DROPDOWN MENU -->
                            <ul class="dropdown-menu" aria-labelledby="coleccionDropdown" role="menu">
                                <li>
                                    <button class="dropdown-item" type="button" data-coleccion="Infraestructura Urbana"
                                        role="menuitem" aria-current="true">
                                        Infraestructura Urbana
                                    </button>
                                </li>
                                <li>
                                    <button class="dropdown-item" type="button" data-coleccion="Seguridad Ciudadana"
                                        role="menuitem">
                                        Seguridad Ciudadana
                                    </button>
                                </li>
                                <li>
                                    <button class="dropdown-item" type="button" data-coleccion="Medio Ambiente"
                                        role="menuitem">
                                        Medio Ambiente
                                    </button>
                                </li>
                                <li>
                                    <button class="dropdown-item" type="button" data-coleccion="Salud Pública"
                                        role="menuitem">
                                        Salud Pública
                                    </button>
                                </li>
                                <li>
                                    <button class="dropdown-item" type="button" data-coleccion="Educación"
                                        role="menuitem">
                                        Educación
                                    </button>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="chart-container">
                            <canvas id="provinciaChart"></canvas>
                        </div>
                        <div class="result-box bg-primary bg-opacity-10">
                            <p class="text-primary mb-0">
                                <!-- Se actualizan con JS-->
                                <strong>Resultado:</strong> En la colección "<span id="resultColeccion"></span>",
                                la mayor cantidad de hechos se concentra en <strong id="resultProvincia"></strong>
                                con <strong id="resultCantidad"></strong> hechos reportados.
                            </p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Tab 2: Categorías General -->
            <div class="tab-pane fade" id="categoria-general" role="tabpanel">
                <div class="card">
                    <div
                        class="card-header d-flex flex-column flex-sm-row justify-content-between align-items-start align-items-sm-center">
                        <h5 class="card-title mb-3 mb-sm-0 d-flex align-items-center">
                            <i class="bi bi-bar-chart me-2"></i>
                            Hechos por Categoría
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-lg-8">
                                <div class="chart-container">
                                    <canvas id="categoriaChart"></canvas>
                                </div>
                            </div>
                            <div class="col-lg-4">
                                <h6 class="fw-bold mb-3">Ranking de Categorías</h6>
                                <div
                                    class="d-flex justify-content-between align-items-center p-3 bg-light rounded mb-2">
                                    <div class="d-flex align-items-center">
                                        <div class="rounded-circle me-3"
                                            style="width: 16px; height: 16px; background-color: #3B82F6;"></div>
                                        <span class="fw-medium">Infraestructura</span>
                                    </div>
                                    <span class="badge bg-primary">342 hechos</span>
                                </div>
                                <div
                                    class="d-flex justify-content-between align-items-center p-3 bg-light rounded mb-2">
                                    <div class="d-flex align-items-center">
                                        <div class="rounded-circle me-3"
                                            style="width: 16px; height: 16px; background-color: #EF4444;"></div>
                                        <span class="fw-medium">Seguridad</span>
                                    </div>
                                    <span class="badge bg-danger">298 hechos</span>
                                </div>
                                <div
                                    class="d-flex justify-content-between align-items-center p-3 bg-light rounded mb-2">
                                    <div class="d-flex align-items-center">
                                        <div class="rounded-circle me-3"
                                            style="width: 16px; height: 16px; background-color: #10B981;"></div>
                                        <span class="fw-medium">Medio Ambiente</span>
                                    </div>
                                    <span class="badge bg-success">234 hechos</span>
                                </div>
                                <div
                                    class="d-flex justify-content-between align-items-center p-3 bg-light rounded mb-2">
                                    <div class="d-flex align-items-center">
                                        <div class="rounded-circle me-3"
                                            style="width: 16px; height: 16px; background-color: #F59E0B;"></div>
                                        <span class="fw-medium">Salud</span>
                                    </div>
                                    <span class="badge bg-warning">189 hechos</span>
                                </div>
                                <div class="d-flex justify-content-between align-items-center p-3 bg-light rounded">
                                    <div class="d-flex align-items-center">
                                        <div class="rounded-circle me-3"
                                            style="width: 16px; height: 16px; background-color: #8B5CF6;"></div>
                                        <span class="fw-medium">Transporte</span>
                                    </div>
                                    <span class="badge" style="background-color: #8B5CF6;">156 hechos</span>
                                </div>
                            </div>
                        </div>
                        <div class="result-box bg-success bg-opacity-10">
                            <p class="text-success mb-0">
                                <strong>Resultado:</strong> La categoría con mayor cantidad de hechos reportados es
                                <strong>Infraestructura</strong> con 342 hechos, seguida por Seguridad con 298
                                hechos.
                            </p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Tab 3: Categoría por Provincia -->
            <div class="tab-pane fade" id="categoria-provincia" role="tabpanel">
                <div class="card">
                    <div
                        class="card-header d-flex flex-column flex-sm-row justify-content-between align-items-start align-items-sm-center">
                        <h5 class="card-title mb-3 mb-sm-0 d-flex align-items-center">
                            <i class="bi bi-geo-alt me-2"></i>
                            Hechos por Provincia - Categoría Específica
                        </h5>
                        <div class="dropdown">
                            <button class="btn btn-outline-secondary dropdown-toggle" type="button"
                                id="categoriaProvinciaDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                <span id="selectedCategoriaP">Infraestructura</span>
                            </button>
                            <ul class="dropdown-menu" aria-labelledby="categoriaProvinciaDropdown" role="menu">
                                <li>
                                    <button class="dropdown-item" type="button" data-categoria-p="Infraestructura"
                                        role="menuitem" aria-current="true">
                                        Infraestructura
                                    </button>
                                </li>
                                <li>
                                    <button class="dropdown-item" type="button" data-categoria-p="Seguridad"
                                        role="menuitem">
                                        Seguridad
                                    </button>
                                </li>
                                <li>
                                    <button class="dropdown-item" type="button" data-categoria-p="Medio Ambiente"
                                        role="menuitem">
                                        Medio Ambiente
                                    </button>
                                </li>
                                <li>
                                    <button class="dropdown-item" type="button" data-categoria-p="Salud"
                                        role="menuitem">
                                        Salud
                                    </button>
                                </li>
                                <li>
                                    <button class="dropdown-item" type="button" data-categoria-p="Transporte"
                                        role="menuitem">
                                        Transporte
                                    </button>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="chart-container">
                            <canvas id="categoriaProvinciaChart"></canvas>
                        </div>
                        <div class="result-box bg-success bg-opacity-10">
                            <p class="text-success mb-0">
                                <strong>Resultado:</strong> Para la categoría "<span
                                    id="resultCategoriaP">Infraestructura</span>",
                                la provincia con mayor cantidad de hechos es <strong id="resultProvinciaP"></strong> con
                                <strong id="resultCantidadP"></strong> hechos reportados.
                            </p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Tab 4: Categoría por Hora -->
            <div class="tab-pane fade" id="categoria-hora" role="tabpanel">
                <div class="card">
                    <div
                        class="card-header d-flex flex-column flex-sm-row justify-content-between align-items-start align-items-sm-center">
                        <h5 class="card-title mb-3 mb-sm-0 d-flex align-items-center">
                            <i class="bi bi-clock me-2"></i>
                            Distribución Horaria de Hechos
                        </h5>
                        <div class="dropdown">
                            <button class="btn btn-outline-secondary dropdown-toggle" type="button"
                                id="categoriaHoraDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                <span id="selectedCategoriaH">Infraestructura</span>
                            </button>
                            <ul class="dropdown-menu" aria-labelledby="categoriaHoraDropdown" role="menu">
                                <li>
                                    <button class="dropdown-item" type="button" data-categoria-h="Infraestructura"
                                        role="menuitem" aria-current="true">
                                        Infraestructura
                                    </button>
                                </li>
                                <li>
                                    <button class="dropdown-item" type="button" data-categoria-h="Seguridad"
                                        role="menuitem">
                                        Seguridad
                                    </button>
                                </li>
                                <li>
                                    <button class="dropdown-item" type="button" data-categoria-h="Medio Ambiente"
                                        role="menuitem">
                                        Medio Ambiente
                                    </button>
                                </li>
                                <li>
                                    <button class="dropdown-item" type="button" data-categoria-h="Salud"
                                        role="menuitem">
                                        Salud
                                    </button>
                                </li>
                                <li>
                                    <button class="dropdown-item" type="button" data-categoria-h="Transporte"
                                        role="menuitem">
                                        Transporte
                                    </button>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="chart-container">
                            <canvas id="horaChart"></canvas>
                        </div>
                        <div class="result-box bg-warning bg-opacity-10">
                            <p class="text-warning-emphasis mb-0">
                                <strong>Resultado:</strong> Los hechos de la categoría "<span
                                    id="resultCategoriaH">Infraestructura</span>"
                                ocurren con mayor frecuencia a las <strong id="resultHoraMaxima">18:00 horas</strong>
                                con
                                <strong id="resultCantidadH">134</strong> reportes, seguido por las
                                <strong id="resultHoraSegunda">16:00 horas</strong> con <strong
                                    id="resultCantidadSegundaH">92</strong> reportes.
                            </p>
                        </div>
                    </div>
                </div>
            </div>


            <!-- Tab 5: Solicitudes Spam -->
            <div class="tab-pane fade" id="solicitudes-spam" role="tabpanel">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title d-flex align-items-center mb-0">
                            <i class="bi bi-exclamation-triangle me-2"></i>
                            Análisis de Solicitudes de Eliminación
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-lg-6">
                                <div class="chart-container">
                                    <canvas id="spamChart"></canvas>
                                </div>
                            </div>
                            <div class="col-lg-6">
                                <h6 class="fw-bold mb-3">Clasificación de Solicitudes</h6>
                                <div
                                    class="d-flex justify-content-between align-items-center p-3 bg-light rounded mb-3">
                                    <div class="d-flex align-items-center">
                                        <div class="rounded-circle me-3"
                                            style="width: 24px; height: 24px; background-color: #EF4444;"></div>
                                        <span class="fw-medium">Spam</span>
                                    </div>
                                    <span class="badge bg-danger">145 solicitudes</span>
                                </div>
                                <div
                                    class="d-flex justify-content-between align-items-center p-3 bg-light rounded mb-3">
                                    <div class="d-flex align-items-center">
                                        <div class="rounded-circle me-3"
                                            style="width: 24px; height: 24px; background-color: #10B981;"></div>
                                        <span class="fw-medium">No Spam</span>
                                    </div>
                                    <span class="badge bg-success">255 solicitudes</span>
                                </div>
                                <div class="p-3 bg-secondary bg-opacity-10 rounded">
                                    <h6 class="fw-bold mb-2">Resumen Total</h6>
                                    <p class="small mb-1">Total de solicitudes procesadas: <strong>400</strong></p>
                                    <p class="small mb-0">Porcentaje de spam: <strong>36.3%</strong></p>
                                </div>
                            </div>
                        </div>
                        <div class="result-box bg-danger bg-opacity-10">
                            <p class="text-danger mb-0">
                                <strong>Resultado:</strong> De un total de 400 solicitudes de eliminación,
                                <strong>145 solicitudes (36.3%)</strong> corresponden a spam, mientras que 255
                                solicitudes
                                (63.7%)
                                son reportes legítimos que requieren revisión.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
    <script src="js/estadisticas.js"></script>
</body>

</html>
 */
