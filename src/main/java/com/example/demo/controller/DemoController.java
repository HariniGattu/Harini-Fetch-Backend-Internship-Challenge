package com.example.demo.controller;

import com.example.demo.model.AddRequest;
import com.example.demo.model.SpendRequest;
import com.example.demo.model.SpendResponse;
import com.example.demo.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class DemoController {

    private DemoService demoService;

    @Autowired
    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @PostMapping(value = "/add")
    public List<AddRequest> add(@RequestBody AddRequest addRequest) {
        return demoService.add(addRequest);
    }

    @PostMapping(value = "/spend")
    public List<SpendResponse> spend(@RequestBody SpendRequest spendRequest) {
        return demoService.spend(spendRequest);
    }

    @GetMapping(value = "/balance")
    public List<String> balance() {
        return demoService.balance();
    }
}
