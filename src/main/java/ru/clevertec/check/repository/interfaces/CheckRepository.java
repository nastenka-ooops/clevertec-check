package ru.clevertec.check.repository.interfaces;

import ru.clevertec.check.entity.Check;

import java.util.List;

public interface CheckRepository {
    void saveCheck(Check check);
    List<Check> getAllChecks();
}
