package com.reservationForm.service;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.reservationForm.bind.Report;
import com.reservationForm.bind.TimeSeriesInfo;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import reactor.core.publisher.Mono;

@Service
public class ReservationService {

	public Map<Integer, Map<LocalDate, String>> getWertherList() {
		String url = getUrl();
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
		List<LocalDate> local = timeSeriesInfo.getTimeDefines().local();
		Map<Integer, Map<LocalDate, String>> localWeather = new HashMap<Integer, Map<LocalDate, String>>();
		for (int i = 0; i < AreaList.size(); i++) {
			Map<LocalDate, String> dateToWeather = new HashMap<LocalDate, String>();
			//天気テロップ番号のリストを取得
			List<Integer> weatherCodeList = timeSeriesInfo.getItemList().get(i).getWeatheCodes();

			List<String> weatherImageList = getWeatherImages(weatherCodeList);
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
				return 1;
			} else {
				return 2;
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

	public String getUrl() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		String url = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse("https://www.data.jma.go.jp/developer/xml/feed/regular.xml");
			NodeList nodeList = doc.getElementsByTagName("id");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element = (Element) nodeList.item(i);
				String id = element.getTextContent();
				if (id.contains("VPFW50_200000")) {
					url = id;
				}
			}
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return url;
	}
}
