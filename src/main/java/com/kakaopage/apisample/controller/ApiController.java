package com.kakaopage.apisample.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/hello")
    public String hello() {
        log.debug("hello");
        return "hello-world";
    }

    @GetMapping("/hello2")
    public String hello2() {
        log.debug("hello2");
        return "hello-world2222222";
    }

    @GetMapping("/{seed}")
    public HashMap<String, String> getHash(@PathVariable String seed) throws NoSuchAlgorithmException {
        log.debug("Call hash method seed : {}", seed);
        String hashData = getHashData(seed);
        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("seed", seed);
        resultMap.put("hash", hashData);
        return resultMap;
    }

    private String getHashData(String seed) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(seed.getBytes(StandardCharsets.UTF_8));
        return HexUtils.toHexString(md.digest());
    }

}
