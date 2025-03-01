package com.mos.backend.apidocs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApiDocsController {

    @GetMapping("/docs/api")
    public String getApiDocs() {
        return "api.html";
    }
}
