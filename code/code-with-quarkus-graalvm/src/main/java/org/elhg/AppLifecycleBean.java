package org.elhg;

import io.quarkus.runtime.ShutdownEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class AppLifecycleBean {
    private static final Logger log = LoggerFactory.getLogger(AppLifecycleBean.class);

    void onStart(@Observes Startup event) {
        log.info("The application is starting...");
    }

    void onStop(@Observes ShutdownEvent event) {
        log.info("The application is stopping... Solo se ve desde la linea de comandos");
    }
}
