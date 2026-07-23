import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlConcatenated {
    private Connection connection;

    private String getCategory() {
        return "";
    }

    void run() throws SQLException {
{
    // BAD: the category might have SQL special characters in it
    String category = getCategory();
    Statement statement = connection.createStatement();
    String query1 = "SELECT ITEM,PRICE FROM PRODUCT WHERE ITEM_CATEGORY='"
        + category + "' ORDER BY PRICE";
    ResultSet results = statement.executeQuery(query1);
}

{
    // GOOD: use a prepared query
    String category = getCategory();
    String query2 = "SELECT ITEM,PRICE FROM PRODUCT WHERE ITEM_CATEGORY=? ORDER BY PRICE";
    PreparedStatement statement = connection.prepareStatement(query2);
    statement.setString(1, category);
    ResultSet results = statement.executeQuery();
}
    }
}
