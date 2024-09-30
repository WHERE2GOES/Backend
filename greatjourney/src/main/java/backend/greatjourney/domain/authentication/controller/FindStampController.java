package backend.greatjourney.domain.authentication.controller;

import backend.greatjourney.domain.authentication.controller.dto.StampRequestDTO;
import backend.greatjourney.domain.authentication.controller.dto.StampResponseDTO;
import backend.greatjourney.domain.authentication.service.FindStampService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stamp")
public class FindStampController {

    private final FindStampService findStampService;





}
