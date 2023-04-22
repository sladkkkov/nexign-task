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

@Component
@RequiredArgsConstructor
public class AbonentNumberGeneratorImpl implements AbonentNumberGenerator {
    private static final String RANDOMMER_GET_TELEPHONE_URL = "https://randommer.io/api/Phone/Generate";

    @Value("${randommer.api.key}")
    private String randommerApiKey;
    @Value("${randommer.api.country_code}")
    private String countryCode;
    @Value("${randommer.api.count}")
    private int countNumber;
    private final RestTemplate restTemplate;

    private List<String> numbers;

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

    @PostConstruct
    public void init() {
        numbers = generateAbonentNumber();
    }


    public List<String> getNumbers() {
        return numbers;
    }

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
