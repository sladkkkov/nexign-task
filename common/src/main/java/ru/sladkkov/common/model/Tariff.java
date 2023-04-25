package ru.sladkkov.common.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tariff {
    @Id
    private String tariffId;
    private String name;

}
