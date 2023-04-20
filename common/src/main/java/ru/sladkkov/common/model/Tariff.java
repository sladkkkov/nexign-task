package ru.sladkkov.common.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tariffId;
    private String name;
    private BigDecimal fixedPrice;
    private Integer freeMinuteForFixedPrice;
    private BigDecimal priceForMinute;
    private Boolean incomingPaid;
    private Boolean outgoingPaid;
    private Boolean freeInsideNetwork;
}