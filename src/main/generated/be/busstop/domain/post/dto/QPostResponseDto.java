package be.busstop.domain.post.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * be.busstop.domain.post.dto.QPostResponseDto is a Querydsl Projection type for PostResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QPostResponseDto extends ConstructorExpression<PostResponseDto> {

    private static final long serialVersionUID = -43683724L;

    public QPostResponseDto(com.querydsl.core.types.Expression<? extends be.busstop.domain.post.entity.Post> post) {
        super(PostResponseDto.class, new Class<?>[]{be.busstop.domain.post.entity.Post.class}, post);
    }

}

