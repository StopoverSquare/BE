package be.busstop.domain.user.entity;

public enum UserReportEnum {

    SEXUAL_CONTENT("성희롱"),
    BULLYING("폭력적 또는 괴롭힘"),
    MISINFORMATION("잘못된 정보 유포"),
    SPAM("스팸 또는 광고성 참여"),
    OTHER("기타");

    private final String description;

    UserReportEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
