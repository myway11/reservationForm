package com.reservationForm.common;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MonthLocalDate {
	private List<List<LocalDate>> weekDateList;
	private int firstWeekInt;
	private int month;

	public MonthLocalDate nowDate(int m) {
		this.weekDateList = new ArrayList<>();
		List<LocalDate> localDates = new ArrayList<>();
		LocalDate now = LocalDate.now();
		now = now.plusMonths(m);
		month = now.getMonthValue();
		now = now.withDayOfMonth(1);
		this.firstWeekInt = now.getDayOfWeek().getValue();
		System.out.print(firstWeekInt);
		LocalDate date = now;
		for (int i = 0; i < now.lengthOfMonth() + firstWeekInt; i++) {
			if (localDates.size() < 7) {
				if (i < firstWeekInt) {
					localDates.add(null);
				} else {
					localDates.add(date.withDayOfMonth(i + 1 - firstWeekInt));
				}
			} else {
				weekDateList.add(localDates);
				localDates = new ArrayList<LocalDate>();
				localDates.add(date.withDayOfMonth(i + 1 - firstWeekInt));
			}
		}
		if (!localDates.isEmpty()) {
			while (localDates.size() < 7) {
				localDates.add(null);
			}
			weekDateList.add(localDates);
		}
		return this;
	}
}