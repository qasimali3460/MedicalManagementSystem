package MMC;

public class Medicin {
	private String name="";
	private String category="";
	private String quantity="";
	private String price="";
	public Medicin(String n,String c, String q, String p) {
			name=n;
			category=c;
			quantity=q;
			price=p;
	}
	public String getName() {
		return name;
	}
	public String getCategory() {
		return category;
	}
	public String getQuantity() {
		return quantity;
	}
	public String getPrice() {
		return price;
	}

}
