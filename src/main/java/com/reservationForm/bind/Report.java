package com.reservationForm.bind;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Report", namespace = "http://xml.kishou.go.jp/jmaxml1/")
public class Report {
	@XmlElement(name = "Body", namespace = "http://xml.kishou.go.jp/jmaxml1/body/meteorology1/")
	private Body body;
}
