import java.time.LocalDate;
import java.util.Scanner;

import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class SupervisorSystem {

public static void main(String[] args) {

    int choice;

    while (true) {

        System.out.println("\n==============================");
        System.out.println("       SUPERVISOR SYSTEM");
        System.out.println("==============================");
        System.out.println("1. Transaction Search");
        System.out.println("2. Warranty Verification");
        System.out.println("3. Delivery Issues");
        System.out.println("4. Assign Delivery Team");
        System.out.println("5. Delivery History");
        System.out.println("6. Delivery Status");
        System.out.println("0. Exit");
        System.out.println("==============================");

        System.out.print("Enter your choice: ");
        Scanner input = new Scanner(System.in);
        choice = Integer.parseInt(input.nextLine());

        if (choice == 1) {
            transactionSearch();
        }
        else if (choice == 2) {
            warrantyVerification();
        }
        else if (choice == 3) {
            deliveryIssuesMenu();
        }
        else if (choice == 4) {
            assignDeliveryTeam();
        }
        else if (choice == 5) {
            deliveryHistory();
        }
        else if (choice == 6) {
            deliveryStatusMonitoring();
        }
        else if (choice == 0) {
            System.out.println("System closed.");
            break;
        }
        else {
            System.out.println("Invalid choice.");
        }
    }
}

public static void transactionSearch() {

    Document doc = loadXML();

    System.out.println("\n--- TRANSACTION SEARCH ---");

    Scanner input = new Scanner(System.in);
    System.out.print("Enter Transaction ID: ");
    String id = input.nextLine();

    NodeList list = doc.getElementsByTagName("transaction");

    boolean found = false;

    for (int i = 0; i < list.getLength(); i++) {

        Element transaction = (Element) list.item(i);

        if (transaction.getAttribute("id").equalsIgnoreCase(id)) {

            System.out.println("Transaction ID: " + id);
            System.out.println("Date: " +
                    transaction.getElementsByTagName("date")
                    .item(0).getTextContent());

            System.out.println("Payment Method: " +
                    transaction.getElementsByTagName("payment")
                    .item(0).getTextContent());

            System.out.println("Status: " +
                    transaction.getElementsByTagName("status")
                    .item(0).getTextContent());

            found = true;
        }
    }

    if (!found) {
        System.out.println("Transaction not found.");
    }
}

public static void warrantyVerification() {

    Document doc = loadXML();

    System.out.println("\n--- WARRANTY VERIFICATION ---");

    
    Scanner input = new Scanner(System.in);
    System.out.print("Enter Transaction ID: ");
    String id = input.nextLine();

    NodeList list = doc.getElementsByTagName("transaction");

    boolean found = false;

    for (int i = 0; i < list.getLength(); i++) {

        Element transaction = (Element) list.item(i);

        if (transaction.getAttribute("id").equalsIgnoreCase(id)) {

            String expiry =
                    transaction.getElementsByTagName("expiry")
                    .item(0).getTextContent();

            LocalDate expiryDate =
                    LocalDate.parse(expiry);

            LocalDate today = LocalDate.now();

            System.out.println("Warranty Expiry: " + expiry);

            if (today.isBefore(expiryDate)
                    || today.isEqual(expiryDate)) {

                System.out.println("Warranty Status: VALID");

            } else {

                System.out.println("Warranty Status: EXPIRED");
            }

            found = true;
        }
    }

    if (!found) {
        System.out.println("Transaction not found.");
    }
}

public static void deliveryIssuesMenu() {Document doc = loadXML();

    NodeList list =
            doc.getElementsByTagName("deliveryIssues");

    Element issues = (Element) list.item(0);

    Scanner input = new Scanner(System.in);
    System.out.print("Enter Issue ID: ");
    String id = input.nextLine();

    System.out.print("Enter Order ID: ");
    String order = input.nextLine();

    System.out.print("Enter Issue Type: ");
    String type = input.nextLine();

    System.out.print("Enter Action: ");
    String action = input.nextLine();

    String urgency;

    if (type.equalsIgnoreCase("Delayed Delivery")
            || type.equalsIgnoreCase("Damaged Parcel")
            || type.equalsIgnoreCase("Wrong Address")) {

        System.out.print("Is this issue urgent? (Yes/No): ");
        urgency = input.nextLine();

        while (!urgency.equalsIgnoreCase("Yes")
                && !urgency.equalsIgnoreCase("No")) {
            System.out.print("Please enter Yes or No: ");
            urgency = input.nextLine();
        }

    } else {
        System.out.print("Is this issue urgent? (Yes/No): ");
        urgency = input.nextLine();

        while (!urgency.equalsIgnoreCase("Yes")
                && !urgency.equalsIgnoreCase("No")) {
            System.out.print("Please enter Yes or No: ");
            urgency = input.nextLine();
        }
    }

    Element issue = doc.createElement("issue");
    

    issue.setAttribute("id", id);

    addElement(doc, issue, "orderID", order);
    addElement(doc, issue, "type", type);
    addElement(doc, issue, "action", action);

    issues.appendChild(issue);
   
    System.out.println("\n--- Delivery Issue Details ---");
        System.out.println("Issue ID: " + id);
        System.out.println("Order ID: " + order);
        System.out.println("Issue Type: " + type);
        System.out.println("Status: " + urgency);

    saveXML(doc);

    System.out.println("Issue created successfully.");
}

public static void displayDeliveryIssues() {
    Document doc = loadXML();

    NodeList list = doc.getElementsByTagName("issue");

    for (int i = 0; i < list.getLength(); i++) {
        Element issue = (Element) list.item(i);

        String id = issue.getAttribute("id");

        String description = issue
                .getElementsByTagName("description")
                .item(0).getTextContent();

        System.out.println("\nIssue ID: " + id);
        System.out.println("Description: " + description);
    }
}
public static void saveXML(Document doc) {
    try {
        TransformerFactory transformerFactory =
                TransformerFactory.newInstance();

        Transformer transformer =
                transformerFactory.newTransformer();

        transformer.setOutputProperty(
                OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);

        StreamResult result =
                new StreamResult(new File("Supervisor.xml"));

        transformer.transform(source, result);

        // System.out.println("XML file saved successfully.");

    } catch (Exception e) {
        System.out.println("Error saving XML: " + e.getMessage());
    }
}

public static void addElement(Document doc, Element parent,
        String tagName, String value) {

    Element newElement = doc.createElement(tagName);
    newElement.setTextContent(value);
    parent.appendChild(newElement);
}

public static void assignDeliveryTeam() {

    Document doc = loadXML();

    System.out.println("\n--- DELIVERY TEAM ASSIGNMENT ---");

    Scanner input = new Scanner(System.in);
    System.out.print("Enter Order ID: ");
    String orderID = input.nextLine();

    NodeList orders =
            doc.getElementsByTagName("order");

    for (int i = 0; i < orders.getLength(); i++) {

        Element order = (Element) orders.item(i);

        if (order.getAttribute("id")
                .equalsIgnoreCase(orderID)) {

            String status =
                    getValue(order, "status");

            // Business rule
            if (!status.equalsIgnoreCase(
                    "Ready for Delivery")) {

                System.out.println(
                        "Order is not ready for delivery.");

                return;
            }

            System.out.print(
                    "Enter Delivery Team ID: ");

            String teamID = input.nextLine();

            order.getElementsByTagName("teamID")
                    .item(0)
                    .setTextContent(teamID);

            order.getElementsByTagName("status")
                    .item(0)
                    .setTextContent("Assigned");

            saveXML(doc);

            System.out.println(
                    "Order assigned successfully.");

            return;
        }
    }

    System.out.println("Order not found.");
}
private static String getValue(Element order, String string) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getValue'");
}

    public static void deliveryHistory() {

    Document doc = loadXML();

    System.out.println("\n--- DELIVERY HISTORY ---");

    NodeList orders =
            doc.getElementsByTagName("order");

    for (int i = 0; i < orders.getLength(); i++) {

        Element order = (Element) orders.item(i);

        String status =
                getValue(order, "status");

        if (status.equalsIgnoreCase("Delivered")) {

            System.out.println(
                    "\nOrder ID: "
                    + order.getAttribute("id"));

            System.out.println(
                    "Transaction ID: "
                    + getValue(order, "transactionID"));

            System.out.println(
                    "Customer: "
                    + getValue(order, "customer"));

            System.out.println(
                    "Delivery Team: "
                    + getValue(order, "teamID"));

            System.out.println(
                    "Status: " + status);
        }
    }
}

    public static void deliveryStatusMonitoring() {

    Document doc = loadXML();

    System.out.println("\n--- DELIVERY STATUS MONITORING ---");

    System.out.println("1. Assigned");
    System.out.println("2. On the Way");
    System.out.println("3. Delivered");
    System.out.println("4. Ready for Pickup");
    System.out.println("5. Failed");

    Scanner input = new Scanner(System.in);
    System.out.print("Enter choice: ");
    int choice = Integer.parseInt(input.nextLine());

    String status = "";

    if (choice == 1) {
        status = "Assigned";
    }
    else if (choice == 2) {
        status = "On the Way";
    }
    else if (choice == 3) {
        status = "Delivered";
    }
    else if (choice == 4) {
        status = "Ready for Pickup";
    }
    else if (choice == 5) {
        status = "Failed";
    }
    else {
        System.out.println("Invalid choice.");
        return;
    }

    NodeList orders =
            doc.getElementsByTagName("order");

    System.out.println(
            "\nOrders with status: " + status);

    for (int i = 0; i < orders.getLength(); i++) {

        Element order = (Element) orders.item(i);

        String orderStatus =
                getValue(order, "status");

        if (orderStatus.equalsIgnoreCase(status)) {

            System.out.println(
                    "Order ID: "
                    + order.getAttribute("id"));

            System.out.println(
                    "Customer: "
                    + getValue(order, "customer"));

            System.out.println(
                    "Delivery Team: "
                    + getValue(order, "teamID"));
        }
    }
}



private static Document loadXML() {
    try {
        File file = new File("Supervisor.xml");

        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(file);

    } catch (Exception e) {
        System.out.println("Error loading XML: " + e.getMessage());
        return null;
    }
}
}




