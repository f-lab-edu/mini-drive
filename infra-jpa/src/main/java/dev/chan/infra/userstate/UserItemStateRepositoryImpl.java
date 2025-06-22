package dev.chan.infra.userstate;

import dev.chan.domain.userstate.UserItemState;
import dev.chan.domain.userstate.UserItemStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserItemStateRepositoryImpl implements UserItemStateRepository {
    @Override
    public UserItemState save(UserItemState itemState) {
        return null;
    }
}
