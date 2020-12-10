package com.fds.opp.app.controller;

import com.fds.opp.app.repository.ProjectRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v3/telegram/notify")
public class WebhookController {
    @Autowired
    private ProjectRepository projectRepository;
    @PostMapping("/create")
    public void newRequest(@RequestBody String JsonString) throws Exception
    {
        System.out.println(JsonString);
        JSONObject request = new JSONObject(JsonString);
    }
}
