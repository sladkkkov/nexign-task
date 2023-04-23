package ru.sladkkov.common.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "call_type_id")
    private TypeCall typeCall;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "abonent_id")
    private Abonent abonent;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String duration;

    private BigDecimal cost;

}
