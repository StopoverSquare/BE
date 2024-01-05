package be.busstop.domain.statistics.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGenderStatic is a Querydsl query type for GenderStatic
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGenderStatic extends EntityPathBase<GenderStatic> {

    private static final long serialVersionUID = 1808526626L;

    public static final QGenderStatic genderStatic = new QGenderStatic("genderStatic");

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final NumberPath<Long> etcCnt = createNumber("etcCnt", Long.class);

    public final NumberPath<Long> femaleCnt = createNumber("femaleCnt", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> maleCnt = createNumber("maleCnt", Long.class);

    public QGenderStatic(String variable) {
        super(GenderStatic.class, forVariable(variable));
    }

    public QGenderStatic(Path<? extends GenderStatic> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGenderStatic(PathMetadata metadata) {
        super(GenderStatic.class, metadata);
    }

}

