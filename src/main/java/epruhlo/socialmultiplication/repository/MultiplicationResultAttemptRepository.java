package epruhlo.socialmultiplication.repository;

import epruhlo.socialmultiplication.domain.MultiplicationResultAttempt;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MultiplicationResultAttemptRepository extends CrudRepository<MultiplicationResultAttempt, Long> {

    /**
     *
     * @param userAlias
     * @return the latest 5 attempts for a given user, identified by their alias
     */
    List<MultiplicationResultAttempt> findTop5ByUserAliasOrderByIdDesc(final String userAlias);
}
