package com.reservationForm.bind;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {
	@XmlElement(name = "Kind", namespace = "http://xml.kishou.go.jp/jmaxml1/body/meteorology1/")
	private List<Kind> kind;
	@XmlElement(name = "Area", namespace = "http://xml.kishou.go.jp/jmaxml1/body/meteorology1/")
	private Area area;

	public List<String> getWeathers() {
		List<String> weatherList = this.getKind().get(0).getProperty().getWeatherPart().getWeatherList().stream()
				.map(weather -> weather.getWeather()).toList();
		return weatherList;
	}
}
