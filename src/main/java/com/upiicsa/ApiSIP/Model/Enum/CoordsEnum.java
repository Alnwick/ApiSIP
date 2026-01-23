package com.upiicsa.ApiSIP.Model.Enum;

import lombok.Getter;

public enum CoordsEnum {
    PATERNO(190, 650),
    MATERNO(315, 650),
    NOMBRE(440, 650),
    CARRERA(140, 610),
    PRACTICAS(227, 585),
    ESTANCIAS(491, 585),
    BOLETA(140,543),
    SEMESTRE_6(340, 542),
    SEMESTRE_7(378, 542),
    SEMESTRE_8(415, 542),
    SEMESTRE_9(451, 542),
    PASANTE(508, 542),
    CALLE(140, 520),
    NUMERO(405, 520),
    COLONIA(55, 480),
    CODIGO_P(210, 480),
    ENTIDAD(240, 480),
    TELEFONO_ALUMNO(405, 480),
    CORREO_ALUMNO(180, 445),
    PLAN_EST(490, 445),
    RAZON_SOCIAL(140, 395),
    DOMICILIO(135, 360),
    TELEFONO_EMPRESA(135, 332),
    EXTESION(400, 332),
    FAX(135, 308),
    CORREO_EMPRESA(400, 308),
    SECTOR_PUBLICO(176, 282),
    SECTOR_PRIVADO(310, 282),
    RESPONSABLE(135, 257),
    PUESTO_RESPONSABLE(135, 220),
    DIRIGE_CARTA(270, 182),
    PUESTO_ALUMNO(140, 135);

    @Getter
    private int coordsX;
    @Getter
    private int coordsY;

    CoordsEnum(int coordsX, int coordsY) {
        this.coordsX = coordsX;
        this.coordsY = coordsY;
    }
}
