package be.busstop.domain.user.entity;

public enum UserReportEnum {

    SEXUAL_CONTENT("성적인 콘텐츠"),
    BULLYING("폭력적 또는 괴롭힘"),
    MISINFORMATION("잘못된 정보"),
    SPAM("스팸 또는 광고성 콘텐츠"),
    OTHER("기타");

    private final String description;

    UserReportEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
