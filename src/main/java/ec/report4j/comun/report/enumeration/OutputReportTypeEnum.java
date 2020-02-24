package ec.report4j.comun.report.enumeration;

import lombok.Getter;

public enum OutputReportTypeEnum {

	XLS(".xls"), DOC(".doc"), HTML(".html"), PDF(".pdf");

	@Getter
	private String extension;

	private OutputReportTypeEnum(String extension) {
		this.extension = extension;
	}

}
