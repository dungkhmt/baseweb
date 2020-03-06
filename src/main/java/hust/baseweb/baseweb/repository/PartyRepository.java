package hust.baseweb.baseweb.repository;

import hust.baseweb.baseweb.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PartyRepository extends JpaRepository<Party, UUID> {
    List<Party> findAll();
}
