package ar.edu.utn.frba.dds.servicioEstadisticas.domain.models;

import java.time.LocalTime;

public enum HoraDelDia {
    H00(LocalTime.of(0, 0)),
    H01(LocalTime.of(1, 0)),
    H02(LocalTime.of(2, 0)),
    H03(LocalTime.of(3, 0)),
    H04(LocalTime.of(4, 0)),
    H05(LocalTime.of(5, 0)),
    H06(LocalTime.of(6, 0)),
    H07(LocalTime.of(7, 0)),
    H08(LocalTime.of(8, 0)),
    H09(LocalTime.of(9, 0)),
    H10(LocalTime.of(10, 0)),
    H11(LocalTime.of(11, 0)),
    H12(LocalTime.of(12, 0)),
    H13(LocalTime.of(13, 0)),
    H14(LocalTime.of(14, 0)),
    H15(LocalTime.of(15, 0)),
    H16(LocalTime.of(16, 0)),
    H17(LocalTime.of(17, 0)),
    H18(LocalTime.of(18, 0)),
    H19(LocalTime.of(19, 0)),
    H20(LocalTime.of(20, 0)),
    H21(LocalTime.of(21, 0)),
    H22(LocalTime.of(22, 0)),
    H23(LocalTime.of(23, 0));

    private final LocalTime hora;

    HoraDelDia(LocalTime hora) {
        this.hora = hora;
    }

    public LocalTime getHora() {
        return hora;
    }

    public static HoraDelDia de(LocalTime time) {
        return HoraDelDia.valueOf("H" + String.format("%02d", time.getHour()));
    }
}
