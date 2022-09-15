package uz.banktraining.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.banktraining.entity.Participants;

@Repository
public interface ParticipantsRepository extends JpaRepository<Participants, Long> {
    Boolean existsByCertificateID(String ID);
}
