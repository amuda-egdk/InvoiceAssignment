# invoice

How to start the invoice application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/Invoices-0.0.1-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

## DOCUMENT:

# Invoice Management System

Overview:
This project is an Invoice Management System that allows users to manage invoices, including creating, updating, and processing overdue invoices. The system is built using Java and utilizes the Dalesbred library for database interactions.

Project Structure:

The project is divided into the following main packages:

1. repository: Contains the `InvoiceRepository` class which handles database operations.
2. resources: Contains the `InvoiceResources` class which exposes RESTful endpoints for invoice operations.
3. service.imp: Contains the `InvoiceServiceImplementation` class which implements the business logic for invoice operations.
4. dto: Contains Data Transfer Objects (DTOs) used for transferring data between different layers of the application.
5. entity: Contains the `Invoice` entity class which represents the invoice data.
6. enums: Contains the `InvoiceEnum` enumeration which defines the possible statuses of an invoice.

repository.InvoiceRepository:

- createTable(): Creates the invoice table in the database if it does not exist.
- addInvoice(InvoiceDto invoiceDto): Adds a new invoice to the database.
- getAllInvoices(): Retrieves all invoices from the database.
- updateAmount(InvoicePayDto invoicePayDto): Updates the amount and status of an invoice based on the payment made.
- Overdue(DueDto dueDto): Processes overdue invoices by updating their status and creating new invoices with late fees.

resources.InvoiceResources:

- addInvoice(InvoiceDto invoiceDto): Endpoint to add a new invoice.
- getAllInvoices(): Endpoint to retrieve all invoices.
- updateInvoice(int id, InvoicePayDto invoicePayDto): Endpoint to update the amount and status of an invoice based on the payment made.
- OverDue(DueDto dueDto): Endpoint to process overdue invoices.

service.imp.InvoiceServiceImplementation:

- addInvoice(InvoiceDto invoiceDto): Validates and adds a new invoice.
- getAllInvoices(): Retrieves all invoices.
- updateAmount(InvoicePayDto invoicePayDto): Validates and updates the amount and status of an invoice based on the payment made.
- OverDue(DueDto dueDto): Validates and processes overdue invoices.

DTOs:

- InvoiceDto: Used for transferring invoice data.
- InvoicePayDto: Used for transferring payment data for an invoice.
- DueDto: Used for transferring data related to overdue invoices.

Enums:

- InvoiceEnum: Defines the possible statuses of an invoice (`PENDING`, `PAID`, `VOID`).

## Implemented Docker and Swagger.
