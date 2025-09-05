package org.elhg;


import static io.quarkus.runtime.Quarkus.run;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class CustomMain {
    public static void main(String... args) {
        run(CustomApp.class, args);
    }

    public static class CustomApp implements QuarkusApplication{
        @Override
        public int run(String... args) throws Exception {
            System.out.println("************* Hello from Custom App *************");
            Quarkus.waitForExit();
            return 0;
        }
    }
}
