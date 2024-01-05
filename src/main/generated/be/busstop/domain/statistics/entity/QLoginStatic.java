package be.busstop.domain.statistics.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLoginStatic is a Querydsl query type for LoginStatic
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLoginStatic extends EntityPathBase<LoginStatic> {

    private static final long serialVersionUID = 9126980L;

    public static final QLoginStatic loginStatic = new QLoginStatic("loginStatic");

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> loginCnt = createNumber("loginCnt", Long.class);

    public QLoginStatic(String variable) {
        super(LoginStatic.class, forVariable(variable));
    }

    public QLoginStatic(Path<? extends LoginStatic> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLoginStatic(PathMetadata metadata) {
        super(LoginStatic.class, metadata);
    }

}

