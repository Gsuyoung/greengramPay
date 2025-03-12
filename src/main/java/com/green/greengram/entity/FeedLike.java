package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode //가짜 mock한테 역할 줄때 필요한 애노테이션(test때)
public class FeedLike extends CreatedAt {
    @EmbeddedId
    private FeedLikeIds feedLikeIds;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    @MapsId("feedId")
    private Feed feed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User user;
}
