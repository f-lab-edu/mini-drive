package dev.chan.domain.file;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DriveItemServiceTest {

    private final DriveItemService service = new DriveItemService();

    @Test
    @DisplayName("문자열 url 과 ThumbnailKey 가 있는 DriveItem 객체가 주어지면, 썸네일 URL 을 리턴한다.")
    void generateThumbnailUrl_success() {
        //given
        String url = "https://cdn.mini-drive.dev";

        DriveItem mockItem = DriveItem.builder()
                .id(new DriveItemId())
                .build();

        //when
        String expectedUrl = service.generateThumbnailUrl(url, mockItem.thumbnailKey("thumbnails"));

        //then
        Assertions.assertThat(expectedUrl).isEqualTo("https://cdn.mini-drive.dev/thumbnails/" + mockItem.getId());


    }
}