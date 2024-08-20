package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.AvatarException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class AvatarService {

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;
    private final Path path;
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    public AvatarService(StudentRepository studentRepository,
                         AvatarRepository avatarRepository,
                         @Value("$ {application.avatars-dir-name}") String avatarsDirName) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
        path = Paths.get(avatarsDirName);
    }

    @Transactional
    public void uploadAvatar(MultipartFile multipartFile, long studentId) {
        logger.info("Был вызван метод для \"uploadAvatar\"");
        try {
            byte[] data = multipartFile.getBytes();
            String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
            Path avatarPath = path.resolve(UUID.randomUUID() + "." + extension);
            Files.write(avatarPath, data);
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() ->{
                        logger.error("Нет студента с id = {}", studentId);
                        return new StudentNotFoundException(studentId);
                    });
            Avatar avatar = avatarRepository.findByStudent_Id(studentId)
                    .orElseGet(Avatar::new);
            avatar.setStudent(student);
            avatar.setData(data);
            avatar.setFileSize(data.length);
            avatar.setFilePath(avatarPath.toString());
            avatar.setMediaType(multipartFile.getContentType());
            avatarRepository.save(avatar);
        } catch (IOException e) {
            logger.error("Аватарка не читается");
            throw new AvatarException();
        }
    }

    public Pair<byte[], String> getAvatarFromDb(long studentId) {
        logger.info("Был вызван метод для \"createStudent\"");
        Avatar avatar = avatarRepository.findByStudent_Id(studentId)
                .orElseThrow(() -> {
                    logger.error("Нет студента с id = {}", studentId);
                    return new StudentNotFoundException(studentId);
                });
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }

    public Pair<byte[], String> getAvatarFromFs(long studentId) {
        logger.info("Был вызван метод для \"getAvatarFromFs\"");
        try {
            Avatar avatar = avatarRepository.findByStudent_Id(studentId)
                    .orElseThrow(() -> {
                        logger.error("Нет студента с id = {}", studentId);
                        return new StudentNotFoundException(studentId);
                    });
            return Pair.of(Files.readAllBytes(Paths.get(avatar.getFilePath())), avatar.getMediaType());
        } catch (IOException e) {
            logger.error("Аватарка не читается");
            throw new AvatarException();
        }
    }

    public List<Avatar> getAllAvatarsByPage(Integer pageNumber, Integer pageSize) {
        logger.info("Был вызван метод для \"getAllAvatarsByPage\"");
        if (pageNumber == 0) {
            logger.error("Недопустимый номер страницы");
            throw new IllegalArgumentException();
        }
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}
