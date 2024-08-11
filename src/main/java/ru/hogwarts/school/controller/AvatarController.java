package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.util.List;

@RestController
@RequestMapping("/avatars")
@Tag(name = "Страница загрузки аватара")
public class AvatarController {

    private AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузка аватара")
    public void uploadAvatar(@RequestPart("avatar") MultipartFile multipartFile,
                             @RequestParam long studentId) {
        avatarService.uploadAvatar(multipartFile, studentId);
    }

    @GetMapping
    @Operation(summary = "Получение списков аватарок постранично")
    public List<Avatar> getAllAvatarsForPage(@RequestParam("page") Integer pageNumber,
                                             @RequestParam("size") Integer pageSize) {
        return avatarService.getAllAvatarsByPage(pageNumber, pageSize);
    }
}
