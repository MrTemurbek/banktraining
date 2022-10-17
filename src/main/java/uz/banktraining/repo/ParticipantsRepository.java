package uz.banktraining.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.banktraining.entity.Participants;

import java.util.List;

@Repository
public interface ParticipantsRepository extends JpaRepository<Participants, Long> {
    Boolean existsByCertificateID(String ID);
    Participants getParticipantsByCertificateID(String id);

    @Modifying
    @Query("update Participants p set p.name= :name, p.number = :number where p.certificateID= :certificateId")
    void updateParticipants(String name, String number, String certificateId);

    @Query("select p.link from Participants p")
    List<String> getAllLinks();
}
