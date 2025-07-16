package txu.auth.mainapp;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import txu.common.grpc.GrpcServer;
import txu.auth.mainapp.grpc.HrmGrpcService;

import java.io.IOException;

@SpringBootApplication
public class MainAppApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
//        SpringApplication.run(MainAppApplication.class, args);
        GrpcServer.start(MainAppApplication.class, HrmGrpcService.class);
    }

}
