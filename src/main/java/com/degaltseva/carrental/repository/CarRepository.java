package com.degaltseva.carrental.repository;

import com.degaltseva.carrental.model.Car;
import com.degaltseva.carrental.util.ConnectionPool;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarRepository implements BaseRepository<Car> {

    private final ConnectionPool pool = ConnectionPool.getInstance();

    private Car mapRow(ResultSet rs) throws SQLException {
        Car car = new Car();
        car.setId(rs.getLong("id"));
        car.setBrand(rs.getString("brand"));
        car.setModel(rs.getString("model"));
        car.setYear(rs.getInt("year"));
        car.setCategoryId(rs.getLong("category_id"));
        car.setLicensePlate(rs.getString("license_plate"));
        car.setColorId(rs.getLong("color_id"));
        car.setDailyCost(rs.getBigDecimal("daily_cost"));
        car.setCarStatusId(rs.getLong("car_status_id"));
        return car;
    }

    @Override
    public List<Car> findAll() {
        List<Car> cars = new ArrayList<>();
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM cars ORDER BY id")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cars.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return cars;
    }

    @Override
    public Optional<Car> findById(Long id) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM cars WHERE id = ?")) {
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
    public Car save(Car car) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO cars (brand, model, year, category_id, license_plate, color_id, daily_cost, car_status_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, car.getBrand());
            ps.setString(2, car.getModel());
            ps.setInt(3, car.getYear());
            ps.setLong(4, car.getCategoryId());
            ps.setString(5, car.getLicensePlate());
            ps.setLong(6, car.getColorId());
            ps.setBigDecimal(7, car.getDailyCost());
            ps.setLong(8, car.getCarStatusId());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                car.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return car;
    }

    @Override
    public Car update(Car car) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE cars SET brand = ?, model = ?, year = ?, category_id = ?, license_plate = ?, " +
                        "color_id = ?, daily_cost = ?, car_status_id = ? WHERE id = ?")) {
            ps.setString(1, car.getBrand());
            ps.setString(2, car.getModel());
            ps.setInt(3, car.getYear());
            ps.setLong(4, car.getCategoryId());
            ps.setString(5, car.getLicensePlate());
            ps.setLong(6, car.getColorId());
            ps.setBigDecimal(7, car.getDailyCost());
            ps.setLong(8, car.getCarStatusId());
            ps.setLong(9, car.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return car;
    }

    public void updateStatus(Long carId, Long carStatusId) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE cars SET car_status_id = ? WHERE id = ?")) {
            ps.setLong(1, carStatusId);
            ps.setLong(2, carId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
    }

    public List<Car> filter(String brand, String model, Long categoryId, Long colorId,
                            Integer minYear, Integer maxYear, BigDecimal minCost, BigDecimal maxCost) {
        List<Car> cars = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM cars WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (brand != null && !brand.isBlank()) {
            sql.append(" AND LOWER(brand) LIKE ?");
            params.add("%" + brand.toLowerCase() + "%");
        }
        if (model != null && !model.isBlank()) {
            sql.append(" AND LOWER(model) LIKE ?");
            params.add("%" + model.toLowerCase() + "%");
        }
        if (categoryId != null) {
            sql.append(" AND category_id = ?");
            params.add(categoryId);
        }
        if (colorId != null) {
            sql.append(" AND color_id = ?");
            params.add(colorId);
        }
        if (minYear != null) {
            sql.append(" AND year >= ?");
            params.add(minYear);
        }
        if (maxYear != null) {
            sql.append(" AND year <= ?");
            params.add(maxYear);
        }
        if (minCost != null) {
            sql.append(" AND daily_cost >= ?");
            params.add(minCost);
        }
        if (maxCost != null) {
            sql.append(" AND daily_cost <= ?");
            params.add(maxCost);
        }
        sql.append(" ORDER BY id");

        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cars.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return cars;
    }

    @Override
    public void delete(Long id) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM cars WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
    }
}
