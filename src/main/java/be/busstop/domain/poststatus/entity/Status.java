package be.busstop.domain.poststatus.entity;

public enum Status {
    IN_PROGRESS("게시글을 진행합니다."),
    BLOCKED("관리자에 의해 차단된 게시물입니다."),
    COMPLETED("게시글을 마감했습니다.");

    private final String message;

    Status(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
