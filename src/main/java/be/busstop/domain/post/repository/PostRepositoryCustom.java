package be.busstop.domain.post.repository;


import be.busstop.domain.post.dto.PostResponseDto;
import be.busstop.domain.post.dto.PostSearchCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;

public interface PostRepositoryCustom {

    Slice<PostResponseDto> searchPostByPage(PostSearchCondition condition, Pageable page);
}
