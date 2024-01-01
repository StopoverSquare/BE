package be.busstop.domain.salvation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSalvation is a Querydsl query type for Salvation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSalvation extends EntityPathBase<Salvation> {

    private static final long serialVersionUID = -969468810L;

    public static final QSalvation salvation = new QSalvation("salvation");

    public final be.busstop.global.utils.QTimestamped _super = new be.busstop.global.utils.QTimestamped(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<String, StringPath> imageUrlList = this.<String, StringPath>createList("imageUrlList", String.class, StringPath.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath title = createString("title");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QSalvation(String variable) {
        super(Salvation.class, forVariable(variable));
    }

    public QSalvation(Path<? extends Salvation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSalvation(PathMetadata metadata) {
        super(Salvation.class, metadata);
    }

}

