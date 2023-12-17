package com.reservationForm.bind;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class WeatherPart {
	@XmlElement(name = "Weather", namespace = "http://xml.kishou.go.jp/jmaxml1/elementBasis1/")
	private List<Weather> weatherList;
}
