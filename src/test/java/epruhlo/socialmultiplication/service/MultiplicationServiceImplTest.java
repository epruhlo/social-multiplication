package epruhlo.socialmultiplication.service;

import epruhlo.socialmultiplication.domain.Multiplication;
import epruhlo.socialmultiplication.domain.MultiplicationResultAttempt;
import epruhlo.socialmultiplication.domain.User;
import epruhlo.socialmultiplication.repository.MultiplicationRepository;
import epruhlo.socialmultiplication.repository.MultiplicationResultAttemptRepository;
import epruhlo.socialmultiplication.repository.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class MultiplicationServiceImplTest {

    private MultiplicationServiceImpl multiplicationServiceImpl;

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private MultiplicationResultAttemptRepository attemptRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MultiplicationRepository multiplicationRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService, attemptRepository, userRepository, multiplicationRepository);
    }

    @Test
    public void createRandomMultiplicationTest() {
        given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

        Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();

        assertThat(multiplication.getFactorA()).isEqualTo(50);
        assertThat(multiplication.getFactorB()).isEqualTo(30);
    }

    @Test
    public void checkCorrectAttemptTest() {
        //given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john_doe");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3000, false);
        MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(user, multiplication, 3000, true);
        given(userRepository.findByAlias(user.getAlias())).willReturn(Optional.empty());
        given(multiplicationRepository.findByFactorAAndFactorB(50, 60)).willReturn(Optional.empty());

        //when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        //then
        assertThat(attemptResult).isTrue();
        verify(attemptRepository).save(verifiedAttempt);
    }

    @Test
    public void checkWrongAttemptTest() {
        //given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john_doe");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3050, false);
        given(userRepository.findByAlias(user.getAlias())).willReturn(Optional.empty());
        given(multiplicationRepository.findByFactorAAndFactorB(50, 60)).willReturn(Optional.empty());

        //when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        //then
        assertThat(attemptResult).isFalse();
        verify(attemptRepository).save(attempt);
    }

    @Test
    public void retrieveStatsTest() {
        //given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("jane_doe");
        MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(user, multiplication, 3010, false);
        MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(user, multiplication, 3051, false);

        final ArrayList<MultiplicationResultAttempt> latestAttempts = Lists.newArrayList(attempt1, attempt2);
        given(attemptRepository.findTop5ByUserAliasOrderByIdDesc(user.getAlias())).willReturn(latestAttempts);

        //when
        final List<MultiplicationResultAttempt> latestAttemptsResult = multiplicationServiceImpl.getStatsForUser("jane_doe");

        //then
        assertThat(latestAttemptsResult).isEqualTo(latestAttempts);
    }
}