package ru.sladkkov.common.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Abonent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String telephoneNumber;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    private BigDecimal balance;
}
