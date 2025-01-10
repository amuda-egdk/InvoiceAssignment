package invoice.Invoices.dto;

public class DueDto {
	private double lateFee;
	private int overDueDays;

	public DueDto() {
	}

	public DueDto(double lateFee, int overDueDays) {
		this.lateFee = lateFee;
		this.overDueDays = overDueDays;
	}

	public double getLateFee() {
		return lateFee;
	}

	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
	}

	public int getOverDueDays() {
		return overDueDays;
	}

	public void setOverDueDays(int overDueDays) {
		this.overDueDays = overDueDays;
	}

}
