package invoice.Invoices.resources;

import java.util.List;
import java.util.Map;

import org.dalesbred.Database;

import invoice.Invoices.dto.DueDto;
import invoice.Invoices.dto.InvoiceDto;
import invoice.Invoices.dto.InvoicePayDto;
import invoice.Invoices.entity.Invoice;
import invoice.Invoices.service.imp.InvoiceServiceImplementation;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/invoices")
@Produces(MediaType.APPLICATION_JSON)
public class InvoiceResources {

	private InvoiceServiceImplementation invoiceservice;

	public InvoiceResources(Database database) {
		this.invoiceservice = new InvoiceServiceImplementation(database);
	}

	@POST
	@Path("")
	public Response addInvoice(InvoiceDto invoiceDto) throws Exception {
		Map<String, Integer> id = invoiceservice.addInvoice(invoiceDto);
		return Response.status(Response.Status.CREATED).entity(id).build();
	}

	@GET
	@Path("")
	public List<Invoice> getAllInvoices() {
		return invoiceservice.getAllInvoices();
	}

	@POST
	@Path("/{id}/payments")
	public Map<String, Object> updateInvoice(@PathParam("id") int id, InvoicePayDto invoicePayDto) throws Exception {
		invoicePayDto.setId(id);
		Map<String, Object> map = invoiceservice.updateAmount(invoicePayDto);
		return map;
	}

	@POST
	@Path("/process-overdue")
	public List<Invoice> OverDue(DueDto dueDto) throws Exception {
		return invoiceservice.OverDue(dueDto);
	}

}
