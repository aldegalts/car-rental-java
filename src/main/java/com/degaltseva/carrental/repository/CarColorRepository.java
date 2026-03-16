package com.degaltseva.carrental.repository;

import com.degaltseva.carrental.model.CarColor;
import com.degaltseva.carrental.util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarColorRepository implements BaseRepository<CarColor> {

    private final ConnectionPool pool = ConnectionPool.getInstance();

    private CarColor mapRow(ResultSet rs) throws SQLException {
        CarColor color = new CarColor();
        color.setId(rs.getLong("id"));
        color.setColor(rs.getString("color"));
        color.setHex(rs.getString("hex"));
        return color;
    }

    @Override
    public List<CarColor> findAll() {
        List<CarColor> list = new ArrayList<>();
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM car_colors ORDER BY id")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return list;
    }

    @Override
    public Optional<CarColor> findById(Long id) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM car_colors WHERE id = ?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public CarColor save(CarColor color) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO car_colors (color, hex) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, color.getColor());
            ps.setString(2, color.getHex());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                color.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return color;
    }

    @Override
    public CarColor update(CarColor color) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE car_colors SET color = ?, hex = ? WHERE id = ?")) {
            ps.setString(1, color.getColor());
            ps.setString(2, color.getHex());
            ps.setLong(3, color.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return color;
    }

    @Override
    public void delete(Long id) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM car_colors WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
    }
}
