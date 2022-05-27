package Client;

import com.anny.grpc.User;
import com.anny.grpc.userGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

public class gRPC_Client {
    public static void main(String[] args) {

        ManagedChannel channel =ManagedChannelBuilder.forAddress("localhost", 4000).usePlaintext().build();

        //stubs
        userGrpc.userBlockingStub userStub = userGrpc.newBlockingStub(channel);
        String username, email, password;
        boolean isLoggedIn = false;
        Scanner input = new Scanner(System.in);
        System.out.println("New User? Type 1");
        System.out.println("Wanna Log In? Type 2");
        System.out.println("Wanna Log Out? Type 3");
        int choice = input.nextInt();
        if(choice == 1)
        {
            System.out.println("Register Here ");
            System.out.print("Username: ");
            username = input.next();
            System.out.print("Email: ");
            email = input.next();
            System.out.print("Password: ");
            password = input.next();
            User.Registration registration = User.Registration.newBuilder().setUsername(username).setEmail(email).setPassword(password).build();
            User.apiResponse response = userStub.register(registration);
            if(response.getResponseCode() == 200){
                isLoggedIn = true;
            }
            System.out.println(response.getResponseMessage());
        }
        else if(choice == 2)
        {
            System.out.println("Log In Here ");
            System.out.print("Email: ");
            email = input.nextLine();
            System.out.print("Password: ");
            password = input.nextLine();
            User.LoginRequest loginReq = User.LoginRequest.newBuilder().setEmail(email).setPassword(password).build();
            User.apiResponse response = userStub.login(loginReq);
            System.out.println(response.getResponseMessage());
        }
        else if(choice == 3)
        {
            if(isLoggedIn)
            {
                System.out.println("log out");
            }
            else
            {
                System.out.println("Please Log In First!");
            }
        }
        else
        {
            System.out.println("Wrong choice!");
            System.out.println("Please Type Between 1, 2 and 3");
        }
    }
}
