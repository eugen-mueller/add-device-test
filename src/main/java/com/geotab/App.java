package com.geotab;

import static com.geotab.http.invoker.ServerInvoker.DEFAULT_TIMEOUT;

import com.geotab.api.GeotabApi;
import com.geotab.http.exception.DbUnavailableException;
import com.geotab.http.exception.InvalidUserException;
import com.geotab.http.request.param.EntityParameters;
import com.geotab.model.Id;
import com.geotab.model.entity.device.Device;
import com.geotab.model.entity.worktime.WorkTimeStandardHours;
import com.geotab.model.login.Credentials;
import com.geotab.model.login.LoginResult;
import com.geotab.model.entity.group.CompanyGroup;
import com.geotab.model.entity.group.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {

        LoginResult loginResult = null;
        
        String server = "my.geotab.com";
        String database = "aaa"; //
        String username = "bbb"; //  Fill in database, username and password 
        String password = "ccc"; //

        Credentials credentials = Credentials.builder()
                .database(database)
                .password(password)
                .userName(username)
                .build();

        try (GeotabApi api = new GeotabApi(credentials, server, DEFAULT_TIMEOUT)) {

            

            // Authenticate user
            try {
                loginResult = api.authenticate();
                System.out.println("Successfully Authenticated");
            } catch (InvalidUserException exception) {
                System.out.print("Invalid user: ");
                System.exit(1);
            } catch (DbUnavailableException exception) {
                System.out.println("Database unavailable: ");
                System.exit(1);
            } catch (Exception exception) {
                System.out.println("Failed to authenticate user: ");
                System.exit(1);
            }

            try {

                List<Group> deviceGroups = new ArrayList<>();
                deviceGroups.add(new CompanyGroup());

                Device newDevice = Device.fromSerialNumber("G9R6HJ16SSFZ");
                newDevice.populateDefaults();
                newDevice.setName("Hello World");
                newDevice.setGroups(deviceGroups);
                newDevice.setWorkTime(new WorkTimeStandardHours());

                // Add the device
                Optional<Id> response = api.callAdd(EntityParameters.entityParamsBuilder()
                        .typeName("Device").entity(newDevice).build());

                if (response.isPresent()) {
                    System.out.print("Added");
                } else {
                    System.out.print("Nope");
                }

            } catch (Exception exception) {
                System.out.println(exception);
            }

        }

    }

}
