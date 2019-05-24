package epruhlo.socialmultiplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import epruhlo.socialmultiplication.domain.Multiplication;
import epruhlo.socialmultiplication.domain.MultiplicationResultAttempt;
import epruhlo.socialmultiplication.domain.User;
import epruhlo.socialmultiplication.service.MultiplicationService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(MultiplicationResultAttemptController.class)
public class MultiplicationResultAttemptControllerTest {

    @MockBean
    private MultiplicationService multiplicationService;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<MultiplicationResultAttempt> jsonResult;
    private JacksonTester<List<MultiplicationResultAttempt>> jsonResultList;

    @Before
    public void setUp() throws Exception {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void postResultReturnNotCorrect() throws Exception {
        genericPostResultTest(false);
    }

    @Test
    public void postResultReturnCorrect() throws Exception {
        genericPostResultTest(true);
    }

    private void genericPostResultTest(boolean correct) throws Exception {
        given(multiplicationService.checkAttempt(any(MultiplicationResultAttempt.class))).willReturn(correct);


        User user = new User("john_doe");
        Multiplication multiplication = new Multiplication(50,70);
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3500, correct);

        MockHttpServletResponse response = mvc.perform(post("/results")
                .contentType(MediaType.APPLICATION_JSON).
                        content(jsonResult.write(attempt).getJson())).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonResult.write(new MultiplicationResultAttempt(attempt.getUser(),
                        attempt.getMultiplication(), attempt.getResultAttempt(), correct)).getJson());
    }

    @Test
    public void getStatisticsTest() throws Exception {
        //given
        final MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(new User("jane_doe"),
                new Multiplication(30, 10), 300, true);
        final ArrayList<MultiplicationResultAttempt> multiplicationResultAttempts = Lists.newArrayList(attempt, attempt);
        given(multiplicationService.getStatsForUser("jane_doe")).willReturn(multiplicationResultAttempts);

        //when
        MockHttpServletResponse response = mvc.perform(get("/results?alias=jane_doe")
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonResultList.write(multiplicationResultAttempts).getJson());
    }
}