package be.busstop.domain.statistics.controller;

import be.busstop.domain.statistics.service.StaticService;
import be.busstop.global.responseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StaticController {
    private final StaticService staticService;

    @GetMapping("/static")
    public ApiResponse<?> getAllStatic(){
        return staticService.getAllStatic();
    }
}
