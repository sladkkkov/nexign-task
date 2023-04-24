package ru.sladkkov.cdr.service.datagenerator.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.sladkkov.cdr.service.datagenerator.AbonentNumberGenerator;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс генерации номеров телефона.
 *
 * @author Danila Sladkov
 */
@Component
@RequiredArgsConstructor
public class AbonentNumberGeneratorImpl implements AbonentNumberGenerator {

    @Value("${randommer.api.key}")
    private String randommerApiKey;

    private static final String RANDOMMER_GET_TELEPHONE_URL = "https://randommer.io/api/Phone/Generate";
    /**
     * Поле - параметр countryCode для запроса телефонов определенного региона.
     */
    @Value("${randommer.api.country_code}")
    private String countryCode;
    /**
     * Поле - параметр countNumber для запроса, разного количества номеров.
     */
    @Value("${randommer.api.count}")
    private int countNumber;
    private final RestTemplate restTemplate;
    /**
     * Поле - хранящее список номеров. Знаю, что плохо хранить состояние, но это необходимо, для генерации Cdr и Abonent,
     * с совпадением номеров.
     */
    private List<String> numbers;

    @PostConstruct
    public void init() {
        numbers = generateAbonentNumber();
    }

    public List<String> getNumbers() {
        return numbers;
    }

    private static List<String> formatResponse(ResponseEntity<String> responseEntity) {
        var body = responseEntity.getBody();

        if (body == null) {
            throw new NullPointerException("Body is null");
        }

        var split = body
                .replace("[", "")
                .replace("]", "")
                .replace("\"", "")
                .split(",");

        return List.of(split);
    }

    /**
     * Метод генерации случайных номеров, который запращивает их по RestTemplate у внешнего API.
     *
     * @return возвращает список из случайных номеров.
     * @throws NullPointerException если произошла ошибка API randommer.io .
     */
    @Override
    public List<String> generateAbonentNumber() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", randommerApiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(RANDOMMER_GET_TELEPHONE_URL)
                .queryParam("CountryCode", "{CountryCode}")
                .queryParam("Quantity", "{Quantity}")
                .encode()
                .toUriString();

        Map<String, Object> params = new HashMap<>();
        params.put("CountryCode", countryCode);
        params.put("Quantity", countNumber);

        var responseEntity = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                entity,
                String.class,
                params
        );

        return formatResponse(responseEntity);
    }
}
