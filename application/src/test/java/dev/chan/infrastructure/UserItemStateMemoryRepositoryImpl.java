package dev.chan.infrastructure;

import dev.chan.domain.userstate.UserItemState;
import dev.chan.domain.userstate.UserItemStateRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserItemStateMemoryRepositoryImpl implements UserItemStateRepository {
    public UserItemState save(UserItemState userItemState) {
        return userItemState;
    }
}
