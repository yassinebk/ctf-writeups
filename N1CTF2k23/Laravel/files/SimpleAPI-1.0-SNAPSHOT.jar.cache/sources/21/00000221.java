package com.api;

import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping({"/api"})
@RestController
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/classes/com/api/APIController.class */
public class APIController {
    @GetMapping({"/"})
    public String index() throws Exception {
        return "try /api/com.api.Person/eyJuYW1lIjoidXNlciIsImFnZSI6IjIwIn0=";
    }

    @GetMapping({"/{Person}/{Json}"})
    public Person handleApiRequest(@PathVariable String Person, @PathVariable String Json) throws Exception {
        Gson gson = new Gson();
        Person person = (Person) gson.fromJson(new String(Base64.getDecoder().decode(Json), StandardCharsets.UTF_8), (Class<Object>) Class.forName(Person));
        return person;
    }
}