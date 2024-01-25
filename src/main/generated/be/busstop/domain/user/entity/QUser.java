package be.busstop.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1448252346L;

    public static final QUser user = new QUser("user");

    public final be.busstop.global.utils.QTimestamped _super = new be.busstop.global.utils.QTimestamped(this);

    public final StringPath age = createString("age");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath gender = createString("gender");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<be.busstop.domain.post.entity.Category> interest = createEnum("interest", be.busstop.domain.post.entity.Category.class);

    public final DateTimePath<java.time.LocalDateTime> lastAccessed = createDateTime("lastAccessed", java.time.LocalDateTime.class);

    public final NumberPath<Double> mannerTemplate = createNumber("mannerTemplate", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profileImageUrl = createString("profileImageUrl");

    public final NumberPath<Integer> reportCount = createNumber("reportCount", Integer.class);

    public final EnumPath<UserRoleEnum> role = createEnum("role", UserRoleEnum.class);

    public final StringPath roomId = createString("roomId");

    public final StringPath sessionId = createString("sessionId");

    public final StringPath userCode = createString("userCode");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

