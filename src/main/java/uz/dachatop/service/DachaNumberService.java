package uz.dachatop.service;

import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.dachatop.exp.AppBadRequestException;
import uz.dachatop.repository.DachaNumberSequenceRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author 'Mukhtarov Sarvarbek' on 28.03.2023
 * @project api.dachatop
 * @contact @sarvargo
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DachaNumberService {

    private final DachaNumberSequenceRepository dachaNumberSequenceRepository;

    public Long save(Long number) {
        String id = UUID.randomUUID().toString();

        if (number != null) {
            int i = dachaNumberSequenceRepository.create(id, number);

            if (i == 0) {
                log.error("Dacha number all ready exists! number = {}", number);
                throw new AppBadRequestException("Number all ready exists!");
            }

            return number;
        } else {
            int value = 0;
            boolean isNext = false;

            while (value == 0) {
                value = dachaNumberSequenceRepository.create(id, isNext);
                isNext = value == 0;
            }
            return dachaNumberSequenceRepository.findById(id).get().getSeqValue();
        }
    }
    public void delete(Long number) {
       dachaNumberSequenceRepository.deleteBySeqValue(number);
    }
}
