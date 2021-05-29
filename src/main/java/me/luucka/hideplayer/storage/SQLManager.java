package me.luucka.hideplayer.storage;

import me.luucka.hideplayer.HidePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLManager {

    public static void init() {
        createTableUser();
        createTableKeepvisible();
    }

    // Table USER
    //------------------------------------------------------------------------------------------------------------------

    private static void createTableUser() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = HidePlayer.getPlugin().getHikari().getConnection();
            ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS user (" +
                    "UUID VARCHAR(36)," +
                    "VISIBLE BOOLEAN," +
                    "PRIMARY KEY (UUID))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, ps);
        }
    }

    public static boolean userExists(UUID uuid) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = HidePlayer.getPlugin().getHikari().getConnection();
            ps = connection.prepareStatement("SELECT * FROM user WHERE UUID=?");
            ps.setString(1, uuid.toString());
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, ps);
        }
        return false;
    }

    public static void createUser(Player player) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            if (userExists(player.getUniqueId())) return;
            connection = HidePlayer.getPlugin().getHikari().getConnection();
            ps = connection.prepareStatement("INSERT INTO user (UUID, VISIBLE) VALUES (?,?)");
            ps.setString(1, player.getUniqueId().toString());
            ps.setBoolean(2, true);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, ps);
        }
    }

    public static boolean getVisible(UUID uuid) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = HidePlayer.getPlugin().getHikari().getConnection();
            ps = connection.prepareStatement("SELECT VISIBLE FROM user WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            boolean visible;
            if (rs.next()) {
                visible = rs.getBoolean("VISIBLE");
                return visible;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, ps);
        }
        return true;
    }

    public static void setVisible(UUID uuid, boolean visible) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = HidePlayer.getPlugin().getHikari().getConnection();
            ps = connection.prepareStatement("UPDATE user SET VISIBLE=? WHERE UUID=?");
            ps.setBoolean(1, visible);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, ps);
        }
    }

    // Table KEEPVISIBLE
    //------------------------------------------------------------------------------------------------------------------

    private static void createTableKeepvisible() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = HidePlayer.getPlugin().getHikari().getConnection();
            ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS keepvisible (" +
                    "MY_UUID VARCHAR(36)," +
                    "OTHER_UUID VARCHAR(36)," +
                    "PRIMARY KEY (MY_UUID, OTHER_UUID))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, ps);
        }
    }

    public static List<String> getKeepvisibleList(UUID myUUID) {
        List<String> myList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = HidePlayer.getPlugin().getHikari().getConnection();
            ps = connection.prepareStatement("SELECT OTHER_UUID FROM keepvisible WHERE MY_UUID=?");
            ps.setString(1, myUUID.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                myList.add(rs.getString("OTHER_UUID"));
            }
            return myList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, ps);
        }
        return null;
    }

    public static boolean isPlayerInKeepvisibleList(UUID myUUID, UUID otherUUID) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = HidePlayer.getPlugin().getHikari().getConnection();
            ps = connection.prepareStatement("SELECT * FROM keepvisible WHERE MY_UUID=? AND OTHER_UUID=?");
            ps.setString(1, myUUID.toString());
            ps.setString(2, otherUUID.toString());
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, ps);
        }
        return false;
    }

    public static void addKeepvisiblePlayer(UUID myUUID, UUID otherUUID) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = HidePlayer.getPlugin().getHikari().getConnection();
            ps = connection.prepareStatement("INSERT INTO keepvisible (MY_UUID, OTHER_UUID) VALUES (?,?)");
            ps.setString(1, myUUID.toString());
            ps.setString(2, otherUUID.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, ps);
        }
    }

    public static void removeKeepvisiblePlayer(UUID myUUID, UUID otherUUID) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = HidePlayer.getPlugin().getHikari().getConnection();
            ps = connection.prepareStatement("DELETE FROM keepvisible WHERE MY_UUID=? AND OTHER_UUID=?");
            ps.setString(1, myUUID.toString());
            ps.setString(2, otherUUID.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, ps);
        }
    }

    public static void resetKeepvisiblePlayer(UUID myUUID) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = HidePlayer.getPlugin().getHikari().getConnection();
            ps = connection.prepareStatement("DELETE FROM keepvisible WHERE MY_UUID=?");
            ps.setString(1, myUUID.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, ps);
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    public static void close(Connection conn, PreparedStatement ps) {
        if (conn != null)
            try {
                conn.close();
            } catch (SQLException ignored) {}
        if (ps != null)
            try {
                ps.close();
            } catch (SQLException ignored) {}
    }
}
