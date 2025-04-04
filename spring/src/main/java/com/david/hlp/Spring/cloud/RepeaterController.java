package com.david.hlp.Spring.cloud;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class RepeaterController {

    private final RepeaterServiceImp repeaterServiceImp;

    @GetMapping("/repeater/get")
    public Object repeater(@RequestParam Map<String, Object> params) {
        return repeaterServiceImp.repeaterGet(params);
    }

    @PostMapping("/repeater/post")
    public Object repeaterPost(@RequestBody Map<String, Object> request) {
        return repeaterServiceImp.repeaterPost(request);
    }

    @DeleteMapping("/repeater/delete")
    public String repeaterDelete(@RequestBody Map<String, Object> request) {
        return repeaterServiceImp.repeaterDelete(request);
    }

    @PutMapping("/repeater/put")
    public String repeaterPut(@RequestBody Map<String, Object> request) {
        return repeaterServiceImp.repeaterPut(request);
    }
}
