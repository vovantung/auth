package txu.auth.mainapp.api;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowCredentials = "false", maxAge = 86400, allowedHeaders = "*")
@RestController
public class TestApi {
    @PostMapping(value = "/test")
    public String test() {
//        throw new TxException("test");

        return "test";
    }
}
