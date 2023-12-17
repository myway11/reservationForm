package com.reservationForm.service;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.reservationForm.bind.Report;
import com.reservationForm.bind.TimeSeriesInfo;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import reactor.core.publisher.Mono;

@Service
public class ReservationService {

	public Map<String, Map<LocalDate, String>> getWertherList() {
		String url = "https://www.data.jma.go.jp/developer/xml/data/20231211074402_0_VPFW50_200000.xml";
		WebClient client = WebClient.create();
		Mono<Report> report = client.get().uri(url).retrieve().bodyToMono(String.class).flatMap(xml -> {
			try {
				JAXBContext context = JAXBContext.newInstance(Report.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				Report report1 = (Report) unmarshaller.unmarshal(new StringReader(xml));
				return Mono.just(report1);
			} catch (JAXBException e) {
				// TODO 自動生成された catch ブロック
				return Mono.error(e);
			}
		});
		TimeSeriesInfo timeSeriesInfo = report.block().getBody().getMeteorologicalInfos().get(0).getTimeSeriesInfo();
		List<String> AreaList = timeSeriesInfo.getItemList().stream().map(item -> item.getArea().getName())
				.collect(Collectors.toList());
		List<LocalDate> local = timeSeriesInfo.getTimeDefines().localDateList();
		Map<String, Map<LocalDate, String>> localWeather = new HashMap<String, Map<LocalDate, String>>();
		for (int i = 0; i < AreaList.size(); i++) {
			Map<LocalDate, String> dateToWeather = new HashMap<LocalDate, String>();
			List<String> weatherList = timeSeriesInfo.getItemList().get(i).getWeathers();
			for (int j = 0; j < local.size(); j++) {
				dateToWeather.put(local.get(j), weatherList.get(j));
			}
			localWeather.put(AreaList.get(i), dateToWeather);
		}
		return localWeather;
	}
}
