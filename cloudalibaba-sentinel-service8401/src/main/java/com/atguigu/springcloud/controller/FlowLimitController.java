package com.atguigu.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;


@RestController
@Slf4j
public class FlowLimitController {
    @GetMapping("/testA")
    public String testA() {
        try {
            TimeUnit.MILLISECONDS.sleep(800);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(Thread.currentThread().getName() + "\t" + "testA");
        return "testA";
    }

    @GetMapping("/testB")
    public String testB() {
        log.info(Thread.currentThread().getName() + "\t" + "testB");
        return "testB";
    }

    @GetMapping("/testD")
    public String testD() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("testD 测试 RT");
        int i = 1 / 0;
        return "testD";
    }


    @GetMapping("/testHotKey")
    @SentinelResource(value/*标识下面的资源,要唯一*/ = "testHotKey", blockHandler/*兜底方法*/ = "deal_testHotKey")
    public String testHotKey(@RequestParam(value = "p1", required = false) String p1,
                             @RequestParam(value = "p2", required = false) String p2) {
        //int i = 1 / 0;
        return "testHotKey";
    }

    /*兜底方法*/
    public String deal_testHotKey(String p1, String p2, BlockException exception) {
        return "deal_testHotKey";  //sentinel系统默认的提示：Blocked by Sentinel (flow limiting)
    }
}
