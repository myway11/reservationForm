package com.reservationForm.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.reservationForm.common.MonthLocalDate;
import com.reservationForm.entity.ReservationEntity;
import com.reservationForm.form.AreaSelect;
import com.reservationForm.form.ReservationData;
import com.reservationForm.repository.ReservationRepository;
import com.reservationForm.service.ReservationService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservationController {
	private final ReservationRepository reservationRepository;
	private final ReservationService reservationService;
	private final HttpSession session;

	@GetMapping("/")
	public ModelAndView showForm(ModelAndView mv) {
		if (session.getAttribute("areaSelect") == null) {
			session.setAttribute("areaSelect", new AreaSelect(1));
		}
		List<LocalDate> scheduleList = new ArrayList<>();
		List<ReservationEntity> reservationLists = reservationRepository.findAll();
		for (int i = 0; i <= reservationLists.size(); i++) {
			for (int j = i + 1; j < reservationLists.size(); j++) {
				LocalDate date1 = reservationLists.get(i).getDate().toLocalDateTime().toLocalDate();
				LocalDate date2 = reservationLists.get(j).getDate().toLocalDateTime().toLocalDate();
				if (date1.equals(date2)) {
					scheduleList.add(date1);
				}
			}
		}
		MonthLocalDate localDate = new MonthLocalDate().nowDate(0);
		MonthLocalDate nextMonth = new MonthLocalDate().nowDate(1);
		List<MonthLocalDate> localDates = new ArrayList<MonthLocalDate>(Arrays.asList(localDate, nextMonth));
		System.out.println(localDate.getWeekDateList());
		Map<Integer, Map<LocalDate, String>> weatherMap = reservationService.getWertherList();
		//仮で予定日リスト
		//現在月取得
		//session.setAttribute("scheduleList", scheduleList);
		mv.addObject("areaSelect", (AreaSelect) session.getAttribute("areaSelect"));
		mv.addObject("weatherMap", weatherMap);
		mv.addObject("localDates", localDates);
		mv.addObject("scheduleList", scheduleList);
		mv.setViewName("test");
		System.out.println((AreaSelect) session.getAttribute("areaSelect"));
		return mv;
	}

	@GetMapping("/form/{day}")
	public String form(@PathVariable(name = "day") String day, Model model) {
		LocalDate date = LocalDate.parse(day, DateTimeFormatter.ISO_DATE);

		List<ReservationEntity> reservationEntityList = reservationRepository.findAll();
		List<LocalDateTime> timeList = reservationEntityList.stream()
				.filter(x -> x.getDate().toLocalDateTime().toLocalDate().equals(date))
				.map(x -> x.getDate().toLocalDateTime())
				.collect(Collectors.toList());
		ReservationData reservationData = new ReservationData();
		reservationData.setDate(date);
		session.setAttribute("daytimeList", timeList);
		model.addAttribute("reservationData", reservationData);
		model.addAttribute("daytimeList", timeList);
		return "form";
	}

	@PostMapping("/form/{}")
	public String reservation(@ModelAttribute @Validated ReservationData reservationData, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			List<Integer> daytimeList = (List<Integer>) session.getAttribute("daytimeList");
			model.addAttribute("daytimeList", daytimeList);
			return "form";
		} else {
			reservationRepository.saveAndFlush(reservationData.toEntity());
			session.removeAttribute("daytimeList");
			redirectAttributes.addFlashAttribute("reservationData", reservationData);
			return "redirect:/result";
		}
	}

	@GetMapping("/result")
	public ModelAndView showResult(ModelAndView mv) {
		mv.setViewName("result");
		return mv;
	}

	@GetMapping("/home")
	public ModelAndView home(ModelAndView mv) {
		mv.setViewName("redirect:/");
		return mv;
	}

	@PostMapping("/area")
	public String postMethodName(@ModelAttribute AreaSelect areaSelect) {
		//TODO: process POST request
		session.setAttribute("areaSelect", areaSelect);
		return "redirect:/";
	}

}
