package uz.dachatop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.dachatop.dto.attach.AttachDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.entity.AttachEntity;
import uz.dachatop.exp.AppBadRequestException;
import uz.dachatop.exp.ItemNotFoundException;
import uz.dachatop.repository.AttachRepository;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class AttachServer {

    private final AttachRepository attachRepository;

    @Value("${attach.upload.folder}")
    private String attachFolder;

    @Value("${server.url}")
    private String domainName;


    public String storeFile(MultipartFile file) {
        AttachEntity entity = new AttachEntity();
        String pathFolder = getDateFolder();

        File folder = new File(attachFolder + "/" + pathFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try {
            String extension = getExtension(file.getOriginalFilename());

            byte[] bytes = file.getBytes();

            entity = saveAttach(entity, pathFolder, extension, file);

            Path url = Paths.get(folder.getAbsolutePath() + "/" + entity.getId() + "." + extension);

            Files.write(url, bytes);

            return entity.getId();
        } catch (IOException | RuntimeException e) {
            log.warn("Cannot Upload");
            delete(entity.getId().toString());
            throw new AppBadRequestException(e.getMessage());
        }
    }

    public byte[] open(String id) {
        byte[] data;

        AttachEntity entity = getById(id);
        String pathFolder = entity.getPath() + "/" + id + "." + entity.getExtension();

        try {
            Path path = Paths.get(attachFolder + "/" + pathFolder);
            data = Files.readAllBytes(path);
            return data;
        } catch (IOException e) {
            log.warn("Cannot Open");
            return new byte[0];
        }
    }

    public ApiResponse<List<AttachDTO>> upload(Map<String, MultipartFile> file) {
        String pathFolder = getDateFolder();

        File folder = new File(attachFolder + pathFolder);

        if (!folder.exists()) {
            boolean create = folder.mkdirs();
        }
        List<AttachDTO> list = new LinkedList<>();

        file.forEach((s, multipartFile) -> {
            AttachEntity attach = new AttachEntity();
            String extension = getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            AttachEntity entity = saveAttach(attach, pathFolder, extension, multipartFile);
            AttachDTO dto = toDTO(entity);

            try {
                byte[] bytes = multipartFile.getBytes();
                Path path = Paths.get(attachFolder + pathFolder + "/" + entity.getId() + "." + extension);
                Files.write(path, bytes);

                list.add(dto);

            } catch (IOException e) {
                log.warn("Upload Attach Exception = {}", e.getMessage());
            }
        });

        return new ApiResponse<>("Success!", 200, false, list);
    }

    public ResponseEntity<?> download(String id) {
        try {
            AttachEntity entity = getById(id);
            String path = entity.getPath() + "/" + id + "." + entity.getExtension();

            Path file = Paths.get(attachFolder + "/" + path);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + entity.getOriginalName() + "\"")
                        .body(resource);
            } else {
                log.warn("Cannot Read");
                throw new AppBadRequestException("Could not read the file!");
            }

        } catch (MalformedURLException e) {
            log.warn("Cannot Download");
            throw new AppBadRequestException("Error" + e.getMessage());
        }
    }


    public AttachDTO toDTO(AttachEntity entity) {
        AttachDTO dto = new AttachDTO();
        dto.setId(entity.getId());
        dto.setOriginalName(entity.getOriginalName());
        dto.setUrl(domainName + "/api/v1/attach/open/" + entity.getId());

        return dto;
    }

    public AttachDTO webContentLink(String attachId) {
        AttachDTO dto = new AttachDTO();
        dto.setId(attachId);
        dto.setUrl(domainName + "/api/v1/attach/open/" + attachId);
        return dto;
    }

    public AttachEntity saveAttach(AttachEntity entity, String pathFolder, String extension, MultipartFile file) {
        entity.setPath(pathFolder);
        entity.setOriginalName(file.getOriginalFilename());
        entity.setExtension(extension);
        entity.setSize(file.getSize());
        attachRepository.save(entity);
        return entity;
    }

    public String toOpenUrl(String id) {
        return domainName + "/api/v1/attach/open/" + id;
    }

    public String getExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    public String getDateFolder() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day;
    }

    public ApiResponse<Boolean> delete(String id) {
        AttachEntity entity = getById(id);

        File file = new File(attachFolder + "/" + entity.getPath() +
                "/" + entity.getId() + "." + entity.getExtension());

        if (file.delete()) {
            attachRepository.deleteById(entity.getId());
            return new ApiResponse<>(200, false, true);
        } else {
            log.warn("Cannot Read");
            attachRepository.deleteById(entity.getId());
            throw new AppBadRequestException("Could not read the file!");
        }
    }

    public AttachEntity getById(String id) {
        return attachRepository.findById(id).orElseThrow(() -> {
            log.warn("Not found {}", id);
            return new ItemNotFoundException("Not found!");
        });
    }

}

