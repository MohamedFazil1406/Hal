package com.hal.agent;

import java.lang.instrument.Instrumentation;

public class HALAgent {

    public static void premain(
            String agentArgs,
            Instrumentation instrumentation) {

        System.out.println(
                "================================"
        );

        System.out.println(
                "       HAL AGENT STARTED"
        );

        System.out.println(
                "================================"
        );

        instrumentation.addTransformer(
                new HALTransformer()
        );

        System.out.println(
                "HAL exception transformer installed."
        );
    }
}