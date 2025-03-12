package com.green.greengram.feed.like;

import com.green.greengram.TestUtils;
import com.green.greengram.config.JpaAuditingConfiguration;
import com.green.greengram.entity.Feed;
import com.green.greengram.entity.FeedLike;
import com.green.greengram.entity.FeedLikeIds;
import com.green.greengram.entity.User;
import com.green.greengram.feed.like.model.FeedLikeVo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JpaAuditingConfiguration.class)//created_at, updated_at 현재 일시값 들어갈 수 있도록 autiditing 기능 활성화
@ActiveProfiles("test") //yaml 적용되는 파일 선택 (application-test.yml)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//테스트는 기본적으로 메모리 데이터베이스 (H2)를 사용하는데 메모리 데이터베이스로 교체하지 않겠다.
//즉, 우리가 원래 쓰는 데이터베이스로 테스트를 진행하겠다.
//@TestInstance(TestInstance.Lifecycle.PER_CLASS) //테스트 객체를 딱 하나만 만든다.
class FeedLikeRepositoryTest {

    @Autowired
    FeedLikeRepository feedLikeRepository;

    static final Long userId_1 = 1L;
    static final Long userId_2 = 2L;

    static final Long feedId_1 = 1L;
    static final Long feedId_2 = 2L;

    FeedLike existedData = FeedLike.builder()
            .feedLikeIds(FeedLikeIds.builder().userId(userId_1).feedId(feedId_1).build())
            .user(User.builder().userId(userId_1).build())
            .feed(Feed.builder().feedId(feedId_1).build())
            .build();

    FeedLike notExistedData = FeedLike.builder()
            .feedLikeIds(FeedLikeIds.builder().userId(userId_2).feedId(feedId_2).build())
            .user(User.builder().userId(userId_2).build())
            .feed(Feed.builder().feedId(feedId_2).build())
            .build();

    /*
        @BeforeAll - 모든 테스트 실행 전에 최초 한번 실행
        ---
        @BeforeEach - 각 테스트 실행 전에 실행
        @Test
        @AfterEach - 각 테스트 실행 후에 실행
        ---
        @AfterAll - 모든 테스트 실행 후에 최초 한번 실행
    */

    // @BeforeAll - 모든 테스트 메소드 실행되기 최초 딱 한번 실행이 되는 메소드
    // 테스트 메소드 마다 테스트 객체가 만들어지면 BeforeAll 메소드는 무조건 static 메소드여야 한다.
    // 한 테스트 객체가 만들어지면 non-static 메소드일 수 있다.
    @BeforeEach
    void initData() {
        feedLikeRepository.deleteAll();
        feedLikeRepository.save(existedData);
    }

    // @BeforeEach - 테스트 메소드 마다 테스트 메소드 실행 전에 실행되는 before메소드
    // before메소드

    @Test
    @DisplayName("중복된 데이터 입력시 DuplicateKeyException 발생 체크")
    void insFeedLikeDuplicateDataThrowDuplicateKeyException() throws Exception {
        //JPA에서는 DuplicateKeyException 발생이 되지 않는다.
    }

    @Test
    void insFeedLike() {
        //when
        List<FeedLike> actualFeedLikeListBefore = feedLikeRepository.findAll(); //insert전 튜플 수 (1개)
        FeedLike actualFeedLikeBefore = feedLikeRepository.findById(FeedLikeIds.builder()
                                                          .userId(userId_2)
                                                          .feedId(feedId_2)
                                                          .build())
                                       .orElse(null); //insert전 존재하지 않는 레코드 읽기 (null임을 기대)

        feedLikeRepository.save(notExistedData);
        List<FeedLike> actualFeedLikeListAfter = feedLikeRepository.findAll(); //insert후 튜플 수 (2개)
        FeedLike actualFeedLikeAfter = feedLikeRepository.findById(FeedLikeIds.builder()
                                                         .userId(userId_2)
                                                         .feedId(feedId_2)
                                                         .build())
                                       .orElse(null); //insert후 존재하는 레코드 읽기 (not null임을 기대)

        //then
        assertAll(
                () -> TestUtils.assertCurrentTimestamp(actualFeedLikeAfter.getCreatedAt())
                , () -> assertEquals(actualFeedLikeListBefore.size() + 1, actualFeedLikeListAfter.size())
                , () -> assertNull(actualFeedLikeBefore) //내가 insert하려고 하는 데이터가 없었는지 단언
                , () -> assertNotNull(actualFeedLikeAfter) //실제 내가 원하는 데이터로 insert가 되었는지 단언

                , () -> assertEquals(notExistedData.getFeed().getFeedId(), actualFeedLikeAfter.getFeed().getFeedId()) //내가 원하는 데이터로 insert 되었는지 더블 체크
                , () -> assertEquals(notExistedData.getUser().getUserId(), actualFeedLikeAfter.getUser().getUserId()) //내가 원하는 데이터로 insert 되었는지 더블 체크
        );
    }

//    @Test
//    void delFeedLikeNoData() {
//        int actualAffectedRows = feedLikeMapper.delFeedLike(notExistedData);
//        assertEquals(0, actualAffectedRows);
//    }
//
//    @Test
//    void delFeedLike() {
//        FeedLikeVo actualFeedLikeVoBefore = feedLikeTestMapper.selFeedLikeByFeedIdAndUserId(existedData);
//        int actualAffectedRows = feedLikeMapper.delFeedLike(existedData);
//        FeedLikeVo actualFeedLikeVoAfter = feedLikeTestMapper.selFeedLikeByFeedIdAndUserId(existedData);
//
//        assertAll(
//              () -> assertEquals(1, actualAffectedRows)
//            , () -> assertNotNull(actualFeedLikeVoBefore)
//            , () -> assertNull(actualFeedLikeVoAfter)
//        );
//
//    }
}