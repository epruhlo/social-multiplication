package epruhlo.socialmultiplication.service;

import epruhlo.socialmultiplication.domain.Multiplication;
import epruhlo.socialmultiplication.domain.MultiplicationResultAttempt;

import java.util.List;

public interface MultiplicationService {

    /**
     * Creates a Multiplication  object with two randomly-generated factors between 11 and 99
     *
     * @return a Multiplicationobject with random factors
     */
    Multiplication createRandomMultiplication();

    /**
     *
     * @param resultAttempt attempt to verify
     * @return true if the attempt matches the result of the multiplication, false otherwise.
     */
    boolean checkAttempt(MultiplicationResultAttempt resultAttempt);

    /**
     * @param userAlias
     * @return stats for the last multiplication attempts for the user by their alias
     */
    List<MultiplicationResultAttempt> getStatsForUser(String userAlias);
}
