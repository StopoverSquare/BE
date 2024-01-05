package be.busstop.domain.statistics.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAgeStatic is a Querydsl query type for AgeStatic
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAgeStatic extends EntityPathBase<AgeStatic> {

    private static final long serialVersionUID = -1603492710L;

    public static final QAgeStatic ageStatic = new QAgeStatic("ageStatic");

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final NumberPath<Long> etcCnt = createNumber("etcCnt", Long.class);

    public final NumberPath<Long> fiftyCnt = createNumber("fiftyCnt", Long.class);

    public final NumberPath<Long> fortyCnt = createNumber("fortyCnt", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> sixtyCnt = createNumber("sixtyCnt", Long.class);

    public final NumberPath<Long> tenCnt = createNumber("tenCnt", Long.class);

    public final NumberPath<Long> thirtyCnt = createNumber("thirtyCnt", Long.class);

    public final NumberPath<Long> twentyCnt = createNumber("twentyCnt", Long.class);

    public QAgeStatic(String variable) {
        super(AgeStatic.class, forVariable(variable));
    }

    public QAgeStatic(Path<? extends AgeStatic> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAgeStatic(PathMetadata metadata) {
        super(AgeStatic.class, metadata);
    }

}

