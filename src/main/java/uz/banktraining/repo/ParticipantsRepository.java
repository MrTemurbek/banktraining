package uz.banktraining.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.banktraining.entity.Participants;

@Repository
public interface ParticipantsRepository extends JpaRepository<Participants, Long> {
    Boolean existsByCertificateID(String ID);
    Participants getParticipantsByCertificateID(String id);

    @Modifying
    @Query("UPDATE Participants p set p.name= :name, p.surname = :surname,p.number = :number, p.course = :course where p.certificateID= :certificateId")
    void updateParticipants(String name, String surname, String number, String course, String certificateId);
}
