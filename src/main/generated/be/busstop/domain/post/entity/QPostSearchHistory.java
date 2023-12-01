package be.busstop.domain.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostSearchHistory is a Querydsl query type for PostSearchHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostSearchHistory extends EntityPathBase<PostSearchHistory> {

    private static final long serialVersionUID = 1760485692L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostSearchHistory postSearchHistory = new QPostSearchHistory("postSearchHistory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath searchCondition = createString("searchCondition");

    public final DateTimePath<java.time.LocalDateTime> timestamp = createDateTime("timestamp", java.time.LocalDateTime.class);

    public final be.busstop.domain.user.entity.QUser user;

    public QPostSearchHistory(String variable) {
        this(PostSearchHistory.class, forVariable(variable), INITS);
    }

    public QPostSearchHistory(Path<? extends PostSearchHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostSearchHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostSearchHistory(PathMetadata metadata, PathInits inits) {
        this(PostSearchHistory.class, metadata, inits);
    }

    public QPostSearchHistory(Class<? extends PostSearchHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new be.busstop.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

