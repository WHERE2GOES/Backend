package backend.greatjourney.domain.community.controller.response;

import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
public class SliceResponse<T> {

    private final List<T> content;
    private final int currentPage;
    private final int size;
    private final boolean first;
    private final boolean last;


    //생성자를 만들어주고 작성하면 된다.
    public SliceResponse(Slice<T> sliceContent) {
        this.content = sliceContent.getContent();
        this.currentPage = sliceContent.getNumber();
        this.size = sliceContent.getSize();
        this.first = sliceContent.isFirst();
        this.last = sliceContent.isLast();
    }

}
