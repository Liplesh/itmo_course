package ru.lipnin.itmohomework.services;

import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.constants.DifficultyLevel;
import ru.lipnin.itmohomework.dto.TechServRequestDTO;
import ru.lipnin.itmohomework.dto.TechServResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TechServService {

    //Создать новую услугу
    public Long createTechServ(TechServRequestDTO techServRequestDTO) {
        //TODO создать новую, записать в БД и вернуть ее id
        return null;
    }

    //Получить услугу по id
    public TechServResponseDTO getTechServById(Long techServId) {
        //TODO вернуть услугу по id
        return null;
    }

    //Получить все услуги
    public List<TechServResponseDTO> getTechServs() {
        //TODO вернуть все услуги из БД
        return null;
    }

    //Получить все забронированные услуги
    public List<TechServResponseDTO> getReservedTechServs() {
        //TODO вернуть все услуги из БД, с флагом reserved = true
        return null;
    }

    //Получить все услуги по переданной сложности
    public List<TechServResponseDTO> getTechServsByDifficulty(DifficultyLevel level){
        //TODO забрать все услуги из БД, соответствующие переданной сложности
        return null;
    }

    //Получить все занятые услуги до какой либо даты
    public List<TechServResponseDTO> getTechServsSignetUntil(LocalDateTime date){
        //TODO найти все услуги, без даты бронирования или с датой раньше переданной (?)
        return null;
    }

    //Создать бронь
    public void reserveTechServ(Long techServId) {
        /*
        TODO проверить по id, свободна ли услуга,
        если нет брони - добавить дату бронирования, id пользователя и поменять флаг reserved = true
        если бронь есть - то (?)выкидываем исключение
        */

    }

    //Снять бронь
    public TechServResponseDTO cancelReserveTechServ(Long techServId) {
        /*
        TODO проверить по id, забронированна ли услуга,
        если есть бронь - убрать дату бронирования, id пользователя и поменять флаг reserved = false
        если нет брони - уведомить (Выкинуть исключение?)
        */

        return null;
    }

    //Обновить время брони
    public TechServResponseDTO updateSignetAt(TechServRequestDTO techServRequestDTO) {
        //TODO найти по id в БД и обновить время бронирования
        return null;
    }

    //Обновить услугу в бд
    public TechServResponseDTO updateTechServ(Long techServId, TechServRequestDTO techServRequestDTO) {
        //TODO забрать из БД по id и обновить данными
        return null;
    }

    //Удалить услугу
    public TechServResponseDTO deleteTechServ(Long techServId) {
        //TODO найти запись в БД по id и пометить ее, как удаленную (remove = true)
        return null;
    }


}
