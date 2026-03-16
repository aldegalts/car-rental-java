package com.degaltseva.carrental.repository;

import com.degaltseva.carrental.model.ViolationType;
import com.degaltseva.carrental.util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ViolationTypeRepository implements BaseRepository<ViolationType> {

    private final ConnectionPool pool = ConnectionPool.getInstance();

    private ViolationType mapRow(ResultSet rs) throws SQLException {
        ViolationType vt = new ViolationType();
        vt.setId(rs.getLong("id"));
        vt.setTypeName(rs.getString("type_name"));
        vt.setDefaultFine(rs.getBigDecimal("default_fine"));
        vt.setDescription(rs.getString("description"));
        return vt;
    }

    @Override
    public List<ViolationType> findAll() {
        List<ViolationType> list = new ArrayList<>();
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM violation_types ORDER BY id")) {
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
    public Optional<ViolationType> findById(Long id) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM violation_types WHERE id = ?")) {
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
    public ViolationType save(ViolationType vt) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO violation_types (type_name, default_fine, description) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, vt.getTypeName());
            ps.setBigDecimal(2, vt.getDefaultFine());
            ps.setString(3, vt.getDescription());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                vt.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return vt;
    }

    @Override
    public ViolationType update(ViolationType vt) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE violation_types SET type_name = ?, default_fine = ?, description = ? WHERE id = ?")) {
            ps.setString(1, vt.getTypeName());
            ps.setBigDecimal(2, vt.getDefaultFine());
            ps.setString(3, vt.getDescription());
            ps.setLong(4, vt.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
        return vt;
    }

    @Override
    public void delete(Long id) {
        Connection conn = pool.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM violation_types WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.releaseConnection(conn);
        }
    }
}
