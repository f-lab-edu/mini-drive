package dev.chan.domain.userstate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class UserItemState {
    private final UUID id;
    private final UUID fileId;
    private boolean important;
    private LocalDateTime lastViewedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;

    public static UserItemState create(UUID fileId, String userId) {
        return UserItemState.builder()
                .id(UUID.randomUUID())
                .fileId(fileId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy(userId)
                .build();
    }
}
