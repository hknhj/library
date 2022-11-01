package library;

import java.util.Scanner;

public class BookTest {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		BookDAO bookdao;
		
		while(true) {
			bookdao=new BookDAO();
			Banner.banner();
			
			String option = scanner.nextLine();
			
			if(option.equals("1")) {
				BookDTO bookdto = bookdao.BookSearch();
				if(bookdto==null) {
					System.out.println("해당 책이 없습니다.\n");
					continue;
				}
				System.out.println("[책 정보]");
				System.out.println(bookdto.toString());
				Banner.subBanner();
				String subOption = scanner.nextLine();
				if(subOption.equals("1")) {
					bookdao.BookUpdate(bookdto);
				} else if(subOption.equals("2")) {
					System.out.println();
					continue;
				}
			} else if(option.equals("2")) {
				bookdao.BookInsert();
			} else if(option.equals("3")) {
				System.out.println("프로그램을 종료합니다.");
				break;
			}
		}
	}
}
