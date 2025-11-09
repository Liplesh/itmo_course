package ru.lipnin.itmohomework.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.client.notification.NotificationClient;
import ru.lipnin.itmohomework.constants.Status;
import ru.lipnin.itmohomework.dto.appointment.AppointmentEarningsResponseDTO;
import ru.lipnin.itmohomework.dto.appointment.AppointmentRequestDTO;
import ru.lipnin.itmohomework.dto.appointment.AppointmentResponseDTO;
import ru.lipnin.itmohomework.dto.appointment.AppointmentUpdateRequestDTO;
import ru.lipnin.itmohomework.entity.Appointment;
import ru.lipnin.itmohomework.entity.BeautyService;
import ru.lipnin.itmohomework.entity.Discount;
import ru.lipnin.itmohomework.exception.AppointmentException;
import ru.lipnin.itmohomework.exception.BeautyServiceException;
import ru.lipnin.itmohomework.mapper.AppointmentMapper;
import ru.lipnin.itmohomework.repository.AppointmentRepository;
import ru.lipnin.itmohomework.repository.BeautyServiceRepository;
import ru.lipnin.itmohomework.repository.DiscountRepository;
import ru.lipnin.itmohomework.security.entity.ApplicationUser;
import ru.lipnin.itmohomework.security.repository.ApplicationUserRepository;
import ru.lipnin.itmohomework.services.notification.MessageService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppointmentService {


    private final AppointmentRepository appointmentRepository;
    private final BeautyServiceRepository beautyServiceRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final AppointmentMapper mapper;
    private final DiscountRepository discountRepository;
    private final NotificationClient notificationClient;
    private final MessageService messageService;
    private final DiscountService discountService;

    //Создать бронь
    public Long createAppointment(AppointmentRequestDTO appointmentRequestDTO) {
        ApplicationUser applicationUser = getCurrentUser();

        BeautyService beautyService = beautyServiceRepository
                .findByIdAndRemovedFalse(appointmentRequestDTO.serviceId())
                .orElseThrow(() -> new BeautyServiceException(HttpStatus.NOT_FOUND, "Услуга не найдена"));

        boolean serviceReserved = beautyServiceRepository.isServiceReserved(beautyService.getId(), appointmentRequestDTO.appointmentTime());
        if (serviceReserved) {
            log.info("Уже зарезервирована услуга {}", beautyService.getName());
            throw new AppointmentException(HttpStatus.CONFLICT, "Услуга уже забронирована");
        }

        Appointment appointment = mapper.mapToEntity(appointmentRequestDTO);
        appointment.setStatus(Status.CREATED);
        appointment.setService(beautyService);
        appointment.setUser(applicationUser);
        appointmentRepository.save(appointment);
        return appointment.getId();
    }

    public List<AppointmentResponseDTO> getAllAppointmentsByClient() {
        ApplicationUser applicationUser = getCurrentUser();

        List<Appointment> allAppointmentByClientName = appointmentRepository.findAllAppointmentByUserAndRemovedFalse(applicationUser);
        if (allAppointmentByClientName.isEmpty()) {
            throw new AppointmentException(HttpStatus.NOT_FOUND, "Записи не найдены");
        }
        return allAppointmentByClientName.stream().map(mapper::mapToDTO).collect(Collectors.toList());
    }

    //Получить все забронированные услуги по времени
    public List<AppointmentResponseDTO> getAllAppointmentsByDate(LocalDateTime appointmentDate) {
        List<Appointment> appointments = appointmentRepository
                .findAllAppointmentByAppointmentTimeAndRemovedFalse(appointmentDate);

        if (appointments.isEmpty()) {
            throw new AppointmentException(HttpStatus.NOT_FOUND, "Запись не найдена");
        }

        return appointments.stream().map(mapper::mapToDTO).collect(Collectors.toList());
    }

    //Закрыть бронь по выполнению
    public void completeAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findByIdAndRemovedFalse(appointmentId)
                .orElseThrow(() -> new AppointmentException(HttpStatus.NOT_FOUND, "Запись не найдена"));

        appointment.setStatus(Status.COMPLETED);
        appointment.setAppointmentClose(LocalDateTime.now());


        Pair<Integer, String> finalPrice = getFinalPrice(appointment.getUser(), appointment);
        System.out.println(finalPrice);
        appointment.setPrice(finalPrice.getFirst());
        if (!finalPrice.getSecond().isEmpty()) {
            appointment.setPriceNote(finalPrice.getSecond());
        }

        appointmentRepository.save(appointment);
    }

    //Подтвердить бронь оператором
    public void confirmAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findByIdAndRemovedFalse(appointmentId)
                .orElseThrow(() -> new AppointmentException(HttpStatus.NOT_FOUND, "Запись не найдена"));

        appointment.setStatus(Status.CONFIRMED);
        appointmentRepository.save(appointment);
        notificationClient.notifyUser(appointment.getUser(), messageService.getConfirmMessage(appointment));
    }

    //Снять свою бронь пользователем
    public void cancelAppointment(Long appointmentId) {
        ApplicationUser applicationUser = getCurrentUser();

        Appointment appointment = appointmentRepository.findByIdAndUserAndRemovedFalse(appointmentId, applicationUser)
                .orElseThrow(() -> new AppointmentException(HttpStatus.NOT_FOUND, "Запись не найдена"));

        appointment.setStatus(Status.CANCELLED);
        appointment.setAppointmentClose(LocalDateTime.now());
        appointmentRepository.save(appointment);
    }

    //Снять бронь (метод администратора), может закрывать бронь кого угодно
    public void forceCancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findByIdAndRemovedFalse(appointmentId)
                .orElseThrow(() -> new AppointmentException(HttpStatus.NOT_FOUND, "Запись не найдена"));

        appointment.setStatus(Status.CANCELLED);
        appointment.setAppointmentClose(LocalDateTime.now());
        appointmentRepository.save(appointment);
        discountService.createPersonalDiscount(appointment.getUser().getId(),
                15.0f, "Извинение за отмену брони", 3);
        notificationClient.notifyUser(appointment.getUser(), messageService.getSorryMessage(appointment.getUser()));
    }

    //Обновить время брони (метод пользователя)
    public AppointmentResponseDTO updateAppointment(AppointmentUpdateRequestDTO updateRequestDTO) {
        ApplicationUser applicationUser = getCurrentUser();

        Appointment appointment = appointmentRepository.findByIdAndUserAndRemovedFalse(updateRequestDTO.id(), applicationUser)
                .orElseThrow(() -> new AppointmentException(HttpStatus.NOT_FOUND, "Запись не найдена"));

        appointment.setAppointmentTime(updateRequestDTO.appointmentTime());
        appointmentRepository.save(appointment);
        notificationClient.notifyUser(appointment.getUser(), messageService.getUpdatedMessage(appointment));

        return mapper.mapToDTO(appointment);
    }

    //Обновить время брони (метод администратора), может менять бронь кого угодно
    public AppointmentResponseDTO forceUpdateAppointment(AppointmentUpdateRequestDTO updateRequestDTO) {
        Appointment appointment = appointmentRepository.findByIdAndRemovedFalse(updateRequestDTO.id())
                .orElseThrow(() -> new AppointmentException(HttpStatus.NOT_FOUND, "Запись не найдена"));

        appointment.setAppointmentTime(updateRequestDTO.appointmentTime());
        appointmentRepository.save(appointment);
        notificationClient.notifyUser(appointment.getUser(), messageService.getUpdatedMessage(appointment));
        return mapper.mapToDTO(appointment);
    }

    //Получить выручку по переданной дате
    public AppointmentEarningsResponseDTO findAllEarnedMoneyByPeriod(LocalDateTime from, LocalDateTime to) {
        to = to == null ? LocalDateTime.now() : to;

        BigDecimal allEarnedMoneyByPeriod = appointmentRepository.findAllEarnedMoneyByPeriod(from, to);
        if (allEarnedMoneyByPeriod == null) {
            allEarnedMoneyByPeriod = BigDecimal.ZERO;
        }

        StringBuilder stringBuilder = new StringBuilder();
        if (from == null) {
            stringBuilder.append("по ").append(to.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        } else {
            stringBuilder.append("с ").append(from.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                    .append(" по ").append(to.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        }
        return new AppointmentEarningsResponseDTO(stringBuilder.toString(), allEarnedMoneyByPeriod);
    }

    //Получить текущего пользователя, который вызвал метод
    private ApplicationUser getCurrentUser() {
        Authentication authenticationService = SecurityContextHolder.getContext().getAuthentication();
        return applicationUserRepository.findByUsername(authenticationService.getName())
                .orElseThrow(() -> new AppointmentException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }

    //Дайте, пожалуйста, обратную связь по этому методу, на сколько корректна придуманная логика
    //Метод проверяет все возможные скидки, которые есть и возвращает пару
    //Где ключ - откорректированная цена (если меняется), значение - описание (description скидки)
    //Приоритет - сначала скидки на конкретную бронь,
    //Далее скидки на услуги (может быть сезонная)
    //Последний приоритет - персональная скидка пользователя (используется один раз)
    //Если не нашлось подходящих скидок - возвращает факт цену и пустое описание
    private Pair<Integer, String> getFinalPrice(ApplicationUser user, Appointment appointment){
        int price = appointment.getService().getPrice();
        //Сначала скидки на текущую бронь
        Set<Discount> activeDiscountsByAppointmentId =
                discountRepository.findActiveDiscountsByAppointmentId(appointment.getId(), LocalDateTime.now());
        System.out.println("activeDiscountsByAppointmentId.size() = " + activeDiscountsByAppointmentId.size());
        if (!activeDiscountsByAppointmentId.isEmpty()) {
            Discount discount = findBestDiscount(activeDiscountsByAppointmentId, price);
            discount.getDiscountAppointments().removeIf(a -> a.getId().equals(appointment.getId()));
            //Удаляем из этой скидки ссылку на эту бронь
            discountRepository.save(discount);
            return getPairByDiscount(discount, price);
        }

        //Скидки на услуги целиком (Сезонные например)
        Set<Discount> activeDiscountsByServiceId = discountRepository
                .findActiveDiscountsByServiceId(appointment.getService().getId(), LocalDateTime.now());
        System.out.println("activeDiscountsByServiceId.size() = " + activeDiscountsByServiceId.size());

        if (!activeDiscountsByServiceId.isEmpty()) {
            Discount discount = findBestDiscount(activeDiscountsByServiceId, price);
            return getPairByDiscount(discount, price);
        }

        //Скидки пользователя (удаляем ссылку на данного пользователя, так как используется один раз)
        Set<Discount> activeDiscountsByUserId = discountRepository
                .findActiveDiscountsByUserId(user.getId(), LocalDateTime.now());
        System.out.println("activeDiscountsByUserId.size() = " + activeDiscountsByUserId.size());
        if (!activeDiscountsByUserId.isEmpty()) {
            Discount discount = findBestDiscount(activeDiscountsByUserId, price);
            //Могу ли я просто переопределить equals и hashCode, чтобы использовать remove(user)?
            //Нормальная ли это практика делать это в Entity
            discount.getDiscountUsers().removeIf(u -> u.getId().equals(user.getId()));
            //Удаляем из этой скидки ссылку на этого пользователя
            discountRepository.save(discount);
            return getPairByDiscount(discount, price);
        }

        return Pair.of(price, "");
    }

    private Pair<Integer, String> getPairByDiscount(Discount discount, int price) {
        price = price - getResultDiscount(price, discount);
        return Pair.of(price, discount.getDescription());
    }

    //Поиск наибольшей скидки из коллекции
    private Discount findBestDiscount(Set<Discount> discounts, int price) {
        return discounts.stream()
                .max(Comparator.comparing(d -> getResultDiscount(price, d)))
                .orElse(null);
    }

    //Возвращаем результат скидки
    private int getResultDiscount(int price, Discount discount) {
        return (int) (price * discount.getValue() / 100);
    }
}
