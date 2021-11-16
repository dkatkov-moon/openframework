package eu.ibagroup.tasks;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import eu.ibagroup.easyrpa.engine.annotation.AfterInit;
import eu.ibagroup.easyrpa.engine.annotation.ApTaskEntry;
import eu.ibagroup.easyrpa.engine.apflow.ApTask;
import eu.ibagroup.easyrpa.openframework.database.service.PostgresService;
import eu.ibagroup.entity.MySqlInvoice;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static eu.ibagroup.ap.Constants.DB_DATE_PATTERN;


@Slf4j
@ApTaskEntry(name = "Print table records with the invoice_date newer than 2016-01-01", description = "Print 'rpa.invoices' table content")
public class PrintTableContentOrm extends ApTask {
    String query = "SELECT * FROM rpa.invoices";
    @Inject
    PostgresService dbService;

    @AfterInit
    public void init() {
    }

    @Override
    public void execute() throws SQLException, ParseException {

        ConnectionSource connectionSource = dbService.initOrmConnection().getOrmConnectionSource();

        Dao<MySqlInvoice, Integer> invoicesDao = DaoManager.createDao(connectionSource, MySqlInvoice.class);

        QueryBuilder<MySqlInvoice, Integer> queryBuilder = invoicesDao.queryBuilder();
        queryBuilder.where().ge("invoice_date", new SimpleDateFormat(DB_DATE_PATTERN).parse("2016-01-01"));
        PreparedQuery<MySqlInvoice> preparedQuery = queryBuilder.prepare();

        List<MySqlInvoice> accountList = invoicesDao.query(preparedQuery);
        accountList.forEach(a -> {
            log.info("id: {}; invoice#: {}; invoice_date: {}; customer_name: {}; amount: {}",
                    a.getId(), a.getInvoiceNumber(), a.getInvoiceDate(), a.getCustomerName(), a.getAmount());
        });
    }
}