package com.reservationForm.bind;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Entry {
	@XmlElement(name = "content", namespace = "http://www.w3.org/2005/Atom")
	private String content;

	@XmlElement(name = "link", namespace = "http://www.w3.org/2005/Atom")
	private Link link;
}
