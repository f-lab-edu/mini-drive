package dev.chan.domain.userstate;

import dev.chan.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserItemState {

    private User user;
    private String fileId;
    private String userId;
    private boolean important;
    private boolean bookmarked;
    private LocalDateTime lastViewedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;


}
