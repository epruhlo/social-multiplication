package epruhlo.socialmultiplication.repository;

import epruhlo.socialmultiplication.domain.Multiplication;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MultiplicationRepository extends CrudRepository<Multiplication, Long> {
    Optional<Multiplication> findByFactorAAndFactorB(int facorA, int factorB);
}
