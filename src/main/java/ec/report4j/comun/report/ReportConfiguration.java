package ec.report4j.comun.report;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportConfiguration {

	private InputReportFile inputReportFile;
	private OutputReportFile outputReportFile;

	public ReportConfiguration(InputReportFile inputReportFile, OutputReportFile outputReportFile) {
		super();
		this.inputReportFile = inputReportFile;

		this.outputReportFile = outputReportFile;
	}

}
