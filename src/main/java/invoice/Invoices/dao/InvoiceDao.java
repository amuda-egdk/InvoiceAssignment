package invoice.Invoices.dao;

import java.util.List;

import invoice.Invoices.dto.DueDto;
import invoice.Invoices.dto.InvoiceDto;
import invoice.Invoices.dto.InvoicePayDto;
import invoice.Invoices.entity.Invoice;

public interface InvoiceDao {

	/**
	 * Creating the invoice table
	 */
	void createTable();

	/**
	 * Adding values for Invoice table
	 * 
	 * @param invoiceDto
	 * @return
	 */
	int addInvoice(InvoiceDto invoiceDto);

	/**
	 * Getting all the values in Invoice
	 * 
	 * @return
	 */
	List<Invoice> getAllInvoices();

	/**
	 * Updating the amount and status
	 * 
	 * @param invoicePayDto
	 * @return
	 * @throws Exception
	 */
	double updateAmount(InvoicePayDto invoicePayDto) throws Exception;

	/**
	 * Adding the overdue and the late fee
	 * 
	 * @param dueDto
	 * @return
	 */
	List<Invoice> Overdue(DueDto dueDto);

}
