package ar.edu.utn.frba.dds.servicioAutenticacion.domain.models.dimensiones;

import java.time.LocalDateTime;
import java.time.LocalTime;

public enum HoraDelDia {
    H00(LocalTime.of(0, 0)) {
        @Override
        public String getValue() {
            return "00:00";
        }
    },
    H01(LocalTime.of(1, 0)) {
        @Override
        public String getValue() {
            return "01:00";
        }
    },
    H02(LocalTime.of(2, 0)) {
        @Override
        public String getValue() {
            return "02:00";
        }
    },
    H03(LocalTime.of(3, 0)) {
        @Override
        public String getValue() {
            return "03:00";
        }
    },
    H04(LocalTime.of(4, 0)) {
        @Override
        public String getValue() {
            return "04:00";
        }
    },
    H05(LocalTime.of(5, 0)) {
        @Override
        public String getValue() {
            return "05:00";
        }
    },
    H06(LocalTime.of(6, 0)) {
        @Override
        public String getValue() {
            return "06:00";
        }
    },
    H07(LocalTime.of(7, 0)) {
        @Override
        public String getValue() {
            return "07:00";
        }
    },
    H08(LocalTime.of(8, 0)) {
        @Override
        public String getValue() {
            return "08:00";
        }
    },
    H09(LocalTime.of(9, 0)) {
        @Override
        public String getValue() {
            return "09:00";
        }
    },
    H10(LocalTime.of(10, 0)) {
        @Override
        public String getValue() {
            return "10:00";
        }
    },
    H11(LocalTime.of(11, 0)) {
        @Override
        public String getValue() {
            return "11:00";
        }
    },
    H12(LocalTime.of(12, 0)) {
        @Override
        public String getValue() {
            return "12:00";
        }
    },
    H13(LocalTime.of(13, 0)) {
        @Override
        public String getValue() {
            return "13:00";
        }
    },
    H14(LocalTime.of(14, 0)) {
        @Override
        public String getValue() {
            return "14:00";
        }
    },
    H15(LocalTime.of(15, 0)) {
        @Override
        public String getValue() {
            return "15:00";
        }
    },
    H16(LocalTime.of(16, 0)) {
        @Override
        public String getValue() {
            return "16:00";
        }
    },
    H17(LocalTime.of(17, 0)) {
        @Override
        public String getValue() {
            return "17:00";
        }
    },
    H18(LocalTime.of(18, 0)) {
        @Override
        public String getValue() {
            return "18:00";
        }
    },
    H19(LocalTime.of(19, 0)) {
        @Override
        public String getValue() {
            return "19:00";
        }
    },
    H20(LocalTime.of(20, 0)) {
        @Override
        public String getValue() {
            return "20:00";
        }
    },
    H21(LocalTime.of(21, 0)) {
        @Override
        public String getValue() {
            return "21:00";
        }
    },
    H22(LocalTime.of(22, 0)) {
        @Override
        public String getValue() {
            return "22:00";
        }
    },
    H23(LocalTime.of(23, 0)) {
        @Override
        public String getValue() {
            return "23:00";
        }
    };

    private final LocalTime hora;

    HoraDelDia(LocalTime hora) {
        this.hora = hora;
    }

    public LocalTime getHora() {
        return hora;
    }

    public abstract String getValue();

    public static HoraDelDia de(LocalDateTime time) {
        String horaStr = String.format("H%02d", time.getHour());
        return valueOf(horaStr);
    }
}
