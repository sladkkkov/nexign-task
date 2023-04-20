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

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AbonentNumberGeneratorImpl implements AbonentNumberGenerator {
    private static final String RANDOMMER_GET_TELEPHONE_URL = "https://randommer.io/api/Phone/Generate";
    private final RestTemplate restTemplate;
    @Value("${randommer.api.key}")
    private String randommerApiKey;
    @Value("${randommer.api.country_code}")
    private String countryCode;
    @Value("${randommer.api.count}")
    private int countNumber;

    private static String formatResponse(ResponseEntity<String> responseEntity) {
        var body = responseEntity.getBody();
        if (body == null) {
            throw new NullPointerException("Body is null");
        }

        return body
                .replace("[", "")
                .replace("]", "")
                .replace("\"", "");
    }

    @Override
    public String generateAbonentNumber() {

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
