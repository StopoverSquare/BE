package be.busstop.domain.post.repository;

import be.busstop.domain.post.dto.PostResponseDto;
import be.busstop.domain.post.dto.PostSearchCondition;
import be.busstop.domain.post.dto.QPostResponseDto;
import be.busstop.domain.post.entity.Category;
import be.busstop.domain.post.entity.QPost;
import be.busstop.domain.poststatus.entity.Status;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Slice<PostResponseDto> searchPostByPage(PostSearchCondition condition, Pageable pageable) {
        // 게시글 조회 쿼리
        List<PostResponseDto> result = query
                .select(new QPostResponseDto(
                        QPost.post))
                .from(QPost.post)
                .where(
                        categoryEq(condition.getInterest()),
                        dateEq(condition.getEndDate()),
                        titleOrContentEq(condition.getTitleOrContent()),
                        statusEq(condition.getStatus()),
                        locationEq(condition.getLocation()))
                .orderBy(QPost.post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        // 전체 게시글 수 조회 쿼리
        long totalCount = query
                .select(QPost.post.count())
                .from(QPost.post)
                .where(
                        categoryEq(condition.getInterest()),
                        dateEq(condition.getEndDate()),
                        titleOrContentEq(condition.getTitleOrContent()),
                        statusEq(condition.getStatus()),
                        locationEq(condition.getLocation()))
                .fetchCount();

        // Slice 객체 생성 및 반환
        return new PageImpl<>(result, pageable, totalCount);
    }

    // 각종 조건에 대한 Predicate 생성 메소드들
    private BooleanExpression dateEq(String endDateCond) {
        return hasText(endDateCond) ? QPost.post.endDate.contains(endDateCond) : null;
    }

    private BooleanExpression locationEq(String locationCond) {
        return hasText(locationCond) ? QPost.post.location.contains(locationCond) : null;
    }

    private BooleanExpression categoryEq(String categoryCond) {
        return hasText(categoryCond) ? QPost.post.category.eq(Category.valueOf(categoryCond)) : null;
    }

    private BooleanExpression statusEq(String statusCond) {
        return hasText(statusCond) ? QPost.post.status.eq(Status.valueOf(statusCond)) : null;
    }

    private BooleanExpression titleOrContentEq(String titleOrContent) {
        return hasText(titleOrContent) ?
                QPost.post.title.contains(titleOrContent)
                        .or(QPost.post.content.contains(titleOrContent)) : null;
    }
}
