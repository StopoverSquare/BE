package be.busstop.domain.salvation.repository;


import be.busstop.domain.salvation.dto.QSalvResponseDto;
import be.busstop.domain.salvation.dto.SalvResponseDto;
import be.busstop.domain.salvation.dto.SalvSearchCondition;
import be.busstop.domain.salvation.entity.QSalvation;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class SalvRepositoryCustomImpl implements SalvRepositoryCustom{
    private final JPAQueryFactory query;

    @Override
    public Slice<SalvResponseDto> searchSalvationByPage(SalvSearchCondition condition, Pageable pageable) {
        List<SalvResponseDto> result = query
                .select(new QSalvResponseDto(QSalvation.salvation))
                .from(QSalvation.salvation)
                .where(
                        titleEq(condition.getTitle()),
                        createAtEq(condition.getCreateAt())
                )
                .orderBy(QSalvation.salvation.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        long totalCount = query
                .select(QSalvation.salvation.count())
                .from(QSalvation.salvation)
                .where(
                        titleEq(condition.getTitle()),
                        createAtEq(condition.getCreateAt())
                )
                .fetchCount();

        return new PageImpl<>(result, pageable, totalCount);
    }
    private BooleanExpression titleEq(String titleCond) {
        return hasText(titleCond) ? QSalvation.salvation.title.contains(titleCond) : null;
    }
    private BooleanExpression createAtEq(String createAtCond) {
        return hasText(createAtCond) ?
                QSalvation.salvation.createdAt.eq(LocalDate.parse(createAtCond, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        .atStartOfDay()) :
                null;
    }

}
