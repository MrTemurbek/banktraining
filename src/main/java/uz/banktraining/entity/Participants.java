package uz.banktraining.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "participants")
public class Participants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;
    private String name;



}
