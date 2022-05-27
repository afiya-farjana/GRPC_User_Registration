package User;

import com.anny.grpc.User;
import com.anny.grpc.userGrpc;
import io.grpc.stub.StreamObserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class userService extends userGrpc.userImplBase {

    String url = "jdbc:mysql://localhost:3306/users";
    String user = "root";
    String pass = "anny1234";

    @Override
    public void register(User.Registration request, StreamObserver<User.apiResponse> responseObserver) {
        //super.register(request, responseObserver);
        System.out.println("Inside register");
        String username = request.getUsername();
        String email = request.getEmail();
        String password = request.getPassword();
        User.apiResponse.Builder response = User.apiResponse.newBuilder();
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            System.out.println("DB Connection Is Successful");
            String query = "INSERT INTO `users`.`user` (`UserName`, `EmailId`, `Password`) VALUES (\'"+username+ "\',\'"+ email + "\',\'" + password +"\')";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users.user where UserName=\""+ username + "\" and EmailId = \""+email+"\"");
            if(resultSet.next()){
                response.setResponseCode(400).setResponseMessage("User Already Exists!");
            }
            else
            {
                statement.executeUpdate(query);
                response.setResponseCode(200).setResponseMessage("User Registration is Successful");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void login(User.LoginRequest request, StreamObserver<User.apiResponse> responseObserver) {
        System.out.println("Inside login");

        String email = request.getEmail();
        String password = request.getPassword();
        User.apiResponse.Builder response = User.apiResponse.newBuilder();
        try{
            Connection connection = DriverManager.getConnection(url, user, pass);
            System.out.println("DB Connection Is Successful");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users.user where EmailId = \""+email+"\"");

            if (resultSet.next())
            {
                String dbEmail = resultSet.getString("EmailId");
                String dbPassword = resultSet.getString("Password");

                if(email.equals(dbEmail) && password.equals(dbPassword)){
                    response.setResponseCode(200).setResponseMessage("Successfully Logged in");
                }
                else {
                    response.setResponseCode(400).setResponseMessage("Invalid Email or Password");
                }
            }
            else
            {
                response.setResponseCode(401).setResponseMessage("Invalid User!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void logout(User.empty request, StreamObserver<User.apiResponse> responseObserver) {

    }


}
