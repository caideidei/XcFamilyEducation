package com.example.familyeducation.lottery.controller;

import com.example.familyeducation.lottery.service.PrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prize")
public class PrizeController {

    @Autowired
    private PrizeService prizeService;

    /**
     * 奖品预热controller方法-预热奖品
     */
    @PostMapping("/preload")
    public ResponseEntity<String> preloadPrizePool() {
        prizeService.preloadPrizePool();
        return ResponseEntity.ok("奖池预热成功");
    }
}
