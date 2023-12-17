package com.reservationForm.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.Data;

@Data
public class MonthData {
	int month;
	List<List<Integer>> dayList = new ArrayList<>();
	int firstWeek;

	public MonthData() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		this.month = c.get(Calendar.MONTH) + 1;
		int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		this.firstWeek = c.get(Calendar.DAY_OF_WEEK);

		List<Integer> d = new ArrayList<>();
		for (int i = 1; i < lastDay + firstWeek; i++) {
			//1つのリストのサイズが7より小さい
			if (d.size() < 7) {
				if (i < firstWeek) {
					d.add(0);
				} else {
					d.add(i + 1 - firstWeek);
				}
			} else {
				dayList.add(d);
				d = new ArrayList<>();
				d.add(i + 1 - firstWeek);
			}
		}
		if (!d.isEmpty()) {
			if (d.size() < 7) {
				while (d.size() < 7) {
					d.add(0);
				}
			}
			dayList.add(d);
		}
	}
}
