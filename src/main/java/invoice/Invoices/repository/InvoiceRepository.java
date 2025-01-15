package invoice.Invoices.repository;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dalesbred.Database;
import org.dalesbred.query.SqlQuery;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import invoice.Invoices.dao.InvoiceDao;
import invoice.Invoices.dto.DueDto;
import invoice.Invoices.dto.InvoiceDto;
import invoice.Invoices.dto.InvoicePayDto;
import invoice.Invoices.entity.Invoice;
import invoice.Invoices.enums.InvoiceEnum;

public class InvoiceRepository implements InvoiceDao {
	private Database database;
	private static final String DATA_SOURCE = "db/table.sql";

	public InvoiceRepository() {
	}

	public InvoiceRepository(Database database) {
		this.database = database;
	}

	private String generateTableIfExists() throws IOException {
		URL url = Resources.getResource(DATA_SOURCE);
		String table = Resources.toString(url, Charsets.UTF_8);
		return table;
	}

	private Invoice getInvoice(int id) {
		final String GET_INVOICE = "SELECT * from INVOICE WHERE id = ?";
		return database.findUnique(Invoice.class, GET_INVOICE, id);
	}

	@Override
	public void createTable() {
		try {
			String table = generateTableIfExists();
			database.update(table);
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	@Override
	public int addInvoice(InvoiceDto invoiceDto) {
		final String ADD_INVOICE = "INSERT INTO INVOICE(amount,paid_amount,due_date,status) VALUES (:amount,:paid_amount,:due_date,:status) returning id";
		Map<String, Object> map = new HashMap<>();
		map.put("amount", invoiceDto.getAmount());
		map.put("paid_amount", 0);
		map.put("due_date", invoiceDto.getDueDate());
		map.put("status", InvoiceEnum.PENDING);
		return database.findUniqueInt(SqlQuery.namedQuery(ADD_INVOICE, map));
	}

	@Override
	public List<Invoice> getAllInvoices() {
		final String GET_ALL_INVOICES = "SELECT * FROM INVOICE";
		List<Invoice> allInvoices = database.findAll(Invoice.class, GET_ALL_INVOICES);
		return allInvoices;
	}

	@Override
	public double updateAmount(InvoicePayDto invoicePayDto) throws Exception {
		final String UPDATE_AMOUNT = "UPDATE INVOICE SET paid_amount = :paid_amount, amount = :amount, status = :status WHERE id = :id";
		Invoice getAmount = getInvoice(invoicePayDto.getId());
		if (invoicePayDto.getPaidAmount() > getAmount.getAmount()) {
			throw new Exception("Paid Amount Exceeds amount");
		}

		Double finalAmount = getAmount.getAmount() - invoicePayDto.getPaidAmount();
		Map<String, Object> map = new HashMap<>();
		map.put("paid_amount", getAmount.getPaidAmount() + invoicePayDto.getPaidAmount());
		map.put("amount", finalAmount);
		map.put("status",getAmount.getAmount() <= invoicePayDto.getPaidAmount() ? InvoiceEnum.PAID : InvoiceEnum.PENDING);
		map.put("id", invoicePayDto.getId());
		database.update(SqlQuery.namedQuery(UPDATE_AMOUNT, map));
		return finalAmount;
	}

	@Override
	public List<Invoice> Overdue(DueDto dueDto) {
		final String GET_ALL_INVOICES = "SELECT * FROM INVOICE WHERE status = 'PENDING' AND due_date < ?";
		Date currDate = new Date();
		List<Invoice> allInvoices = database.findAll(Invoice.class, SqlQuery.query(GET_ALL_INVOICES, currDate));
		for (Invoice invoice : allInvoices) {
			final String UPDATE_INVOICE = "UPDATE INVOICE SET status = :status where id = :id";
			Map<String, Object> params = new HashMap<>();
			InvoiceEnum updatedState = invoice.getPaidAmount() == 0.0 ? InvoiceEnum.VOID : InvoiceEnum.PAID;
			double updatedAmount = invoice.getAmount() + dueDto.getLateFee();
			LocalDate updatedDate = invoice.getDueDate().plusDays(dueDto.getOverDueDays());

			params.put("id", invoice.getId());
			params.put("status", updatedState);
			database.update(SqlQuery.namedQuery(UPDATE_INVOICE, params));

			InvoiceDto dto = new InvoiceDto();
			dto.setAmount(updatedAmount);
			dto.setDueDate(updatedDate);
			int id = addInvoice(dto);

			invoice.setId(id);
			invoice.setStatus(updatedState);
			invoice.setAmount(updatedAmount);
			invoice.setDueDate(updatedDate);
		}
		return allInvoices;
	}

}
