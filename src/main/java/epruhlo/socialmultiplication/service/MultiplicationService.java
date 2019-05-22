package epruhlo.socialmultiplication.service;

import epruhlo.socialmultiplication.domain.Multiplication;

public interface MultiplicationService {

    /**
     * Creates a Multiplication  object with two randomly-generated factors between 11 and 99
     *
     * @return a Multiplicationobject with random factors
     */
    Multiplication createRandomMultiplication();
}
