package uz.banktraining.entity;

import com.sun.istack.NotNull;
import lombok.*;

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
    private String course;
    @NotNull
    @Column(name = "phone_number")
    private String number;
    @Column(name = "certificate_id", unique = true)
    @NotNull
    private String certificateID;
    @Column(name = "certificate_date")
    @NotNull
    private String certificateDate;
    @Column(name = "path")
    private String path;
    @Column(name = "created_at")
    private Date createdAt;
    private String link;




}
