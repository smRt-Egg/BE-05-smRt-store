package com.programmers.smrtstore.domain.qna.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.programmers.smrtstore.core.config.RedisTestConfig;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import com.programmers.smrtstore.domain.qna.domain.entity.ProductAnswer;
import com.programmers.smrtstore.domain.qna.domain.entity.ProductQuestion;
import com.programmers.smrtstore.domain.qna.infrastructure.ProductAnswerRepository;
import com.programmers.smrtstore.domain.qna.infrastructure.ProductQuestionRepository;
import com.programmers.smrtstore.domain.qna.presentation.dto.req.CreateAnswerRequest;
import com.programmers.smrtstore.domain.qna.presentation.dto.req.CreateQuestionRequest;
import com.programmers.smrtstore.domain.qna.presentation.dto.req.FindQuestionRequest;
import com.programmers.smrtstore.domain.qna.presentation.dto.req.UpdateAnswerRequest;
import com.programmers.smrtstore.domain.qna.presentation.dto.req.UpdateQuestionRequest;
import com.programmers.smrtstore.domain.qna.presentation.dto.res.AnswerResponse;
import com.programmers.smrtstore.domain.qna.presentation.dto.res.CreateQuestionResponse;
import com.programmers.smrtstore.domain.qna.presentation.dto.res.DeleteQuestionResponse;
import com.programmers.smrtstore.domain.qna.presentation.dto.res.QuestionResponse;
import com.programmers.smrtstore.domain.qna.presentation.dto.res.UpdateAnswerResponse;
import com.programmers.smrtstore.domain.qna.presentation.dto.res.UpdateQuestionResponse;
import com.programmers.smrtstore.domain.user.domain.entity.Gender;
import com.programmers.smrtstore.domain.user.domain.entity.Role;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Transactional
@Import(RedisTestConfig.class)
class ProductQnAServiceTest {

    @Autowired
    ProductQnAService productQnAService;
    @Autowired
    ProductQuestionRepository questionRepository;
    @Autowired
    ProductAnswerRepository answerRepository;
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    ProductJpaRepository productRepository;

    static Long userId;
    static Long product1Id;
    static Long product2Id;
    static User user;
    static Product product1;
    static Product product2;

    @BeforeEach
    void init() throws Exception {
        // 유저가 등록되어 있어야 한다.
        user = User.builder()
            .age(27)
            .birth("1998-07-05")
            .email("test@email.com")
            .role(Role.ROLE_USER)
            .gender(Gender.MALE)
            .phone("010-1111-1111")
            .nickName("nickName")
            .thumbnail("lsfsdfds")
            .point(0)
            .marketingAgree(true)
            .membershipYn(true)
            .build();
        User saveUser = userJpaRepository.save(user);
        userId = saveUser.getId();
        // 상품이 등록되어 있어야 한다.
        product1 = Product.builder()
            .name("productName")
            .price(1000)
            .category(Category.IT)
            .thumbnail(new URL("https://www.naver.com"))
            .build();
        product2 = Product.builder()
            .name("productName2")
            .price(10000)
            .category(Category.IT)
            .thumbnail(new URL("https://www.google.com"))
            .build();
        Product saveProduct1 = productRepository.save(product1);
        Product saveProduct2 = productRepository.save(product2);
        product1Id = saveProduct1.getId();
        product2Id = saveProduct2.getId();
    }

    @DisplayName("문의를 추가할 수 있다.")
    @Test
    void createQuestion() {
        //Given
        String content = "question1 content";
        CreateQuestionRequest request = CreateQuestionRequest.builder()
            .userId(userId)
            .productId(product1Id)
            .content(content)
            .build();
        //When
        CreateQuestionResponse response = productQnAService.createQuestion(userId, request);
        //Then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getProductId()).isEqualTo(product1Id);
        assertThat(response.getContent()).isEqualTo(content);
    }

    @DisplayName("유저가 질문한 문의들을 가져온다.")
    @Test
    void getQuestionsByUser() {
        //Given
        FindQuestionRequest request = FindQuestionRequest.builder().userId(userId).build();
        String content1 = "content1";
        String content2 = "content2";
        ProductQuestion question1 = ProductQuestion.builder()
            .userId(userId)
            .productId(product1Id)
            .content(content1)
            .build();
        ProductQuestion question2 = ProductQuestion.builder()
            .userId(userId)
            .productId(product2Id)
            .content(content2)
            .build();
        questionRepository.save(question1);
        questionRepository.save(question2);
        //When
        List<QuestionResponse> questionResponseList = productQnAService.findByUserId(userId,
            request);
        questionResponseList.sort(Comparator.comparing(QuestionResponse::getId));
        //Then
        assertThat(questionResponseList).hasSize(2);
        assertThat(questionResponseList.get(0).getProductName()).isEqualTo(product1.getName());
        assertThat(questionResponseList.get(0).getUserId()).isEqualTo(userId);
        assertThat(questionResponseList.get(0).getIsAnswered()).isFalse();
        assertThat(questionResponseList.get(0).getProductId()).isEqualTo(product1Id);
    }

    @DisplayName("문의를 수정할 수 있다.")
    @Test
    void updateQuestion() {
        //Given
        String content = "pre_content";
        ProductQuestion question1 = ProductQuestion.builder()
            .userId(userId)
            .productId(product1Id)
            .content(content)
            .build();
        questionRepository.save(question1);
        String updateContent = "update_content";
        UpdateQuestionRequest request = UpdateQuestionRequest.builder()
            .id(question1.getId())
            .content(updateContent)
            .build();
        //When
        UpdateQuestionResponse response = productQnAService.updateQuestion(userId, request);
        //Then
        assertThat(response.getProductId()).isEqualTo(product1Id);
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getContent()).isEqualTo(updateContent);
        assertThat(response.getContent()).isNotEqualTo(content);
        assertThat(response.getId()).isEqualTo(question1.getId());
    }

    @DisplayName("문의를 삭제할 수 있다.")
    @Test
    void deleteQuestion() {
        //Given
        ProductQuestion question = ProductQuestion.builder()
            .userId(userId)
            .productId(product1Id)
            .content("content")
            .build();
        questionRepository.save(question);
        //When
        DeleteQuestionResponse response = productQnAService.deleteQuestion(userId,
            question.getId());
        //Then
        assertThat(response.getId()).isEqualTo(question.getId());
    }

    @DisplayName("상품에 대응하는 문의들을 조회할 수 있다.")
    @Test
    void getQuestionsByProduct() {
        //Given
        ProductQuestion question1 = ProductQuestion.builder()
            .userId(userId)
            .productId(product1Id)
            .content("content1")
            .build();
        ProductQuestion question2 = ProductQuestion.builder()
            .userId(userId)
            .productId(product1Id)
            .content("content2")
            .build();
        questionRepository.save(question1);
        questionRepository.save(question2);
        //When
        List<QuestionResponse> questionResponseList = productQnAService.findByProductId(product1Id);
        questionResponseList.sort(Comparator.comparing(QuestionResponse::getId));
        //Then
        assertThat(questionResponseList).hasSize(2);
        assertThat(questionResponseList.get(0).getProductId()).isEqualTo(question1.getProductId());
        assertThat(questionResponseList.get(0).getProductName()).isEqualTo(product1.getName());
    }

    @DisplayName("문의에 답변을 달 수 있다.")
    @Test
    void addAnswerToQuestion() {
        //Given
        ProductQuestion question = ProductQuestion.builder()
            .userId(userId)
            .productId(product1Id)
            .content("content1")
            .build();
        questionRepository.save(question);
        String answerContent = "answerContent";
        CreateAnswerRequest request = CreateAnswerRequest.builder()
            .questionId(question.getId())
            .content(answerContent)
            .build();
        //When
        AnswerResponse response = productQnAService.addAnswer(userId, request);
        //Then
        assertThat(response.getQuestionId()).isEqualTo(question.getId());
        assertThat(response.getContent()).isEqualTo(answerContent);
        assertThat(question.getProductAnswerList()).hasSize(1);
        assertThat(question.getProductAnswerList().get(0).getContent()).isEqualTo(answerContent);
    }

    @DisplayName("문의에 해당하는 답변들을 가져온다.")
    @Test
    void getAnswersByQuestion() {
        //Given
        String questionContent = "questionContent";
        ProductQuestion question = ProductQuestion.builder()
            .userId(userId)
            .productId(product1Id)
            .content(questionContent)
            .build();
        questionRepository.save(question);

        String answerContent1 = "answerContent1";
        String answerContent2 = "answerContent2";
        ProductAnswer answer1 = ProductAnswer.builder()
            .productQuestion(question)
            .content(answerContent1)
            .build();
        ProductAnswer answer2 = ProductAnswer.builder()
            .productQuestion(question)
            .content(answerContent2)
            .build();
        answerRepository.save(answer1);
        answerRepository.save(answer2);
        //When
        List<AnswerResponse> answerResponseList = productQnAService.getAnswersByQuestionId(userId,
            question.getId());
        //Then
        assertThat(answerResponseList).hasSize(2);
        assertThat(answerResponseList.get(0).getContent()).isEqualTo(answerContent1);
        assertThat(answerResponseList.get(1).getContent()).isEqualTo(answerContent2);
        assertThat(answerResponseList.get(0).getQuestionId()).isEqualTo(question.getId());
    }

    @DisplayName("답변을 수정할 수 있다.")
    @Test
    void updateAnswerTest() {
        //Given
        String questionContent = "questionContent";
        ProductQuestion question = ProductQuestion.builder()
            .userId(userId)
            .productId(product1Id)
            .content(questionContent)
            .build();
        questionRepository.save(question);
        String answerContent = "answerContent";
        ProductAnswer answer = ProductAnswer.builder()
            .productQuestion(question)
            .content(answerContent)
            .build();
        answerRepository.save(answer);

        String updateAnswerContent = "updateContent";
        UpdateAnswerRequest request = UpdateAnswerRequest.builder()
            .id(answer.getId())
            .content(updateAnswerContent)
            .build();
        //When
        UpdateAnswerResponse updateAnswerResponse = productQnAService.updateAnswer(userId, request);
        //Then
        assertThat(answer.getContent()).isNotEqualTo(answerContent);
        assertThat(answer.getContent()).isEqualTo(updateAnswerContent);
    }

}