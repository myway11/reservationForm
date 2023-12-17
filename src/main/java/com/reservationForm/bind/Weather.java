package com.reservationForm.bind;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlValue;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Weather {
	@XmlValue
	private String weather;
}
