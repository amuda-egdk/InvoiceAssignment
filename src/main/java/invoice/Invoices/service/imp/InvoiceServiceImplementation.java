package invoice.Invoices.service.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dalesbred.Database;

import invoice.Invoices.dto.DueDto;
import invoice.Invoices.dto.InvoiceDto;
import invoice.Invoices.dto.InvoicePayDto;
import invoice.Invoices.entity.Invoice;
import invoice.Invoices.repository.InvoiceRepository;
import invoice.Invoices.service.InvoiceService;

public class InvoiceServiceImplementation implements InvoiceService {
	private InvoiceRepository invoiceRepo;

	public InvoiceServiceImplementation(Database database) {
		this.invoiceRepo = new InvoiceRepository(database);
		invoiceRepo.createTable();
	}

	@Override
	public Map<String, Integer> addInvoice(InvoiceDto invoiceDto) throws Exception {

		if (invoiceDto == null) {
			throw new Exception("Enter the data");
		}

		if (invoiceDto.getAmount() <= 0) {
			throw new Exception("Amount cannot be Zero");
		}

		if (invoiceDto.getDueDate() == null) {
			throw new Exception("Enter the Date");
		}

		int id = invoiceRepo.addInvoice(invoiceDto);
		return new HashMap<>() {
			{
				put("id",id);
			}
		};
	}

	@Override
	public List<Invoice> getAllInvoices() {
		return invoiceRepo.getAllInvoices();
	}

	@Override
	public Map<String, Object> updateAmount(InvoicePayDto invoicePayDto) throws Exception {
		double map = invoiceRepo.updateAmount(invoicePayDto);
		
		if(invoicePayDto.getPaidAmount() <= 0) {
			throw new Exception("Enter the valid paid amount");
		}
		return new HashMap<>() {
			{
				put("amount", map);
			}
		};
	}

	@Override
	public List<Invoice> OverDue(DueDto dueDto) throws Exception {
		if (dueDto.getOverDueDays() <= 0) {
			throw new Exception("Over due days cannot be null or negative");
		}

		if (dueDto.getLateFee() == 0) {

			throw new Exception("Add a valid value for late fee");
		}
		return invoiceRepo.Overdue(dueDto);
	}

	
}
