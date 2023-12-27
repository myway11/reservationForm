package com.reservationForm.controller;

import java.sql.Timestamp;
import java.time.Instant;
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

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservationController {
	private final ReservationRepository reservationRepository;
	private final ReservationService reservationService;
	private final HttpSession session;

	@PostConstruct
	public void init() {
		Timestamp time = Timestamp.from(Instant.now());
		List<ReservationEntity> entitie = reservationRepository.findByDateLessThanEqual(time);
		entitie.stream().forEach(e -> {
			e.setBefore("Y");
		});
		reservationRepository.saveAllAndFlush(entitie);
	}

	@GetMapping("/")
	public ModelAndView showForm(ModelAndView mv) {
		if (session.getAttribute("areaSelect") == null) {
			session.setAttribute("areaSelect", new AreaSelect(1));
		}
		List<ReservationEntity> reservationLists = reservationRepository.findAll();
		List<LocalDate> scheduleList = reservationService.createScheduleList(reservationLists);

		MonthLocalDate localDate = new MonthLocalDate().nowDate(0);
		MonthLocalDate nextMonth = new MonthLocalDate().nowDate(1);
		List<MonthLocalDate> localDates = new ArrayList<MonthLocalDate>(Arrays.asList(localDate, nextMonth));

		Map<Integer, Map<LocalDate, String>> weatherMap = reservationService.getWertherList();
		System.out.println(localDates.size());
		mv.addObject("areaSelect", (AreaSelect) session.getAttribute("areaSelect"));
		mv.addObject("weatherMap", weatherMap);
		mv.addObject("localDates", localDates);
		mv.addObject("scheduleList", scheduleList);
		mv.addObject("nowdate", LocalDate.now());
		mv.setViewName("reservationForm");
		return mv;
	}

	@GetMapping("/form/{day}")
	public String form(@PathVariable(name = "day") String day, Model model) {
		//urlからLocaldateを取得
		LocalDate date = LocalDate.parse(day, DateTimeFormatter.ISO_DATE);
		//予約リストをDBから取得し上記と一致する日付をLocalDateTimeでリスト化
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

	//エラーがあった場合に戻すためこのURLとしている
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
