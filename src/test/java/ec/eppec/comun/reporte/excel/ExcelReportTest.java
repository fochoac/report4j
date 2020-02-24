package ec.eppec.comun.reporte.excel;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ec.eppec.comun.reporte.excel.modelo.Employee;
import ec.report4j.comun.report.InputReportFile;
import ec.report4j.comun.report.OutputReportFile;
import ec.report4j.comun.report.Report;
import ec.report4j.comun.report.ReportConfiguration;
import ec.report4j.comun.report.enumeration.OutputReportTypeEnum;
import ec.report4j.comun.report.excel.ExcelReport;

public class ExcelReportTest {
	private static final String TEMPLATE_NAME = "empleados.xls";
	private static final Path TEMPORAL_FOLDER = Paths.get(System.getProperty("java.io.tmpdir"));
	private InputReportFile inputReportFile;

	@Before
	public void setUp() throws Exception {
		inputReportFile = new InputReportFile(getClass().getClassLoader().getResourceAsStream(TEMPLATE_NAME),
				getData(100));
	}

	@Test
	public final void testExportPdf() throws IOException, Throwable {

		OutputReportFile outputReportFile = new OutputReportFile(TEMPORAL_FOLDER, "testPdf", OutputReportTypeEnum.PDF);
		ReportConfiguration configuration = new ReportConfiguration(inputReportFile, outputReportFile);
		Report excelReport = new ExcelReport(configuration);
		OutputReportFile report = excelReport.buildReport();
		report.saveFile();
		assertNotNull(report.getFileBase64());
	}

	@Test
	public final void testExportXls() throws IOException, Throwable {

		OutputReportFile outputReportFile = new OutputReportFile(TEMPORAL_FOLDER, "testXls", OutputReportTypeEnum.XLS);
		ReportConfiguration configuration = new ReportConfiguration(inputReportFile, outputReportFile);
		Report excelReport = new ExcelReport(configuration);
		OutputReportFile report = excelReport.buildReport();
		report.saveFile();
		assertNotNull(report.getFileBase64());
	}

	@Test
	public final void testExportHtml() throws IOException, Throwable {

		OutputReportFile outputReportFile = new OutputReportFile(TEMPORAL_FOLDER, "testHtml",
				OutputReportTypeEnum.HTML);
		ReportConfiguration configuration = new ReportConfiguration(inputReportFile, outputReportFile);
		Report excelReport = new ExcelReport(configuration);
		OutputReportFile report = excelReport.buildReport();
		report.saveFile();
		assertNotNull(report.getFileBase64());
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

}
