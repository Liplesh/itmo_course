package ru.lipnin.itmohomework.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.dto.discont.DiscountRequestDTO;
import ru.lipnin.itmohomework.entity.Appointment;
import ru.lipnin.itmohomework.entity.BeautyService;
import ru.lipnin.itmohomework.entity.Discount;
import ru.lipnin.itmohomework.exception.AppointmentException;
import ru.lipnin.itmohomework.exception.DiscountException;
import ru.lipnin.itmohomework.mapper.DiscountMapper;
import ru.lipnin.itmohomework.repository.AppointmentRepository;
import ru.lipnin.itmohomework.repository.BeautyServiceRepository;
import ru.lipnin.itmohomework.repository.DiscountRepository;
import ru.lipnin.itmohomework.security.entity.ApplicationUser;
import ru.lipnin.itmohomework.security.repository.ApplicationUserRepository;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final AppointmentRepository appointmentRepository;
    private final BeautyServiceRepository beautyServiceRepository;
    private final DiscountMapper discountMapper;

    //Метод создания скидки
    public Long createDiscount(DiscountRequestDTO discountRequestDTO) {
        ApplicationUser currentUser = getCurrentUser();

        Discount discount;
        switch (discountRequestDTO.discountType()){
            case PERSON -> discount = getPersonDiscount(discountRequestDTO);
            case APPOINTMENT -> discount = getAppointmentDiscount(discountRequestDTO);
            case SERVICE -> discount = getServiceDiscount(discountRequestDTO);
            default -> throw new DiscountException(HttpStatus.BAD_REQUEST, "Invalid discount type");
        }

        discount.setUser(currentUser);
        discountRepository.save(discount);
        return discount.getId();
    }

    //Метод создания сущности скидки для конкретных людей
    private Discount getPersonDiscount(DiscountRequestDTO discountRequestDTO){
        List<Long> longs = discountRequestDTO.personId();
        if (longs == null || longs.isEmpty()){
            throw new DiscountException(HttpStatus.BAD_REQUEST, "Не передана кол-ция из id юзеров");
        }

        Set<ApplicationUser> userSet = applicationUserRepository.findAllByIdInAndRemovedFalse(longs);
        if (userSet.isEmpty()){
            throw new DiscountException(HttpStatus.NOT_FOUND, "Не найдены пользователи");
        }

        Discount discount = discountMapper.mapToEntity(discountRequestDTO);
        discount.setDiscountUsers(userSet);
        return discount;
    }

    //Метод создания сущности скидки на конкретные брони
    private Discount getAppointmentDiscount(DiscountRequestDTO discountRequestDTO){
        List<Long> longs = discountRequestDTO.appointmentId();
        if (longs == null || longs.isEmpty()){
            throw new DiscountException(HttpStatus.BAD_REQUEST, "Не передана кол-ция из id бронирований");
        }

        Set<Appointment> appointmentSet = appointmentRepository.findAllByIdInAndRemovedFalse(longs);
        if (appointmentSet.isEmpty()){
            throw new DiscountException(HttpStatus.NOT_FOUND, "Не найдены брони");
        }

        Discount discount = discountMapper.mapToEntity(discountRequestDTO);
        discount.setDiscountAppointments(appointmentSet);
        return discount;
    }

    //Метод создания сущности скидки на конкретные услуги
    private Discount getServiceDiscount(DiscountRequestDTO discountRequestDTO){
        List<Long> longs = discountRequestDTO.serviceId();
        if (longs == null || longs.isEmpty()){
            throw new DiscountException(HttpStatus.BAD_REQUEST, "Не передана кол-ция из id услуг");
        }

        Set<BeautyService> beautyServiceSet = beautyServiceRepository.findAllByIdInAndRemovedFalse(longs);
        if (beautyServiceSet.isEmpty()){
            throw new DiscountException(HttpStatus.NOT_FOUND, "Не найдены услуги");
        }

        Discount discount = discountMapper.mapToEntity(discountRequestDTO);
        discount.setDiscountServices(beautyServiceSet);
        return discount;
    }

    private ApplicationUser getCurrentUser() {
        Authentication authenticationService = SecurityContextHolder.getContext().getAuthentication();
        return applicationUserRepository.findByUsername(authenticationService.getName())
                .orElseThrow(() -> new AppointmentException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }
}
