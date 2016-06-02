package com.ideamos.web.questionit.Database;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Creado por Deimer Villa on 12/1/2016.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {

    public static void main(String[] args)throws IOException, SQLException {
        writeConfigFile("ormlite_config.txt");
    }

}