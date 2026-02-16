package com.unina.foodlab.Database.Dao;

import java.sql.SQLException;
import java.util.Map;

public interface ReportDaoPort {
    int contaCorsi(String emailChef) throws SQLException;

    Map<String, Integer> contaSessioniPerTipo(String emailChef) throws SQLException;

    double[] getStatisticheRicette(String emailChef) throws SQLException;
}
