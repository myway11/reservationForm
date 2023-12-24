package com.reservationForm.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "reservations")
@Where(clause = "before='N'")
@Data
public class ReservationEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "subject")
	private String subject;

	@Column(name = "purpose")
	private String purpose;

	@Column(name = "request")
	private String request;

	@Column(name = "date")
	private Timestamp date;

	@Column(name = "before")
	private String before;
}
