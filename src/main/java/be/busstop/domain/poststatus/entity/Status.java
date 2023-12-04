package be.busstop.domain.poststatus.entity;

public enum Status {
    IN_PROGRESS("게시글을 진행합니다."),
    COMPLETED("게시글을 마감했습니다.");

    private final String message;

    Status(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
