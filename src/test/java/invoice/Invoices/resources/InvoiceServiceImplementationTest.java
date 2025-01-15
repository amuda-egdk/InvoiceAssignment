package invoice.Invoices.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dalesbred.Database;
import org.dalesbred.junit.TestDatabaseProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import invoice.Invoices.dto.DueDto;
import invoice.Invoices.dto.InvoiceDto;
import invoice.Invoices.dto.InvoicePayDto;
import invoice.Invoices.entity.Invoice;
import invoice.Invoices.service.imp.InvoiceServiceImplementation;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

@ExtendWith(DropwizardExtensionsSupport.class)
public class InvoiceServiceImplementationTest {

    private static InvoiceServiceImplementation invoiceService;
    private static Database db = TestDatabaseProvider.databaseForProperties("db.properties");

    @BeforeAll
    public static void setUp() throws Exception {
        invoiceService = new InvoiceServiceImplementation(db);
        Date date = new Date(1731993685000L);
        LocalDate localDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());

        InvoiceDto dto = new InvoiceDto();
        dto.setDueDate(localDate);
        dto.setAmount(450);
        invoiceService.addInvoice(dto);
    }

    @AfterAll
    public static void afterSetUp() {
        db.update("TRUNCATE TABLE invoice RESTART IDENTITY");
    }

    @Test
    public void addInvoice() throws Exception {
        Date date = new Date(1731993685000L);
        LocalDate localDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());

        InvoiceDto dto = new InvoiceDto();
        dto.setDueDate(localDate);
        dto.setAmount(450);

        int expectedId = 2;

        Map<String, Integer> generatedIdMap = invoiceService.addInvoice(dto);
        int generatedId = generatedIdMap.get("id");

        assertThat(expectedId).isEqualTo(generatedId);
        assertNotNull(generatedIdMap);
    }

    @Test
    public void getAllInvoices() throws Exception {
        Date date = new Date(1731993685000L);
        LocalDate localDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());

        InvoiceDto dto = new InvoiceDto();
        dto.setDueDate(localDate);
        dto.setAmount(450);
        invoiceService.addInvoice(dto);

        List<Invoice> invoices = invoiceService.getAllInvoices();

        assertNotNull(invoices);
        assertThat((invoices.size() > 0)).isEqualTo(true);
    }

    @Test
    public void updateAmount() throws Exception {
        InvoicePayDto payDto = new InvoicePayDto();
        payDto.setId(1);
        payDto.setPaidAmount(100);
        Map<String, Object> result = invoiceService.updateAmount(payDto);

        assertNotNull(result);
        assertThat(result.get("amount")).isEqualTo(350.0);
    }

    @Test
    public void Overdue() throws Exception {
        System.out.println("overdue");
        db.update("TRUNCATE TABLE invoice RESTART IDENTITY");

        Date date = new Date(1731993685000L);
        LocalDate localDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());

        InvoiceDto dto = new InvoiceDto();
        dto.setDueDate(localDate);
        dto.setAmount(450);
        invoiceService.addInvoice(dto);

        DueDto dueDto = new DueDto();
        dueDto.setLateFee(100);
        dueDto.setOverDueDays(10);

        List<Invoice> overdueInvoices = invoiceService.OverDue(dueDto);

        Invoice overdueInvoice = overdueInvoices.get(0);
        System.out.println(overdueInvoices.size());
        assertThat(overdueInvoice.getAmount()).isEqualTo(550);
        assertThat(overdueInvoice.getDueDate()).isEqualTo(localDate.plusDays(10));
    }


    @Test
    public void updateAmountWithInvalidPaidAmount() {
        InvoicePayDto payDto = new InvoicePayDto();
        payDto.setId(1);
        payDto.setPaidAmount(0);

        Exception exception = assertThrows(Exception.class, () -> {
            invoiceService.updateAmount(payDto);
        });

        String expectedMessage = "Enter the valid paid amount";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void overdueWithInvalidOverdueDays() {
        DueDto dueDto = new DueDto();
        dueDto.setLateFee(100);
        dueDto.setOverDueDays(0);

        Exception exception = assertThrows(Exception.class, () -> {
            invoiceService.OverDue(dueDto);
        });

        String expectedMessage = "Over due days cannot be null or negative";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void overdueWithInvalidLateFee() {
        DueDto dueDto = new DueDto();
        dueDto.setLateFee(0);
        dueDto.setOverDueDays(10);

        Exception exception = assertThrows(Exception.class, () -> {
            invoiceService.OverDue(dueDto);
        });

        String expectedMessage = "Add a valid value for late fee";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }



    @Test
    public void updateAmountExceedingInvoiceAmount() throws Exception {
        InvoicePayDto payDto = new InvoicePayDto();
        payDto.setId(1);
        payDto.setPaidAmount(500);

        Exception exception = assertThrows(Exception.class, () -> {
            invoiceService.updateAmount(payDto);
        });

        String expectedMessage = "Paid Amount Exceeds amount";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
