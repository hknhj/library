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
			" [author] : "+this.author+
			" [publisher] : "+this.publisher+
			" [price] : "+this.price+
			" [num] : "+this.num;
		
		return str;
	}
}
