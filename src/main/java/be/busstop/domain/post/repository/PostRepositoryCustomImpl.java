package be.busstop.domain.post.repository;


import be.busstop.domain.post.dto.PostResponseDto;
import be.busstop.domain.post.dto.PostSearchCondition;
import be.busstop.domain.post.dto.QPostResponseDto;
import be.busstop.domain.post.entity.Category;
import be.busstop.domain.post.entity.QPost;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Slice<PostResponseDto> searchPostByPage(PostSearchCondition condition, Pageable pageable) {
        List<PostResponseDto> result = query
                .select(new QPostResponseDto(QPost.post))
                .from(QPost.post)
                .where(
                        categoryEq(condition.getCategory()),
                        dateEq(condition.getStartDate()),
                        titleOrContentEq(condition.getTitleOrContent()),
                        locationEq(condition.getLocation()))
                .orderBy(QPost.post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        long totalCount = query
                .select(QPost.post.count())
                .from(QPost.post)
                .where(
                        categoryEq(condition.getCategory()),
                        dateEq(condition.getStartDate()),
                        titleOrContentEq(condition.getTitleOrContent()),
                        locationEq(condition.getLocation()))
                .fetchCount();

        return new PageImpl<>(result, pageable, totalCount);
    }
    private BooleanExpression dateEq(String StartDate) {
        return hasText(StartDate) ? QPost.post.title.contains(StartDate) : null;
    }
    private BooleanExpression locationEq(String locationCond) {
        return hasText(locationCond) ? QPost.post.location.contains(locationCond) : null;
    }

    private BooleanExpression categoryEq(String categoryCond) {
        return hasText(categoryCond) ? QPost.post.category.eq(Category.valueOf(categoryCond)) : null;
    }

    private BooleanExpression titleOrContentEq(String titleOrContent) {
        return hasText(titleOrContent) ?
                QPost.post.title.contains(titleOrContent)
                        .or(QPost.post.content.contains(titleOrContent)) : null;
    }


}
