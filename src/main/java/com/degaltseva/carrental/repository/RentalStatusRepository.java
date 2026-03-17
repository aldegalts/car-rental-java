package com.degaltseva.carrental.repository;

import com.degaltseva.carrental.model.RentalStatus;
import com.degaltseva.carrental.util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RentalStatusRepository implements BaseRepository<RentalStatus> {

    private final ConnectionPool pool = ConnectionPool.getInstance();

    private RentalStatus mapRow(ResultSet rs) throws SQLException {
        RentalStatus status = new RentalStatus();
        status.setId(rs.getLong("id"));
        status.setStatus(rs.getString("status"));
        return status;
    }

    @Override
    public List<RentalStatus> findAll() {
        List<RentalStatus> list = new ArrayList<>();
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM rental_statuses ORDER BY id")) {
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
    public Optional<RentalStatus> findById(Long id) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM rental_statuses WHERE id = ?")) {
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

    public Optional<RentalStatus> findByStatus(String status) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM rental_statuses WHERE status = ?")) {
            ps.setString(1, status);
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
    public RentalStatus save(RentalStatus status) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO rental_statuses (status) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, status.getStatus());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                status.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return status;
    }

    @Override
    public RentalStatus update(RentalStatus status) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE rental_statuses SET status = ? WHERE id = ?")) {
            ps.setString(1, status.getStatus());
            ps.setLong(2, status.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return status;
    }

    @Override
    public void delete(Long id) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM rental_statuses WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
    }
}
