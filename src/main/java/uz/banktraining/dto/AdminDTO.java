package uz.banktraining.dto;

import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "admins")
public class AdminDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
}
