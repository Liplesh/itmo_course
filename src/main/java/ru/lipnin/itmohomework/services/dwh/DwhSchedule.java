package ru.lipnin.itmohomework.services.dwh;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.client.dwh.DwhClient;
import ru.lipnin.itmohomework.dto.dwh.DwhRequestStatisticDto;
import ru.lipnin.itmohomework.dto.dwh.DwnAppointmentDto;
import ru.lipnin.itmohomework.entity.Appointment;
import ru.lipnin.itmohomework.mapper.DwhMapper;
import ru.lipnin.itmohomework.repository.AppointmentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class DwhSchedule {

    private final AppointmentRepository appointmentRepository;
    private final DwhClient dwhClient;
    private final DwhMapper dwhMapper;

    /*
    Не совсем понял второй пункт условия
    2. Если запрос не был передан, в ответ вернулся не пустой список, данные необходимо отправить повторно
    Если не передан запрос, как вернуть не пустой список
     */

    @Async("dwh-executor") // задачи будут выполняться отдельным пулом
    @Scheduled(cron = "0 0 23 * * *", zone = "Europe/Moscow")
    public void sendStatistic() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay =  now.toLocalDate().atStartOfDay();
        LocalDateTime startOfNextDay = startOfDay.plusDays(1);
        Set<Appointment> appointments = appointmentRepository.findAllCompletedAppointmentByDate(startOfDay, startOfNextDay);
        if (appointments.isEmpty()) {
            //Что тут лучше сделать, если за день не было закрытых например услуг?
            return;
        }

        List<DwnAppointmentDto> list = appointments.stream().map(dwhMapper::mapToDto).toList();
        dwhClient.putStatistic(new DwhRequestStatisticDto(now, list));
    }

}
