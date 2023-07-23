package uz.dachatop.service;
//PROJECT NAME --> api.dachatop
// YEAR --> 2023
// MONTH --> 02
// DAY --> 28
// TIME --> 13:26

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.dachatop.dto.message.MessagePaginationDTO;
import uz.dachatop.dto.message.MessageRequestDTO;
import uz.dachatop.dto.message.MessageResponseDTO;
import uz.dachatop.dto.response.ApiResponse;
import uz.dachatop.entity.MessageEntity;
import uz.dachatop.exp.ItemAlreadyExistsException;
import uz.dachatop.exp.ItemNotFoundException;
import uz.dachatop.mapper.MessageMapper;
import uz.dachatop.repository.MessageRepository;
import uz.dachatop.telegrambot.service.TelegramBotService;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    private final TelegramBotService telegramBotService;

    public ApiResponse<MessageResponseDTO> create(MessageRequestDTO dto) {
        Optional<MessageEntity> optional = messageRepository.findByIdAndVisibleTrue(dto.getId());

        if (optional.isPresent()) {
            log.warn("This Message is already exists");
            throw new ItemAlreadyExistsException("This Message is already exists");
        }

        MessageEntity entity = new MessageEntity();
        entity.setText(dto.getText());
        entity.setPhone(dto.getPhone());
        entity.setFromDay(dto.getFromDay());
        entity.setToDay(dto.getToDay());
        entity.setPrice(dto.getPrice());


        messageRepository.save(entity);

        telegramBotService.sendMessageToGroup(entity);

        return new ApiResponse<>(200, false, toDTO(entity));
    }

    public ApiResponse<Boolean> delete(Long id) {
        int i = messageRepository.deleteStatus(false, id);
        return new ApiResponse<Boolean>(200, false, i > 0);

    }

    private MessageResponseDTO toDTO(MessageEntity entity) {
        MessageResponseDTO dto = new MessageResponseDTO();
        dto.setText(entity.getText());
        dto.setPhone(entity.getPhone());
        dto.setFromDay(entity.getFromDay());
        dto.setToDay(entity.getToDay());
        dto.setPrice(entity.getPrice());
        dto.setVisible(entity.getVisible());

        return dto;
    }

    public MessagePaginationDTO pagination(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdDate");
        Page<MessageMapper> all = messageRepository.findAllAndVisibleTrue(pageable);

        long totalElements = all.getTotalElements();
        int totalPages = all.getTotalPages();

        List<MessageResponseDTO> dtoList = all.stream().map(this::toDTO).toList();
        return new MessagePaginationDTO(totalElements, totalPages, dtoList);
    }

    private MessageResponseDTO toDTO(MessageMapper mapper) {
        MessageResponseDTO dto = new MessageResponseDTO();

        dto.setId(mapper.getId());
        dto.setPhone(mapper.getPhone());
        dto.setText(mapper.getText());
        dto.setVisible(mapper.getVisible());
        dto.setPrice(mapper.getPrice());

        dto.setFromDay(mapper.getFrom_day());
        dto.setToDay(mapper.getTo_day());

        return dto;
    }

    public MessageEntity get(Long id) {
        return messageRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("Message not found");
        });
    }
}
