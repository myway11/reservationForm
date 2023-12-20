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
		//バインドした値を取得する為の起点を取得
		TimeSeriesInfo timeSeriesInfo = report.block().getBody().getMeteorologicalInfos().get(0).getTimeSeriesInfo();
		//地域をリストで取得
		List<Integer> AreaList = timeSeriesInfo.getItemList().stream().map(item -> areaNum(item.getArea().getName()))
				.collect(Collectors.toList());
		//日付取得
		List<Integer> local = timeSeriesInfo.getTimeDefines().local();
		Map<Integer, Map<Integer, String>> localWeather = new HashMap<Integer, Map<Integer, String>>();
		for (int i = 0; i < AreaList.size(); i++) {
			Map<Integer, String> dateToWeather = new HashMap<Integer, String>();
			//天気テロップ番号のリストを取得
			List<Integer> weatherCodeList = timeSeriesInfo.getItemList().get(i).getWeatheCodes();

			List<String> weatherImageList = getWeatherImages(weatherCodeList);
			System.out.println(weatherImageList);
			for (int j = 0; j < local.size(); j++) {
				dateToWeather.put(local.get(j), weatherImageList.get(j));
			}
			localWeather.put(AreaList.get(i), dateToWeather);
			//System.out.println(weatherList);
			//System.out.println(weatherImageList);
			//System.out.println(localWeather);
		}
		//System.out.println();
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

	public List<String> getWeatherImages(List<Integer> weatherCodeList) {
		List<String> weatherImageList = new ArrayList<String>();
		weatherCodeList.stream().forEach(code -> {
			weatherImageList.add("/weatherImage/" + code + ".png");
		});
		return weatherImageList;
	}
}
