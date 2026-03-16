package com.degaltseva.carrental.repository;

import com.degaltseva.carrental.model.Violation;
import com.degaltseva.carrental.util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ViolationRepository implements BaseRepository<Violation> {

    private final ConnectionPool pool = ConnectionPool.getInstance();

    private Violation mapRow(ResultSet rs) throws SQLException {
        Violation v = new Violation();
        v.setId(rs.getLong("id"));
        v.setRentalId(rs.getLong("rental_id"));
        v.setViolationTypeId(rs.getLong("violation_type_id"));
        v.setDescription(rs.getString("description"));
        v.setFineAmount(rs.getBigDecimal("fine_amount"));
        v.setViolationDate(rs.getTimestamp("violation_date").toLocalDateTime());
        v.setPaid(rs.getBoolean("is_paid"));
        return v;
    }

    @Override
    public List<Violation> findAll() {
        List<Violation> list = new ArrayList<>();
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM violations ORDER BY id")) {
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
    public Optional<Violation> findById(Long id) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM violations WHERE id = ?")) {
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

    public List<Violation> findByRentalId(Long rentalId) {
        List<Violation> list = new ArrayList<>();
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM violations WHERE rental_id = ? ORDER BY id")) {
            ps.setLong(1, rentalId);
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

    public List<Violation> findByUserId(Long userId) {
        List<Violation> list = new ArrayList<>();
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT v.* FROM violations v " +
                        "JOIN rentals r ON v.rental_id = r.id " +
                        "JOIN clients c ON r.client_id = c.id " +
                        "WHERE c.user_id = ? ORDER BY v.id")) {
            ps.setLong(1, userId);
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

    public Optional<Violation> findByUserIdAndViolationId(Long userId, Long violationId) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT v.* FROM violations v " +
                        "JOIN rentals r ON v.rental_id = r.id " +
                        "JOIN clients c ON r.client_id = c.id " +
                        "WHERE c.user_id = ? AND v.id = ?")) {
            ps.setLong(1, userId);
            ps.setLong(2, violationId);
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
    public Violation save(Violation v) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO violations (rental_id, violation_type_id, description, fine_amount, violation_date, is_paid) " +
                        "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, v.getRentalId());
            ps.setLong(2, v.getViolationTypeId());
            ps.setString(3, v.getDescription());
            ps.setBigDecimal(4, v.getFineAmount());
            ps.setTimestamp(5, Timestamp.valueOf(v.getViolationDate()));
            ps.setBoolean(6, v.isPaid());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                v.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return v;
    }

    @Override
    public Violation update(Violation v) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE violations SET rental_id = ?, violation_type_id = ?, description = ?, " +
                        "fine_amount = ?, violation_date = ?, is_paid = ? WHERE id = ?")) {
            ps.setLong(1, v.getRentalId());
            ps.setLong(2, v.getViolationTypeId());
            ps.setString(3, v.getDescription());
            ps.setBigDecimal(4, v.getFineAmount());
            ps.setTimestamp(5, Timestamp.valueOf(v.getViolationDate()));
            ps.setBoolean(6, v.isPaid());
            ps.setLong(7, v.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return v;
    }

    @Override
    public void delete(Long id) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM violations WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
    }
}
