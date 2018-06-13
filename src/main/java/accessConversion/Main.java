package accessConversion;

import com.healthmarketscience.jackcess.*;

import java.io.*;
import java.util.Set;

public class Main {

    /**
     * convert an access database to csv
     *
     * @param args the database to convert
     */
    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            System.out.println("takes one parameter: /path/to/access-db-to-convert");
            System.exit(0);
        }

        String inputFilename = args[0];
        if (!new File(inputFilename).exists()) {
            throw new IOException("access db does not exist: " + inputFilename);
        }

        Database db;

        File dbFile = new File(inputFilename);
        if(dbFile.exists() && !dbFile.isDirectory()) {
            db = DatabaseBuilder.open(dbFile);
        } else {
            db = DatabaseBuilder.create(Database.FileFormat.V2007, dbFile);
        }
        String parts[] = inputFilename.split("/");
        String lastPart = parts[parts.length - 1];
        parts = lastPart.split("\\.");
        String prefix = parts[0];

        Set<String> tables = db.getTableNames();
        for (String t : tables) {

            // create a unique filename for each table name
            StringBuilder filenameSb = new StringBuilder();
            filenameSb.append(prefix).append("-");
            for (char ch : t.toLowerCase().toCharArray()) {
                if (ch >= '0' && ch <= '9') {
                    filenameSb.append(ch);
                } else if (ch >= 'a' && ch <= 'z') {
                    filenameSb.append(ch);
                } else {
                    filenameSb.append("_");
                }
            }
            filenameSb.append(".csv");

            try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(filenameSb.toString())), "UTF-8"))) {

                out.write("\n[" + t + "]\n");
                Table tbl = db.getTable(t);
                System.out.println("converting table \"" + t + "\"");

                // Add all columns from table
                StringBuilder sb = new StringBuilder();
                for (Column col : tbl.getColumns()) {
                    sb.append(col.getName()).append(",");
                }
                if (sb.length() > 0) {
                    sb.setLength(sb.length() - 1);
                }
                sb.append("\n");
                out.write(sb.toString());

                // Add all rows from table
                for (Row row : tbl) {
                    sb.setLength(0);
                    for (Object value : row.values().toArray()) {
                        if (value == null) {
                            sb.append("NULL").append(",");
                        } else {
                            String valueStr = value.toString();
                            if (valueStr.contains(",")) {
                                valueStr = "\"" + valueStr + "\"";
                            }
                            sb.append(valueStr).append(",");
                        }
                    }
                    if (sb.length() > 0) {
                        sb.setLength(sb.length() - 1);
                    }
                    sb.append("\n");
                    out.write(sb.toString());
                }

            }

        }


    }

}
