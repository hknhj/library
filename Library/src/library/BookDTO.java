package library;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
	private String title;
	private String author;
	private String publisher;
	private int price;
	private int num;
	
	@Override
	public String toString() {
		String str = "[title] : "+this.title+
			"\n[author] : "+this.author+
			"\n[publisher] : "+this.publisher+
			"\n[price] : "+this.price+
			"\n[num] : "+this.num;
		
		return str;
	}
}
