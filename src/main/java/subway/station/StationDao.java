package subway.station;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.exceptions.DuplicateStationNameException;

import java.util.List;

@Repository
public class StationDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Station> stationRowMapper = (resultSet, rowNum) -> {
        Station station = new Station(
                resultSet.getLong("id"),
                resultSet.getString("name")
        );
        return station;
    };

    public Station save(Station station) {
        String sql = "insert into STATION(name) VALUES (?)";
        try {
            jdbcTemplate.update(sql, station.getName());
        } catch (Exception e) {
            throw new DuplicateStationNameException("중복된 역 이름입니다.");
        }
        return jdbcTemplate.queryForObject("select id, name from STATION where name = ?", stationRowMapper, station.getName());
    }

    public List<Station> findAll() {
        String sql = "select id, name from station limit 20";
        return jdbcTemplate.query(sql, stationRowMapper);
    }

    public int deleteById(long id) {
        String sql = "delete from station where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public Station findById(long id) {
        String sql = "select id, name from station where id = ?";
        return jdbcTemplate.queryForObject(sql, stationRowMapper, id);
    }
}
