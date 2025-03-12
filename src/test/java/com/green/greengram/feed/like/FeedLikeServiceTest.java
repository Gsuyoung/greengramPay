package com.green.greengram.feed.like;

import com.green.greengram.config.security.AuthenticationFacade;
import com.green.greengram.entity.Feed;
import com.green.greengram.entity.FeedLike;
import com.green.greengram.entity.FeedLikeIds;
import com.green.greengram.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FeedLikeServiceTest {

    @InjectMocks
    private FeedLikeService feedLikeService;

    @Mock
    private FeedLikeRepository feedLikeRepository;

    @Mock
    private AuthenticationFacade authenticationFacade;

    private final long SIGNED_USER_ID_3 = 3L;
    private final long FEED_ID_7 = 7L;
    private final long FEED_ID_8 = 8L;

    @BeforeEach
    void setUpAuthenticationFacade() {
        given(authenticationFacade.getSignedUserId()).willReturn(SIGNED_USER_ID_3);
    }

    @Test
    @DisplayName("좋아요 처리")
    void feedLikeToggleIns() {
        FeedLikeIds ids = FeedLikeIds.builder()
                .feedId(FEED_ID_7)
                .userId(SIGNED_USER_ID_3)
                .build();

        FeedLike feedLike = FeedLike.builder()
                .feedLikeIds(ids)
                .feed(Feed.builder().feedId(FEED_ID_7).build())
                .user(User.builder().userId(SIGNED_USER_ID_3).build())
                .build();

        given(feedLikeRepository.findById(ids)).willReturn(Optional.ofNullable(null));
        int actualResult = feedLikeService.feedLikeToggle(FEED_ID_7);

        assertEquals(1, actualResult);

        verify(feedLikeRepository).save(feedLike);
    }

    @Test
    @DisplayName("좋아요 취소")
    void feedLikeToggleDel() {
        FeedLikeIds ids = FeedLikeIds.builder()
                .feedId(FEED_ID_7)
                .userId(SIGNED_USER_ID_3)
                .build();

        FeedLike feedLike = FeedLike.builder()
                .feedLikeIds(ids)
                .feed(Feed.builder().feedId(FEED_ID_7).build())
                .user(User.builder().userId(SIGNED_USER_ID_3).build())
                .build();

        given(feedLikeRepository.findById(ids)).willReturn(Optional.of(feedLike));


        int actualResult = feedLikeService.feedLikeToggle(FEED_ID_7);

        assertEquals(0, actualResult);

        verify(feedLikeRepository).delete(feedLike);
    }
}