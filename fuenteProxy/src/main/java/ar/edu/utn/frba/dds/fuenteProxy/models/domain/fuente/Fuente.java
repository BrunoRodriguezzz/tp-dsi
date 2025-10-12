package ar.edu.utn.frba.dds.fuenteProxy.models.domain.fuente;

import ar.edu.utn.frba.dds.fuenteProxy.converters.tipoFuenteConverter;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.enums.Origen;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.enums.TipoFuenteEnum;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.fuente.impl.APICatedra;
import ar.edu.utn.frba.dds.fuenteProxy.models.domain.fuente.impl.InstanciaMetaMapa;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputColeccionDTO;
import ar.edu.utn.frba.dds.fuenteProxy.models.dtos.input.InputHecho;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Fuente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean activo; // baja logica

    @Transient
    private TipoFuente tipoFuente;

    @Column(nullable = false)
    @Convert(converter = tipoFuenteConverter.class)
    private TipoFuenteEnum tipoFuenteEnum;

    @Column()
    private String nombre;

    @Column()
    private String ruta;

    @Column
    private LocalDateTime ultimaConsulta;

    public Fuente(TipoFuenteEnum tipoFuente, String nombre, String ruta) {
        this.tipoFuenteEnum = tipoFuente;
        this.nombre = nombre;
        this.ruta = ruta;
        this.ultimaConsulta = LocalDateTime.now();
        this.contruirTipoFuente();
    }

    public Flux<InputHecho> getAllHechos() {
        return tipoFuente.getAll().map(inputHecho -> {
            inputHecho.setId_fuente(this.id);
            inputHecho.setOrigen(Origen.PROXY);
            return inputHecho;
        });
    }

    public Flux<InputColeccionDTO> getAllColecciones() {
        return tipoFuente.getAllColecciones();
    }

    public Flux<InputHecho> getNuevos(LocalDateTime date) {
        return this.tipoFuente.getNuevos(date);
    }

    @PostLoad
    private void contruirTipoFuente() {
        switch (this.tipoFuenteEnum) {
            case APICATEDRA -> {
                APICatedra tipo = new APICatedra(this.ruta);
                this.tipoFuente = tipo;
            }
            case INSTANCIAMETAMAPA -> {
                InstanciaMetaMapa tipo = new InstanciaMetaMapa(this.ruta);
                this.tipoFuente = tipo;
            }
            default -> {}
        }
    }
}
