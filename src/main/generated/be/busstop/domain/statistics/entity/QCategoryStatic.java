package be.busstop.domain.statistics.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCategoryStatic is a Querydsl query type for CategoryStatic
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategoryStatic extends EntityPathBase<CategoryStatic> {

    private static final long serialVersionUID = -625836833L;

    public static final QCategoryStatic categoryStatic = new QCategoryStatic("categoryStatic");

    public final NumberPath<Long> cultureCnt = createNumber("cultureCnt", Long.class);

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final NumberPath<Long> eatsCnt = createNumber("eatsCnt", Long.class);

    public final NumberPath<Long> etcCnt = createNumber("etcCnt", Long.class);

    public final NumberPath<Long> exerciseCnt = createNumber("exerciseCnt", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> studyCnt = createNumber("studyCnt", Long.class);

    public QCategoryStatic(String variable) {
        super(CategoryStatic.class, forVariable(variable));
    }

    public QCategoryStatic(Path<? extends CategoryStatic> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategoryStatic(PathMetadata metadata) {
        super(CategoryStatic.class, metadata);
    }

}

