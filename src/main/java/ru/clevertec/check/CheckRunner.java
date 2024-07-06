package ru.clevertec.check;

import ru.clevertec.check.controller.CheckController;
import ru.clevertec.check.exception.CheckException;
import ru.clevertec.check.util.CsvUtil;

import static ru.clevertec.check.controller.CheckController.RESULTS_FILE_PATH;

public class CheckRunner {
    public static void main(String[] args) {
        CheckController checkController = new CheckController();
        try {
            checkController.create(args);
        } catch (CheckException e) {
            CsvUtil.saveError(RESULTS_FILE_PATH, e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}