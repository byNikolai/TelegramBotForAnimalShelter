package Telegram.Bot.AnimalShelter.service;

import Telegram.Bot.AnimalShelter.entity.Report;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    Report create(Report report);

    Report createWithTelegramData(String photoId, String caption, Long id);

    Report update(Report report);

    Report getById(Long id);

    List<Report> getByAdaptationPeriodId(Long id);

    Report getByDateAndAdaptationPeriodId(LocalDate date, Long id);

    List<Report> getAll();

    void delete(Report report);

    void deleteById(Long id);

}
