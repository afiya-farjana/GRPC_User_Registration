package gRPC_Server;

import User.userService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class gRPCServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(4000).addService(new userService()).build();
        server.start();
        System.out.println("Server has started at port - " + server.getPort());
        server.awaitTermination();
    }
}
