package main.java.ru.clevertec.check;

import main.java.ru.clevertec.check.controller.CheckController;
import main.java.ru.clevertec.check.exception.CheckException;
import main.java.ru.clevertec.check.util.CsvUtil;

import static main.java.ru.clevertec.check.controller.CheckController.RESULTS_FILE_PATH;

public class CheckRunner {
    public static void main(String[] args) {
        CheckController checkController = new CheckController();
        try {
            checkController.create(args);
        } catch (CheckException e) {
            CsvUtil.saveError(RESULTS_FILE_PATH, e.getMessage());
        }
    }
}