package com.reservationForm.bind;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class MeteorologicalInfos {
	@XmlElement(name = "TimeSeriesInfo", namespace = "http://xml.kishou.go.jp/jmaxml1/body/meteorology1/")
	private TimeSeriesInfo timeSeriesInfo;
}
