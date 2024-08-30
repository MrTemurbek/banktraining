package uz.banktraining.dto;

import lombok.*;

import javax.persistence.Entity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ParticipantDTO {
    private String name;
    private String number;
    private String certificateID;
}
