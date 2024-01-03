package be.busstop.domain.salvation.repository;

import be.busstop.domain.salvation.dto.SalvResponseDto;
import be.busstop.domain.salvation.dto.SalvSearchCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface SalvRepositoryCustom {
    Slice<SalvResponseDto> searchSalvationByPage(SalvSearchCondition condition, Pageable page);
}
