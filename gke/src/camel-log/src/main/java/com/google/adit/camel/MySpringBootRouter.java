package com.google.adit.camel;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * A simple Camel route that triggers from a timer and calls a bean and prints to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class MySpringBootRouter extends RouteBuilder {

    @Override
    public void configure() {
    	from("timer:hello?period={{timer.period}}").routeId("hello").autoStartup(true)
            .log("key-${exchangeProperty.CamelTimerFiredTime.getTime}")
            .log(LoggingLevel.WARN,"key-${exchangeProperty.CamelTimerFiredTime.getTime}");
    	
    }

}
