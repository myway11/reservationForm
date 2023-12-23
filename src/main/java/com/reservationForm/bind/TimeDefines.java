package com.reservationForm.bind;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class TimeDefines {
	@XmlElement(name = "TimeDefine", namespace = "http://xml.kishou.go.jp/jmaxml1/body/meteorology1/")
	List<TimeDefine> timeDefine;

	public List<LocalDate> localDateList() {
		if (this.timeDefine != null || this.timeDefine.size() != 0) {
			List<LocalDate> localDateList = new ArrayList<LocalDate>();
			localDateList = this.timeDefine.stream().map(time -> OffsetDateTime.parse(time.getDateTime()).toLocalDate())
					.toList();
			return localDateList;
		} else {
			return null;
		}
	}

	public List<LocalDate> local() {
		if (this.timeDefine != null || this.timeDefine.size() != 0) {
			List<LocalDate> localDateList = new ArrayList<LocalDate>();
			localDateList = this.timeDefine.stream()
					.map(time -> OffsetDateTime.parse(time.getDateTime()).toLocalDate())
					.collect(Collectors.toList());
			return localDateList;
		} else {
			return null;
		}
	}
}
