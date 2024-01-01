package be.busstop.domain.salvation.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * be.busstop.domain.salvation.dto.QSalvResponseDto is a Querydsl Projection type for SalvResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QSalvResponseDto extends ConstructorExpression<SalvResponseDto> {

    private static final long serialVersionUID = -1028439437L;

    public QSalvResponseDto(com.querydsl.core.types.Expression<? extends be.busstop.domain.salvation.entity.Salvation> salvation) {
        super(SalvResponseDto.class, new Class<?>[]{be.busstop.domain.salvation.entity.Salvation.class}, salvation);
    }

}

