package com.degaltseva.carrental.repository;

import com.degaltseva.carrental.model.CarCategory;
import com.degaltseva.carrental.util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarCategoryRepository implements BaseRepository<CarCategory> {

    private final ConnectionPool pool = ConnectionPool.getInstance();

    private CarCategory mapRow(ResultSet rs) throws SQLException {
        CarCategory cat = new CarCategory();
        cat.setId(rs.getLong("id"));
        cat.setCategoryName(rs.getString("category_name"));
        cat.setDescription(rs.getString("description"));
        cat.setBaseCost(rs.getBigDecimal("base_cost"));
        return cat;
    }

    @Override
    public List<CarCategory> findAll() {
        List<CarCategory> list = new ArrayList<>();
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM car_categories ORDER BY id")) {
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
    public Optional<CarCategory> findById(Long id) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM car_categories WHERE id = ?")) {
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
    public CarCategory save(CarCategory cat) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO car_categories (category_name, description, base_cost) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cat.getCategoryName());
            ps.setString(2, cat.getDescription());
            ps.setBigDecimal(3, cat.getBaseCost());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                cat.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return cat;
    }

    @Override
    public CarCategory update(CarCategory cat) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE car_categories SET category_name = ?, description = ?, base_cost = ? WHERE id = ?")) {
            ps.setString(1, cat.getCategoryName());
            ps.setString(2, cat.getDescription());
            ps.setBigDecimal(3, cat.getBaseCost());
            ps.setLong(4, cat.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return cat;
    }

    @Override
    public void delete(Long id) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM car_categories WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
    }
}
