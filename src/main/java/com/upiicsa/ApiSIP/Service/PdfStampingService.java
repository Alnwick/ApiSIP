package com.upiicsa.ApiSIP.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.upiicsa.ApiSIP.Model.Company;
import com.upiicsa.ApiSIP.Model.Enum.CoordsEnum;
import com.upiicsa.ApiSIP.Model.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PdfStampingService {

    @Value("${storage.certificate.pdf}")
    private String SCR;

    @Value("${storage.save.certificate}")
    private  String DEST;

    public void stampTextOnPdf(Student student, Company company) {
        String companyAddress = company.getAddress().getStreet() + " " + company.getAddress().getNumber() + " " +
                company.getAddress().getColony()  + " " + company.getAddress().getZipCode() + " "
                + company.getAddress().getState().getName();

        int[] semesterCoords = getSemesterCoords(student);
        int[] procedureCoords = getProcedureCoords(student);
        int[] sectorCoords = getSectorCoords(company);

        try {
            PdfReader pdfReader = new PdfReader(SCR);
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(DEST + student.getEnrollment() + ".pdf"));

            PdfContentByte content = pdfStamper.getOverContent(1);

            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

            content.beginText();
            content.setFontAndSize(bf, 10);
            content.setColorFill(BaseColor.BLUE);

            //Student Data
            writeText(content, student.getFLastName(),
                    CoordsEnum.PATERNO.getCoordsX(), CoordsEnum.PATERNO.getCoordsY());
            writeText(content, student.getMLastName(),
                    CoordsEnum.MATERNO.getCoordsX(), CoordsEnum.MATERNO.getCoordsY());
            writeText(content, student.getName(),
                    CoordsEnum.NOMBRE.getCoordsX(), CoordsEnum.NOMBRE.getCoordsY());
            writeText(content, student.getOffer().getCareer().getName(),
                    CoordsEnum.CARRERA.getCoordsX(), CoordsEnum.CARRERA.getCoordsY());
            writeText(content, "X", procedureCoords[0], procedureCoords[1]);
            writeText(content, student.getEnrollment(),
                    CoordsEnum.BOLETA.getCoordsX(), CoordsEnum.BOLETA.getCoordsY());
            writeText(content, "X", semesterCoords[0], semesterCoords[1]);

            writeText(content, student.getAddress().getStreet(),
                    CoordsEnum.CALLE.getCoordsX(),  CoordsEnum.CALLE.getCoordsY());
            writeText(content, student.getAddress().getNumber(),
                    CoordsEnum.NUMERO.getCoordsX(), CoordsEnum.NUMERO.getCoordsY());
            writeText(content, student.getAddress().getColony(),
                    CoordsEnum.COLONIA.getCoordsX(),  CoordsEnum.COLONIA.getCoordsY());
            writeText(content, student.getAddress().getZipCode(),
                    CoordsEnum.CODIGO_P.getCoordsX(),  CoordsEnum.CODIGO_P.getCoordsY());
            writeText(content, student.getAddress().getState().getName(),
                    CoordsEnum.ENTIDAD.getCoordsX(),  CoordsEnum.ENTIDAD.getCoordsY());
            writeText(content, student.getPhone(),
                    CoordsEnum.TELEFONO_ALUMNO.getCoordsX(), CoordsEnum.TELEFONO_ALUMNO.getCoordsY());
            writeText(content, student.getEmail(),
                    CoordsEnum.CORREO_ALUMNO.getCoordsX(),   CoordsEnum.CORREO_ALUMNO.getCoordsY());
            writeText(content, student.getOffer().getSyllabus().getCode(),
                    CoordsEnum.PLAN_EST.getCoordsX(),  CoordsEnum.PLAN_EST.getCoordsY());

            //Company Data
            writeText(content, company.getName(),
                    CoordsEnum.RAZON_SOCIAL.getCoordsX(), CoordsEnum.RAZON_SOCIAL.getCoordsY());
            writeText(content, companyAddress,
                    CoordsEnum.DOMICILIO.getCoordsX(), CoordsEnum.DOMICILIO.getCoordsY());
            writeText(content, company.getPhone(),
                    CoordsEnum.TELEFONO_EMPRESA.getCoordsX(), CoordsEnum.TELEFONO_EMPRESA.getCoordsY());
            writeText(content, company.getExtension(),
                    CoordsEnum.EXTESION.getCoordsX(), CoordsEnum.EXTESION.getCoordsY());
            writeText(content, company.getFax(),
                    CoordsEnum.FAX.getCoordsX(), CoordsEnum.FAX.getCoordsY());
            writeText(content, company.getEmail(),
                    CoordsEnum.CORREO_EMPRESA.getCoordsX(),  CoordsEnum.CORREO_EMPRESA.getCoordsY());
            writeText(content, "X", sectorCoords[0], sectorCoords[1]);
            writeText(content, company.getDegreeSupervisor() + " " + company.getSupervisor(),
                    CoordsEnum.RESPONSABLE.getCoordsX(), CoordsEnum.RESPONSABLE.getCoordsY());
            writeText(content, company.getPositionSupervisor(),
                    CoordsEnum.PUESTO_RESPONSABLE.getCoordsX(),  CoordsEnum.PUESTO_RESPONSABLE.getCoordsY());
            writeText(content, "A quien??",
                    CoordsEnum.DIRIGE_CARTA.getCoordsX(),  CoordsEnum.DIRIGE_CARTA.getCoordsY());
            writeText(content, company.getPositionStudent(),
                    CoordsEnum.PUESTO_ALUMNO.getCoordsX(),   CoordsEnum.PUESTO_ALUMNO.getCoordsY());

            content.endText();

            pdfStamper.close();
            pdfReader.close();
            System.out.println("Text included and saved");
        } catch (IOException | com.itextpdf.text.DocumentException e){
            e.printStackTrace();
        }
    }
    private void writeText(PdfContentByte content, String text, float x, float y) {
        if(text != null && !text.trim().isEmpty()){
            content.showTextAligned(
                    PdfContentByte.ALIGN_LEFT,
                    text,
                    x,
                    y,
                    0
            );
        }
    }

    private int[] getSemesterCoords(Student student){
        int[] coords = new int[2];

        if(student.isGraduate()){
            coords[0] = CoordsEnum.PASANTE.getCoordsX();
            coords[1] = CoordsEnum.PASANTE.getCoordsY();
        }else {
            switch (student.getSemester().getDescription()){
                case "6to":
                    coords[0] = CoordsEnum.SEMESTRE_6.getCoordsX();
                    coords[1] = CoordsEnum.SEMESTRE_6.getCoordsY();
                    break;
                case "7to":
                    coords[0] = CoordsEnum.SEMESTRE_7.getCoordsX();
                    coords[1] = CoordsEnum.SEMESTRE_7.getCoordsY();
                    break;
                case "8to":
                    coords[0] = CoordsEnum.SEMESTRE_8.getCoordsX();
                    coords[1] = CoordsEnum.SEMESTRE_8.getCoordsY();
                    break;
                case "9to":
                    coords[0] = CoordsEnum.SEMESTRE_9.getCoordsX();
                    coords[1] = CoordsEnum.SEMESTRE_9.getCoordsY();
                    break;
                default:
                    coords[0] = -1;
                    coords[1] = -1;
            }
        }
        return coords;
    }

    private int[] getProcedureCoords(Student student){
        String career = student.getOffer().getCareer().getAcronym();
        int[] coords = new int[2];
        
        if("ING_INFORMATICA".equals(career) || "CIE_INFORMATICA".equals(career)){
            coords[0] = CoordsEnum.PRACTICAS.getCoordsX();
            coords[1] = CoordsEnum.PRACTICAS.getCoordsY();
        } else if ("ING_TRANSPORTE".equals(career) || "ING_FERROVIARIA".equals(career) ||
        "ING_INDUSTRIAL".equals(career) || "ADM_INDUSTRIAL".equals(career)) {
            coords[0] = CoordsEnum.PASANTE.getCoordsX();
            coords[1] = CoordsEnum.PASANTE.getCoordsY();
        } else {
            coords[0] = -1;
            coords[1] = -1;
        }
        return coords;
    }

    private int[] getSectorCoords(Company company){
        int[] coords = new int[2];

        if(company.getSector().equals("PUBLICO")){
            coords[0] = CoordsEnum.SECTOR_PUBLICO.getCoordsX();
            coords[1] = CoordsEnum.SECTOR_PUBLICO.getCoordsY();
        }else if(company.getSector().equals("PRIVADO")) {
            coords[0] = CoordsEnum.SECTOR_PRIVADO.getCoordsX();
            coords[1] = CoordsEnum.SECTOR_PRIVADO.getCoordsY();
        }else {
            coords[0] = -1;
            coords[1] = -1;
        }
        return coords;
    }
}
