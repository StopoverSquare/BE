package be.busstop.domain.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBlockedPost is a Querydsl query type for BlockedPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlockedPost extends EntityPathBase<BlockedPost> {

    private static final long serialVersionUID = -1434429220L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBlockedPost blockedPost = new QBlockedPost("blockedPost");

    public final be.busstop.domain.user.entity.QUser admin;

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPost post;

    public QBlockedPost(String variable) {
        this(BlockedPost.class, forVariable(variable), INITS);
    }

    public QBlockedPost(Path<? extends BlockedPost> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBlockedPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBlockedPost(PathMetadata metadata, PathInits inits) {
        this(BlockedPost.class, metadata, inits);
    }

    public QBlockedPost(Class<? extends BlockedPost> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.admin = inits.isInitialized("admin") ? new be.busstop.domain.user.entity.QUser(forProperty("admin")) : null;
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
    }

}

