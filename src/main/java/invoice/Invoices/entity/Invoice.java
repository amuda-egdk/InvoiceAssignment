package invoice.Invoices.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import invoice.Invoices.enums.InvoiceEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

	private int Id;
	private double amount;
	private double paidAmount;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dueDate;
	private InvoiceEnum status;

}
