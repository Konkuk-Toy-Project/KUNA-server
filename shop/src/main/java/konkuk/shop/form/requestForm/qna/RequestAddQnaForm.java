package konkuk.shop.form.requestForm.qna;

import lombok.Data;

@Data
public class RequestAddQnaForm {
    Long itemId;
    boolean secret; // isSecret 으로 할 경우 파싱 오류남. // https://jungguji.github.io/2020/12/31/RequestBody-Annotation-%EC%82%AC%EC%9A%A9-%EC%8B%9C-boolean-%EB%B3%80%EC%88%98-%EB%B0%94%EC%9D%B8%EB%94%A9-%EC%97%90%EB%9F%AC/
    String question;
    String title;
}

