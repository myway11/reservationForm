package com.reservationForm.service;

import java.io.StringReader;
import java.util.ArrayList;
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

	public Map<Integer, Map<Integer, String>> getWertherList() {
		String url = "https://www.data.jma.go.jp/developer/xml/data/20231219014928_0_VPFW50_200000.xml";
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
		List<Integer> AreaList = timeSeriesInfo.getItemList().stream().map(item -> areaNum(item.getArea().getName()))
				.collect(Collectors.toList());
		List<Integer> local = timeSeriesInfo.getTimeDefines().local();
		Map<Integer, Map<Integer, String>> localWeather = new HashMap<Integer, Map<Integer, String>>();
		for (int i = 0; i < AreaList.size(); i++) {
			Map<Integer, String> dateToWeather = new HashMap<Integer, String>();
			//ここのリストを画像のDIRに
			List<String> weatherList = timeSeriesInfo.getItemList().get(i).getWeathers();
			List<String> weatherImageList = getWeatherImages(weatherList);
			System.out.println(weatherImageList);
			for (int j = 0; j < local.size(); j++) {
				dateToWeather.put(local.get(j), weatherImageList.get(j));
			}
			localWeather.put(AreaList.get(i), dateToWeather);
			//System.out.println(weatherList);
			//System.out.println(weatherImageList);
			//System.out.println(localWeather);
		}
		System.out.println(localWeather);
		return localWeather;
	}

	public int areaNum(String area) {
		if (area != null) {
			if (area.equals("中部・南部")) {
				return 2;
			} else {
				return 1;
			}
		} else {
			return 0;
		}
	}

	public List<String> getWeatherImages(List<String> weatherList) {
		String dir = "/images/";
		String cloudy_rainy = "cloudy_rainy.png";
		String cloudy_snow = "cloudy_snow.png";
		String cloudy_sunny = "cloudy_sunny.png";
		//仮データ
		String sunny_cloudy = "cloudy_sunny.png";
		String rainy_cloudy = "rainy.png";
		List<String> weatherImageList = new ArrayList<String>();
		weatherList.stream().forEach(weather -> {
			switch (weather) {
			case "くもり一時雨":
				weatherImageList.add(dir + cloudy_rainy);
				break;
			case "くもり時々晴れ":
				weatherImageList.add(dir + cloudy_sunny);
				break;
			case "晴れ時々くもり":
				weatherImageList.add(dir + sunny_cloudy);
				break;
			case "雨後くもり":
				weatherImageList.add(dir + rainy_cloudy);
				break;
			case "くもり一時雪か雨":
				weatherImageList.add(dir + cloudy_snow);
				break;
			case "雨時々止む":
				weatherImageList.add(dir + rainy_cloudy);
				break;
			}
		});
		return weatherImageList;
	}
}
