package be.busstop.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserReport is a Querydsl query type for UserReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserReport extends EntityPathBase<UserReport> {

    private static final long serialVersionUID = 496099610L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserReport userReport = new QUserReport("userReport");

    public final be.busstop.global.utils.QTimestamped _super = new be.busstop.global.utils.QTimestamped(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<String, StringPath> imageUrlList = this.<String, StringPath>createList("imageUrlList", String.class, StringPath.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final EnumPath<UserReportEnum> report = createEnum("report", UserReportEnum.class);

    public final NumberPath<Long> reportedUserId = createNumber("reportedUserId", Long.class);

    public final QUser reporter;

    public final StringPath type = createString("type");

    public final QUser user;

    public QUserReport(String variable) {
        this(UserReport.class, forVariable(variable), INITS);
    }

    public QUserReport(Path<? extends UserReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserReport(PathMetadata metadata, PathInits inits) {
        this(UserReport.class, metadata, inits);
    }

    public QUserReport(Class<? extends UserReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reporter = inits.isInitialized("reporter") ? new QUser(forProperty("reporter")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

