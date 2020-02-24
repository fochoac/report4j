package ec.report4j.comun.report;

import lombok.Getter;
import lombok.Setter;

public class ReportConfiguration {
	@Getter
	@Setter
	private InputReportFile inputReportFile;
	@Getter
	@Setter
	private OutputReportFile outputReportFile;

	public ReportConfiguration(InputReportFile inputReportFile, OutputReportFile outputReportFile) {
		this.inputReportFile = inputReportFile;
		this.outputReportFile = outputReportFile;
	}

}
