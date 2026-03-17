package com.degaltseva.carrental.repository;

import com.degaltseva.carrental.model.Rental;
import com.degaltseva.carrental.util.ConnectionPool;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RentalRepository implements BaseRepository<Rental> {

    private final ConnectionPool pool = ConnectionPool.getInstance();

    private Rental mapRow(ResultSet rs) throws SQLException {
        Rental rental = new Rental();
        rental.setId(rs.getLong("id"));
        rental.setClientId(rs.getLong("client_id"));
        rental.setCarId(rs.getLong("car_id"));
        rental.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
        rental.setEndDate(rs.getTimestamp("end_date").toLocalDateTime());
        rental.setTotalAmount(rs.getBigDecimal("total_amount"));
        rental.setRentalStatusId(rs.getLong("rental_status_id"));
        return rental;
    }

    @Override
    public List<Rental> findAll() {
        List<Rental> rentals = new ArrayList<>();
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM rentals ORDER BY id")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rentals.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return rentals;
    }

    @Override
    public Optional<Rental> findById(Long id) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM rentals WHERE id = ?")) {
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

    public List<Rental> findByClientId(Long clientId) {
        List<Rental> rentals = new ArrayList<>();
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM rentals WHERE client_id = ? ORDER BY id")) {
            ps.setLong(1, clientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rentals.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return rentals;
    }

    public List<Rental> findByUserId(Long userId) {
        List<Rental> rentals = new ArrayList<>();
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT r.* FROM rentals r JOIN clients c ON r.client_id = c.id WHERE c.user_id = ? ORDER BY r.id")) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rentals.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return rentals;
    }

    public Optional<Rental> findByUserIdAndRentalId(Long userId, Long rentalId) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT r.* FROM rentals r JOIN clients c ON r.client_id = c.id " +
                        "WHERE c.user_id = ? AND r.id = ?")) {
            ps.setLong(1, userId);
            ps.setLong(2, rentalId);
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

    public List<Rental> findExpiredActive(Long activeStatusId) {
        List<Rental> rentals = new ArrayList<>();
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM rentals WHERE rental_status_id = ? AND end_date < NOW()")) {
            ps.setLong(1, activeStatusId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rentals.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return rentals;
    }

    public void updateStatus(Long rentalId, Long rentalStatusId) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE rentals SET rental_status_id = ? WHERE id = ?")) {
            ps.setLong(1, rentalStatusId);
            ps.setLong(2, rentalId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
    }

    public List<Rental> filter(Long carId, Long clientId) {
        List<Rental> rentals = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM rentals WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (carId != null) {
            sql.append(" AND car_id = ?");
            params.add(carId);
        }
        if (clientId != null) {
            sql.append(" AND client_id = ?");
            params.add(clientId);
        }
        sql.append(" ORDER BY id");

        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rentals.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return rentals;
    }

    public List<Rental> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Rental> rentals = new ArrayList<>();
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM rentals WHERE start_date >= ? AND end_date <= ? ORDER BY id")) {
            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rentals.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return rentals;
    }

    public int countRentalsWithViolations(LocalDateTime startDate, LocalDateTime endDate) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(DISTINCT v.rental_id) FROM violations v " +
                        "JOIN rentals r ON v.rental_id = r.id " +
                        "WHERE r.start_date >= ? AND r.end_date <= ?")) {
            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return 0;
    }

    @Override
    public Rental save(Rental rental) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO rentals (client_id, car_id, start_date, end_date, total_amount, rental_status_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, rental.getClientId());
            ps.setLong(2, rental.getCarId());
            ps.setTimestamp(3, Timestamp.valueOf(rental.getStartDate()));
            ps.setTimestamp(4, Timestamp.valueOf(rental.getEndDate()));
            ps.setBigDecimal(5, rental.getTotalAmount());
            ps.setLong(6, rental.getRentalStatusId());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                rental.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return rental;
    }

    @Override
    public Rental update(Rental rental) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE rentals SET car_id = ?, start_date = ?, end_date = ?, total_amount = ?, rental_status_id = ? WHERE id = ?")) {
            ps.setLong(1, rental.getCarId());
            ps.setTimestamp(2, Timestamp.valueOf(rental.getStartDate()));
            ps.setTimestamp(3, Timestamp.valueOf(rental.getEndDate()));
            ps.setBigDecimal(4, rental.getTotalAmount());
            ps.setLong(5, rental.getRentalStatusId());
            ps.setLong(6, rental.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return rental;
    }

    @Override
    public void delete(Long id) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM rentals WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
    }
}
