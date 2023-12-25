package be.busstop.domain.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostApplicant is a Querydsl query type for PostApplicant
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostApplicant extends EntityPathBase<PostApplicant> {

    private static final long serialVersionUID = -2010558862L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostApplicant postApplicant = new QPostApplicant("postApplicant");

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final StringPath gender = createString("gender");

    public final StringPath nickname = createString("nickname");

    public final QPost post;

    public final StringPath profileImageUrl = createString("profileImageUrl");

    public final NumberPath<Integer> reportCount = createNumber("reportCount", Integer.class);

    public final NumberPath<Long> turn = createNumber("turn", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QPostApplicant(String variable) {
        this(PostApplicant.class, forVariable(variable), INITS);
    }

    public QPostApplicant(Path<? extends PostApplicant> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostApplicant(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostApplicant(PathMetadata metadata, PathInits inits) {
        this(PostApplicant.class, metadata, inits);
    }

    public QPostApplicant(Class<? extends PostApplicant> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
    }

}

