package org.edelweiss.logging.controller;//package com.neon.logdemo.log.controller;
//
//import com.uniubi.visitor.log.annotation.LogOperation;
//import com.uniubi.visitor.log.pojo.OperationType;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@Slf4j
//@RequestMapping(path = "/log/test")
//@LogOperation(successTemplate = "操作人: {#operator#}")
//public class LogOperationTestController {
//
//    @Data
//    @NoArgsConstructor
//    static class TestReq {
//        private String id;
//        private String name;
//    }
//
//    @LogOperation(operationType = OperationType.LOG, successTemplate = "查询参数: {#msg#}, 接口结果: [#result#]", failTemplate = "查询异常")
//    @GetMapping(path = "/test_1")
//    public String test_1(@RequestParam("msg") String msg) {
//        return "success";
//    }
//
//    @LogOperation(operationType = OperationType.EXCEPTION_HANDLE, successTemplate = "创建参数: {#testReq.id#}, {#testReq.name#}, 入参修改: [#testReq.name#], 接口结果: [#result#]", failTemplate = "创建异常")
//    @PostMapping(path = "/test_2")
//    public String test_2(@RequestBody TestReq testReq) {
//        testReq.setName("changed:" + testReq.getName());
//        return "success";
//    }
//
//    @LogOperation(operator = "测试员1号", operationType = OperationType.ACCOUNT_MANAGE, successTemplate = "修改参数: {#testReq.id#}, {#testReq.name#}, 入参修改: [#testReq.name#], 接口结果: [#result#]", failTemplate = "修改异常")
//    @PostMapping(path = "/test_3")
//    public String test_3(@RequestBody TestReq testReq) {
//        testReq.setName("changed:" + testReq.getName());
//        return "success";
//    }
//
//    @LogOperation(operationType = OperationType.ITINERARY_CARD, successTemplate = "查询参数: {#msg#}, 接口结果: [#result#]", failTemplate = "查询异常")
//    @GetMapping(path = "/test_4")
//    public String test_4(@RequestParam("msg") String msg) {
//        return "success";
//    }
//}
