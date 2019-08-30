package org.tangxi.testplatform.controller;


import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tangxi.testplatform.log.LoggerQueue;
import org.tangxi.testplatform.model.LoggerMessage;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Controller
public class ExecutionLogController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

//    @PostConstruct
//    public void log()  {
//    ExecutorService executorService = Executors.newFixedThreadPool(1);
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            while (true) {
//                try {
//                    LoggerMessage log = LoggerQueue.getInstance().poll();
//                    if (log != null) {
//                        if (messagingTemplate != null)
//                            Thread.sleep(500);
//                            messagingTemplate.convertAndSend("/topic/greetings", log);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    };
//    executorService.submit(runnable);
//}
}


