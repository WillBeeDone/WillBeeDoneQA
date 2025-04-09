package wbd.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OfferResponseDto {
    private int totalPages;
    private int totalElements;
    private boolean last;
    private boolean first;
    private PageableDto pageable;
    private int numberOfElements;
    private int size;
    private List<OfferDto> content;
    private int number;
    private SortDto sort;
    private boolean empty;

    @Getter
    @Setter
    @ToString
    public static class PageableDto {
        private boolean paged;
        private int pageNumber;
        private int pageSize;
        private boolean unpaged;
        private int offset;
        private SortDto sort;
    }

    @Getter
    @Setter
    @ToString
    public static class SortDto {
        private boolean sorted;
        private boolean unsorted;
        private boolean empty;
    }

}

