package xu.environment.jdbc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @GetMapping
    public String test() {
        String sql = "select name from test where id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        return jdbcTemplate.queryForObject(sql, params, String.class);
    }
}
