package ec.eppec.comun.reporte.excel;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ec.eppec.comun.reporte.excel.modelo.Employee;
import ec.report4j.comun.report.Report;

public class ExcelReportTest {
    private static final String TEMPLATE_NAME = "empleados.xls";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public final void testExportBytes() throws IOException, Throwable {
        byte[] resultado = Report.getExcelInstance().assignTemplate(getClass().getClassLoader().getResourceAsStream(TEMPLATE_NAME), getData(100)).buildReport()
                .exportBytes();
        assertNotNull(resultado);
    }

    @Test
    public final void testExportOS() throws IOException, Throwable {
        OutputStream os = Report.getExcelInstance().assignTemplate(getClass().getClassLoader().getResourceAsStream(TEMPLATE_NAME), getData(100)).buildReport()
                .exportOS();
        assertNotNull(os);
    }

    @Test
    public final void testExportBase64() throws IOException, Throwable {
        String cadena = Report.getExcelInstance().assignTemplate(getClass().getClassLoader().getResourceAsStream(TEMPLATE_NAME), getData(100)).buildReport()
                .exportBase64();
        assertNotNull(cadena);
    }

    @Test
    public final void testExportPdf() throws IOException, Throwable {
        byte[] pdf = Report.getExcelInstance().assignTemplate(getClass().getClassLoader().getResourceAsStream(TEMPLATE_NAME), getData(100)).buildReport()
                .exportPdf();
        File archivo = saveFile(pdf, "report-excel-pdf", ".pdf");
        assertNotNull(archivo);

    }

    @Test
    public final void testExportHtml() throws IOException, Throwable {
        byte[] html = Report.getExcelInstance().assignTemplate(getClass().getClassLoader().getResourceAsStream(TEMPLATE_NAME), getData(100)).buildReport()
                .exportHtml();
        File archivo = saveFile(html, "report-excel-html", ".html");
        assertNotNull(archivo);
    }

    private static Map<String, Object> getData(int numero) {
        List<Employee> employees = new ArrayList<>();

        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.set(1992, 7, 21);
        employees.add(new Employee("Rajeev Singh", "rajeev@example.com", dateOfBirth.getTime(), 1200000.0));
        dateOfBirth.set(1965, 10, 15);
        employees.add(new Employee("Thomas cook", "thomas@example.com", dateOfBirth.getTime(), 1500000.25));
        dateOfBirth.set(1987, 4, 18);
        employees.add(new Employee("Steve Maiden", "steve@example.com", dateOfBirth.getTime(), 1800000.100));
        Map<String, Object> parametros = new HashMap<>();
        for (int i = 0; i < numero; i++) {
            employees.add(employees.get(0));
        }
        parametros.put("fechaCorte", new Date());
        parametros.put("empleados", employees);
        return parametros;
    }

    private File saveFile(byte[] contenido, String nombre, String extension) throws FileNotFoundException, IOException {
        File archivo = Files.createTempFile(nombre, extension).toFile();
        try (FileOutputStream out = new FileOutputStream(archivo)) {
            out.write(contenido);
        }
        return archivo;
    }
}
