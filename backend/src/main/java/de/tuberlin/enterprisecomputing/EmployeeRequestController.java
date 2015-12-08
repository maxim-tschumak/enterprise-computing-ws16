package de.tuberlin.enterprisecomputing;

import de.tuberlin.enterprisecomputing.integrations.S3Service;
import de.tuberlin.enterprisecomputing.domain.EmployeeRequest;
import de.tuberlin.enterprisecomputing.integrations.DynamoDBService;
import de.tuberlin.enterprisecomputing.integrations.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class EmployeeRequestController {

    final static String MANAGER_EMAIL = "ec2015manager@gmail.com";
    final static String EMPLOYEE_EMAIL = "ec2015employee@gmail.com";

    @Autowired
    DynamoDBService dynamo;
    @Autowired
    MailService mail;
    @Autowired
    S3Service s3;

    @RequestMapping(path = "/requests/{id}", method = RequestMethod.GET)
    public EmployeeRequest getRequest(@PathVariable String id) {
        return dynamo.getRequestById(id);
    }

    @RequestMapping(path = "/requests/{id}", method = RequestMethod.PUT)
    public void updateRequest(@PathVariable String id, @RequestBody EmployeeRequest request) {
        dynamo.updateRequest(id, request);
    }

    @RequestMapping(path = "/requests", method = RequestMethod.GET)
    public List<EmployeeRequest> getRequests() {
        return dynamo.getAllRequests();
    }

    @RequestMapping(path = "/requests", method = RequestMethod.POST)
    public void createRequest(@RequestParam("name") String name, /*add other values as request/query parameters*/
                              @RequestParam(value = "file", required = false) MultipartFile file) {
        final EmployeeRequest request = new EmployeeRequest();
        //TODO fill the request object with values


        final String requestId = dynamo.createRequestEntry(request);
        //TODO store the request in the db and store the binary in s3 bucket
        // some help: https://spring.io/guides/gs/uploading-files/
        // s3.storeFile(requestId, ..


        //TODO create a message and send to the manager
        // mail.sendMail(...
        return;
    }

    @RequestMapping(path = "/requests/{id}/status", method = RequestMethod.PUT)
    public void putStatus(@PathVariable String id, @RequestParam("status") String status) {
        dynamo.setStatus(id, status);
        //TODO create a message and send to the employee
        // mail.sendMail(...
    }
}
