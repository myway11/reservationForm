package com.reservationForm.form;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.reservationForm.entity.ReservationEntity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationData {
	@NotBlank
	private String name;

	@NotBlank
	@Pattern(regexp = "[0-9]{8}")
	//適当な数字を入力した場合のバリデーションも追加する
	private String birthday;

	@Min(value = 0)
	private int subject;

	@Min(value = 0)
	private int purpose;

	private String request;

	private LocalDate date;

	@Min(value = 0)
	private int time;

	public ReservationEntity toEntity() {
		ReservationEntity reservationEntity = new ReservationEntity();
		LocalDateTime dateTime = date.atTime(time, 0);
		Timestamp timestamp = Timestamp.valueOf(dateTime);

		reservationEntity.setDate(timestamp);
		reservationEntity.setName(this.name);
		reservationEntity.setRequest(this.request);
		reservationEntity.setSubject("仮データ");
		reservationEntity.setPurpose("仮データ");
		return reservationEntity;
	}
}
