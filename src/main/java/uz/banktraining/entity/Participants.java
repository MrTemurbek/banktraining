package uz.banktraining.entity;

import com.sun.istack.NotNull;
import lombok.*;
import uz.banktraining.dto.ParticipantDTO;

import javax.persistence.*;
import java.util.Date;

@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "participants")
public class Participants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @NotNull
    @Column(length = 1000)
    private String course;
    @NotNull
    @Column(name = "phone_number")
    private String number;
    @Column(name = "certificate_id", unique = true)
    @NotNull
    private String certificateID;
    @Column(name = "path")
    private String path;
    @Column(name = "created_at")
    private Date createdAt;
    private String link;

    public Participants (ParticipantDTO participantDTO) {
        this.name = participantDTO.getName();
        this.surname = participantDTO.getSurname();
        this.course =participantDTO.getCourse();
        this.number =participantDTO.getNumber();
        this.certificateID =participantDTO.getCertificateID();
    }
}
