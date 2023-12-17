package com.reservationForm.bind;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "feed", namespace = "http://www.w3.org/2005/Atom")
public class Feed {
	@XmlElement(name = "entry", namespace = "http://www.w3.org/2005/Atom")
	private List<Entry> entryList;
}
