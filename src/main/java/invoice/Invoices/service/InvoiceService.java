package invoice.Invoices.service;

import java.util.List;
import java.util.Map;

import invoice.Invoices.dto.DueDto;
import invoice.Invoices.dto.InvoiceDto;
import invoice.Invoices.dto.InvoicePayDto;
import invoice.Invoices.entity.Invoice;

public interface InvoiceService {

	/**
	 * Add values to invoice table
	 * @param invoiceDto
	 * @return
	 * @throws Exception
	 */
	Map<String, Integer> addInvoice(InvoiceDto invoiceDto) throws Exception;

	/**
	 * Getting all values from invoice
	 * @return
	 */
	List<Invoice> getAllInvoices();

	/**
	 * Updating the amount and status
	 * @param invoicePayDto
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> updateAmount(InvoicePayDto invoicePayDto) throws Exception;

	/**
	 * Adding overdue and late fee to existing values
	 * @param dueDto
	 * @return
	 * @throws Exception
	 */
	List<Invoice> OverDue(DueDto dueDto) throws Exception;

}
